package com.ourcompany.presenter.fragment;

import android.content.Context;

import com.ourcompany.bean.bmob.Post;
import com.ourcompany.utils.Constant;
import com.ourcompany.view.fragment.FoundNewsFragmentView;

import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import company.com.commons.framework.presenter.MvpBasePresenter;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:04
 * Des    :
 */

public class FoundNewsFragPresenter extends MvpBasePresenter<FoundNewsFragmentView> {
    public FoundNewsFragPresenter(Context context) {
        super(context);
    }

    public void refreshData() {

    }

    public void getData(final int start) {
        BmobQuery<Post> query = new BmobQuery<Post>();
        query.include(Constant.BMOB_POST_USER);
        query.order(Constant.BMOB_ORDER_DESCENDING+Constant.BMOB_CREATE);
        //查询playerName叫“比目”的数据
        query.addWhereLessThanOrEqualTo(Constant.BMOB_CREATE, new BmobDate(new Date(System.currentTimeMillis())));
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(Constant.IM_PAGESIZE);
        //执行查询方法
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(final List<Post> list, BmobException e) {
                if (e == null) {
                    if (start == 0 && list.size() <= 0) {
                        showEmptyView();
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                getView().showContentView(list);
                            }
                        });
                    }
                } else {
                    getView().showErrorView();
                }
            }
        });
    }

    private void showEmptyView() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                getView().showEmptyView();
            }
        });
    }

    private void showErrorView() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                getView().showErrorView();
            }
        });
    }
}
