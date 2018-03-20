package com.ourcompany.fragment;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.presenter.fragment.MineFragPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.view.fragment.MineFragmentView;

import butterknife.BindView;
import butterknife.OnClick;
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


    @Override
    protected void initView(View view) {
        super.initView(view);
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

    @OnClick(R.id.img_user)
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_user:
                //需要判断是否登录
                getPresenter().onClickUserImage(mActivity, this);
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
        LogUtils.e("sen",Constant.CURRENT_USER.signature.get());
        strUserSign.setText(Constant.CURRENT_USER.signature.get());

    }

    @Override
    public void changeUserLoginState(boolean isLogin) {

    }


    public void reflesh() {
        showUserInfo();
    }
}
