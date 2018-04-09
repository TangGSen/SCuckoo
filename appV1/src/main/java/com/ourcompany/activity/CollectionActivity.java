package com.ourcompany.activity;

import android.support.design.widget.TabLayout;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.presenter.fragment.LoginFragPresenter;
import com.ourcompany.view.fragment.LoginFragmentView;

import butterknife.BindView;
import company.com.commons.framework.view.impl.MvpActivity;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 16:11
 * Des    :收藏列表
 */

public class CollectionActivity extends MvpActivity<LoginFragmentView, LoginFragPresenter> implements LoginFragmentView {


    @BindView(R.id.tabLayout)
    TabLayout mTablayout;

    @Override
    protected void initView() {
        super.initView();


    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_layout_message;
    }

    @Override
    protected LoginFragmentView bindView() {
        return this;
    }

    @Override
    protected LoginFragPresenter bindPresenter() {
        return new LoginFragPresenter(MApplication.mContext);
    }

    @Override
    public void showToastMsg(String string) {

    }


}
