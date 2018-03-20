package com.ourcompany.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.bmob.Comment;
import com.ourcompany.bean.bmob.Post;
import com.ourcompany.presenter.activity.PostDeailActPresenter;
import com.ourcompany.utils.InputMethodUtils;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.TimeFormatUtil;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.PostDeailActView;
import com.ourcompany.widget.NineGridlayout;
import com.ourcompany.widget.StateFrameLayout;
import com.ourcompany.widget.recycleview.commadapter.ImageLoader;
import com.ourcompany.widget.recycleview.commadapter.OnItemOnclickLinstener;
import com.ourcompany.widget.recycleview.commadapter.RecycleCommonAdapter;
import com.ourcompany.widget.recycleview.commadapter.SViewHolder;
import com.ourcompany.widget.recycleview.commadapter.SimpleDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/13 14:59
 * Des    :
 */

public class PostDetailActivity extends MvpActivity<PostDeailActView, PostDeailActPresenter> implements PostDeailActView, InputMethodUtils.OnKeyboardEventListener {
    private static final String KEY_INTENT = "key_intent";
    private static final String KEY_BUNDLE = "key_bundle";
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindView(R.id.ivNineLayout)
    NineGridlayout ivNineLayout;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.likes)
    TextView likes;
    @BindView(R.id.comments)
    TextView comments;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.layoutBottom)
    LinearLayout layoutBottom;
    @BindView(R.id.imgLove)
    ImageView imgLove;
    @BindView(R.id.layoutState)
    StateFrameLayout layoutState;
    @BindView(R.id.etInputFor)
    TextView etInputFor;
    @BindView(R.id.etInput)
    EditText etInput;
    @BindView(R.id.btn_a_t)
    TextView btnAT;
    @BindView(R.id.btnSend)
    TextView btnSend;
    @BindView(R.id.layoutInpts)
    LinearLayout layoutInpts;

    @BindView(R.id.imageKeyBorad)
    ImageView imageKeyBorad;
    @BindView(R.id.v_panel)
    View vPanel;
    @BindView(R.id.inputOutSildeView)
    View inputOutSildeView;

    private Post mPost;
    private List<Comment> mCommentList = new ArrayList<>();
    private RecycleCommonAdapter recycleCommonAdapter;
    private int currentIndex;

    public static void gotoThis(Context context, Post post) {
        if (post == null) {
            return;
        }
        Intent intent = new Intent(context, PostDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_BUNDLE, post);
        intent.putExtra(KEY_INTENT, bundle);
        context.startActivity(intent);
    }

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_post_deail;
    }

    @Override
    protected PostDeailActView bindView() {
        return this;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        Bundle bun = getIntent().getBundleExtra(KEY_INTENT);
        if (bun != null) {
            mPost = (Post) bun.getSerializable(KEY_BUNDLE);
        }
        return super.initArgs(bundle);

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
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MApplication.mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleview.setLayoutManager(linearLayoutManager);
        recycleview.setHasFixedSize(true);
        recycleview.addItemDecoration(new SimpleDecoration(MApplication.mContext, R.drawable.recycle_line_divider_padding, 2));
        recycleCommonAdapter = new RecycleCommonAdapter<Comment>(
                MApplication.mContext, mCommentList, R.layout.layout_item_comment) {
            @Override
            public void bindItemData(SViewHolder holder, Comment itemData, int position) {
                holder.setText(R.id.tvUserName, itemData.getUser() == null ? ResourceUtils.getString(R.string.defualt_userName) : TextUtils.isEmpty(itemData.getUser().getUserName()) ? ResourceUtils.getString(R.string.defualt_userName) : itemData.getUser().getUserName());
                holder.setText(R.id.tvContent, itemData.getContent());
                holder.setText(R.id.tvTime, TimeFormatUtil.getIntervalFormString(itemData.getCreatedAt()));
                holder.setImage(R.id.imgUser, itemData.getUser() == null ? "" : itemData.getUser().getImageUrl());
            }


        };
        recycleview.setItemAnimator(null);
        recycleview.setAdapter(recycleCommonAdapter);
        recycleCommonAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
            @Override
            public void itemOnclickLinstener(int position) {
            }
        });

        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendTxtMsg();
                }
                return true;
            }
        });
        InputMethodUtils.detectKeyboard(this, this);

    }


    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {
        layoutInpts.setVisibility(View.VISIBLE);
        layoutBottom.setVisibility(View.GONE);
        vPanel.setVisibility(View.GONE);
        InputMethodUtils.toggleSoftInputForEt(etInput);
        ViewGroup.LayoutParams params = vPanel.getLayoutParams();
        if (params != null && params.height != keyboardHeight) {
            params.height = keyboardHeight;
            vPanel.setLayoutParams(params);
        }
    }

    @Override
    public void onSoftKeyboardClosed() {
        if (!isEmotionPanelShowing()) {
            layoutInpts.setVisibility(View.GONE);
            layoutBottom.setVisibility(View.VISIBLE);
            vPanel.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean isEmotionPanelShowing() {
        return vPanel.getVisibility() == View.VISIBLE;
    }

    @Override
    public void hideEmotionPanel() {
        if (vPanel.getVisibility() != View.GONE) {
            vPanel.setVisibility(View.GONE);
            InputMethodUtils.updateSoftInputMethod(this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    public void showEmotionPanel() {
        vPanel.removeCallbacks(mHideEmotionPanelTask);
        vPanel.setVisibility(View.VISIBLE);
        InputMethodUtils.updateSoftInputMethod(this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        InputMethodUtils.hideKeyboard();

    }

    private void sendTxtMsg() {
        getPresenter().submitComment(etInput.getText().toString(), mPost.getObjectId());
    }

    @Override
    protected void initStateLayout() {
        super.initStateLayout();
        //初始化状态的布局
        View emptyView = getLayoutInflater().inflate(R.layout.layout_state_empty, (ViewGroup) findViewById(android.R.id.content), false);
        layoutState.setEmptyView(emptyView);
        layoutState.changeState(StateFrameLayout.LOADING);

    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (mPost != null) {
            tvUserName.setText(mPost.getUser() == null ? ResourceUtils.getString(R.string.defualt_userName) : TextUtils.isEmpty(mPost.getUser().getUserName()) ? ResourceUtils.getString(R.string.defualt_userName) : mPost.getUser().getUserName());
            tvContent.setText(mPost.getContent());
            tvTime.setText(TimeFormatUtil.getIntervalFormString(mPost.getCreatedAt()));
            imgUser.setTag(R.id.loading_image_url, mPost.getUser() == null ? "" : mPost.getUser().getImageUrl());
            ImageLoader.getImageLoader().loadImage(imgUser, "");
            ivNineLayout.setImagesData(mPost.getImageUrls());
            if (mPost.getLikeCount() == null) {
                mPost.setLikeCount(0);
            }
            if (mPost.getCommentCount() == null) {
                mPost.setCommentCount(0);
            }
            likes.setText(mPost.getLikeCount() + "");
            comments.setText(mPost.getCommentCount() + "");
        }
        //加载评论
        getPresenter().loadComments(currentIndex, mPost.getObjectId());
        //查看用户是否喜欢这个帖子
        getPresenter().loadIsUserLike(mPost.getObjectId());
    }

    @Override
    protected PostDeailActPresenter bindPresenter() {
        return new PostDeailActPresenter(MApplication.mContext);
    }


    @Override
    public void showEmptyView() {
        layoutState.changeState(StateFrameLayout.EMPTY);
        closeReflshView();
    }

    @Override
    public void showContentView(List<Comment> list) {
        int start = mCommentList.size();
        mCommentList.addAll(list);
        int end = mCommentList.size();
        recycleCommonAdapter.notifyItemRangeChanged(start, end);
        layoutState.changeState(StateFrameLayout.SUCCESS);
        closeReflshView();
    }

    private void closeReflshView() {
//        mRefreshLayout.setEnabled(true);
//        if (mRefreshLayout.isRefreshing()) {
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mRefreshLayout.setRefreshing(false);
//                }
//            }, 1000);
//
//        }
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

    @Override
    public void submitError() {
        LogUtils.e("sen", "submitError");
        getView().showToastMsg(ResourceUtils.getString(R.string.submit_fail));
    }

    @Override
    public void submitOk() {
        //  LogUtils.e("sen", "submitOk");
        // etInput.setText("");
        getView().showToastMsg(ResourceUtils.getString(R.string.submit_success));
        etInput.setText("");
        int count = mPost.getCommentCount() + 1;
        comments.setText(count + "");
        showLayoutButtomView();
    }

    @Override
    public void userIsLikeThis(boolean isLike) {
        if (isLike) {
            LogUtils.e("sen", "userIsLikeThis is true");
        } else {
            LogUtils.e("sen", "userIsLikeThis is false");
        }
        imgLove.setSelected(isLike);
        int count = mPost.getLikeCount();
        count = isLike ? ++count : --count;
        count = count < 0 ? 0 : count;
        mPost.setLikeCount(count);
        likes.setText(count + "");
    }

    @OnClick({R.id.imgLove, R.id.etInputFor, R.id.btnSend, R.id.imageKeyBorad, R.id.inputOutSildeView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgLove:
                getPresenter().updateUserLove(imgLove.isSelected(), mPost.getObjectId());
                break;
            case R.id.etInputFor:
                if (isEmotionPanelShowing()) {
                    vPanel.postDelayed(mHideEmotionPanelTask, 500);
                } else {
                    layoutInpts.setVisibility(View.VISIBLE);
                    InputMethodUtils.toggleSoftInputForEt(etInput);
                }
                InputMethodUtils.setKeyboardShowing(true);
                break;


            case R.id.btnSend:
                sendTxtMsg();
                break;

            case R.id.imageKeyBorad:
                if (isEmotionPanelShowing()) {
                    InputMethodUtils.toggleSoftInput(getCurrentFocus());
                    vPanel.postDelayed(mHideEmotionPanelTask, 500);
                } else {
                    showEmotionPanel();
                }
                break;
            case R.id.inputOutSildeView:
                showLayoutButtomView();
                break;


        }
    }

    /**
     * 显示底部的view
     */
    public void showLayoutButtomView() {
        InputMethodUtils.hideKeyboard();
        InputMethodUtils.hideEmotionPanel();
        layoutInpts.setVisibility(View.GONE);
        layoutBottom.setVisibility(View.VISIBLE);
    }


    //隐藏的任务
    private Runnable mHideEmotionPanelTask = new Runnable() {
        @Override
        public void run() {
            hideEmotionPanel();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (layoutInpts.getVisibility() == View.VISIBLE) {
                showLayoutButtomView();
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }

        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}



