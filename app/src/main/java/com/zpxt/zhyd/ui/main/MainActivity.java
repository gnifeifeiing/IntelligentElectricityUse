package com.zpxt.zhyd.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.ServerError;
import com.yanzhenjie.nohttp.error.StorageReadWriteError;
import com.yanzhenjie.nohttp.error.StorageSpaceNotEnoughError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zpxt.zhyd.MyApplication;
import com.zpxt.zhyd.R;
import com.zpxt.zhyd.common.CallServer;
import com.zpxt.zhyd.common.base.BaseActivity;
import com.zpxt.zhyd.common.cache.SharedPreferenceCache;
import com.zpxt.zhyd.common.utils.CommonUtils;
import com.zpxt.zhyd.common.utils.Constants;
import com.zpxt.zhyd.common.utils.FileUtil;
import com.zpxt.zhyd.common.utils.FormetFileSizeUtils;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.AlarmListModule;
import com.zpxt.zhyd.model.BaseModule;
import com.zpxt.zhyd.model.MovieModel;
import com.zpxt.zhyd.model.UserModule;
import com.zpxt.zhyd.model.VersionMessageModule;
import com.zpxt.zhyd.retrofit.BaseAction;
import com.zpxt.zhyd.retrofit.MyCallBack;
import com.zpxt.zhyd.ui.fragmnt.MainFirstFragment;
import com.zpxt.zhyd.ui.fragmnt.MainFourthFragment;
import com.zpxt.zhyd.ui.fragmnt.MainSecondFragment;
import com.zpxt.zhyd.ui.fragmnt.MainThirdFragment;
import com.zpxt.zhyd.ui.login.LoginActivity;
import com.zpxt.zhyd.ui.report.RecentlyReportActivity;
import com.zpxt.zhyd.ui.setting.SettingMainActivity;
import com.zpxt.zhyd.views.DownloadDialog;
import com.zpxt.zhyd.views.PrompfDialog;
import com.zpxt.zhyd.views.WDSeekBar;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private LinearLayout mContentLl;
    private NavigationView mNavigationNv;
    private DrawerLayout mDrawerLayout;

    private TextView mTitleTv;
    /***************内容页***************/
    private FrameLayout mContentFl;
    private RadioGroup mRadioGroup;
    private RadioButton mSssjRb;
    private RadioButton mLssjRb;
    private RadioButton mJczbRb;
    private RadioButton mLsgjRb;


    private Fragment mFirstFragment;
    private Fragment mSecondFragment;
    private Fragment mThirdFragment;
    private Fragment mFourthFragment;

    FragmentTransaction mFragmentTransaction;

    private int mStatusBarHeight = -1;
    private Menu mMenu;         //获取optionmenu
    private VersionMessageModule.ObjectBean object;
    private String downPath;
    private DownloadRequest mDownloadRequest;

    private static final int REQUEST_CODE_INSTALL_PACKAGE = 10086;
    private File mApkFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initStatusBarHeight();
        setToolBar();

        //设置自动登录
        autoLogin();
    }


    private void autoLogin() {
        showProgress(getResources().getString(R.string.logining_text));
        Map<String, String> parameter = new HashMap<>();
        parameter.put("username", SharedPreferenceCache.getInstance().getPres("LoginName"));
        parameter.put("password", SharedPreferenceCache.getInstance().getPres("CustPassword"));
        Call<UserModule> call = BaseAction.newInstance(false, false).login(parameter);
        call.enqueue(new MyCallBack<UserModule>(this, "login") {
            @Override
            public void onSuccess(UserModule response) {
                cancelProgress();
                if (response.isSuccess()) {
                    SharedPreferenceCache.getInstance().putPres("AccessToken", response.getToken());
                    SharedPreferenceCache.getInstance().putPres("UserId", response.getUserId());
                    SharedPreferenceCache.getInstance().putPres("IsLogin", "1");

                    //登录成功再请求
                    setInitFragment();

                    //检查版本
                    checkVersion();
                } else {
                    SharedPreferenceCache.getInstance().clearUserInfo();
                    Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onFail(String message) {
                cancelProgress();
                SharedPreferenceCache.getInstance().clearUserInfo();
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //判断是否开启推送
        if (SharedPreferenceCache.getInstance().getPres("IsOpenJPush").equals("1")) {
            //极光推送
            String userid = SharedPreferenceCache.getInstance().getPres("UserId");
            Log.e("_____", userid);
            JPushInterface.setDebugMode(false);
            JPushInterface.init(this);
            JPushInterface.setAlias(this, //上下文对象
                    userid, //别名
                    new TagAliasCallback() {//回调接口,i=0表示成功,其它设置失败
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {
                            Log.d("alias", "set alias result is" + i);
                        }
                    });
        }
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mContentLl = findViewById(R.id.main_contentLl);
        mNavigationNv = findViewById(R.id.main_navigationNv);
        mDrawerLayout = findViewById(R.id.main_drawerLayout);
        mTitleTv = findViewById(R.id.main_titleTv);

        mContentFl = findViewById(R.id.main_contentFl);
        mRadioGroup = findViewById(R.id.main_radioGroup);
        mSssjRb = findViewById(R.id.main_sssjRb);
        mLssjRb = findViewById(R.id.main_lssjRb);
        mJczbRb = findViewById(R.id.main_jczbRb);
        mLsgjRb = findViewById(R.id.main_lsgjRb);
    }

    private void initListener() {
        mSssjRb.setOnClickListener(this);
        mLssjRb.setOnClickListener(this);
        mJczbRb.setOnClickListener(this);
        mLsgjRb.setOnClickListener(this);

        mNavigationNv.setItemIconTintList(null);
        //侧滑项监听事件
        mNavigationNv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    //设置
                    case R.id.mainNav_setting:
                        startActivity(new Intent(MainActivity.this, SettingMainActivity.class));
                        break;
                    //退出登录
                    case R.id.mainNav_advice:
                        showOneSureNewDialog(MainActivity.this, "温馨提示", "功能正在加急开发中", "知道了");
                        break;
                    //退出登录
                    case R.id.mainNav_logout:
                        logOut();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 退出登录
     */
    private void logOut() {
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
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                } else {
                    SnackbarUtil.ShortSnackbar(mSssjRb, "注销失败", SnackbarUtil.INFO).show();
                }
            }

            @Override
            public void onFail(String message) {
                cancelProgress();
                SnackbarUtil.ShortSnackbar(mSssjRb, message, SnackbarUtil.INFO).show();
            }
        });
    }

    /**
     * 获取状态栏高度
     */
    private void initStatusBarHeight() {
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            mStatusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
    }

    /**
     * 设置toolBar
     */
    private void setToolBar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        //设置内容页随侧滑而移动
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                WindowManager windowManager = (WindowManager) getSystemService(getApplicationContext().WINDOW_SERVICE);
                Display display = windowManager.getDefaultDisplay();
                mContentLl.layout(mNavigationNv.getRight(), mStatusBarHeight, display.getWidth() + mNavigationNv.getRight(), display.getHeight());
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    public void setInitFragment() {
        mFirstFragment = new MainFirstFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_contentFl, mFirstFragment).commit();
    }

    @Override
    public void onClick(View view) {
        showFragment(view.getId());
    }

    public void showFragment(int resId) {
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        //先隐藏所有
        hideFragments();
        switch (resId) {
            case R.id.main_sssjRb:
                mMenu.getItem(0).setVisible(true);
                if (mFirstFragment == null) {
                    mFirstFragment = new MainFirstFragment();
                    mFragmentTransaction.add(R.id.main_contentFl, mFirstFragment);
                } else {
                    mFragmentTransaction.show(mFirstFragment);
                }
                mTitleTv.setText("实时数据");
                break;
            case R.id.main_lssjRb:
                mMenu.getItem(0).setVisible(false);
                if (mSecondFragment == null) {
                    mSecondFragment = new MainSecondFragment();
                    mFragmentTransaction.add(R.id.main_contentFl, mSecondFragment);
                } else {
                    mFragmentTransaction.show(mSecondFragment);
                }
                mTitleTv.setText("历史数据");
                break;
            case R.id.main_jczbRb:
                mMenu.getItem(0).setVisible(false);
                if (mThirdFragment == null) {
                    mThirdFragment = new MainThirdFragment();
                    mFragmentTransaction.add(R.id.main_contentFl, mThirdFragment);
                } else {
                    mFragmentTransaction.show(mThirdFragment);
                }
                mTitleTv.setText("检测周报");
                break;
            case R.id.main_lsgjRb:
                mMenu.getItem(0).setVisible(false);
                if (mFourthFragment == null) {
                    mFourthFragment = new MainFourthFragment();
                    mFragmentTransaction.add(R.id.main_contentFl, mFourthFragment);
                } else {
                    mFragmentTransaction.show(mFourthFragment);
                }
                mTitleTv.setText("历史告警");
                break;
        }
        mFragmentTransaction.commit();
    }

    public void hideFragments() {
        if (mFirstFragment != null) {
            mFragmentTransaction.hide(mFirstFragment);
        }
        if (mSecondFragment != null) {
            mFragmentTransaction.hide(mSecondFragment);
        }
        if (mThirdFragment != null) {
            mFragmentTransaction.hide(mThirdFragment);
        }
        if (mFourthFragment != null) {
            mFragmentTransaction.hide(mFourthFragment);
        }
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        mMenu = menu;
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * 设置toolBar
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //最近告警
            case R.id.mainFirst_toolbarRight:
                startActivity(new Intent(this, RecentlyReportActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isExit;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (!isExit) {
                if (mRadioGroup.getCheckedRadioButtonId() == R.id.main_sssjRb) {
                    isExit = true;
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show();
                    Timer tExit = new Timer();
                    tExit.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            isExit = false; // 取消退出
                        }
                    }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
                    return true;
                } else {
                    mSssjRb.setChecked(true);
                    mLssjRb.setChecked(false);
                    mJczbRb.setChecked(false);
                    mLsgjRb.setChecked(false);
                    showFragment(R.id.main_sssjRb);
                    return true;
                }
            } else {
                MyApplication.getInstance().ExitApp();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void checkVersion() {
        Call<VersionMessageModule> call = BaseAction.newInstance(false, true).checkVersion();
        call.enqueue(new MyCallBack<VersionMessageModule>(this) {
            @Override
            public void onSuccess(VersionMessageModule response) {
                if (response != null) {
                    object = response.getObject();
                    if (object != null && !TextUtils.isEmpty(object.getVersion())) {
                        try {
                            if ((CommonUtils.compareVersion(object.getVersion(), getAppVersionName()) > 0)) {
                                showUpdateDialog(MainActivity.this, object.getDownloadUrl(),
                                        object.getVersion(), mDrawerLayout);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFail(String message) {

            }
        });
    }

    PrompfDialog updateDialog;

    public void showUpdateDialog(final Context context,
                                 final String versionPath, final String versionName, final View view) {
        if (updateDialog == null) {
            updateDialog = new PrompfDialog(context,
                    R.style.transparentFrameWindowStyle, "更  新", "关  闭",
                    "检测到最新版本，需要更新吗？", "智慧用电");
            updateDialog.setCanceledOnTouchOutside(false);
            updateDialog.setUpdateOnClickListener(new PrompfDialog.UpdateOnclickListener() {
                // 这里的逻辑和后面的逻辑正好相反 所以使用！
                @Override
                public void dismiss() {
                }

                @Override
                public void BtnYesOnClickListener(View v) {
                    Log.i("传过去的数据", versionPath + "," + versionName);
                    downPath = versionPath;
                    showDownloadDig(downPath);
                    updateDialog.dismiss();
                }

                @Override
                public void BtnCancleOnClickListener(View v) {
                    updateDialog.dismiss();
                }
            });
            updateDialog.show();
            //居中
            Window window = updateDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            //设置宽度
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = this.getWindowManager().getDefaultDisplay().getWidth() - CommonUtils.dip2px(this, 100);
            window.setAttributes(params);
        } else {
            updateDialog.show();
        }
    }

    private void showDownloadDig(String versionPath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AndPermission.with(MainActivity.this)
                    .requestCode(Constants.REQUEST_CODE_PERMISSION_MULTI)
                    .permission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .callback(permissionListener)
                    // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                    // 这样避免用户勾选不再提示，导致以后无法申请权限。
                    // 你也可以不设置。
                    .rationale(new RationaleListener() {
                        @Override
                        public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                            // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                            AndPermission.rationaleDialog(MainActivity.this, rationale).show();
                        }
                    })
                    .start();
        } else {
            download(versionPath);
        }
    }

    /**
     * 回调监听。
     */
    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case Constants.REQUEST_CODE_PERMISSION_SINGLE: {
                    download(downPath);
                    break;
                }
                case Constants.REQUEST_CODE_PERMISSION_MULTI: {
                    download(downPath);
                    break;
                }
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case Constants.REQUEST_CODE_PERMISSION_SINGLE: {
                    SnackbarUtil.LongSnackbar(mDrawerLayout, "未获得文件存储权限,不能更新", SnackbarUtil.ERROR).show();

                    break;
                }
                case Constants.REQUEST_CODE_PERMISSION_MULTI: {
                    SnackbarUtil.LongSnackbar(mDrawerLayout, "未获得文件存储权限,不能更新", SnackbarUtil.ERROR).show();
                    break;
                }
            }
            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions)) {
                // 第二种：用自定义的提示语。
                AndPermission.defaultSettingDialog(MainActivity.this, Constants.REQUEST_CODE_SETTING)
                        .setTitle("权限申请失败")
                        .setMessage("文件存储权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                        .setPositiveButton("好，去设置")
                        .show();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_CODE_SETTING:
                if (AndPermission.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.REQUEST_INSTALL_PACKAGES)) {
                    download(downPath);
                } else {
                    SnackbarUtil.LongSnackbar(mDrawerLayout, "未获得文件存储权限,不能更新", SnackbarUtil.ERROR).show();
                }
                break;
            case REQUEST_CODE_INSTALL_PACKAGE:
                installProcess();//再次执行安装流程，包含权限判等
                break;
        }
    }

    /**
     * 开始下载。
     *
     * @param apkurl
     */
    private void download(String apkurl) {
        // 开始下载了，但是任务没有完成，代表正在下载，那么暂停下载。
        if (mDownloadRequest != null && mDownloadRequest.isStarted() && !mDownloadRequest.isFinished()) {
            // 暂停下载。
            mDownloadRequest.cancel();
        } else if (mDownloadRequest == null || mDownloadRequest.isFinished()) {// 没有开始或者下载完成了，就重新下载。
            //apkurl=NetURL.key + apkurl;
            //apkurl= "https://www.pgyer.com/apiv2/app/install?appKey=035b323c2ea79fe467fa1f3552208fbe&_api_key=ae99cd8ded1a876f098050265b3564e2";
            mDownloadRequest = new DownloadRequest(apkurl, RequestMethod.GET,
                    FileUtil.getDown().getAbsolutePath(),
                    true, true);

            // what 区分下载。
            // downloadRequest 下载请求对象。
            // downloadListener 下载监听。
            CallServer.getInstance().download(0, mDownloadRequest, downloadListener);

            // 添加到队列，在没响应的时候让按钮不可用。
            //    mBtnStart.setEnabled(false);
        }
    }

    private DownloadDialog downloadDialog;
    private WDSeekBar bar;
    private TextView textView;
    private String info_down;
    /**
     * 下载监听
     */
    private DownloadListener downloadListener = new DownloadListener() {

        @Override
        public void onStart(int what, boolean isResume, long beforeLength, Headers headers, long allCount) {
            if (downloadDialog == null) {
                downloadDialog = new DownloadDialog(MainActivity.this, R.style.transparentFrameWindowStyle, "关 闭", textView, "正在下载", bar);
                downloadDialog.setCanceledOnTouchOutside(false);
                downloadDialog.setCancelable(false);

                Window window = downloadDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                downloadDialog.show();
            } else {
                downloadDialog.show();
            }
            //  / mBtnStart.setText(R.string.download_status_pause);
            //  mBtnStart.setEnabled(true);
        }

        @Override
        public void onProgress(int what, int progress, long fileCount, long speed) {
            if (downloadDialog != null) {
                info_down = FormetFileSizeUtils.FormetFileSize((int) fileCount) + "/" + FormetFileSizeUtils.FormetFileSize((int) speed);
                downloadDialog.tv_content.setText(progress + "/" + "100");
                downloadDialog.wdSeekBar.setMax(100);
                downloadDialog.wdSeekBar.setProgress((int) progress);
            }
        }

        @Override
        public void onFinish(int what, String filePath) {
            Log.i("Download finish", "file path: " + filePath);
//            startInstall(filePath);
            mApkFile=new File(filePath);
            installProcess();
        }

        @Override
        public void onCancel(int what) {
            //  mTvResult.setText(R.string.download_status_be_pause);
            // mBtnStart.setText(R.string.download_status_resume);
            //   mBtnStart.setEnabled(true);
        }


        @Override
        public void onDownloadError(int what, Exception exception) {
            Logger.e(exception);
            //   mBtnStart.setText(R.string.download_status_again_download);
            //  mBtnStart.setEnabled(true);
            String message = getString(R.string.download_error);
            String messageContent;
            if (exception instanceof ServerError) {
                messageContent = getString(R.string.download_error_server);
            } else if (exception instanceof NetworkError) {
                messageContent = getString(R.string.download_error_network);
            } else if (exception instanceof StorageReadWriteError) {
                messageContent = getString(R.string.download_error_storage);
            } else if (exception instanceof StorageSpaceNotEnoughError) {
                messageContent = getString(R.string.download_error_space);
            } else if (exception instanceof TimeoutError) {
                messageContent = getString(R.string.download_error_timeout);
            } else if (exception instanceof UnKnownHostError) {
                messageContent = getString(R.string.download_error_un_know_host);
            } else if (exception instanceof URLError) {
                messageContent = getString(R.string.download_error_url);
            } else {
                messageContent = getString(R.string.download_error_un);
            }
            message = String.format(Locale.getDefault(), message, messageContent);
            Log.i("下载失败", "onDownloadError: " + message);
        }

    };
    private int REQUEST_FOR_INSTALL_APK = 1;

    private void startInstall(String filePath) {
        if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
            //在AndroidManifest中的android:authorities值
            Uri apkUri = FileProvider.getUriForFile(MainActivity.this, "com.zpxt.zhyd.fileprovider", new File(filePath));
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            startActivity(install);
            // 开启安装界面的目的 还要拿到用户点的是哪个
            startActivityForResult(install, REQUEST_FOR_INSTALL_APK);
            android.os.Process.killProcess(android.os.Process.myPid());//如果不加，最后不会提示完成、打开。
        } else {
            //System.out.println("下载完成至：" + apkFile.getPath());
            Intent intent = new Intent();// 提示用户进行安装 开启一个系统的界面 用到隐式意图
            intent.setAction("android.intent.action.VIEW");
            Uri uri = Uri.fromFile(new File(filePath)); // 翻译一个file 成Uri
                   /* intent.setType("application/vnd.android.package-archive");
                    intent.setData(uri);*/
            intent.setDataAndType(uri, "application/vnd.android.package-archive"); // 如果需要同时设置type和data需要 同时设置
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//如果不加，最后不会提示完成、打开。
            // 开启安装界面的目的 还要拿到用户点的是哪个
            startActivityForResult(intent, REQUEST_FOR_INSTALL_APK);
            android.os.Process.killProcess(android.os.Process.myPid());//如果不加，最后不会提示完成、打开。
        }
    }

    @Override
    protected void onDestroy() {
        // 暂停下载
        if (mDownloadRequest != null) {
            mDownloadRequest.cancel();
        }
        super.onDestroy();
    }


    //安装应用的流程
    private void installProcess() {
        boolean haveInstallPermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先获取是否有安装未知来源应用的权限
            haveInstallPermission = getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {//没有权限
                showCustomDialog(this, "提示", "安装应用需要打开未知来源权限，请去设置中开启权限", "确定", "取消", new CustomDialogInterface() {
                    @Override
                    public void onCommitClick() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startInstallPermissionSettingActivity();
                        }
                    }

                    @Override
                    public void onCancleClick() {

                    }
                });
                return;
            }
        }
        //有权限，开始安装应用程序
        installApk(mApkFile);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        Uri packageURI = Uri.parse("package:" + getPackageName());
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        startActivityForResult(intent, REQUEST_CODE_INSTALL_PACKAGE);
    }

    //安装应用
    private void installApk(File apk) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
        } else {//Android7.0之后获取uri要用contentProvider
            Uri uri = FileProvider.getUriForFile(this, "com.zpxt.zhyd.fileprovider", apk);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getBaseContext().startActivity(intent);
    }
}
