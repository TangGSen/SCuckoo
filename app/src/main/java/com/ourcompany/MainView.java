package com.ourcompany;


import company.com.commons.framework.view.MvpView;

/**
 * Created by Administrator on 2017/8/20.
 */

public interface MainView extends MvpView {
    void onLogining();

    void onLoginResult(String result);
}
