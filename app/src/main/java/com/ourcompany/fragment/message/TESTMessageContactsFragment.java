package com.ourcompany.fragment.message;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mob.ums.OperationCallback;
import com.mob.ums.User;
import com.mob.ums.gui.UMSGUI;
import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.presenter.fragment.LoginFragPresenter;
import com.ourcompany.view.fragment.LoginFragmentView;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 16:11
 * Des    : 最近聊天，联系人页面
 */

public class TESTMessageContactsFragment extends MvpFragment<LoginFragmentView, LoginFragPresenter> implements LoginFragmentView {
    @BindView(R.id.test_id)
    TextView textView;
    @BindView(R.id.test_login)
    Button testLogin;
    @BindView(R.id.test_my)
    Button testMy;
    @BindView(R.id.test_showRecommend)
    Button testShowRecommend;
    Unbinder unbinder;

    @Override
    protected void initView(View view) {
        super.initView(view);
        textView.setText("MessageContactsFragment");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_layout_message_chat_test;
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
    }

    @OnClick({R.id.test_login, R.id.test_my, R.id.test_showRecommend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.test_login:
                UMSGUI.showLogin(new OperationCallback<User>() {
                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                    }
                });
                break;
            case R.id.test_my:
                UMSGUI.showProfilePage();
                break;
            case R.id.test_showRecommend:
                UMSGUI.showRecommendationPage();
                break;
        }
    }
}
