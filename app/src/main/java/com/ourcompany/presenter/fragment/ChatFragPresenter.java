package com.ourcompany.presenter.fragment;

import android.content.Context;

import com.mob.imsdk.MobIM;
import com.mob.imsdk.MobIMCallback;
import com.mob.imsdk.MobIMMessageReceiver;
import com.mob.imsdk.MobIMReceiver;
import com.mob.imsdk.model.IMConversation;
import com.mob.imsdk.model.IMMessage;
import com.ourcompany.bean.ChatConversations;
import com.ourcompany.utils.Constant;
import com.ourcompany.view.fragment.ChatFrameView;

import java.util.List;

import company.com.commons.framework.presenter.MvpBasePresenter;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:04
 * Des    :
 */

public class ChatFragPresenter extends MvpBasePresenter<ChatFrameView> {
    private MobIMCallback<List<IMConversation>> conversationCallback;

    public ChatFragPresenter(Context context) {
        super(context);
    }

    public void initIMReceiver() {
        MobIMMessageReceiver messageReceiver = new MobIMMessageReceiver() {
            public void onMessageReceived(List<IMMessage> messageList) {
                //接收到消息，则刷新界面
                refreshData();
                //更新未读消息总数
                //  ((MainActivity) getActivity()).freshUnreadMessageCount();
            }
        };

        MobIMReceiver generalReceiver = new MobIMReceiver() {
            public void onConnected() {
                setIMConnectStatus(0);
                //连接im成功后，刷新会话列表
                refreshData();
            }

            public void onConnecting() {
                setIMConnectStatus(1);
            }

            public void onDisconnected(int error) {
                setIMConnectStatus(error);
            }
        };
        MobIM.addMessageReceiver(messageReceiver);
        MobIM.addGeneralReceiver(generalReceiver);
    }

    public void refreshData() {
        if (Constant.CURRENT_USER == null) {
            //加载失败
            getView().showLoadingFailed();
            return;
        }
        //加载本地会话
        MobIM.getChatManager().getAllLocalConversations(initConversationCallback());
    }

    //本地会话回调对象
    private MobIMCallback<List<IMConversation>> initConversationCallback() {
        if (conversationCallback == null) {
            conversationCallback = new MobIMCallback<List<IMConversation>>() {
                public void onSuccess(List<IMConversation> list) {
                    ChatConversations conversations = new ChatConversations();
                    getView().showContentView(conversations);
//                    setAdapter(list);
//                    srLayout.setEnabled(true);
//                    if (srLayout.isRefreshing()) {
//                        srLayout.setRefreshing(false);
//                    }
                }

                public void onError(int code, String message) {
                    getView().showLoadingFailed();
//                    showLoadingFailed();
//                    Utils.showErrorToast(code);
//                    srLayout.setEnabled(true);
//                    if (srLayout.isRefreshing()) {
//                        srLayout.setRefreshing(false);
//                    }
                }
            };
        }
        return conversationCallback;
    }

    private void setIMConnectStatus(int error) {

    }


}
