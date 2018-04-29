package com.ourcompany.bean.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/11 18:30
 * Des    : 使用mob 的userID 作为 这的objectid
 */

public class SUser extends BmobObject {
    //公司名或个人设计师的名称
    private String userName;
    private String imageUrl;
    private String userId;

    //基础信息
    /**
     * 地址
     */
    private String address;
    /**
     * 营业时间
     */
    private String businessHours;
    /**
     * 业务范围
     */
    private String serviceArea;
    /**
     * 承接价格范围
     */
    private String priceRange;
    /**
     * 综合评价
     */
    private Integer evaluation;

    /**
     * 已经选择的布谷平台
     */
    private String cuckooService;

    public String getCuckooService() {
        return cuckooService;
    }

    public void setCuckooService(String cuckooService) {
        this.cuckooService = cuckooService;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public Integer getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Integer evaluation) {
        this.evaluation = evaluation;
    }
}
