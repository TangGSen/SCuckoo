package com.ourcompany.activity.tab_mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.activity.animation.Rotate3DAnimation;
import com.ourcompany.adapter.ViewPagerAdapter;
import com.ourcompany.app.MApplication;
import com.ourcompany.fragment.coupon.CouponNameFragment;
import com.ourcompany.presenter.activity.AddCouponActPresenter;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.AddCouponActView;
import com.ourcompany.widget.CouponConstraintLayoutView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
        final List<Fragment> mFragment = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Bundle bundle = new Bundle();
            bundle.putInt(CouponNameFragment.KEY_CURRENT_INDEX, i + 1);
            bundle.putInt(CouponNameFragment.KEY_COUNT, 10);
            CouponNameFragment couponNameFragment = new CouponNameFragment();
            couponNameFragment.setArguments(bundle);
            mFragment.add(couponNameFragment);
        }


//
//        CouponNameFragment couponNameFragment1 = new CouponNameFragment();
//        mFragment.add(couponNameFragment1);
//        CouponNameFragment couponNameFragment2 = new CouponNameFragment();
//        mFragment.add(couponNameFragment2);
//        CouponNameFragment couponNameFragment3 = new CouponNameFragment();
//        mFragment.add(couponNameFragment3);
//        CouponNameFragment couponNameFragment4 = new CouponNameFragment();
//        mFragment.add(couponNameFragment4);
//        CouponNameFragment couponNameFragment5 = new CouponNameFragment();
//        mFragment.add(couponNameFragment5);
//        CouponNameFragment couponNameFragment6 = new CouponNameFragment();
//        mFragment.add(couponNameFragment6);
//        CouponNameFragment couponNameFragment7 = new CouponNameFragment();
//        mFragment.add(couponNameFragment7);
//        CouponNameFragment couponNameFragment8 = new CouponNameFragment();
//        mFragment.add(couponNameFragment8);


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
