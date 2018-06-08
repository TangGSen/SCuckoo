package com.ourcompany.fragment.coupon;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ourcompany.EmptyMvpPresenter;
import com.ourcompany.EmptyMvpView;
import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.utils.ToastUtils;

import butterknife.BindView;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/5 下午3:52
 * Des    :
 */
public class CouponNameFragment extends MvpFragment<EmptyMvpView, EmptyMvpPresenter> implements EmptyMvpView {
    public static final String KEY_CURRENT_INDEX = "current_index";
    public static final String KEY_COUNT = "count";
    @BindView(R.id.tvPosition)
    TextView tvPosition;
    @BindView(R.id.tvInputTip)
    TextView tvInputTip;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.tvInputCount)
    TextView tvInputCount;
    Unbinder unbinder;
    private int currentIndex;
    private int count;

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        currentIndex = bundle.getInt(KEY_CURRENT_INDEX);
        count = bundle.getInt(KEY_COUNT);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        tvPosition.setText(currentIndex+"/"+count);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_coupon_name;
    }

    @Override
    protected EmptyMvpView bindView() {
        return this;
    }

    @Override
    protected EmptyMvpPresenter bindPresenter() {
        return new EmptyMvpPresenter(MApplication.mContext);
    }


}
