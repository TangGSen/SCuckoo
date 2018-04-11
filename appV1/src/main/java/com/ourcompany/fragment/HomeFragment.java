package com.ourcompany.fragment;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.bean.MainItemChooses;
import com.ourcompany.presenter.fragment.LoginFragPresenter;
import com.ourcompany.utils.DisplayUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.fragment.LoginFragmentView;
import com.ourcompany.widget.ImageCycleView;
import com.ourcompany.widget.recycleview.commadapter.RecycleCommonAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import company.com.commons.framework.view.impl.MvpFragment;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 16:11
 * Des    :
 */

public class HomeFragment extends MvpFragment<LoginFragmentView, LoginFragPresenter> implements LoginFragmentView {

    @BindView(R.id.head_ImageCycle)
    ImageCycleView headImageCycle;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    Unbinder unbinder;
    @BindView(R.id.layoutItemRoot)
    LinearLayout layoutItemRoot;
    private ArrayList<String> urls = new ArrayList<>();
    private String[] mTiltes;
    private List<MainItemChooses> mMessages = new ArrayList<>();
    private RecycleCommonAdapter<MainItemChooses> recycleCommonAdapter;
    int resId[] = new int[]{R.drawable.ic_design, R.drawable.ic_working, R.drawable.ic_supervisor, R.drawable.ic_repair, R.drawable.ic_learning};
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           ToastUtils.showSimpleToast(mTiltes[(Integer) view.getTag(R.id.nine_layout_of_index)]);

        }
    };

    @Override
    protected void initView(View view) {
        super.initView(view);
        int commomHeight = (int) (DisplayUtils.getInstance(getActivity()).getScreenSize()[1] * 0.3);

        ViewGroup.LayoutParams params = headImageCycle.getLayoutParams();
        params.height = commomHeight;
        headImageCycle.setLayoutParams(params);

        ViewGroup.LayoutParams collapsingLayoutParams = collapsingToolbarLayout.getLayoutParams();
        collapsingLayoutParams.height = commomHeight;
        collapsingToolbarLayout.setLayoutParams(collapsingLayoutParams);


        mTiltes = ResourceUtils.getStringArray(R.array.MainChooseItems);
        int size = mTiltes.length;

        for (int i = 0; i < size; i++) {
            layoutItemRoot.addView(generateItemView(i), i);
        }

    }


    private View generateItemView(int position) {
        View iv = LayoutInflater.from(mActivity).inflate(R.layout.layout_item_main_choose, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        iv.setLayoutParams(params);
        iv.setTag(R.id.nine_layout_of_index, position);
        ImageView imageView = iv.findViewById(R.id.image);
        imageView.setImageDrawable(ResourceUtils.getDrawable(resId[position]));
        imageView.setBackground((ResourceUtils.getDrawable(R.drawable.bg_main_ic_repair)));
        TextView textView = iv.findViewById(R.id.tvItemName);
        textView.setText(mTiltes[position]);
         iv.setOnClickListener(onClickListener);
        return iv;
    }

    @Override
    protected void initData() {
        super.initData();
        urls.add("http://5.595818.com/2015/pic/000/372/8e32ba756b80507414dcfdd2e0ffec40.jpg");
        urls.add("http://image6.huangye88.com/2013/09/06/13024fa212299bd1.jpg");
        urls.add("http://img0.imgtn.bdimg.com/it/u=2872266855,3141017511&fm=27&gp=0.jpg");
        urls.add("http://image.bao315.com/UploadFiles/image/20150810/20150810164021_3135.jpg");
        headImageCycle.setImageResources(urls, new ImageCycleView.ImageCycleViewListener() {
            @Override
            public void onImageClick(int position, View imageView) {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_act_home;
    }

    @Override
    protected LoginFragmentView bindView() {
        return this;
    }

    @Override
    protected LoginFragPresenter bindPresenter() {
        return new LoginFragPresenter(MApplication.mContext);
    }


    private int getTabLayoutHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getContext().getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    @Override
    public void showToastMsg(String string) {

    }

    public void reflesh() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
