package com.zpxt.zhyd.ui.setting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.suke.widget.SwitchButton;
import com.zpxt.zhyd.R;
import com.zpxt.zhyd.common.base.BaseActivity;
import com.zpxt.zhyd.common.cache.SharedPreferenceCache;
import com.zpxt.zhyd.common.utils.FileCacheUtils;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.BaseModule;
import com.zpxt.zhyd.retrofit.BaseAction;
import com.zpxt.zhyd.retrofit.MyCallBack;
import com.zpxt.zhyd.ui.login.LoginActivity;
import com.zpxt.zhyd.views.PrompfDialog;

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;

/**
 * Description:      设置列表页
 * Autour：          LF
 * Date：            2018/3/16 16:05
 */
public class SettingMainActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mUpdatePwdRl;
    private RelativeLayout mVersionInfoRl;
    private RelativeLayout mPushSettingRl;

    private TextView mLoginOutTv;
    private TextView mCacheTv;
    private TextView mVersionTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_main);
        setActionBarTitle("设置");
        initView();
        initListener();
    }

    private void initView() {
        mUpdatePwdRl = findViewById(R.id.setting_updatePwdRl);
        mVersionInfoRl = findViewById(R.id.setting_versionInfoRl);
        mPushSettingRl = findViewById(R.id.setting_msgPushRl);
        mLoginOutTv = findViewById(R.id.setting_loginOutTv);
        mVersionTv = findViewById(R.id.setting_versionTv);

        mVersionTv.setText(getAppVersionName());
        try {
            String str=FileCacheUtils.getCacheSize(getExternalCacheDir());
            mCacheTv.setText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListener() {
        mUpdatePwdRl.setOnClickListener(this);
        mVersionInfoRl.setOnClickListener(this);
        mLoginOutTv.setOnClickListener(this);
        mPushSettingRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //修改密码
            case R.id.setting_updatePwdRl:
                startActivity(new Intent(this,UpdatePwdActivity.class));
                break;
            //推送设置
            case R.id.setting_msgPushRl:
                startActivity(new Intent(this,MsgPushSettingActivity.class));
                break;
            //版本信息
            case R.id.setting_versionInfoRl:
                break;
            //退出登录
            case R.id.setting_loginOutTv:
                logOut();
                break;
        }
    }

    private void logOut(){
        showLogOutDialog(this, new PrompfDialog.UpdateOnclickListener() {
            @Override
            public void dismiss() {

            }

            @Override
            public void BtnYesOnClickListener(View v) {
                clearToken();
            }

            @Override
            public void BtnCancleOnClickListener(View v) {

            }
        });
    }

    private void clearToken() {
        showProgress(getResources().getString(R.string.loading_text));
        Call<BaseModule> call = BaseAction.newInstance(false, true).signOut();
        call.enqueue(new MyCallBack<BaseModule>(this) {
            @Override
            public void onSuccess(BaseModule response) {
                cancelProgress();
                if (response.getResultCode().equals("1")) {
                    SharedPreferenceCache.getInstance().clearUserInfo();
                    startActivity(new Intent(SettingMainActivity.this,LoginActivity.class));
                    finish();
                } else {
                    SnackbarUtil.ShortSnackbar(mUpdatePwdRl, "注销失败", SnackbarUtil.INFO).show();
                }
            }

            @Override
            public void onFail(String message) {
                cancelProgress();
                SnackbarUtil.ShortSnackbar(mUpdatePwdRl, message, SnackbarUtil.INFO).show();
            }
        });
    }
}
