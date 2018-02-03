package com.ourcompany.presenter.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.mob.MobSDK;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.NetWorkUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.activity.LoginActvityView;

import company.com.commons.framework.presenter.MvpBasePresenter;


/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/17 21:14
 * Des    :
 */

public class LoginActPresenter extends MvpBasePresenter<LoginActvityView> {

    //开始倒数
    public static final int MSG_LOGIN_SUCCESS = 0;
    public static final int MSG_LOGIN_FAIL = 1;
    //验证成功
    public int currentTime = 0;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_LOGIN_SUCCESS:
                    getView().loginSucess();
                    break;
                case MSG_LOGIN_FAIL:
                    getView().loginFial();
                    break;


            }
        }
    };

    public LoginActPresenter(Context context) {
        super(context);
    }


    public void login(String phones, String password) {
        phones = phones.replaceAll(" ","");
        password = password.replaceAll(" ","");
        if (TextUtils.isEmpty(phones)) {
            getView().showToastMsg(ResourceUtils.getString(R.string.frag_login_username_hint));
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
        UMSSDK.loginWithPhoneNumber(Constant.COUNTRY_CODE,phones,password,new OperationCallback<User>(){

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                Message message = mHandler.obtainMessage();
                message.what = MSG_LOGIN_FAIL;
                message.sendToTarget();
            }

            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                Constant.CURRENT_USER = user;
                Message message = mHandler.obtainMessage();
                // 处理验证成功的结果
                message.what = MSG_LOGIN_SUCCESS;
                message.sendToTarget();
                //同时也将登陆的信息保存一下
                //登陆IM
                MServiceManager.getInstance().login(user.id.get(),user.nickname.get(),"");
            }
        });



    }


    public void loginIMAccount(String userName, String password) {
        MobSDK.setUser("用户ID", "用户昵称","用户头像地址", null);
    }



}
