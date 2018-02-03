package com.ourcompany.fragment.message;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mob.imsdk.model.IMConversation;
import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.ChatConversations;
import com.ourcompany.presenter.fragment.ChatFragPresenter;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.fragment.ChatFrameView;
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
 * On     : 2018/1/18 16:11
 * Des    : 最近聊天，会话功能页面
 */

public class ChatFragment extends MvpFragment<ChatFrameView, ChatFragPresenter> implements ChatFrameView {
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    Unbinder unbinder;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    Unbinder unbinder1;
    private List<IMConversation> mMessages = new ArrayList<>();
    private RecycleCommonAdapter<IMConversation> recycleCommonAdapter;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    protected void initView(View view) {
        super.initView(view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MApplication.mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleview.setLayoutManager(linearLayoutManager);
        recycleview.setHasFixedSize(true);
        recycleview.addItemDecoration(new SimpleDecoration(MApplication.mContext, R.drawable.recycle_line_divider, 5));
        recycleCommonAdapter = new RecycleCommonAdapter<IMConversation>(
                MApplication.mContext, mMessages, R.layout.layout_item_message_chat) {
            @Override
            public void bindItemData(SViewHolder holder, IMConversation itemData, int position) {
                holder.setText(R.id.tvUserName, itemData.getOtherInfo().getNickname());
                holder.setText(R.id.tvMessage, itemData.getLastMessage().getBody());
                holder.setText(R.id.tvTime, itemData.getCreateTime() + "");
            }
        };
        recycleview.setAdapter(recycleCommonAdapter);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                getPresenter().refreshData();
            }
        });
    }


    @Override
    protected void initData() {
        super.initData();
        //先加载会话
        getPresenter().initIMReceiver();

    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_layout_message_chat;
    }

    @Override
    protected ChatFrameView bindView() {
        return this;
    }

    @Override
    protected ChatFragPresenter bindPresenter() {
        return new ChatFragPresenter(MApplication.mContext);
    }

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }


    @Override
    public void showLoadingFailed() {
        ToastUtils.showSimpleToast("showLoadingFailed");
        mRefreshLayout.setEnabled(true);
        if (mRefreshLayout.isRefreshing()) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                }
            },1000);

        }
    }

    @Override
    public void showContentView(ChatConversations conversations) {
        List<IMConversation> messages = conversations.getList();

        if (conversations != null && messages != null && messages.size() > 0) {
            mMessages.addAll(messages);
            recycleCommonAdapter.notifyDataSetChanged();
        } else {
            showToastMsg("没有数据");
        }
        mRefreshLayout.setEnabled(true);
        if (mRefreshLayout.isRefreshing()) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                }
            },1000);

        }
    }
}
