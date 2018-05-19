package com.ourcompany.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xlhratingbar_lib.XLHRatingBar;
import com.ourcompany.R;
import com.ourcompany.adapter.TabLayoutViewPagerAdapter;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.bmob.SUser;
import com.ourcompany.fragment.user_class_detail.UserClassBaseInfoFragment;
import com.ourcompany.fragment.user_class_detail.UserClassTeamFragment;
import com.ourcompany.interfaces.MOnTabSelectedListener;
import com.ourcompany.presenter.activity.UserDetailActPresenter;
import com.ourcompany.utils.DisplayUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.TabLayoutIndicatorWith;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.UserClassifyDetailActView;
import com.ourcompany.widget.recycleview.commadapter.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import company.com.commons.framework.view.impl.MvpActivity;


public class UserClassifyDetailActivity extends MvpActivity<UserClassifyDetailActView, UserDetailActPresenter> implements UserClassifyDetailActView {


    private static final String KEY_BUNDLE_USER = "bundle_user";
    private static final String KEY_INTENT = "intent";
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.imageUser)
    ImageView imageUser;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.ratingBar)
    XLHRatingBar ratingBar;
    @BindView(R.id.mDiviedView)
    View mDiviedView;
    @BindView(R.id.tabLayout)
    TabLayout mTablayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    private String[] mTiltes;
    private ArrayList<Fragment> fragments;
    private SUser mUser = null;

    @Override
    public void showToastMsg(String string) {
        if (TextUtils.isEmpty(string)) return;
        ToastUtils.showSimpleToast(string);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_detail;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        Bundle res = getIntent().getBundleExtra(KEY_INTENT);
        if (res != null) {
            mUser = (SUser) res.getSerializable(KEY_BUNDLE_USER);
            if (mUser == null) {
                finish();
            }
        } else {
            finish();
        }
        return super.initArgs(bundle);

    }


    public static void gotoThis(Context context, SUser sUser) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserClassifyDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_BUNDLE_USER, sUser);
        intent.putExtra(KEY_INTENT, bundle);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(commonToolbar);
        commonToolbar.setNavigationIcon(R.drawable.ic_back_v3);
        commonToolbar.setContentInsetStartWithNavigation(0);
        commonToolbar.setBackground(null);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        TabLayoutIndicatorWith.resetWith(mTablayout);
        fragments = new ArrayList<>();


        mTiltes = ResourceUtils.getStringArray(R.array.tabUserClassifyDetailItems);
        for (int i = 0; i < mTiltes.length; i++) {
            mTablayout.addTab(mTablayout.newTab().setText(mTiltes[i]));
            if (i == 1) {
                UserClassTeamFragment teamFragment = new UserClassTeamFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(KEY_BUNDLE_USER, mUser);
//                    infoFragment.setArguments(bundle);
                fragments.add(teamFragment);
            } else {
                UserClassBaseInfoFragment infoFragment = new UserClassBaseInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_BUNDLE_USER, mUser);
                infoFragment.setArguments(bundle);
                fragments.add(infoFragment);
            }
        }

        TabLayoutViewPagerAdapter viewPagerAdapter = new TabLayoutViewPagerAdapter(getSupportFragmentManager(), mTiltes, fragments);
        //tablayout 和viewpager 联动
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
        mTablayout.addOnTabSelectedListener(new MOnTabSelectedListener(mViewPager));
        mViewPager.setCurrentItem(0);
        if (mUser != null)

        {
            tvUserName.setText(TextUtils.isEmpty(mUser.getUserName())
                    ? ResourceUtils.getString(R.string.defualt_userName) : mUser.getUserName());
            ratingBar.setCountSelected(mUser.getEvaluation());
            imageUser.setTag(R.id.loading_image_url, mUser.getImageUrl());
            ImageLoader.getImageLoader().loadImageRound(imageUser, "", DisplayUtils.dip2px(2));
        }

    }

    @Override
    protected UserClassifyDetailActView bindView() {
        return this;
    }

    @Override
    protected UserDetailActPresenter bindPresenter() {
        return new UserDetailActPresenter(MApplication.mContext);
    }


}
