package com.ourcompany;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import company.com.commons.framework.presenter.MvpBasePresenter;
import company.com.commons.framework.view.impl.MvpActivity;

public class MainActivity extends MvpActivity<MainView,MvpBasePresenter<MainView>> implements MainView {
    @BindView(R.id.test_id)
    TextView textView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
   //     Bmob.initialize(this,"2db1fc7ea509d3ea6639495a3a24066d");
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected MainView bindView() {
        return this;
    }

    @Override
    protected MvpBasePresenter<MainView> bindPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void onLogining() {

    }

    @Override
    public void onLoginResult(String result) {

    }

    @Override
    public void showToastMsg(String string) {

    }
}
