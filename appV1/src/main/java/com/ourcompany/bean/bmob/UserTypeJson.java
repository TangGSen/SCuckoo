package com.ourcompany.bean.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/30 18:23
 * Des    :
 */

public class UserTypeJson extends BmobObject {
    public String userType;

    public String getUserTypeJson() {
        return userType;
    }

    public void setUserTypeJson(String userTypeJson) {
        this.userType = userTypeJson;
    }
}
