package com.ourcompany.fragment;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.ourcompany.R;
import com.ourcompany.activity.PostDetailActivity;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.bmob.Post;
import com.ourcompany.presenter.fragment.FoundNewsFragPresenter;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.TimeFormatUtil;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.fragment.FoundNewsFragmentView;
import com.ourcompany.widget.NineGridlayout;
import com.ourcompany.widget.StateFrameLayout;
import com.ourcompany.widget.recycleview.commadapter.OnItemOnclickLinstener;
import com.ourcompany.widget.recycleview.commadapter.RecycleCommonAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;
import com.ourcompany.widget.recycleview.commadapter.SimpleDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/6 22:39
 * Des    : 显示最新的发布信息
 */

public class FoundNewsFragment extends MvpFragment<FoundNewsFragmentView, FoundNewsFragPresenter> implements FoundNewsFragmentView {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.layoutState)
    StateFrameLayout layoutState;
    Unbinder unbinder1;
    private RecycleCommonAdapter recycleCommonAdapter;
    private List<Post> mPostList = new ArrayList<>();
    private int currentIndex;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void showToastMsg(String string) {
        if (!TextUtils.isEmpty(string)) {
            ToastUtils.showSimpleToast(string);
        }

    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MApplication.mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleview.setLayoutManager(linearLayoutManager);
        recycleview.setHasFixedSize(true);
        recycleview.addItemDecoration(new SimpleDecoration(MApplication.mContext, R.drawable.recycle_line_divider_padding, 2));
        recycleCommonAdapter = new RecycleCommonAdapter<Post>(
                MApplication.mContext, mPostList, R.layout.layout_item_post) {
            @Override
            public void bindItemData(SViewHolder holder, Post itemData, int position) {

                holder.setText(R.id.tvUserName, itemData.getUser() == null ? ResourceUtils.getString(R.string.defualt_userName) : TextUtils.isEmpty(itemData.getUser().getUserName()) ? ResourceUtils.getString(R.string.defualt_userName) : itemData.getUser().getUserName());
                holder.setText(R.id.tvContent, itemData.getContent());
                holder.setText(R.id.tvTime, TimeFormatUtil.getIntervalFormString(itemData.getCreatedAt()));
                holder.setImage(R.id.imgUser, itemData.getUser() == null ? "" : itemData.getUser().getImageUrl());
                ((NineGridlayout) holder.getView(R.id.ivNineLayout)).setImagesData(itemData.getImageUrls());
                holder.setText(R.id.likes, itemData.getLikeCount() != null ? itemData.getLikeCount() + "" : "0");
                holder.setText(R.id.comments, itemData.getCommentCount() != null ? itemData.getCommentCount() + "" : "0");
            }


        };
        recycleview.setItemAnimator(null);
        recycleview.setAdapter(recycleCommonAdapter);
        recycleCommonAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
            @Override
            public void itemOnclickLinstener(int position) {
                PostDetailActivity.gotoThis(mActivity, mPostList.get(position));
            }
        });
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                currentIndex = 0;
                getPresenter().getData(currentIndex);

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        getPresenter().getData(currentIndex);
    }

    @Override
    protected void initStateLayout() {
        super.initStateLayout();
        //初始化状态的布局
        View emptyView = getLayoutInflater().inflate(R.layout.layout_state_empty, (ViewGroup) mActivity.findViewById(android.R.id.content), false);
        layoutState.setEmptyView(emptyView);
        layoutState.changeState(StateFrameLayout.LOADING);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_layout_news_found;
    }

    @Override
    protected FoundNewsFragmentView bindView() {
        return this;
    }

    @Override
    protected FoundNewsFragPresenter bindPresenter() {
        return new FoundNewsFragPresenter(MApplication.mContext);
    }


    @Override
    public void showEmptyView() {
        layoutState.changeState(StateFrameLayout.EMPTY);
        closeReflshView();
    }

    @Override
    public void showContentView(List<Post> list) {
        int start = mPostList.size();
        mPostList.addAll(list);
        int end = mPostList.size();
        recycleCommonAdapter.notifyItemRangeChanged(start, end);
        layoutState.changeState(StateFrameLayout.SUCCESS);
        closeReflshView();
    }

    private void closeReflshView() {
        mRefreshLayout.setEnabled(true);
        if (mRefreshLayout.isRefreshing()) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                }
            }, 1000);

        }
    }

    @Override
    public void showLoadView() {
        layoutState.changeState(StateFrameLayout.LOADING);
        closeReflshView();
    }

    @Override
    public void showErrorView() {
        closeReflshView();
        showToastMsg(ResourceUtils.getString(R.string.load_data_fail));
    }
}
