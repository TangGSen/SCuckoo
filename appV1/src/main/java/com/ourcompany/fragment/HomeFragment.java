package com.ourcompany.fragment;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.presenter.fragment.LoginFragPresenter;
import com.ourcompany.utils.DisplayUtils;
import com.ourcompany.view.fragment.LoginFragmentView;
import com.ourcompany.widget.ImageCycleView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 16:11
 * Des    :
 */

public class HomeFragment extends MvpFragment<LoginFragmentView, LoginFragPresenter> implements LoginFragmentView {

    @BindView(R.id.head_ImageCycle)
    ImageCycleView headImageCycle;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    Unbinder unbinder;
    private ArrayList<String> urls = new ArrayList<>();

    @Override
    protected void initView(View view) {
        super.initView(view);
        int commomHeight = (int) (DisplayUtils.getInstance(getActivity()).getScreenSize()[1] * 0.3);

        ViewGroup.LayoutParams params = headImageCycle.getLayoutParams();
        params.height = commomHeight;
        headImageCycle.setLayoutParams(params);

        ViewGroup.LayoutParams collapsingLayoutParams = collapsingToolbarLayout.getLayoutParams();
        collapsingLayoutParams.height = commomHeight + getTabLayoutHeight();
        collapsingToolbarLayout.setLayoutParams(collapsingLayoutParams);
    }

    @Override
    protected void initData() {
        super.initData();
        urls.add("http://5.595818.com/2015/pic/000/372/8e32ba756b80507414dcfdd2e0ffec40.jpg");
        urls.add("http://image6.huangye88.com/2013/09/06/13024fa212299bd1.jpg");
        urls.add("http://img0.imgtn.bdimg.com/it/u=2872266855,3141017511&fm=27&gp=0.jpg");
        urls.add("http://image.bao315.com/UploadFiles/image/20150810/20150810164021_3135.jpg");
        headImageCycle.setImageResources(urls, new ImageCycleView.ImageCycleViewListener() {
            @Override
            public void onImageClick(int position, View imageView) {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_act_home;
    }

    @Override
    protected LoginFragmentView bindView() {
        return this;
    }

    @Override
    protected LoginFragPresenter bindPresenter() {
        return new LoginFragPresenter(MApplication.mContext);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private int getTabLayoutHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getContext().getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    @Override
    public void showToastMsg(String string) {

    }

    public void reflesh() {

    }
}