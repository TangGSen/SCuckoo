package com.ourcompany.fragment;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.activity.AboutCuckooActivity;
import com.ourcompany.activity.FeedbackActivity;
import com.ourcompany.activity.MessageActivity;
import com.ourcompany.activity.imui.UserInfoActivity;
import com.ourcompany.activity.setting.SystemSettingActivity;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.UserAccoutLoginRes;
import com.ourcompany.bean.eventbus.UserLogout;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.presenter.fragment.MineFragPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.fragment.MineFragmentView;
import com.ourcompany.widget.recycleview.commadapter.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 16:11
 * Des    :
 */

public class MineFragment extends MvpFragment<MineFragmentView, MineFragPresenter> implements MineFragmentView {
    @BindView(R.id.img_user)
    CircleImageView mUserImage;
    @BindView(R.id.str_user_name)
    TextView strUserName;

    @BindView(R.id.personal_rootLayout)
    CoordinatorLayout personalRootLayout;
    @BindView(R.id.btnMessage)
    TextView btnMessage;
    Unbinder unbinder;
    @BindView(R.id.btCollection)
    TextView btCollection;
    @BindView(R.id.btMyDynamic)
    TextView btMyDynamic;
    @BindView(R.id.btMyVote)
    TextView btMyVote;
    @BindView(R.id.btFeedback)
    TextView btFeedback;
    @BindView(R.id.btAboutCuckoo)
    TextView btAboutCuckoo;
    @BindView(R.id.btSetting)
    TextView btSetting;
    Unbinder unbinder1;


    @Override
    protected void initView(View view) {
        super.initView(view);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_act_personal;
    }

    @Override
    protected MineFragmentView bindView() {
        return this;
    }

    @Override
    protected MineFragPresenter bindPresenter() {
        return new MineFragPresenter(MApplication.mContext);
    }

    @OnClick({R.id.img_user, R.id.btnMessage, R.id.btCollection, R.id.btMyDynamic,
            R.id.btMyVote, R.id.btFeedback, R.id.btAboutCuckoo, R.id.btSetting})
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_user:
                //需要判断是否登录
                getPresenter().onClickUserImage(mActivity, this);
                break;
            case R.id.btnMessage:
                startActivity(new Intent(mActivity, MessageActivity.class));
                break;
            case R.id.btCollection:
                getPresenter().gotoCollection(mActivity);
                break;
            case R.id.btMyDynamic:
                UserInfoActivity.gotoThis(mActivity, false, MServiceManager.getInstance().getCurrentLoginUserId());

                break;
            case R.id.btMyVote:
                break;
            case R.id.btFeedback:
                FeedbackActivity.gotoThis(mActivity);
                break;
            case R.id.btAboutCuckoo:
                AboutCuckooActivity.gotoThis(mActivity);
                break;
            case R.id.btSetting:
                SystemSettingActivity.gotoThis(mActivity);
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        //获取当前登陆的User
        getPresenter().getUserInfos();
    }

    @Override
    public void showToastMsg(String string) {

    }

    @Override
    public void showUserInfo() {
        if (!MServiceManager.getInstance().getUserIsLogin()) {
            userLogout();
            return;
        }
        strUserName.setText(Constant.CURRENT_USER.nickname.get());
        mUserImage.setTag(R.id.loading_image_url, MServiceManager.getInstance().getLocalUserImage());
        ImageLoader.getImageLoader().loadImage(mUserImage,"");
    }

    /**
     * 用户退出登录了
     */
    public void userLogout() {
        strUserName.setText(ResourceUtils.getString(R.string.str_user_notlogin_sign));
        mUserImage.setImageDrawable(ResourceUtils.getDrawable(R.mipmap.photo));

    }

    @Override
    public void changeUserLoginState(boolean isLogin) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 登陆成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventResigister(UserAccoutLoginRes res) {
        if (res != null && res.isLoginSuccess()) {
            showUserInfo();
        }
    }

    /**
     * 登陆成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLogout(UserLogout res) {
        if (res != null && res.isLogout()) {
            userLogout();
        }
    }

}
