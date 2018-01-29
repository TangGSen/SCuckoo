package com.ourcompany.presenter.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ourcompany.activity.login.LoginActivity;
import com.ourcompany.view.fragment.MineFragmentView;

import cn.bmob.v3.BmobUser;
import company.com.commons.framework.presenter.MvpBasePresenter;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 22:09
 * Des    : 我的 页面 Presenter
 */

public class MineFragPresenter extends MvpBasePresenter<MineFragmentView> {
    public MineFragPresenter(Context context) {
        super(context);
    }
    //点击用户头像
    public void onClickUserImage(Activity activity, MvpFragment mineFragment){
        if(activity==null){
            return;
        }
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser !=null){

        }else{
            Intent intent = new Intent(activity,LoginActivity.class);
            mineFragment.startActivity(intent);
        }
    }
}
