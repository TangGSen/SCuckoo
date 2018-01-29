package com.ourcompany.fragment;

import android.view.View;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.presenter.fragment.LoginFragPresenter;
import com.ourcompany.view.fragment.LoginFragmentView;

import butterknife.BindView;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 16:11
 * Des    :
 */

public class FoundFragment extends MvpFragment<LoginFragmentView,LoginFragPresenter> implements LoginFragmentView {
    @BindView(R.id.test_id)
    TextView textView;

    @Override
    protected void initView(View view) {
        super.initView(view);
        textView.setText("FoundFragment");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_layout_found;
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
    public void showToastMsg(String string) {

    }
}
