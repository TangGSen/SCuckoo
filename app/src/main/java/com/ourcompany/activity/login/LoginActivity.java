package com.ourcompany.activity.login;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ourcompany.R;
import com.ourcompany.activity.HomeActivity;
import com.ourcompany.presenter.activity.LoginActPresenter;
import com.ourcompany.utils.PhoneTextWatcher;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;
import com.ourcompany.view.activity.LoginActvityView;

import butterknife.BindView;
import butterknife.OnClick;
import company.com.commons.framework.view.impl.MvpActivity;

public class LoginActivity extends MvpActivity<LoginActvityView, LoginActPresenter> implements LoginActvityView {

    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.find_password)
    Button findPassword;
    @BindView(R.id.singUp)
    Button singUp;

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(commonToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        commonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public void initLinstener() {
        super.initLinstener();


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userName = etUserName.getText().toString().trim();
                String password = etPassword.getText().toString();
                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password) ) {
                    btLogin.setEnabled(true);
                } else {
                    btLogin.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        etPassword.addTextChangedListener(textWatcher);
        etUserName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        etUserName.addTextChangedListener(new PhoneTextWatcher(etUserName));
        etUserName.addTextChangedListener(textWatcher);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginActvityView bindView() {
        return this;
    }

    @Override
    protected LoginActPresenter bindPresenter() {
        return new LoginActPresenter(this);
    }


    @OnClick({R.id.bt_login, R.id.find_password, R.id.singUp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.find_password:
                Intent fintent = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(fintent);
                break;
            case R.id.singUp:
                Intent intent = new Intent(LoginActivity.this, ResigisterActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_login:
                String phones = etUserName.getText().toString().trim();
                String password = etPassword.getText().toString();
                getPresenter().login( phones, password);
                break;
        }
    }

    @Override
    public void loading() {

    }

    @Override
    public void loaded() {

    }

    @Override
    public void loginSucess() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

    @Override
    public void loginFial() {
        showToastMsg(ResourceUtils.getString(R.string.login_fail));
    }

    @Override
    public void showToastMsg(String string) {
        ToastUtils.showSimpleToast(string);
    }
}
