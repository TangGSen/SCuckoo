package com.ourcompany.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ourcompany.EmptyMvpPresenter;
import com.ourcompany.EmptyMvpView;
import com.ourcompany.R;
import com.ourcompany.adapter.TabLayoutViewPagerAdapter;
import com.ourcompany.app.MApplication;
import com.ourcompany.fragment.coupon.CouponHistoryFragment;
import com.ourcompany.interfaces.MOnTabSelectedListener;
import com.ourcompany.utils.DisplayUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.TabLayoutIndicatorWith;

import java.util.ArrayList;

import butterknife.BindView;
import company.com.commons.swidget.BaseSheetDialogFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/21 下午6:41
 * Des    :
 */
public class HistoryCouponListDialog extends BaseSheetDialogFragment<EmptyMvpView, EmptyMvpPresenter> implements EmptyMvpView {
    @BindView(R.id.tabLayout)
    TabLayout mTablayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    private ArrayList<Fragment> fragments;
    private String[] mTiltes;

    public static HistoryCouponListDialog newInstance() {
        return new HistoryCouponListDialog();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_coupon_history_dialog;
    }

    @Override
    protected EmptyMvpView bindView() {
        return this;
    }

    @Override
    protected EmptyMvpPresenter bindPresenter() {
        return new EmptyMvpPresenter(MApplication.mContext);
    }



//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
//
//
//        try {
//            Field mBehaviorField = bottomSheetDialog.getClass().getDeclaredField("mBehavior");
//            mBehaviorField.setAccessible(true);
//            final BottomSheetBehavior behavior = (BottomSheetBehavior) mBehaviorField.get(bottomSheetDialog);
//            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//                @Override
//                public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
//                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                    }
//                }
//
//                @Override
//                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                }
//            });
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//        return bottomSheetDialog;
//    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        initTabView();
    }

    private void initTabView() {
        TabLayoutIndicatorWith.resetWith(mTablayout);
        fragments = new ArrayList<>();

        mTiltes = ResourceUtils.getStringArray(R.array.tabCouponItems);
        for (int i = 0; i < mTiltes.length; i++) {
            mTablayout.addTab(mTablayout.newTab().setText(mTiltes[i]));
        }
        //需要加个标识是未过期，已过期的类型标识,0 代表未过期，1代表已过期
        CouponHistoryFragment notOverdueFrag = new CouponHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CouponHistoryFragment.KEY_TYPE, CouponHistoryFragment.TYPE_NOT_OVERDUE);
        notOverdueFrag.setArguments(bundle);
        fragments.add(notOverdueFrag);

        CouponHistoryFragment overdueFrag = new CouponHistoryFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt(CouponHistoryFragment.KEY_TYPE, CouponHistoryFragment.TYPE_OVERDUE);
        overdueFrag.setArguments(bundle2);
        fragments.add(overdueFrag);


        TabLayoutViewPagerAdapter viewPagerAdapter = new TabLayoutViewPagerAdapter(getChildFragmentManager(), mTiltes, fragments);
        //tablayout 和viewpager 联动
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
        mTablayout.addOnTabSelectedListener(new MOnTabSelectedListener(mViewPager));
        mViewPager.setCurrentItem(0);
        setTopOffset(DisplayUtils.getWindowHeight()/4);
    }


    public void closeDialog() {
        if (getBehavior() != null) {
            getBehavior().setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    @Override
    public void showToastMsg(String string) {

    }
}
