package com.zpxt.zhyd.ui.setting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.suke.widget.SwitchButton;
import com.zpxt.zhyd.R;
import com.zpxt.zhyd.common.base.BaseActivity;
import com.zpxt.zhyd.common.cache.SharedPreferenceCache;
import com.zpxt.zhyd.common.utils.FileCacheUtils;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.BaseModule;
import com.zpxt.zhyd.retrofit.BaseAction;
import com.zpxt.zhyd.retrofit.MyCallBack;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;

/**
 * Description:      消息推送设置页
 * Autour：          LF
 * Date：            2018/6/21 10:51
 */
public class MsgPushSettingActivity extends BaseActivity implements View.OnClickListener {

    private SwitchButton mMsgPushTogleBtn;
    private RadioGroup mPushTypeRg;
    private RadioButton mPushTypeAllRb;
    private RadioButton mPushTypeTRb;
    private RadioButton mPushTypeITb;
    private Button mSubmitBtn;

    private String mSeletedPushType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_push_setting);
        setActionBarTitle("推送设置");
        initView();
        initListener();
        initData();
    }

    private void initData() {
        if(TextUtils.isEmpty(SharedPreferenceCache.getInstance().getPres("JPushType"))){
            mSeletedPushType="ALL";
            mPushTypeAllRb.setChecked(true);
        }else{
            if(SharedPreferenceCache.getInstance().getPres("JPushType").equals("ALL")){
                mSeletedPushType="ALL";
                mPushTypeAllRb.setChecked(true);
            }else if(SharedPreferenceCache.getInstance().getPres("JPushType").equals("T")){
                mSeletedPushType="T";
                mPushTypeTRb.setChecked(true);
            }else{
                mSeletedPushType="I";
                mPushTypeITb.setChecked(true);
            }
        }
    }


    private void initView() {
        mMsgPushTogleBtn = findViewById(R.id.pushSetting_msgPushTogleBtn);

        mPushTypeRg = findViewById(R.id.pushSetting_pushTypeRg);
        mPushTypeAllRb = findViewById(R.id.pushSetting_pushTypeAllRb);
        mPushTypeTRb = findViewById(R.id.pushSetting_pushTypeTRb);
        mPushTypeITb = findViewById(R.id.pushSetting_pushTypeIRb);
        mSubmitBtn = findViewById(R.id.pushSetting_submitBtn);
    }

    private void initListener() {
        mSubmitBtn.setOnClickListener(this);
        //推送是否开启
        mMsgPushTogleBtn.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    //开启极光推送
                    SharedPreferenceCache.getInstance().putPres("IsOpenJPush", "1");
                    JPushInterface.resumePush(getApplicationContext());
                    mSubmitBtn.setEnabled(true);
                } else {
                    //关闭极光推送
                    SharedPreferenceCache.getInstance().putPres("IsOpenJPush", "0");
                    JPushInterface.stopPush(getApplicationContext());
                    mSubmitBtn.setEnabled(false);
                }
            }
        });
        if (SharedPreferenceCache.getInstance().getPres("IsOpenJPush").equals("0")) {
            mMsgPushTogleBtn.setChecked(false);
        } else {
            mMsgPushTogleBtn.setChecked(true);
        }
        //是否更改推送类型
        mPushTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    //全部类型
                    case R.id.pushSetting_pushTypeAllRb:
                        mSeletedPushType="ALL";
                        break;
                    //温度
                    case R.id.pushSetting_pushTypeTRb:
                        mSeletedPushType="T";
                        break;
                    //电流
                    case R.id.pushSetting_pushTypeIRb:
                        mSeletedPushType="I";
                        break;
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pushSetting_submitBtn:
                savePushSetting();
                break;
        }
    }

    private void savePushSetting(){
        showProgress(getResources().getString(R.string.loading_text));
        Map<String, String> parameter = new HashMap<>();
        parameter.put("alarmType", mSeletedPushType);

        Call<BaseModule> call = BaseAction.newInstance(false, true).savePushSetting(parameter);
        call.enqueue(new MyCallBack<BaseModule>(this) {
            @Override
            public void onSuccess(BaseModule response) {
                cancelProgress();
                if (response.getResultCode().equals("1")) {
                    SharedPreferenceCache.getInstance().putPres("JPushType",mSeletedPushType);
                    Toast.makeText(MsgPushSettingActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    SnackbarUtil.ShortSnackbar(mMsgPushTogleBtn, "保存成功", SnackbarUtil.INFO).show();
                }
            }

            @Override
            public void onFail(String message) {
                cancelProgress();
                SnackbarUtil.ShortSnackbar(mMsgPushTogleBtn, message, SnackbarUtil.INFO).show();
            }
        });
    }
}
