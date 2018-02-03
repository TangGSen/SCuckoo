package com.ourcompany.presenter.fragment;

import android.content.Context;

import com.ourcompany.view.fragment.MessageContactsFrameView;

import company.com.commons.framework.presenter.MvpBasePresenter;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/2/3 22:44
 * Des    :
 */

public class ContactsFragPresenter extends MvpBasePresenter<MessageContactsFrameView> {
    public ContactsFragPresenter(Context context) {
        super(context);
    }

    /**
     * 刷新数据
     */
    public void refreshData() {

    }
}
