package com.ourcompany.utils;

import com.mob.ums.User;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/26 11:21
 * Des    :
 */

public class Constant {
    //获取验证码的时间间隔，单位秒
    public static final int SAFETY_CODE_TIME_INTERVAL = 30;
    public static final String COUNTRY_CODE = "86";

    //Actiivity bundle 字段
    public static final String ACT_FROM = "act_from";

    public static final String ACT_FROM_RESIGISTER = "act_from_resigister";
    public static final String ACT_LOGIN_BUNDLE = "act_login_bundle";
    public static final String ACT_LOGIN_PHONE = "act_login_phone";

    //来自登陆注册成功跳转到HomeActivity
    public static final String ACT_FROM_LOGIN_SUCCESS = "act_from_login_success";

    //保存当前的user
    public static  User CURRENT_USER = null;
}
