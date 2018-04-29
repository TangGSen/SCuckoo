package com.ourcompany.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xlhratingbar_lib.XLHRatingBar;
import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.bmob.SUser;
import com.ourcompany.presenter.activity.UserClassifyActPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LocationOption;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.TabLayoutIndicatorWith;
import com.ourcompany.view.activity.UserClassifyActView;
import com.ourcompany.widget.StateFrameLayout;
import com.ourcompany.widget.recycleview.commadapter.OnItemOnclickLinstener;
import com.ourcompany.widget.recycleview.commadapter.RecycleCommonAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;
import com.ourcompany.widget.recycleview.commadapter.SimpleDecoration;
import com.ourcompany.widget.recycleview.headfooter.MFooter;
import com.ourcompany.widget.recycleview.headfooter.MHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/4/24 10:46
 * Des    : 设计，施工，监理维修的分类列表+搜索
 */

public class UserClassifyActivity extends MvpActivity<UserClassifyActView, UserClassifyActPresenter> implements UserClassifyActView {

    private static final String KEY_INTENT = "key_intent";
    private static final String KEY_BUNDLE_TITLE = "key_bundle_position";
    @BindView(R.id.btAddress)
    TextView btAddress;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.btSerach)
    ImageView btSerach;
    @BindView(R.id.titleBar)
    RelativeLayout titleBar;
    @BindView(R.id.tabLayout)
    TabLayout mTablayout;
    @BindView(R.id.btClassSerach)
    TextView btClassSerach;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.layoutState)
    StateFrameLayout layoutState;
    private String mTitle;
    private String[] mTiltes;
    private LocationOption.MLocation mLocation;
    private RecycleCommonAdapter<SUser> recycleCommonAdapter;
    private List<SUser> mUserList = new ArrayList<>();
    private int currentIndex;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_classify;
    }

    @Override
    protected void initView() {
        super.initView();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MApplication.mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //解决嵌套在NestedScrollView 的滑动不顺的问题1

        recycleview.setLayoutManager(linearLayoutManager);
        recycleview.setHasFixedSize(true);
        //解决嵌套在NestedScrollView 的滑动不顺的问题2
        recycleview.setNestedScrollingEnabled(true);
        refreshLayout.setEnableRefresh(true);
        recycleview.addItemDecoration(new SimpleDecoration(MApplication.mContext, R.drawable.recycle_line_divider_padding, 1));

        recycleCommonAdapter = new RecycleCommonAdapter<SUser>(
                MApplication.mContext, mUserList, R.layout.layout_item_class_suser) {
            @Override
            public void bindItemData(SViewHolder holder, final SUser itemData, int position) {
                holder.setText(R.id.tvUserName, TextUtils.isEmpty(itemData.getUserName()) ? ResourceUtils.getString(R.string.defualt_userName) : itemData.getUserName());
                holder.setText(R.id.tvAddress, itemData.getAddress());
                holder.setText(R.id.tvCuckooService, itemData.getCuckooService());
                holder.setImage(R.id.imageUser, itemData.getImageUrl());
                int evaluation = itemData.getEvaluation() == null ? 0 : itemData.getEvaluation() > Constant.START_COUNT ? Constant.START_COUNT : itemData.getEvaluation() < 0 ? 0 : itemData.getEvaluation();
                ((XLHRatingBar) holder.getView(R.id.ratingBar)).setCountSelected(evaluation);
            }
        };
        recycleview.setItemAnimator(null);
        recycleview.setAdapter(recycleCommonAdapter);
        recycleCommonAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
            @Override
            public void itemOnclickLinstener(int position) {
            }
        });


        refreshLayout.setRefreshHeader(new MHeader(UserClassifyActivity.this).setEnableLastTime(false).setTextSizeTitle(14).setAccentColor(ResourceUtils.getResColor(R.color.text_gray)).setFinishDuration(100));
        refreshLayout.setRefreshFooter(new MFooter(UserClassifyActivity.this).setTextSizeTitle(14).setSpinnerStyle(SpinnerStyle.Scale).setAccentColor(ResourceUtils.getResColor(R.color.text_gray)).setFinishDuration(100));
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //按照时间来加载，刷新是加载比第一个更晚的时间，
                if (mUserList != null && mUserList.size() > 0) {
                    getPresenter().getDataOnReFresh(mUserList.get(mUserList.size() - 1).getCreatedAt(), mUserList.get(mUserList.size() - 1).getObjectId());
                } else {
                    //传空默认传当前时间
                    getPresenter().getDataOnReFresh("", "");
                }
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++currentIndex;
                getPresenter().getDataOnLoadMore(currentIndex);
            }
        });
    }

    public static void gotoThis(Context context, String title) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserClassifyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE_TITLE, title);
        intent.putExtra(KEY_INTENT, bundle);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        Bundle bun = getIntent().getBundleExtra(KEY_INTENT);
        if (bun != null) {
            mTitle = bun.getString(KEY_BUNDLE_TITLE);
        }

        return super.initArgs(bundle);
    }

    @Override
    protected void initStateLayout() {
        super.initStateLayout();
        //初始化状态的布局
        View emptyView = getLayoutInflater().inflate(R.layout.layout_state_empty_with_retry, (ViewGroup) this.findViewById(android.R.id.content), false);
        layoutState.setEmptyView(emptyView);
        layoutState.changeState(StateFrameLayout.LOADING);

    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        tvTitle.setText(TextUtils.isEmpty(mTitle) ? "" : mTitle);
        TabLayoutIndicatorWith.resetWith(mTablayout);

        mTiltes = ResourceUtils.getStringArray(R.array.tabUserClassInfoItems);
        for (int i = 0; i < mTiltes.length; i++) {
            mTablayout.addTab(mTablayout.newTab().setText(mTiltes[i]));
        }
        //tablayout 和viewpager 联动
        mTablayout.addOnTabSelectedListener(new MOnTabSelectedListener());
        //开始定位
        getRootView().post(new Runnable() {
            @Override
            public void run() {
                getPresenter().getLoactionInfo(false, UserClassifyActivity.this);
            }
        });
        getPresenter().getData(currentIndex, false);
    }

    @Override
    public void showMLocation(LocationOption.MLocation location, boolean isCityPick) {
        mLocation = location;
        if (mLocation != null) {

            showAddressText(location.getCity(), isCityPick);
        } else {
            showAddressText(null, isCityPick);
        }
    }

