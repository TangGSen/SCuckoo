package company.com.commons.framework.view.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;
import company.com.commons.app.BaseFragment;
import company.com.commons.framework.presenter.MvpPresenter;
import company.com.commons.framework.view.MvpView;


/**
 * Created by Administrator on 2017/8/21.
 */

public abstract class MvpActivity<V extends MvpView, P extends MvpPresenter<V>> extends AppCompatActivity {
    private V view;
    private P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        windowsSetting();
        //初始化参数
        if(initArgs(getIntent().getExtras())){
            //初始化相关参数正确才能进入界面
            setContentView(getLayoutId());
            initView();
            initLinstener();
            initData(savedInstanceState);
        }else{
            finish();
            return;
        }
        if (presenter == null) {
            presenter = bindPresenter();
        }

        if (view == null) {
            view = bindView();
        }

        if (presenter != null && view != null) {
            presenter.attachView(view);
        }
    }

    public void initLinstener() {

    }

    /**
     * 获取布局的id
     * @return
     */
    protected abstract int getLayoutId() ;

    /**
     * 窗口的设置
     */
    protected void windowsSetting(){

    }

    /**
     * 初始化界面的相关参数
     * @param bundle
     * @return
     */
    protected boolean initArgs(Bundle bundle){
        return true;
    }

    /**
     * 初始化控件
     */
    protected void initView(){
        ButterKnife.bind(this);
    }

    /**
     * 初始化数据
     */
    protected void initData(Bundle savedInstanceState){

    }

    /**
     *  点击了界面导航返回时，finish
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }



    /**
     * activity 的返回事件，
     * 如果fragment 处理了，并返回true ,activity 不需要finish
     */
    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList =  getSupportFragmentManager().getFragments();
        if(fragmentList!=null && fragmentList.size()>0){
            for (Fragment fragemnt :fragmentList ) {
                //再判断是否是自己的fragment
                if(fragemnt instanceof BaseFragment){
                    //在判断是否拦截了返回按钮
                    if(((BaseFragment)fragemnt).onBackEnvent()){
                        //拦截了，就让她处理
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();
    }

    protected abstract V bindView();

    //绑定Presenter
    protected abstract P bindPresenter();

    protected P getPresenter() {
        return presenter;
    }

    protected V getView() {
        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.dettachView();
            presenter =null;
        }
    }
}
