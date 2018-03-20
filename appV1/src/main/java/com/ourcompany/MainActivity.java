package com.ourcompany;

import android.os.Bundle;
import android.widget.TextView;

import com.ourcompany.presenter.fragment.LoginFragPresenter;
import com.ourcompany.view.fragment.LoginFragmentView;

import butterknife.BindView;
import cn.bmob.v3.Bmob;
import company.com.commons.framework.view.impl.MvpActivity;

public class MainActivity extends MvpActivity<LoginFragmentView,LoginFragPresenter> implements LoginFragmentView {
    @BindView(R.id.test_id)
    TextView textView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        Bmob.initialize(this,"2db1fc7ea509d3ea6639495a3a24066d");

    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected LoginFragmentView bindView() {
        return this;
    }

    @Override
    protected LoginFragPresenter bindPresenter() {
        return new LoginFragPresenter(this);
    }


    @Override
    public void showToastMsg(String string) {

    }
}