//    onConfigurationChanged

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.e("sen", "onConfigurationChanged");
    }

    /**
     * 显示地址
     *
     * @param city
     * @param isCityPick
     */
    private void showAddressText(String city, boolean isCityPick) {
        if (!TextUtils.isEmpty(city)) {
            LogUtils.e("sen", "city 实质显示");
            btAddress.setText(city);
            if (isCityPick && mLocation != null) {
                LogUtils.e("sen", "city  pick success ");
                CityPicker.getInstance()
                        .locateComplete(new LocatedCity(mLocation.getCity(), mLocation.getProvince(), mLocation.getCityCode()),
                                LocateState.SUCCESS);
            }
        } else {
            LogUtils.e("sen", "city error");
            if (isCityPick) {
                CityPicker.getInstance()
                        .locateComplete(new LocatedCity(ResourceUtils.getString(R.string.str_address_error), "0", "0"),
                                LocateState.FAILURE);
            }
            btAddress.setText(ResourceUtils.getString(R.string.str_address_error));
        }
    }

    @Override
    public void showLoactionError(boolean isCityPick) {
        showAddressText(null, isCityPick);
    }


    class MOnTabSelectedListener implements TabLayout.OnTabSelectedListener {
        public MOnTabSelectedListener() {
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int position = tab.getPosition();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }

    @Override
    protected UserClassifyActView bindView() {
        return this;
    }

    @Override
    protected UserClassifyActPresenter bindPresenter() {
        return new UserClassifyActPresenter(MApplication.mContext);
    }


    @Override
    public void showToastMsg(String string) {

    }

    @OnClick({R.id.btAddress, R.id.btSerach, R.id.btClassSerach})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btAddress:
                showAddress();
                break;
            case R.id.btSerach:
                break;
            case R.id.btClassSerach:
                break;
        }
    }

    private void showAddress() {
        CityPicker.getInstance()
                .setFragmentManager(getSupportFragmentManager())
                .enableAnimation(true)
                .setAnimationStyle(R.style.DefaultCityPickerAnimation)
                .setLocatedCity(mLocation != null ? new LocatedCity(mLocation.getCity(), mLocation.getProvince(), mLocation.getCityCode()) : null)
                .setHotCities(null)
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        if (data == null) {
                            LogUtils.e("sen", "city 没选");
                            if (mLocation != null) {
                                LogUtils.e("sen", "city 选本地的");
                                showAddressText(mLocation.getCity(), false);
                            } else {
                                LogUtils.e("sen", "city 没选，并本地也没");
                                showAddressText(null, false);
                            }
                        } else {
                            LogUtils.e("sen", "city 已选,已修改");
                            showAddressText(data.getName(), false);
                        }
                    }

                    @Override
                    public void onLocate() {
                        LogUtils.e("sen", "onLocate开始定位");
                        //开始定位
                        getPresenter().getLoactionInfo(true, UserClassifyActivity.this);
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getPresenter().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.e("sen", "onLocate开始定位");
    }

    @Override
    public void showEmptyView() {
        layoutState.changeState(StateFrameLayout.EMPTY);
    }

    @Override
    public void showContentView(List<SUser> list) {
        LogUtils.e("sen", "list" + list.size());
        recycleCommonAdapter.addDatasInLast(list);
        layoutState.changeState(StateFrameLayout.SUCCESS);
    }


    @Override
    public void showLoadView() {
        layoutState.changeState(StateFrameLayout.LOADING);
    }

    @Override
    public void showErrorView() {
        showToastMsg(ResourceUtils.getString(R.string.load_data_fail));
    }

    @Override
    public void showOnReflsh(List<SUser> list) {
        refreshLayout.finishRefresh(0, true);
        recycleCommonAdapter.addDatasInFirst(0, list);
        recycleview.scrollToPosition(0);
    }

    @Override
    public void showOnReflshNoNewsData() {
        showToastMsg(ResourceUtils.getString(R.string.str_reflsh_no_new_data));
        refreshLayout.finishRefresh(0, true);
    }

    @Override
    public void showOnReflshError() {
        showToastMsg(ResourceUtils.getString(R.string.str_reflesh_error));
        refreshLayout.finishRefresh(0, false);

    }

    @Override
    public void showOnLoadError() {
        refreshLayout.finishLoadMore(0, false, false);
        showToastMsg(ResourceUtils.getString(R.string.str_onload_error));

    }

    @Override
    public void showOnLoadFinish() {
        //如果是没有更新的数据时需要停止刷新半分钟，防止频繁的刷新
        refreshLayout.finishLoadMore(0, true, false);
    }

    @Override
    public void showOnloadMoreNoData() {
        refreshLayout.finishLoadMore(0, true, true);
    }

}