package com.zpxt.zhyd.ui.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.zpxt.zhyd.R;
import com.zpxt.zhyd.common.base.BaseActivity;
import com.zpxt.zhyd.common.cache.SharedPreferenceCache;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.MovieModel;
import com.zpxt.zhyd.model.UserModule;
import com.zpxt.zhyd.retrofit.BaseAction;
import com.zpxt.zhyd.retrofit.MyCallBack;
import com.zpxt.zhyd.ui.main.MainActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Description:      登录页
 * Autour：          LF
 * Date：            2018/3/22 11:46
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextView mLoginTv;
    private EditText mUsernameEt;
    private EditText mPasswordEt;
    private CheckBox mRemberBoxCb;

    private String mUsername;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(SharedPreferenceCache.getInstance().getPres("IsLogin").equals("1")){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        hideActionBar();
        initView();
        initListener();
    }

    private void initListener() {
        mLoginTv.setOnClickListener(this);
    }

    private void initView() {
        mLoginTv = findViewById(R.id.login_loginTv);
        mUsernameEt = findViewById(R.id.login_usernameEt);
        mPasswordEt = findViewById(R.id.login_passwordEt);
        mRemberBoxCb = findViewById(R.id.login_remberPwdCb);

        if(!TextUtils.isEmpty(SharedPreferenceCache.getInstance().getPres("LoginName"))){
            mUsernameEt.setText(SharedPreferenceCache.getInstance().getPres("LoginName"));
        }
        if(!TextUtils.isEmpty(SharedPreferenceCache.getInstance().getPres("CustPassword"))){
            mPasswordEt.setText(SharedPreferenceCache.getInstance().getPres("CustPassword"));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_loginTv:
                if(checkInput()){
                    login();
                }
                break;
        }
    }

    /**
     * 检查输入是否合法
     */
    private boolean checkInput() {
        mUsername = mUsernameEt.getText().toString().trim();
        mPassword = mPasswordEt.getText().toString().trim();
        if (TextUtils.isEmpty(mUsername)) {
            SnackbarUtil.ShortSnackbar(mLoginTv, "请输入您的账号", SnackbarUtil.INFO).show();
            return false;

        }
        if (TextUtils.isEmpty(mPassword)) {
            SnackbarUtil.ShortSnackbar(mLoginTv, "请输入您的密码", SnackbarUtil.INFO).show();
            return false;
        }
        return true;
    }

    /**
     * 登录
     */
    private void login() {
        if(mRemberBoxCb.isChecked()){
            SharedPreferenceCache.getInstance().putPres("LoginName",mUsername);
            SharedPreferenceCache.getInstance().putPres("CustPassword",mPassword);
        }
        showProgress(getResources().getString(R.string.loading_text));
        Map<String, String> parameter = new HashMap<>();
        parameter.put("username", mUsername);
        parameter.put("password", mPassword);
        Call<UserModule> call = BaseAction.newInstance(false, false).login(parameter);
        call.enqueue(new MyCallBack<UserModule>(this,"login") {
            @Override
            public void onSuccess(UserModule response) {
                cancelProgress();
                if (response.isSuccess()) {
                    SharedPreferenceCache.getInstance().putPres("AccessToken",response.getToken());
                    SharedPreferenceCache.getInstance().putPres("UserId",response.getUserId());
                    SharedPreferenceCache.getInstance().putPres("IsLogin","1");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }else{
                    SnackbarUtil.ShortSnackbar(mLoginTv, "登录失败", SnackbarUtil.INFO).show();
                }
            }

            @Override
            public void onFail(String message) {
                cancelProgress();
                SnackbarUtil.ShortSnackbar(mLoginTv, message, SnackbarUtil.INFO).show();
            }
        });
    }
}
