package com.ourcompany.fragment;

import android.view.View;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.presenter.fragment.MineFragPresenter;
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
        if (id == R.id.img_user) {
            //需要判断是否登陆
            getPresenter().onClickUserImage(mActivity,this);
        }
    }

    @Override
    public void showToastMsg(String string) {

    }
}
