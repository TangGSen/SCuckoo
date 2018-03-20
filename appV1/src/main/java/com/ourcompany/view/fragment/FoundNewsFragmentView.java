package com.ourcompany.view.fragment;

import com.ourcompany.bean.bmob.Post;

import java.util.List;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/6 21:58
 * Des    :
 */

public interface FoundNewsFragmentView extends MvpView {
    void showEmptyView();
    void showContentView(List<Post> list);
    void showLoadView();

    void showErrorView();
}
