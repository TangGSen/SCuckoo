package com.ourcompany.presenter.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.utils.NetWorkUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.activity.ResigisterActView;

import company.com.commons.framework.presenter.MvpBasePresenter;


/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/17 21:14
 * Des    :
 */

public class ResigisterActPresenter extends MvpBasePresenter<ResigisterActView> {
    //开始倒数
    private static final int MSG_COUNTING_TIME = 0;
    private static final int MSG_ERROR_GET_CODE = 1;
    //验证成功
    private static final int MSG_RESIGISTER_SUCCESS = 2;
    private static final int MSG_RESIGISTER_FAIL = 3;
    private static final int MSG_LOGIN_SUCCESS = 4;
    private static final int MSG_LOGIN_FAIL = 5;
    private static String currentPhone = "";
    public int currentTime = 0;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_COUNTING_TIME:
                    if (currentTime >= 0) {
                        String mess = String.format(ResourceUtils.getString(R.string.get_count_time), currentTime);
                        currentTime--;
                        getView().setSafetyBtnText(mess);
                        Message message = mHandler.obtainMessage();
                        mHandler.sendEmptyMessageDelayed(MSG_COUNTING_TIME, 1000);
                    } else {
                        getView().setSafetyBtnEnable(true);
                        getView().setSafetyBtnText(ResourceUtils.getString(R.string.get_safety_code));
                    }
                    break;
                case MSG_ERROR_GET_CODE:
                    getView().setSafetyBtnEnable(true);
                    getView().setSafetyBtnText(ResourceUtils.getString(R.string.get_safety_code));
                    getView().showToastMsg(ResourceUtils.getString(R.string.get_code_error));

                    break;
                case MSG_RESIGISTER_FAIL:
                    //失败和成功都得注销
                    String mes = (String) msg.obj;

                    getView().verifyFail(mes);

                    break;
                case MSG_RESIGISTER_SUCCESS:
                    getView().showToastMsg(ResourceUtils.getString(R.string.resigister_success));
                    getView().verifySuccess();
                    getView().logining();
                    break;

                case MSG_LOGIN_SUCCESS:
                    getView().loginSuccess();
                    break;
                case MSG_LOGIN_FAIL:
                    getView().loginFail(currentPhone);
                    break;

            }
        }
    };

    public ResigisterActPresenter(Context context) {
        super(context);
    }

    //获取验证码
    public void getSafetyCode(String phone) {
        if (TextUtils.isEmpty(phone)) {
            getView().showToastMsg(ResourceUtils.getString(R.string.frag_login_username_hint));
        } else {
            if (NetWorkUtils.isConnected(MApplication.mContext)) {
                getView().setSafetyBtnEnable(false);
                getView().setSafetyBtnText(ResourceUtils.getString(R.string.getting));
                getView().getSafetyCodeing();
                //目前先默认使用国内
                phone = phone.replaceAll(" ","");
                requestCode(Constant.COUNTRY_CODE, phone);
            } else {
                //网络未连接
                getView().showToastMsg(ResourceUtils.getString(R.string.net_unlink));
            }

        }

    }

    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public void requestCode(String country, String phone) {


//        // 触发操作
//        SMSSDK.getVerificationCode(country, phone);
        UMSSDK.sendVerifyCode(country, phone, new OperationCallback<Boolean>() {
            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                Message message = mHandler.obtainMessage();
                message.what = MSG_ERROR_GET_CODE;
                message.sendToTarget();
                getView().showToastMsg(throwable.getMessage()+"\n***"+throwable.getLocalizedMessage());

            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                super.onSuccess(aBoolean);
                Message message = mHandler.obtainMessage();
                currentTime = Constant.SAFETY_CODE_TIME_INTERVAL;
                message.what = MSG_COUNTING_TIME;
                message.sendToTarget();
            }
        });
    }

    //提交验证码
    public void sendSafetyCode(String phone, String code, String password) {
        if (TextUtils.isEmpty(phone)) {
            getView().showToastMsg(ResourceUtils.getString(R.string.frag_login_username_hint));
            return;
        }

        if (TextUtils.isEmpty(code)) {
            getView().showToastMsg(ResourceUtils.getString(R.string.frag_login_safety_hint));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            getView().showToastMsg(ResourceUtils.getString(R.string.frag_login_password_hint));
            return;
        }
        //再检查网络
        if (!NetWorkUtils.isConnected(MApplication.mContext)) {
            getView().showToastMsg(ResourceUtils.getString(R.string.net_unlink));
            return;
        }
        //开始验证
        getView().sendSafetyCodeing();
        phone = phone.replaceAll(" ","");
        submitCode(Constant.COUNTRY_CODE, phone, code, password);
    }

    // 提交验证码，其中的code表示验证码，如“1357”
    public void submitCode(String country, final String phone, String code, final String password) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
//        // 触发操作
//        SMSSDK.submitVerificationCode(country, phone, code);
        User user = new User();
        UMSSDK.registerWithPhoneNumber(country, phone, code, password, user, new OperationCallback<User>() {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                LogUtils.e("sen", " UMSSDK.registerWithPhoneNumber ThreadName:" + Thread.currentThread().getName());
                Message message = mHandler.obtainMessage();
                // 处理验证成功的结果
                message.what = MSG_RESIGISTER_SUCCESS;
                message.sendToTarget();
                // 然后登陆
                loginUser(phone, password);

            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                LogUtils.e("sen", "onFailed ThreadName:" + Thread.currentThread().getName());
                //  处理错误的结果
                Message message = mHandler.obtainMessage();
                message.what = MSG_RESIGISTER_FAIL;
                message.obj = ResourceUtils.getString(R.string.submit_info_error);
                message.sendToTarget();
            }
        });
    }

    /**
     *  登陆,这里是自动登陆，如果失败后，就返回上一个登陆页面让用户手动登陆
     */

    private void loginUser(String phone, String password) {
        currentPhone = phone;
        UMSSDK.loginWithPhoneNumber(Constant.COUNTRY_CODE, phone, password, new OperationCallback<User>() {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                Constant.CURRENT_USER = user;
                MServiceManager.getInstance().login(user.id.get(), user.nickname.get(), "");
                Message message = mHandler.obtainMessage();
                message.what = MSG_LOGIN_SUCCESS;
                message.sendToTarget();
            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                Message message = mHandler.obtainMessage();
                message.what = MSG_LOGIN_FAIL;
                message.sendToTarget();
            }
        });
    }

    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
    }


    public void resigisterIMAccount(String userName, String password) {

    }

}
