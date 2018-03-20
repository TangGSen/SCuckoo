package com.ourcompany.fragment.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mob.jimu.query.data.DataType;
import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.presenter.fragment.LoginFragPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.view.fragment.LoginFragmentView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 16:11
 * Des    : 最近聊天，联系人页面
 */

public class TestMobFragment extends MvpFragment<LoginFragmentView, LoginFragPresenter> implements LoginFragmentView {
    @BindView(R.id.test_id)
    TextView textView;
    @BindView(R.id.test_login)
    Button testLogin;
    @BindView(R.id.test_my)
    Button testMy;
    @BindView(R.id.test_showRecommend)
    Button testShowRecommend;
    Unbinder unbinder;
    @BindView(R.id.test_addfriends)
    Button testAddfriends;
    Unbinder unbinder1;
    @BindView(R.id.test_add_bmob)
    Button testAddBmob;
    @BindView(R.id.test_get_comstor_key)
    Button testGetComstorKey;
    Unbinder unbinder2;
    private HashMap<String, DataType<?>> idType;

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


    @OnClick({R.id.test_login, R.id.test_my, R.id.test_showRecommend, R.id.test_addfriends, R.id.test_add_bmob, R.id.test_get_comstor_key})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.test_login:
//                UMSGUI.showLogin(new OperationCallback<User>() {
//                    @Override
//                    public void onSuccess(User user) {
//                        super.onSuccess(user);
//                    }
//                });
                break;
            case R.id.test_my:
                //  UMSGUI.showProfilePage();
                break;
            case R.id.test_showRecommend:
                //  UMSGUI.showRecommendationPage();
                break;
            case R.id.test_add_bmob:
                MServiceManager.getInstance().saveUserToBmob(Constant.CURRENT_USER);
                break;
            case R.id.test_get_comstor_key:
               String id =  MServiceManager.getInstance().getLocalThirdPartyId();
                LogUtils.e("sen",id);
                break;

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
