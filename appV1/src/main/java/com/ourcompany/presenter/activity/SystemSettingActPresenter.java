package com.ourcompany.presenter.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.activity.SystemSettingActView;

import company.com.commons.framework.presenter.MvpBasePresenter;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:04
 * Des    :
 */

public class SystemSettingActPresenter extends MvpBasePresenter<SystemSettingActView> {

    public SystemSettingActPresenter(Context context) {
        super(context);
    }


    public void getLocalVersionName() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String localVersion = "";
                try {
                    PackageInfo packageInfo = MApplication.mContext
                            .getPackageManager()
                            .getPackageInfo(MApplication.mContext.getPackageName(), 0);
                    localVersion = String.format(ResourceUtils.getString(R.string.str_current_version), packageInfo.versionName);
                    getView().setLocalVersionName(localVersion);
                } catch (PackageManager.NameNotFoundException e) {
                }
            }
        }, 1000);

    }
}
