package com.zpxt.zhyd.ui.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.zpxt.zhyd.R;
import com.zpxt.zhyd.common.base.BaseActivity;
/**
 * Description:      欢迎界面
 * Autour：          LF
 * Date：            2018/3/31 10:07
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉状态栏，实现全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideActionBar();
        setContentView(R.layout.activity_splash);
        getAllInfomation();

    }

    private void getAllInfomation() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    startAc();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    startAc();
                }
            }
        }).start();
    }

    private void startAc() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
