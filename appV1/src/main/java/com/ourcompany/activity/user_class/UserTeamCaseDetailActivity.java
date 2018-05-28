package com.ourcompany.activity.user_class;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.presenter.activity.UserTeamCaseDetailPresenter;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.UserTeamCaseDetailView;

import company.com.commons.framework.view.impl.MvpActivity;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/5/28 上午9:12
 * Des    :
 */
public class UserTeamCaseDetailActivity extends MvpActivity<UserTeamCaseDetailView, UserTeamCaseDetailPresenter> implements UserTeamCaseDetailView {


    public static final String KEY_INTENT = "key_intent";
    private static final String KEY_BUNDLE_USER = "key_bundle_data";

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }



    @Override
    protected int getLayoutId() {
        return  R.layout.activity_user_team_case_detail;
    }

    @Override
    protected UserTeamCaseDetailView bindView() {
        return this;
    }

    @Override
    protected UserTeamCaseDetailPresenter bindPresenter() {
        return new UserTeamCaseDetailPresenter(MApplication.mContext);
    }


}
