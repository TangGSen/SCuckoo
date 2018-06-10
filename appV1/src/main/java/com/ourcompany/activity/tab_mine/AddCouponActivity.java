package com.ourcompany.activity.tab_mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.activity.animation.Rotate3DAnimation;
import com.ourcompany.adapter.ViewPagerAdapter;
import com.ourcompany.app.MApplication;
import com.ourcompany.fragment.coupon.CouponDateFragment;
import com.ourcompany.fragment.coupon.CouponNameFragment;
import com.ourcompany.presenter.activity.AddCouponActPresenter;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.AddCouponActView;
import com.ourcompany.widget.CouponConstraintLayoutView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import company.com.commons.framework.view.impl.MvpActivity;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/4 下午10:42
 * Des    : 添加优惠券
 */
public class AddCouponActivity extends MvpActivity<AddCouponActView, AddCouponActPresenter> implements AddCouponActView {


    @BindView(R.id.tvCouponMoney)
    TextView tvCouponMoney;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.couponLayout)
    CouponConstraintLayoutView couponLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.tvAddCoupon)
    TextView tvAddCoupon;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;

    @BindView(R.id.tvStates)
    TextView tvStates;
    @BindView(R.id.groupBaseInfo)
    Group groupBaseInfo;
    @BindView(R.id.otherBaseInfo)
    Group otherBaseInfo;
    @BindView(R.id.tvLimit)
    TextView tvLimit;
    private int centerX;
    private int centerY;
    private int depthZ = 400;
    private int duration = 600;
    private Rotate3DAnimation openAnimation;
    private Rotate3DAnimation closeAnimation;
    private boolean isOpen;
    private Rotate3DAnimation onOpenAnimationEnd;
    private Rotate3DAnimation onCloseAnimationEnd;
    private int lastPostion;
    //从这开始为第二页
    private int middlePosition = 5;

    private final int INDEX_COUPON_NAME = 1;
    private final int INDEX_COUPON_MONEY = 2;

    private final int INDEX_COUPON_STARTTIME = 3;
    private final int INDEX_COUPON_ENDTIME = 4;
    //fragemnet 的总数
    private final int FRAGEMENT_SIZE = 4;
    private String startTime = ResourceUtils.getString(R.string.str_counpon_strat_time);
    private String endTime = ResourceUtils.getString(R.string.str_counpon_end_time);

    public static void gotoThis(Context context) {
        Intent intent = new Intent(context, AddCouponActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_coupon;
    }

    @Override
    protected void initView() {
        super.initView();
        getWindow().setBackgroundDrawable(null);
        setSupportActionBar(commonToolbar);
        commonToolbar.setBackground(null);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
        initFragments();
        tvTime.setText(startTime+" 至 "+endTime);
    }

    private void initFragments(){
        final List<Fragment> mFragment = new ArrayList<>();
        //第一个 输入名称的
        Bundle bundle = new Bundle();
        bundle.putInt(CouponNameFragment.KEY_CURRENT_INDEX, INDEX_COUPON_NAME);
        bundle.putInt(CouponNameFragment.KEY_COUNT, FRAGEMENT_SIZE);
        bundle.putString(CouponNameFragment.KEY_INPUT_TITLE,ResourceUtils.getString(R.string.str_input_counpon_name));
        bundle.putInt(CouponNameFragment.KEY_INTPUT_COUNT,14);
        CouponNameFragment couponNameFragment = new CouponNameFragment();
        couponNameFragment.setArguments(bundle);
        mFragment.add(couponNameFragment);

        //第二个 输入金额的
        Bundle bundle1 = new Bundle();
        bundle1.putInt(CouponNameFragment.KEY_CURRENT_INDEX, INDEX_COUPON_MONEY);
        bundle1.putInt(CouponNameFragment.KEY_COUNT, FRAGEMENT_SIZE);
        bundle1.putString(CouponNameFragment.KEY_INPUT_TITLE,ResourceUtils.getString(R.string.str_input_counpon_money));
        bundle1.putInt(CouponNameFragment.KEY_INTPUT_COUNT,8);
        bundle1.putInt(CouponNameFragment.KEY_INPUT_TYPE,CouponNameFragment.INPUT_TYPE_NUMBER);
        CouponNameFragment couponNameFragment1 = new CouponNameFragment();
        couponNameFragment1.setArguments(bundle1);
        mFragment.add(couponNameFragment1);


        //第3个 输入开始时间
        Bundle bundle2 = new Bundle();
        bundle2.putInt(CouponDateFragment.KEY_CURRENT_INDEX, INDEX_COUPON_STARTTIME);
        bundle2.putInt(CouponDateFragment.KEY_COUNT, FRAGEMENT_SIZE);
        bundle2.putString(CouponDateFragment.KEY_INPUT_TITLE,ResourceUtils.getString(R.string.str_input_counpon_strat_time));
        CouponDateFragment dateFragment = new CouponDateFragment();
        dateFragment.setArguments(bundle2);
        mFragment.add(dateFragment);

        //第4个 输入结束时间
        Bundle bundle3 = new Bundle();
        bundle3.putInt(CouponDateFragment.KEY_CURRENT_INDEX, INDEX_COUPON_ENDTIME);
        bundle3.putInt(CouponDateFragment.KEY_COUNT, FRAGEMENT_SIZE);
        bundle3.putString(CouponDateFragment.KEY_INPUT_TITLE,ResourceUtils.getString(R.string.str_input_counpon_end_time));
        CouponDateFragment dateFragment1 = new CouponDateFragment();
        dateFragment1.setArguments(bundle3);
        mFragment.add(dateFragment1);






        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragment);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                float v = Math.abs(position);
                float v1 = (float) (0.2 * (v * v));
                page.setScaleY(1 - v1);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == middlePosition-1&&  middlePosition-1<lastPostion) {
                    isOpen = false;
                    starAnimation();
                } else if (position == middlePosition && middlePosition> lastPostion) {
                    isOpen = true;
                    starAnimation();
                }
                lastPostion = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initOpenAnim() {
        //从0到90度，顺时针旋转视图，此时reverse参数为true，达到90度时动画结束时视图变得不可见，
        openAnimation = new Rotate3DAnimation(0, 90, centerX, centerY, depthZ, true);
        openAnimation.setDuration(duration);
        openAnimation.setFillAfter(true);
        openAnimation.setInterpolator(new AccelerateInterpolator());
        openAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                groupBaseInfo.setVisibility(View.GONE);
                otherBaseInfo.setVisibility(View.VISIBLE);
                tvStates.setText(ResourceUtils.getString(R.string.str_coupon_other_info));
                //从270到360度，顺时针旋转视图，此时reverse参数为false，达到360度动画结束时视图变得可见
                if (onOpenAnimationEnd == null) {
                    initOpenAnimationEnd();
                }
                couponLayout.startAnimation(onOpenAnimationEnd);
            }
        });
    }

    public void initOpenAnimationEnd() {
        onOpenAnimationEnd = new Rotate3DAnimation(270, 360, centerX, centerY, depthZ, false);
        onOpenAnimationEnd.setDuration(duration);
        onOpenAnimationEnd.setFillAfter(true);
        onOpenAnimationEnd.setInterpolator(new DecelerateInterpolator());
    }

    public void initCloseAnimationEnd() {
        onCloseAnimationEnd = new Rotate3DAnimation(90, 0, centerX, centerY, depthZ, false);
        onCloseAnimationEnd.setDuration(duration);
        onCloseAnimationEnd.setFillAfter(true);
        onCloseAnimationEnd.setInterpolator(new DecelerateInterpolator());
    }

    /**
     * 卡牌文本介绍关闭效果：旋转角度与打开时逆行即可
     */
    private void initCloseAnim() {
        closeAnimation = new Rotate3DAnimation(360, 270, centerX, centerY, depthZ, true);
        closeAnimation.setDuration(duration);
        closeAnimation.setFillAfter(true);
        closeAnimation.setInterpolator(new AccelerateInterpolator());
        closeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                groupBaseInfo.setVisibility(View.VISIBLE);
                otherBaseInfo.setVisibility(View.GONE);
                tvStates.setText(ResourceUtils.getString(R.string.str_coupon_base_info));
                if (onCloseAnimationEnd == null) {
                    initCloseAnimationEnd();
                }
                couponLayout.startAnimation(onCloseAnimationEnd);
            }
        });
    }


    @Override
    protected AddCouponActView bindView() {
        return this;
    }

    @Override
    protected AddCouponActPresenter bindPresenter() {
        return new AddCouponActPresenter(MApplication.mContext);
    }


    private void starAnimation() {
        //以旋转对象的中心点为旋转中心点，这里主要不要再onCreate方法中获取，因为视图初始绘制时，获取的宽高为0
        centerX = couponLayout.getWidth() / 2;
        centerY = couponLayout.getHeight() / 2;
        if (openAnimation == null) {
            initOpenAnim();
            initCloseAnim();
        }
        //用作判断当前点击事件发生时动画是否正在执行
        if (openAnimation.hasStarted() && !openAnimation.hasEnded()) {
            return;
        }
        if (closeAnimation.hasStarted() && !closeAnimation.hasEnded()) {
            return;
        }
        //判断动画执行
        if (isOpen) {
            couponLayout.startAnimation(openAnimation);
        } else {
            couponLayout.startAnimation(closeAnimation);

        }

    }





    public void setInputTextChanged(int currentIndex, String content) {
        if(TextUtils.isEmpty(content)){
            content ="";
        }
        switch (currentIndex){
            case INDEX_COUPON_NAME:
                tvName.setText(content);
                break;
            case INDEX_COUPON_MONEY:
                if(TextUtils.isEmpty(content)){
                    content ="0";
                }
                tvCouponMoney.setText("￥"+content);
                break;
        }
    }

    public void changeDate(int currentIndex, String date) {
        if(TextUtils.isEmpty(date)){
            return;
        }
        switch (currentIndex){
            case INDEX_COUPON_STARTTIME:
                this.startTime = date;
                break;
            case INDEX_COUPON_ENDTIME:
                this.endTime = date;

                break;
        }
        tvTime.setText(startTime+" 至 "+endTime);
    }

//    @OnClick({R.id.tvAniation})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.tvAniation:
//
//                break;
//        }
//    }
}
