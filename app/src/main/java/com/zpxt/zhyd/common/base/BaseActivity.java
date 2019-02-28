package com.zpxt.zhyd.common.base;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zpxt.zhyd.MyApplication;
import com.zpxt.zhyd.R;
import com.zpxt.zhyd.common.cache.SharedPreferenceCache;
import com.zpxt.zhyd.common.utils.CommonUtils;
import com.zpxt.zhyd.common.utils.Constants;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.PickerViewListModule;
import com.zpxt.zhyd.retrofit.IApiSeivice;
import com.zpxt.zhyd.ui.main.MainActivity;
import com.zpxt.zhyd.ui.report.AreaReportListActivity;
import com.zpxt.zhyd.ui.report.HistoryAlarmDetailActivity;
import com.zpxt.zhyd.views.OneSureDialog;
import com.zpxt.zhyd.views.PrompfDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import retrofit2.Retrofit;


/**
 * Description:      Activity基类
 * Autour：          LF
 * Date：            2017/7/7 16:13
 */
public abstract class BaseActivity<T> extends AppCompatActivity {

    protected ActionBar mActionBar;
    protected TextView mActionBarReturnTv;
    protected TextView mActionBarTitleTv;
    protected ImageView mActionBarHeadIv;
    protected TextView mActionBarContentTv;

    public InputMethodManager mImm;
    public IApiSeivice mService;
    protected Retrofit mRetrofit;

