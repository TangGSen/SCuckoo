package com.ourcompany.presenter.activity;

import android.content.Context;
import android.text.TextUtils;

import com.ourcompany.R;
import com.ourcompany.bean.bmob.Comment;
import com.ourcompany.bean.bmob.Post;
import com.ourcompany.bean.bmob.SUser;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.view.activity.PostDeailActView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import company.com.commons.framework.presenter.MvpBasePresenter;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:04
 * Des    :
 */

public class PostDeailActPresenter extends MvpBasePresenter<PostDeailActView> {
    public PostDeailActPresenter(Context context) {
        super(context);
    }

    /**
     * 加载评论，首先加载最新的
     */
    public void loadComments(final int start, String objectId) {
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.include(Constant.BMOB_POST_USER);
        query.order(Constant.BMOB_ORDER_DESCENDING + Constant.BMOB_CREATE);
        //查询playerName叫“比目”的数据
        query.addWhereLessThanOrEqualTo(Constant.BMOB_CREATE, new BmobDate(new Date(System.currentTimeMillis())));
        query.addWhereEqualTo(Constant.BMOB_POST, objectId);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(Constant.IM_PAGESIZE);
        //执行查询方法
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(final List<Comment> list, BmobException e) {
                if (e == null) {
                    if (start == 0 && list.size() <= 0) {
                        showEmptyView();
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                getView().showContentView(list);
                            }
                        });
                    }
                } else {
                    getView().showErrorView();
                }
            }
        });
    }


    private void showEmptyView() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                getView().showEmptyView();
            }
        });
    }

    private void showErrorView() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                getView().showErrorView();
            }
        });
    }

    /**
     * 提交评论
     *
     * @param conent
     */
    public void submitComment(String conent, String postId) {
        if (TextUtils.isEmpty(conent)) {
            getView().showToastMsg(ResourceUtils.getString(R.string.input_not_null));
            return;
        }

        Comment comment = new Comment();
        comment.setContent(conent);
         final Post post = new Post();
        post.setObjectId(postId);
        comment.setPost(post);
        SUser user = new SUser();
        user.setObjectId(MServiceManager.getInstance().getLocalThirdPartyId());
        comment.setUser(user);
        comment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    getView().submitOk();
                   LogUtils.e("sen","comment is ok time is:"+System.currentTimeMillis());
                } else {
                    getView().submitError();
                    if(e!=null){
                        LogUtils.e("sen","getErrorCode:"+e.getErrorCode());
                    }
                    LogUtils.e("sen","comment is error+time is"+System.currentTimeMillis());
                }
            }
        });
    }

    private void updateCommentCount(Post post) {
//        post.increment(Constant.BMOB_COMMENT_COUNT, Constant.BMOB_ADD_1);
//        post.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                if(e==null){
//                    LogUtils.e("sen",s+"updateCommentCount "+e.getMessage());
//                }else{
//                    LogUtils.e("sen",s+""+e.getMessage());
//                }
//            }
//        });
    }

    /**
     * 查看用户是否喜欢
     *
     * @param objectId
     */
    public void loadIsUserLike(String objectId) {
        // 查询喜欢这个帖子的所有用户，因此查询的是用户表
        if (TextUtils.isEmpty(MServiceManager.getInstance().getLocalThirdPartyId())) {
            LogUtils.e("sen", "用户未登陆，不去查loadIsUserLike");
            return;
        }
        LogUtils.e("sen", "用户登陆，去查loadIsUserLike");
        BmobQuery<SUser> query1 = new BmobQuery<SUser>();
        Post post = new Post();
        post.setObjectId(objectId);

        //likes是Post表中的字段，用来存储所有喜欢该帖子的用户 条件有1
        query1.addWhereRelatedTo(Constant.BMOB_LIKES, new BmobPointer(post));
        List<BmobQuery<SUser>> queries = new ArrayList<BmobQuery<SUser>>();
        queries.add(query1);
        //条件2
        BmobQuery<SUser> query2 = new BmobQuery<SUser>();
        query2.addWhereEqualTo(Constant.BMOB_OBJECT_ID, MServiceManager.getInstance().getLocalThirdPartyId());
        queries.add(query2);
        BmobQuery<SUser> query3 = new BmobQuery<SUser>();
        query3.and(queries);
        query3.findObjects(new FindListener<SUser>() {
            @Override
            public void done(List<SUser> object, BmobException e) {

                if (e == null) {
                    LogUtils.e("sen","loadIsUserLike 1");
                    if (object != null && object.size() >= 1 && !TextUtils.isEmpty(object.get(0).getUserId())) {
                        if (object.get(0).getUserId().equals(Constant.CURRENT_USER.id.get())) {
                            LogUtils.e("sen","用户是喜欢的22");
                            getView().userIsLikeThis(true);

                        }else{
                            LogUtils.e("sen","用户是不喜欢0");
                        }
                    }else{
                        LogUtils.e("sen","用户是不喜欢1");
                    }

                }else{
                    LogUtils.e("sen","用户是不喜欢3");
                    if(object!=null){
                        LogUtils.e("sen", "***返回值size："+object.size() );
                    }else{
                        LogUtils.e("sen", "***返回值null" );
                    }
                    if(e!=null){
                        LogUtils.e("sen","查看异常信息0"+e.getMessage()+e.getErrorCode());
                    }
                    LogUtils.e("sen","查看异常信息1"+e.getMessage()+e.getErrorCode());
                }
            }

        });
    }

    /**
     * 用户更新喜欢操作
     *
     * @param selected
     * @param objectId
     */
    public void updateUserLove(final boolean selected, String objectId) {
        Post post = new Post();
        post.setObjectId(objectId);
        BmobRelation relation = new BmobRelation();
        //增加或减少
        post.increment(Constant.BMOB_LIKE_COUNT, selected ? Constant.BMOB_REMOVE_1 : Constant.BMOB_ADD_1);
        SUser user = new SUser();
        user.setObjectId(MServiceManager.getInstance().getLocalThirdPartyId());
        if (selected) {
            //已经喜欢那么就删除
            relation.remove(user);
        } else {
            relation.add(user);
        }
        post.setLikes(relation);
        post.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    getView().userIsLikeThis(!selected);
                    LogUtils.e("sen","用户喜欢操作成功");
                } else {
                   // LogUtils.e("sen", "unLikeThis 用户喜欢操作失败"+e.getErrorCode());
                }
            }
        });
    }
}
