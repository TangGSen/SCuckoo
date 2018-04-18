package com.ourcompany.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.presenter.activity.SystemSettingActPresenter;
import com.ourcompany.view.activity.SystemSettingActView;

import butterknife.BindView;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;

public class SystemSettingActivity extends MvpActivity<SystemSettingActView, SystemSettingActPresenter> implements SystemSettingActView {

    @BindView(R.id.btEditexInfo)
    TextView btEditexInfo;
    @BindView(R.id.btAccountSetting)
    TextView btAccountSetting;
    @BindView(R.id.btAccpetPush)
    TextView btAccpetPush;
    @BindView(R.id.swith_push)
    Switch swithPush;
    @BindView(R.id.btCurrentVersion)
    TextView btCurrentVersion;
    @BindView(R.id.btCheckNewVersion)
    TextView btCheckNewVersion;
    @BindView(R.id.btLogout)
    TextView btLogout;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;

    public static void gotoThis(Context context) {
        Intent intent = new Intent(context, SystemSettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_layout_systemsetting;
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(commonToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });


    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //延时获取版本号
        getPresenter().getLocalVersionName();
    }

    @Override
    protected SystemSettingActView bindView() {
        return this;
    }

    @Override
    protected SystemSettingActPresenter bindPresenter() {
        return new SystemSettingActPresenter(this);
    }


    @Override
    public void showToastMsg(String string) {

    }


    @OnClick({R.id.btEditexInfo, R.id.btAccountSetting, R.id.btCheckNewVersion, R.id.btLogout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btEditexInfo:
                EditextUserInfoActivity.gotoThis(SystemSettingActivity.this);
                break;
            case R.id.btAccountSetting:
                AccountSettingAcitivity.gotoThis(SystemSettingActivity.this);
                break;
            case R.id.btAccpetPush:
                break;
            case R.id.btCheckNewVersion:

                break;
            case R.id.btLogout:
                break;
        }
    }

    @Override
    public void setLocalVersionName(String localVersion) {
        btCurrentVersion.setText(localVersion);
    }
}
