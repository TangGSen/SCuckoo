package com.ourcompany.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.activity.MessageActivity;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.UserAccoutLoginRes;
import com.ourcompany.presenter.fragment.MineFragPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.view.fragment.MineFragmentView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.str_user_sign)
    TextView strUserSign;
    @BindView(R.id.class_toolbar)
    Toolbar classToolbar;
    @BindView(R.id.personal_rootLayout)
    CoordinatorLayout personalRootLayout;
    @BindView(R.id.btnMessage)
    TextView btnMessage;
    Unbinder unbinder;
    @BindView(R.id.btCollection)
    TextView btCollection;


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

    @OnClick({R.id.img_user, R.id.btnMessage,R.id.btCollection})
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
        if (Constant.CURRENT_USER == null) {
            return;
        }
        LogUtils.e("sen", Constant.CURRENT_USER.signature.get());
        strUserSign.setText(Constant.CURRENT_USER.signature.get());

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
