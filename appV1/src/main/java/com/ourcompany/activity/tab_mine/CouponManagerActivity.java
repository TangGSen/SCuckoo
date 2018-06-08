package com.ourcompany.activity.tab_mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ourcompany.EmptyMvpPresenter;
import com.ourcompany.EmptyMvpView;
import com.ourcompany.R;
import com.ourcompany.adapter.TabLayoutViewPagerAdapter;
import com.ourcompany.app.MApplication;
import com.ourcompany.fragment.tab_mine.CouponManagerFragment;
import com.ourcompany.interfaces.MOnTabSelectedListener;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.TabLayoutIndicatorWith;
import com.ourcompany.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/1 下午2:08
 * Des    :
 */
public class CouponManagerActivity extends MvpActivity<EmptyMvpView, EmptyMvpPresenter> implements EmptyMvpView {


    @BindView(R.id.tabLayout)
    TabLayout mTablayout;
    @BindView(R.id.addCoupon)
    TextView addCoupon;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    private ArrayList<Fragment> fragments;
    private String[] mTiltes;
    //所属的公司的，或者是个人的优惠券，如果为空那么直接就为加载失败或者为空


    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    public static void gotoThis(Context context) {
        Intent intent = new Intent(context, CouponManagerActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(commonToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
        initTabView();
    }

    private void initTabView() {
        TabLayoutIndicatorWith.resetWith(mTablayout);
        fragments = new ArrayList<>();

        mTiltes = ResourceUtils.getStringArray(R.array.tabCouponItems);
        for (int i = 0; i < mTiltes.length; i++) {
            mTablayout.addTab(mTablayout.newTab().setText(mTiltes[i]));

        }
        //需要加个标识是未过期，已过期的类型标识,0 代表未过期，1代表已过期
        CouponManagerFragment notOverdueFrag = new CouponManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CouponManagerFragment.KEY_TYPE,CouponManagerFragment.TYPE_NOT_OVERDUE);
        notOverdueFrag.setArguments(bundle);
        fragments.add(notOverdueFrag);

        CouponManagerFragment overdueFrag = new CouponManagerFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt(CouponManagerFragment.KEY_TYPE,CouponManagerFragment.TYPE_OVERDUE);
        overdueFrag.setArguments(bundle2);
        fragments.add(overdueFrag);


        TabLayoutViewPagerAdapter viewPagerAdapter = new TabLayoutViewPagerAdapter(getSupportFragmentManager(), mTiltes, fragments);
        //tablayout 和viewpager 联动
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
        mTablayout.addOnTabSelectedListener(new MOnTabSelectedListener(mViewPager));
        mViewPager.setCurrentItem(0);
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    //    /**
//     * 改变SVG图片着色
//     * @param imageView
//
//     */
//    public void changeSVGColor(TextView imageView){
//
//       // drawable1.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//
//        VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(getResources(),R.drawable.ic_coupon,getTheme());
//        //你需要改变的颜色
//        vectorDrawableCompat.setTint(ResourceUtils.getResColor(R.color.colorPrimary));
//
//        imageView.setCompoundDrawables(vectorDrawableCompat,null,null,null);
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_coupon_manager;
    }

    @Override
    protected EmptyMvpView bindView() {
        return this;
    }

    @Override
    protected EmptyMvpPresenter bindPresenter() {
        return new EmptyMvpPresenter(MApplication.mContext);
    }


    @OnClick(R.id.addCoupon)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.addCoupon:
                AddCouponActivity.gotoThis(CouponManagerActivity.this);
                break;
        }
    }
}
