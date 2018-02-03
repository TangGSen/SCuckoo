package com.ourcompany.fragment.message;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mob.imsdk.model.IMConversation;
import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.ChatConversations;
import com.ourcompany.presenter.fragment.ContactsFragPresenter;
import com.ourcompany.view.fragment.MessageContactsFrameView;
import com.ourcompany.widget.recycleview.commadapter.RecycleCommonAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;
import com.ourcompany.widget.recycleview.commadapter.SimpleDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 16:11
 * Des    : 最近聊天，联系人页面
 */

public class MessageContactsFragment extends MvpFragment<MessageContactsFrameView, ContactsFragPresenter> implements MessageContactsFrameView {


    @BindView(R.id.recycleview)
    RecyclerView mRecycleview;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    private RecycleCommonAdapter recycleCommonAdapter;
    private List<IMConversation> mMessages = new ArrayList<>();

    @Override
    protected void initView(View view) {
        super.initView(view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MApplication.mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleview.setLayoutManager(linearLayoutManager);
        mRecycleview.setHasFixedSize(true);
        mRecycleview.addItemDecoration(new SimpleDecoration(MApplication.mContext, R.drawable.recycle_line_divider, 5));
        recycleCommonAdapter = new RecycleCommonAdapter<IMConversation>(
                MApplication.mContext, mMessages, R.layout.layout_item_message_chat) {
            @Override
            public void bindItemData(SViewHolder holder, IMConversation itemData, int position) {
                holder.setText(R.id.tvUserName, itemData.getOtherInfo().getNickname());
                holder.setText(R.id.tvMessage, itemData.getLastMessage().getBody());
                holder.setText(R.id.tvTime, itemData.getCreateTime() + "");
            }
        };
        mRecycleview.setAdapter(recycleCommonAdapter);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                getPresenter().refreshData();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_layout_message_contacts;
    }

    @Override
    protected MessageContactsFrameView bindView() {
        return this;
    }

    @Override
    protected ContactsFragPresenter bindPresenter() {
        return new ContactsFragPresenter(MApplication.mContext);
    }

    @Override
    public void showToastMsg(String string) {

    }


    @Override
    public void showLoadingFailed() {

    }

    @Override
    public void showContentView(ChatConversations conversations) {

    }
}
