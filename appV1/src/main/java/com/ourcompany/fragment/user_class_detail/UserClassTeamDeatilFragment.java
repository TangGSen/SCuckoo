package com.ourcompany.fragment.user_class_detail;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.activity.imui.UserInfoActivity;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.bmob.Comment;
import com.ourcompany.presenter.activity.UserClassTeamDetailFragPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.TimeFormatUtil;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.fragment.UserClassTeamDetailFragView;
import com.ourcompany.widget.StateFrameLayout;
import com.ourcompany.widget.recycleview.commadapter.OnItemOnclickLinstener;
import com.ourcompany.widget.recycleview.commadapter.RecycleCommonAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/5/16 10:29
 * Des    : 公司或个人的team,详细过程
 */

public class UserClassTeamDeatilFragment extends MvpFragment<UserClassTeamDetailFragView, UserClassTeamDetailFragPresenter> implements UserClassTeamDetailFragView {
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.layoutState)
    StateFrameLayout layoutState;
    @BindView(R.id.mConstraintLayout)
    ConstraintLayout mConstraintLayout;
    @BindView(R.id.title)
    TextView mTitle;
    Unbinder unbinder;
    private int mCurrentIndex;
    private int teamType;

    private RecycleCommonAdapter<Comment> recycleCommonAdapter;
    private List<Comment> mCommentList = new ArrayList<>();

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_class_team_detail;
    }

    @Override
    protected UserClassTeamDetailFragView bindView() {
        return this;
    }

    @Override
    protected UserClassTeamDetailFragPresenter bindPresenter() {
        return new UserClassTeamDetailFragPresenter(MApplication.mContext);
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        Bundle res = getArguments();
        if (res != null) {
            teamType = res.getInt(Constant.KEY_TEAM_TYPE);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        getPresenter().getData(mCurrentIndex);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        //回调父Fragment 的方法
//                List<Fragment>list=UserClassTeamDeatilFragment.this.getFragmentManager().getFragments();
//                for(Fragment f:list){
//                    if(f!=null&&f instanceof UserClassTeamFragment){
//                        ((UserClassTeamFragment) f).setLayoutHeight(mRootView.getWidth(),mRootView.getHeight());
//                        break;
//                    }
//                }
        String text = " ";
        if (teamType == Constant.TEAM_TYPE_DESINGE) {
            text = ResourceUtils.getString(R.string.str_team_design);
        } else if (teamType == Constant.TEAM_TYPE_WORKER) {
            text = ResourceUtils.getString(R.string.str_team_worker);
        }
        mTitle.setText(text);

        recycleCommonAdapter = new RecycleCommonAdapter<Comment>(
                MApplication.mContext, mCommentList, R.layout.layout_item_comment) {
            @Override
            public void bindItemData(SViewHolder holder, final Comment itemData, int position) {
                holder.setText(R.id.tvUserName, itemData.getUser() == null ? ResourceUtils.getString(R.string.defualt_userName) : TextUtils.isEmpty(itemData.getUser().getUserName()) ? ResourceUtils.getString(R.string.defualt_userName) : itemData.getUser().getUserName());
                holder.setText(R.id.tvContent, itemData.getContent());
                holder.setText(R.id.tvTime, TimeFormatUtil.getIntervalFormString(itemData.getCreatedAt()));
                holder.setImage(R.id.imgUser, itemData.getUser() == null ? "" : itemData.getUser().getImageUrl());
                holder.getView(R.id.imgUser).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userId = "";
                        if (itemData.getUser() != null && !TextUtils.isEmpty(itemData.getUser().getUserId())) {
                            userId = itemData.getUser().getUserId();
                        }
                        UserInfoActivity.gotoThis(mActivity, false, userId);
                    }
                });
            }


        };
        recycleview.setItemAnimator(null);
        recycleview.setAdapter(recycleCommonAdapter);
        recycleCommonAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
            @Override
            public void itemOnclickLinstener(int position) {

            }
        });
    }

    @Override
    protected void initStateLayout(View view) {
        super.initStateLayout(view);
        //初始化状态的布局
        View emptyView = getLayoutInflater().inflate(R.layout.layout_state_empty_with_retry, (ViewGroup) mActivity.findViewById(android.R.id.content), false);
        layoutState.setEmptyView(emptyView);
        layoutState.changeState(StateFrameLayout.LOADING);
    }


}
