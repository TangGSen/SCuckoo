package com.ourcompany.app;

import android.app.Application;
import android.content.Context;

import com.ourcompany.manager.MServiceManager;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/16 16:59
 * Des    :
 */

public class MApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        MServiceManager.getInstance().init(this);
    }
}
