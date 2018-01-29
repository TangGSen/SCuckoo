package com.ourcompany.app;

import android.app.Application;
import android.content.Context;

import com.mob.MobSDK;

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
        MobSDK.init(this);
    }
}
