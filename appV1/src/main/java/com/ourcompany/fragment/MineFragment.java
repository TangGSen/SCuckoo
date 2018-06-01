package com.ourcompany.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.activity.AboutCuckooActivity;
import com.ourcompany.activity.FeedbackActivity;
import com.ourcompany.activity.MessageActivity;
import com.ourcompany.activity.imui.UserInfoActivity;
import com.ourcompany.activity.setting.SystemSettingActivity;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.UserAccoutLoginRes;
import com.ourcompany.bean.eventbus.UserLogout;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.presenter.fragment.MineFragPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.DisplayUtils;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.fragment.MineFragmentView;
import com.ourcompany.widget.recycleview.commadapter.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 16:11
 * Des    :
 */

public class MineFragment extends MvpFragment<MineFragmentView, MineFragPresenter> implements MineFragmentView {
    @BindView(R.id.img_user)
    CircleImageView mUserImage;
    @BindView(R.id.str_user_name)
    TextView strUserName;

    @BindView(R.id.personal_rootLayout)
    CoordinatorLayout personalRootLayout;
    @BindView(R.id.btnMessage)
    TextView btnMessage;
    Unbinder unbinder;
    @BindView(R.id.btCollection)
    TextView btCollection;
    @BindView(R.id.btMyDynamic)
    TextView btMyDynamic;
    @BindView(R.id.btMyVote)
    TextView btMyVote;
    @BindView(R.id.btFeedback)
    TextView btFeedback;
    @BindView(R.id.btAboutCuckoo)
    TextView btAboutCuckoo;
    @BindView(R.id.btSetting)
    TextView btSetting;
    Unbinder unbinder1;
    @BindView(R.id.btManager)
    TextView btManager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    Unbinder unbinder2;
    private String[] tabTiles;


    @Override
    protected void initView(View view) {
        super.initView(view);
        EventBus.getDefault().register(this);
        //其实需要判断 除了业主不显示管理，其他都行显示管理
        initManagerTabView();
    }

    /**
     * tab 点击触摸事件
     */


    private View.OnTouchListener onTabTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Integer index = (Integer) view.getTag(R.id.nine_layout_of_index);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (index != null) {
                    switch (index) {
                        case 0:

                            break;
                    }
                }

            }
            return true;
        }
    };

    //自定义TabView
    public View getTabView(int id, String text, int position) {
        TextView view = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.tab_textview_for_mine, null);
        //设置图片
        Drawable topDrawable = ResourceUtils.getDrawable(id);
        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
        view.setCompoundDrawables(topDrawable, null, null, null);
        view.setTextColor(ResourceUtils.getResColor(R.color.whiles));
        view.setText(text);
        view.setTag(R.id.nine_layout_of_index, position);
        return view;
    }

    private void initManagerTabView() {
        tabTiles = ResourceUtils.getStringArray(R.array.tabMineManager);
        int[] tabItemDrawableNormal = new int[]{R.drawable.ic_my_team, R.drawable.ic_my_case, R.drawable.ic_coupon};
        int tabCount = tabTiles.length;
        int height = DisplayUtils.dip2px(60);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
        if (tabLayout.getLayoutParams() == null) {
            tabLayout.getLayoutParams().height = height;
        } else {
            tabLayout.setLayoutParams(params);
        }

        for (int i = 0; i < tabCount; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = getTabView(tabItemDrawableNormal[i], tabTiles[i], i);
            view.setBackgroundResource(R.drawable.bg_gradient_tab1);
            view.setLayoutParams(params);
            tab.setCustomView(view);
            tabLayout.addTab(tab, i);
            tab.getCustomView().setOnTouchListener(onTabTouchListener);
        }


    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_act_personal;
    }

    @Override
    protected MineFragmentView bindView() {
        return this;
    }

    @Override
    protected MineFragPresenter bindPresenter() {
        return new MineFragPresenter(MApplication.mContext);
    }

    @OnClick({R.id.img_user, R.id.btnMessage, R.id.btCollection, R.id.btMyDynamic,
            R.id.btMyVote, R.id.btFeedback, R.id.btAboutCuckoo, R.id.btSetting})
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_user:
                //需要判断是否登录
                getPresenter().onClickUserImage(mActivity, this);
                break;
            case R.id.btnMessage:
                startActivity(new Intent(mActivity, MessageActivity.class));
                break;
            case R.id.btCollection:
                getPresenter().gotoCollection(mActivity);
                break;
            case R.id.btMyDynamic:
                UserInfoActivity.gotoThis(mActivity, false, MServiceManager.getInstance().getCurrentLoginUserId());

                break;
            case R.id.btMyVote:
                break;
            case R.id.btFeedback:
                FeedbackActivity.gotoThis(mActivity);
                break;
            case R.id.btAboutCuckoo:
                AboutCuckooActivity.gotoThis(mActivity);
                break;
            case R.id.btSetting:
                SystemSettingActivity.gotoThis(mActivity);
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        //获取当前登陆的User
        getPresenter().getUserInfos();
    }

    @Override
    public void showToastMsg(String string) {

    }

    @Override
    public void showUserInfo() {
        if (!MServiceManager.getInstance().getUserIsLogin()) {
            userLogout();
            return;
        }
        strUserName.setText(Constant.CURRENT_USER.nickname.get());
        mUserImage.setTag(R.id.loading_image_url, MServiceManager.getInstance().getLocalUserImage());
        ImageLoader.getImageLoader().loadImage(mUserImage, "");
    }

    /**
     * 用户退出登录了
     */
    public void userLogout() {
        strUserName.setText(ResourceUtils.getString(R.string.str_user_notlogin_sign));
        mUserImage.setImageDrawable(ResourceUtils.getDrawable(R.mipmap.photo));

    }

    @Override
    public void changeUserLoginState(boolean isLogin) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 登陆成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventResigister(UserAccoutLoginRes res) {
        if (res != null && res.isLoginSuccess()) {
            showUserInfo();
        }
    }

    /**
     * 登陆成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLogout(UserLogout res) {
        if (res != null && res.isLogout()) {
            userLogout();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder2 = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder2.unbind();
    }
}
