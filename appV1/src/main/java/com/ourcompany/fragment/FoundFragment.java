package com.ourcompany.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ourcompany.R;
import com.ourcompany.adapter.ViewPagerAdapter;
import com.ourcompany.app.MApplication;
import com.ourcompany.presenter.fragment.FoundFragPresenter;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.TabLayoutIndicatorWith;
import com.ourcompany.view.fragment.FoundFragmentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 16:11
 * Des    :
 */

public class FoundFragment extends MvpFragment<FoundFragmentView, FoundFragPresenter> implements FoundFragmentView {

    @BindView(R.id.message_viewpager)
    ViewPager mViewPager;

    @BindView(R.id.tabLayout)
    TabLayout mTablayout;
    String mTiltes[];
    private List<Fragment> fragments;

    @Override
    protected void initView(View view) {
        super.initView(view);
        TabLayoutIndicatorWith.resetWith(mTablayout);
        fragments = new ArrayList<>();

        mTiltes = ResourceUtils.getStringArray(R.array.tabFoundItems);
        for (int i = 0; i < mTiltes.length; i++) {
            mTablayout.addTab(mTablayout.newTab().setText(mTiltes[i]));
            FoundNewsFragment testMobFragment = new FoundNewsFragment();
            fragments.add(testMobFragment);
        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), mTiltes, fragments);
        //tablayout 和viewpager 联动
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
        mTablayout.addOnTabSelectedListener(new MOnTabSelectedListener());
        mViewPager.setCurrentItem(0);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_layout_message;
    }

    @Override
    protected FoundFragmentView bindView() {
        return this;
    }

    @Override
    protected FoundFragPresenter bindPresenter() {
        return new FoundFragPresenter(MApplication.mContext);
    }

    @Override
    public void showToastMsg(String string) {

    }


    class MOnTabSelectedListener implements TabLayout.OnTabSelectedListener {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int position = tab.getPosition();
            mViewPager.setCurrentItem(position);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }
}
