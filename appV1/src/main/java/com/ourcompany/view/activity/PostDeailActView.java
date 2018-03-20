package com.ourcompany.view.activity;

import com.ourcompany.bean.bmob.Comment;

import java.util.List;

import company.com.commons.framework.view.MvpView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:03
 * Des    :
 */

public interface PostDeailActView extends MvpView {
    void showEmptyView();
    void showContentView(List<Comment> list);
    void showLoadView();

    void showErrorView();

    void submitError();

    void submitOk();

    void userIsLikeThis(boolean isLike);
}
