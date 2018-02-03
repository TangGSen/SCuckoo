package com.ourcompany.manager;

import android.content.Context;

import com.mob.MobSDK;
import com.mob.ums.UMSSDK;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/30 21:58
 * Des    : 账号注册和登陆，聊天等的封装
 */

public class MServiceManager {
    private volatile static MServiceManager instance;

    private MServiceManager() {
    }

    public static MServiceManager getInstance() {
        if (instance == null) {
            synchronized (MServiceManager.class) {
                if (instance == null) {
                    instance = new MServiceManager();
                }
            }
        }
        return instance;
    }

    /**
     * 注册IM 的账号
     *
     * @param username
     * @param password
     */
    public int resigisterAccount(String username, String password) {
        return 0;
    }


    //注册成功需要登陆
    public void login(String userId, String username, String imagUrl) {
        //先做一些事情
        MobSDK.setUser(userId, username, imagUrl, null);
    }

    /**
     * 1.初始化 mob SDK
     */
    public void init(Context context) {
        MobSDK.init(context);
    }


    public boolean isUserLogin() {
        String userId = UMSSDK.getLoginUserId();
        return false;
    }

    public void getUserInfos() {

    }
}
