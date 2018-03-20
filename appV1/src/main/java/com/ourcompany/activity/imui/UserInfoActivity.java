package com.ourcompany.activity.imui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.im.ui.ChatActivity;
import com.ourcompany.presenter.activity.UserInfoPresenter;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.UserInfoActvityView;

import butterknife.BindView;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/2/8 16:46
 * Des    :
 */

public class UserInfoActivity extends MvpActivity<UserInfoActvityView, UserInfoPresenter> implements UserInfoActvityView {
    @BindView(R.id.bg_head)
    ImageView bgHead;
    @BindView(R.id.tv_nickName)
    TextView tvNickName;
    @BindView(R.id.btnChat)
    TextView btnChat;
    @BindView(R.id.btnAddFriend)
    TextView btnAddFriend;
    @BindView(R.id.userSign)
    TextView userSign;
    @BindView(R.id.userImage)
    CircleImageView userImage;


//    private User mUser;
//    @Override
//    protected boolean initArgs(Bundle bundle) {
//        Bundle res = getIntent().getBundleExtra(Constant.ACT_SEARCH_BUNDLE);
//        if(res !=null){
//            MUser suer =  (MUser) res.getSerializable(Constant.KEY_ITEM_USER);
//            if(suer!=null){
//                mUser = suer.getUser();
//                ToastUtils.showSimpleToast(mUser.nickname.get());
//            }
//
//        }else {
//            return false;
//        }
//        return super.initArgs(bundle);
//    }


    @Override
    protected void initView() {
        super.initView();
        tvNickName.setText(Constant.CURRENT_ITEM_USER.nickname.get());
        userSign.setText(Constant.CURRENT_ITEM_USER.signature.get());



    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getPresenter().getFriendShip(Constant.CURRENT_ITEM_USER.id.get());
    }

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    public void loading() {

    }

    @Override
    public void loaded() {

    }

    @Override
    public void showError(String message) {
        showToastMsg(message);
    }

    @Override
    public void isMyFriend() {
        btnChat.setVisibility(View.VISIBLE);
        btnChat.setEnabled(true);
        btnAddFriend.setVisibility(View.GONE);
        btnAddFriend.setEnabled(false);


    }

    @Override
    public void isNotMyFriend() {
        btnChat.setVisibility(View.GONE);
        btnChat.setEnabled(false);

        btnAddFriend.setVisibility(View.VISIBLE);
        btnAddFriend.setEnabled(true);
    }

    @Override
    public void requestIsMyFriendFaild() {
        showToastMsg(ResourceUtils.getString(R.string.net_exception));
    }

    @Override
    public void onErrortToToast(String string) {
        showToastMsg(string);
    }

    @Override
    public void showSuccess(String string) {
        showToastMsg(string);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected UserInfoActvityView bindView() {
        return this;
    }

    @Override
    protected UserInfoPresenter bindPresenter() {
        return new UserInfoPresenter(MApplication.mContext);
    }


    @OnClick({R.id.btnChat, R.id.btnAddFriend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnChat:
               if(Constant.CURRENT_ITEM_USER!=null &&!TextUtils.isEmpty( Constant.CURRENT_ITEM_USER.id.get())){
                  // ChatingActivity.gotoUserChatPageById(UserInfoActivity.this,Constant.CURRENT_ITEM_USER.id.get());
                   ChatActivity.gotoUserChatPage(UserInfoActivity.this,Constant.CURRENT_ITEM_USER.id.get());
               }

                break;
            case R.id.btnAddFriend:
                //弹出选框
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setMessage(ResourceUtils.getString(R.string.request_add_friend_msg));
                View root = View.inflate(UserInfoActivity.this, R.layout.layout_cunstomer_dialog, null);
                final EditText editText = root.findViewById(R.id.et_add_message);
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constant.MAX_ADD_FRIEND_MESSAGE)});
                builder.setView(root);
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        String message = editText.getText().toString();
                        getPresenter().addFrindByUserId(Constant.CURRENT_ITEM_USER, message);
                    }
                });
                builder.setCancelable(false);
                builder.show();
                break;


        }
    }
}
