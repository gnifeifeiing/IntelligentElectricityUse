package com.zpxt.zhyd.common.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;


import com.zpxt.zhyd.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Describe:    捕获全局异常并写入sd卡中
 * Author:      LF
 * Date:        2017/5/31
 */
public class UncaughtException implements UncaughtExceptionHandler {
    private final static String TAG = "UncaughtException";
    private static UncaughtException mUncaughtException;
    private Context context;
    private boolean isSaveLocal = true;
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String path = Constants.APP_SDCARD_FILE_PATH + "/errlog";//错误日志的文件路径
    private String filePath = path + "/error.txt";//错误日志文件
    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private UncaughtException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * 同步方法，以免单例多线程环境下出现异常
     */
    public synchronized static UncaughtException getInstance() {
        if (mUncaughtException == null) {
            mUncaughtException = new UncaughtException();
        }
        return mUncaughtException;
    }

    /**
     * 初始化，把当前对象设置成UncaughtExceptionHandler处理器
     *
     * @param context
     * @param isSaveLocal 是否将错误日志文件保存到本地
     */
    public void init(Context context, boolean isSaveLocal) {
        this.context = context;
        this.isSaveLocal = isSaveLocal;
        Thread.setDefaultUncaughtExceptionHandler(mUncaughtException);
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {
        // TODO Auto-generated method stub
        //处理异常,我们还可以把异常信息写入文件，以供后来分析。
        if (isSaveLocal) {
            saveCrashInfo2File(ex);
        } else {
            Log.e(TAG, "uncaughtException thread : " + thread + "||name=" + thread.getName() + "||id=" + thread.getId() + "||exception=" + ex);
            new Thread() {
                public void run() {
                    Looper.prepare();
                    showDialog();
                    Looper.loop();
                }

            }.start();
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("提示")
                .setMessage("系统错误，请上传日志")
                .setPositiveButton("我知道了",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                MyApplication.getInstance().ExitApp();
                            }
                        });

        AlertDialog dialog = builder.create();
        //需要的窗口句柄方式，没有这句会报错的
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    /**
     * 保存错误信息到文件中
     */
    private void saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(filePath);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
    }
}
