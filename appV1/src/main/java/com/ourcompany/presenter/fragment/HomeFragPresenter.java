package com.ourcompany.presenter.fragment;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.ourcompany.bean.bmob.AdvertisementSetting;
import com.ourcompany.bean.json.AdvertisementData;
import com.ourcompany.utils.Constant;
import com.ourcompany.view.fragment.HomeFragView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import company.com.commons.framework.presenter.MvpBasePresenter;


/**
 * Created by Administrator on 2017/8/20.
 */

public class HomeFragPresenter extends MvpBasePresenter<HomeFragView> {

    public HomeFragPresenter(Context context) {
        super(context);
    }

    /**
     * 获取广告页
     */
    public void getAdsData() {
        BmobQuery<AdvertisementSetting> query = new BmobQuery<AdvertisementSetting>();

        query.order(Constant.BMOB_ORDER_DESCENDING + Constant.BMOB_CREATE);
        //查询playerName叫“比目”的数据
        // 注意第二个参数表名不要写错 是系统表
        query.setLimit(1);
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.setMaxCacheAge(TimeUnit.HOURS.toMillis(1));
        //执行查询方法
        query.findObjects(new FindListener<AdvertisementSetting>() {
            @Override
            public void done(final List<AdvertisementSetting> list, BmobException e) {

                if (e == null) {
                    EXECUTOR.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (list.size() > 0) {
                                String dataStr = list.get(0).getContent();
                                if (!TextUtils.isEmpty(dataStr)) {
                                    final AdvertisementData data = new Gson().fromJson(dataStr, AdvertisementData.class);
                                    final List<String> urlStr = new ArrayList<>();
                                    final int dataSize = data.getAdSetting().size();
                                    for (int i = 0; i < dataSize; i++) {
                                        urlStr.add(data.getAdSetting().get(i).getImageUrl());
                                    }
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            getView().showAdDatas(data.getAdSetting(), urlStr);
                                        }
                                    });
                                } else {
                                    //应该显示一张默认的
                                    getAdDataError();
                                }

                            } else {
                                getAdDataError();
                            }
                        }
                    });

                } else {
                    getAdDataError();
                }
            }
        });
    }

    /**
     * 获取广告数据为空
     */
    public void getAdDataError() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                getView().showEmptyAdData();

            }
        });
    }
}
