package com.ourcompany.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ourcompany.R;
import com.ourcompany.adapter.ViewPagerAdapter;
import com.ourcompany.app.MApplication;
import com.ourcompany.fragment.message.ChatFragment;
import com.ourcompany.fragment.message.MessageContactsFragment;
import com.ourcompany.presenter.fragment.LoginFragPresenter;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.fragment.LoginFragmentView;

import java.util.ArrayList;
import java.util.List;

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

public class MessageFragment extends MvpFragment<LoginFragmentView, LoginFragPresenter> implements LoginFragmentView {

    @BindView(R.id.message_viewpager)
    ViewPager mViewPager;

    Unbinder unbinder;
    @BindView(R.id.tabLayout)
    TabLayout mTablayout;
    Unbinder unbinder1;
    private List<Fragment> fragments;
    String mTiltes[];
    @Override
    protected void initView(View view) {
        super.initView(view);
        fragments  = new ArrayList<>();
        ChatFragment chatFragment = new ChatFragment();
        MessageContactsFragment contactsFragment = new MessageContactsFragment();
        fragments.add(chatFragment);
        fragments.add(contactsFragment);

        mTiltes = ResourceUtils.getStringArray( R.array.tabMessageItems);
        for (int i = 0;i<mTiltes.length;i++){
            mTablayout.addTab(mTablayout.newTab().setText(mTiltes[i]));
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
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
