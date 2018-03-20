package com.ourcompany.manager;

import android.content.Context;

import com.mob.MobSDK;
import com.mob.imsdk.MobIM;
import com.mob.imsdk.MobIMCallback;
import com.mob.imsdk.model.IMUser;
import com.mob.jimu.query.data.DataType;
import com.mob.jimu.query.data.Text;
import com.mob.ums.FriendRequest;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.ourcompany.bean.bmob.SUser;
import com.ourcompany.im.biz.UserManager;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/30 21:58
 * Des    : 账号注册和登陆，聊天等的封装
 */

public class MServiceManager {
    private static final Executor EXECUTOR = Executors.newCachedThreadPool();
    private volatile static MServiceManager instance;

    private MServiceManager() {
    }

    public static MServiceManager getInstance() {
        if (instance == null) {
            synchronized (MServiceManager.class) {
                if (instance == null) {
                    instance = new MServiceManager();
                }
            }
        }
        return instance;
    }

    /**
     * 注册IM 的账号
     *
     * @param username
     * @param password
     */
    public int resigisterAccount(String username, String password) {
        return 0;
    }


    //注册成功需要登陆
    public void login(String userId, String username, String imagUrl) {
        //先做一些事情
        MobSDK.setUser(userId, username, imagUrl, null);
    }

    /**
     * 1.初始化 mob SDK
     */
    public void init(Context context) {
        MobSDK.init(context);
    }


    public boolean isUserLogin() {
        String userId = UMSSDK.getLoginUserId();
        return false;
    }

    public void getUserInfos() {

    }

    private String getFriendsCachePath() {
        return new File(MobSDK.getContext().getCacheDir(), "friends").getPath();
    }

    /* 获取好友列表 */
    public synchronized void getFriends(final int start, final int count, final OperationCallback<ArrayList<User>> callback) {
        EXECUTOR.execute(new Runnable() {
            public void run() {
                UMSSDK.getFrinds(Constant.CURRENT_USER, start, count, callback);
            }
        });
    }

    /**
     * 回复好友请求
     */
    public void replyFriendsRequesting(String userId, boolean accept, OperationCallback callback) {
        UMSSDK.replyFriendRequesting(userId, accept, callback);

    }

    /**
     * 删除好友
     */
    public void deleteFriend(User target, String message, OperationCallback<Void> callback) {
        UMSSDK.deleteFriend(target, callback);
    }


    /**
     * 添加好友
     */
    public void addFriend(User target, String message, OperationCallback<Void> callback) {
        UMSSDK.addFriend(target, message, callback);
    }

    /**
     * 根据id 和手机号，昵称搜索User
     *
     * @param query
     * @param start
     * @param cound
     * @param callback
     */
    public void requestSearch(String query, int start, int cound, OperationCallback<ArrayList<User>> callback) {
        UMSSDK.search(query, start, cound, callback);
    }

    /**
     * 获取 好友关系
     */
    public void getFriendShip(Text[] ids, OperationCallback<Set<String>> callback) {
        UMSSDK.isMyFriends(ids, callback);
    }

    /**
     * 获取新朋友的id列表
     */
    public void getNewFriend(OperationCallback<ArrayList<String>> callback) {
        UMSSDK.getNewFriendsCount(callback);
    }

    /**
     * 获取
     */