    //进度加载框
    public Dialog mProgressDialog;
    //帅选框
    OptionsPickerView pvOptionsOne;
    OptionsPickerView pvOptionsTwo;
    private String downPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //减少window图层overdraw
        getWindow().setBackgroundDrawable(null);
        // 默认软键盘不弹出
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        MyApplication.getInstance().addActivity(this);
        if(!this.getClass().getName().equals(MainActivity.class.getName())){
            initBaseActionBar();
            initBaseView();
        }
    }

    //自定义actionbar
    protected void initBaseActionBar() {
        mActionBar = getSupportActionBar();
        if (mActionBar == null) {
            return;
        }
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.actionbar_base);
        mActionBar.setElevation(0);
    }

    /**
     * 初始化视图控件
     */
    private void initBaseView() {
        mActionBarReturnTv = findViewById(R.id.actionbar_returnTv);
        mActionBarTitleTv = findViewById(R.id.actionbar_titleTv);
        mActionBarContentTv = findViewById(R.id.actionbar_contentTv);
        mActionBarHeadIv = findViewById(R.id.actionbar_headIv);
        mActionBarReturnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     * 设置标题
     * @param title
     */
    public void setActionBarTitle(String title) {
        mActionBarTitleTv.setText(title);
    }

    /**
     * 隐藏标题栏
     */
    public void hideActionBar(){
        getSupportActionBar().hide();
    }

    /**
     * 两个格式化的日期字符串比较日期大小
     * @param startTime
     * @param endTime
     * @return
     */
    public boolean compareTwoDateString(String startTime,String endTime){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startDate = dateFormat.parse(startTime);
            Date endDate = dateFormat.parse(endTime);
            if(endDate.getTime()>=startDate.getTime()){
                return true;
            }else{
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取版本号
     * @return
     */
    public int getAppVersionCode() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 获取版本号名称
     * @return
     */
    public final String getAppVersionName() {
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 退出登录
     */
    PrompfDialog logOutDialog;
    public void showLogOutDialog(final Context context, final PrompfDialog.UpdateOnclickListener onclickListener) {
        if (logOutDialog == null) {
            logOutDialog = new PrompfDialog(context,
                    R.style.transparentFrameWindowStyle, "退  出", "关  闭",
                    "您确定要退出登录账户吗？", "智慧用电");
            logOutDialog.setCanceledOnTouchOutside(false);
            logOutDialog.setUpdateOnClickListener(new PrompfDialog.UpdateOnclickListener() {
                        // 这里的逻辑和后面的逻辑正好相反 所以使用！
                        @Override
                        public void dismiss() {
                        }

                        @Override
                        public void BtnYesOnClickListener(View v) {
                            onclickListener.BtnYesOnClickListener(v);
                            logOutDialog.dismiss();
                        }

                        @Override
                        public void BtnCancleOnClickListener(View v) {
                            logOutDialog.dismiss();
                        }
                    });
            logOutDialog.show();

            //居中
            Window window = logOutDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            //设置宽度
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = this.getWindowManager().getDefaultDisplay().getWidth() - CommonUtils.dip2px(this, 100);
            window.setAttributes(params);
        } else {
            logOutDialog.show();
        }
    }

    OneSureDialog mOneSureNewDialog;

    public void showOneSureNewDialog(Context context, String title, String content, String positiveStr) {
        if (mOneSureNewDialog == null) {
            mOneSureNewDialog = new OneSureDialog(context, R.style.transparentFrameWindowStyle, positiveStr, content, title);
            mOneSureNewDialog.setCanceledOnTouchOutside(false);
            mOneSureNewDialog.setOnClickListener(new OneSureDialog.UpdateOnclickListener() {

                @Override
                public void BtnYesOnClickListener(OneSureDialog dialog) {
                    dialog.dismiss();
                }
            });
            mOneSureNewDialog.show();

            //居中
            Window window = mOneSureNewDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            //设置宽度
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = this.getWindowManager().getDefaultDisplay().getWidth() - CommonUtils.dip2px(this, 100);
            window.setAttributes(params);
        } else {
            mOneSureNewDialog.show();
        }
    }

    /**
     * 显示对话框
     * @param strMsg
     */
    public void showProgress(String strMsg) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        mProgressDialog = new Dialog(this, R.style.progress_dialog);
        mProgressDialog.setContentView(R.layout.dialog_progress);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        TextView msg = mProgressDialog.findViewById(R.id.id_tv_loadingmsg);
        if (strMsg.isEmpty())
            msg.setText("正在加载");
        else
            msg.setText(strMsg);
        try {
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     * 筛选弹框一
     */
    public void showSearchOneDialog(List<PickerViewListModule> pickerList,final OnOptionsSelectListener onOptionsSelectListener) {
        if (pvOptionsOne == null) {
            pvOptionsOne = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    onOptionsSelectListener.onOptionsSelect(options1,options2,options3,v);
                }
            })
            .setTitleText("请选择")
            .setDividerColor(Color.BLACK)
            .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
            .setContentTextSize(20)
            .setLineSpacingMultiplier(1.8f)
            .build();

            pvOptionsOne.setPicker(pickerList);
            pvOptionsOne.show();
        } else {
            if (!pvOptionsOne.isShowing()) {
                pvOptionsOne.show();
            }
        }
    }

    /**
     * 筛选弹框二
     */
    public void showSearchTwoDialog(List<PickerViewListModule> pickerList,final OnOptionsSelectListener onOptionsSelectListener) {
        if (pvOptionsTwo == null) {
            pvOptionsTwo = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    onOptionsSelectListener.onOptionsSelect(options1,options2,options3,v);
                }
            })
                    .setTitleText("请选择")
                    .setDividerColor(Color.BLACK)
                    .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                    .setContentTextSize(20)
                    .setLineSpacingMultiplier(1.8f)
                    .build();

            pvOptionsTwo.setPicker(pickerList);
            pvOptionsTwo.show();
        } else {
            if (!pvOptionsTwo.isShowing()) {
                pvOptionsTwo.show();
            }
        }
    }

    /**
     * 显示筛选框
     */
    public AlertDialog alertDialog;
    public void showDialogList(Context context, String[] items, String title, final DialogInterface.OnClickListener onClickListener) {
        if(alertDialog==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            // 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onClickListener.onClick(dialog,which);

                }
            });
            alertDialog=builder.create();
            alertDialog.show();
        }else{
            alertDialog.show();
        }
    }


    PrompfDialog mCustomDialog;
    public void showCustomDialog(Context context, String title, String content, String positiveStr, String negativeStr, final CustomDialogInterface customDialogInterface) {
        if (mCustomDialog == null) {
            mCustomDialog = new PrompfDialog(context, R.style.transparentFrameWindowStyle, positiveStr, negativeStr, content, title);
            mCustomDialog.setCanceledOnTouchOutside(false);
            mCustomDialog.setUpdateOnClickListener(new PrompfDialog.UpdateOnclickListener() {
                // 这里的逻辑和后面的逻辑正好相反 所以使用！
                @Override
                public void dismiss() {
                }

                @Override
                public void BtnYesOnClickListener(View v) {
                    customDialogInterface.onCommitClick();
                    mCustomDialog.dismiss();
                }

                @Override
                public void BtnCancleOnClickListener(View v) {
                    customDialogInterface.onCancleClick();
                    mCustomDialog.dismiss();
                }
            });
            mCustomDialog.show();
            //居中
            Window window = mCustomDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            //设置宽度
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = this.getWindowManager().getDefaultDisplay().getWidth() - CommonUtils.dip2px(this, 100);
            window.setAttributes(params);

        } else {
            mCustomDialog.show();
        }
    }






    public interface CustomDialogInterface {
        void onCommitClick();

        void onCancleClick();
    }
}
