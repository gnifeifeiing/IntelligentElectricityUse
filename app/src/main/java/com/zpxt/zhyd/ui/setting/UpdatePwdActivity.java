package com.zpxt.zhyd.ui.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zpxt.zhyd.R;
import com.zpxt.zhyd.common.base.BaseActivity;
import com.zpxt.zhyd.common.utils.CommonUtils;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.AlarmListModule;
import com.zpxt.zhyd.model.BaseModule;
import com.zpxt.zhyd.retrofit.BaseAction;
import com.zpxt.zhyd.retrofit.MyCallBack;
import com.zpxt.zhyd.views.ClearEditText;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Description:      修改密码
 * Autour：          LF
 * Date：            2018/3/29 14:51
 */
public class UpdatePwdActivity extends BaseActivity implements View.OnClickListener {

    private ClearEditText mOldPwdClet;
    private ClearEditText mNewPwdClet;
    private ClearEditText mConfirmPwdClet;
    private Button mCommitBtn;

    private String mOldPwd,mNewPwd,mConfirmPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);
        setActionBarTitle("修改密码");
        initView();
        initListener();
    }

    private void initView() {
        mOldPwdClet=findViewById(R.id.updatePwd_oldPwdClet);
        mNewPwdClet=findViewById(R.id.updatePwd_newPwdClet);
        mConfirmPwdClet=findViewById(R.id.updatePwd_confirmPwdClet);
        mCommitBtn=findViewById(R.id.updatePwd_commitBtn);
    }

    private void initListener() {
        mCommitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.updatePwd_commitBtn:
                commitNewPwd();
                break;
        }
    }

    private boolean checkInput(){
        mOldPwd = mOldPwdClet.getText().toString().trim();
        mNewPwd = mNewPwdClet.getText().toString().trim();
        mConfirmPwd = mConfirmPwdClet.getText().toString().trim();
        if (TextUtils.isEmpty(mOldPwd)) {
            SnackbarUtil.ShortSnackbar(mOldPwdClet, "请输入原密码", SnackbarUtil.INFO).show();
            return false;
        }else if(mOldPwd.length()<4 || mOldPwd.length()>16){
            SnackbarUtil.ShortSnackbar(mOldPwdClet, "原密码请输入4到18位", SnackbarUtil.INFO).show();
            return false;
        }
        if (TextUtils.isEmpty(mNewPwd)) {
            SnackbarUtil.ShortSnackbar(mOldPwdClet, "请输入4-18位数字和字母组合密码", SnackbarUtil.INFO).show();
            return false;
        }
        if(!CommonUtils.checkoutPassword(mNewPwd)){
            SnackbarUtil.ShortSnackbar(mOldPwdClet, "请输入4-18位数字和字母组合密码", SnackbarUtil.INFO).show();
            return false;
        }
        if (TextUtils.isEmpty(mConfirmPwd)) {
            SnackbarUtil.ShortSnackbar(mOldPwdClet, "请输入您的确认密码", SnackbarUtil.INFO).show();
            return false;
        }
        if (mNewPwd.equals(mOldPwd)) {
            SnackbarUtil.ShortSnackbar(mOldPwdClet, "新密码原密码不能相同", SnackbarUtil.INFO).show();
            return false;
        }
        if (!mNewPwd.equals(mConfirmPwd)) {
            SnackbarUtil.ShortSnackbar(mOldPwdClet, "两次新密码输入不同", SnackbarUtil.INFO).show();
            return false;
        }
        return true;
    }

    private void commitNewPwd(){
        if (checkInput()) {
            showProgress(getResources().getString(R.string.loading_text));
            Map<String, String> parameter = new HashMap<>();
            parameter.put("oldPassword", mOldPwd);
            parameter.put("newPassword", mNewPwd);

            Call<BaseModule> call = BaseAction.newInstance(false, true).updatePwd(parameter);
            call.enqueue(new MyCallBack<BaseModule>(this) {
                @Override
                public void onSuccess(BaseModule response) {
                    cancelProgress();
                    if (response.getResultCode().equals("1")) {
                        Toast.makeText(UpdatePwdActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        SnackbarUtil.ShortSnackbar(mOldPwdClet, "修改失败", SnackbarUtil.INFO).show();
                    }
                }

                @Override
                public void onFail(String message) {
                    cancelProgress();
                    SnackbarUtil.ShortSnackbar(mOldPwdClet, message, SnackbarUtil.INFO).show();
                }
            });
        }
    }
}
