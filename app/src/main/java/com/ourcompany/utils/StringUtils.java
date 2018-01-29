package com.ourcompany.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/26 20:29
 * Des    :
 */

public class StringUtils {
    /**
     * 判断传入的字符串是否是一个手机号码
     *
     * @param strPhone
     * @return
     */
    public static boolean isPhoneNumber(String strPhone) {

        String str = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9])|(14[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(strPhone);
        return m.find();
    }

}
