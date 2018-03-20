package com.ourcompany;

import android.content.Context;

import company.com.commons.framework.presenter.MvpBasePresenter;


/**
 * Created by Administrator on 2017/8/20.
 */

public class MainPresenter extends MvpBasePresenter<MainView> {
    private MainMode mainMode;

    public MainPresenter(Context context) {
        super(context);
        mainMode = new MainMode();
    }

    public void login(String usrName, String url) {
        if (isAttachView())
            getView().onLogining();
        mainMode.login(usrName, url);
    }


}