    public void getAddFriendRequests(final int start, final int end, final OperationCallback<ArrayList<FriendRequest>> callback) {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                UMSSDK.getAddFriendRequests(start, end, callback);
            }
        });

    }

    public void replyFriendRequesting(final String requesterId, final boolean isAccpet, final OperationCallback<Void> callback) {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                UMSSDK.replyFriendRequesting(requesterId, isAccpet, callback);
            }
        });
    }

    public void saveImUserInfos(final String userId) {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                MobIM.getUserManager().getUserInfo(userId, new MobIMCallback<IMUser>() {
                    @Override
                    public void onSuccess(IMUser user) {
                        UserManager.saveUserInfo(user);
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }
        });
    }

    /**
     * 修改用户的数据比如后台增加了第三方id 放在UMSDK中
     */

    public void saveThridPartyId(String id, OperationCallback<Void> callback) {
        HashMap<String, Object> objectMap = new HashMap<>();
        DataType<String> idType = new DataType<String>(id) {
        };
        DataType<Boolean> exist = new DataType<Boolean>(true) {
        };
        //objectMap.put(Constant.UMSDK_COMSTOR_KEY_THRID_ID, idType);
        objectMap.put(Constant.UMSDK_COMSTOR_KEY_EXIST_ID, exist);
        UMSSDK.updateUserInfo(objectMap, callback);
    }
    /************************************
     * 以下是Bomb Sdk
     */
    /**
     * 将用户也绑定到bmob 数据库上
     * 然后将bmob 的也绑定到mob 上
     *
     * @param user
     */
    public void saveUserToBmob(User user) {
        LogUtils.e("sen", "saveUserToBmob");
        SUser sUser = new SUser();
        sUser.addUnique(Constant.BMOB_SUSER_ID, user.id.get());
        sUser.setUserName(user.nickname.get());
        sUser.setImageUrl(Constant.test_user_image);
        sUser.save(new SaveListener<String>() {
            @Override
            public void done(final String s, final BmobException e) {
                LogUtils.e("sen", "done1");
                EXECUTOR.execute(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.e("sen", "done2");
                        if (e == null) {
                            //直接存
                            saveThirdPartyImp(s);
                            LogUtils.e("sen", "直接存bmob user ok");
                        } else {
                            LogUtils.e("sen", "查出来在存bmob user error");
                            //意思是重复保存了，那么就查出来，在存
                            findObjectIdFromSUser(s);
                        }
                    }
                });

            }
        });
    }

    //设置本地第三方的id 绑定到user
    private void setLocalUserThridPartyId(String id) {
        if (Constant.CURRENT_USER != null) {
            DataType<Boolean> exist = new DataType<Boolean>(true) {
                @Override
                public Object value() {
                    return super.value();
                }
            };
            DataType<String> idstr = new DataType<String>(id) {
                @Override
                public Object value() {
                    return super.value();
                }
            };
            Constant.CURRENT_USER.setCustomField(Constant.UMSDK_COMSTOR_KEY_EXIST_ID, exist);
            Constant.CURRENT_USER.setCustomField(Constant.UMSDK_COMSTOR_KEY_THRID_ID, idstr);
        }
    }

    public String getLocalThirdPartyId() {
        try {
            DataType<String> idType = (DataType<String>) Constant.CURRENT_USER.getCustomField(Constant.UMSDK_COMSTOR_KEY_THRID_ID);
            if (idType != null) {
                String id = (String) idType.value();
                return id;
            }
        }catch (Exception e){
            LogUtils.e("sen","getLocaTlhirdPartyId Exception: "+e.getMessage());
        }

        return "";

    }

    public void saveThirdPartyImp(final String id) {
        saveThridPartyId(id, new OperationCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                super.onSuccess(aVoid);
                setLocalUserThridPartyId(id);
                LogUtils.e("sen", "saveThridPartyId ok");
            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                LogUtils.e("sen", "saveThridPartyId" + throwable.getLocalizedMessage() + throwable.getMessage());
            }
        });
    }

    public void findObjectIdFromSUser(String userId) {
        BmobQuery<SUser> query = new BmobQuery<SUser>();
        query.getObject(userId, new QueryListener<SUser>() {
            @Override
            public void done(SUser object, BmobException e) {
                if (e == null) {
                    //获得数据的objectId信息
                    LogUtils.e("sen", "找到了在存");
                    saveImUserInfos(object.getObjectId());
                } else {

                }
            }

        });
    }

}
