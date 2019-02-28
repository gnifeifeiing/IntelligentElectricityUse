package com.zpxt.zhyd.common.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.zpxt.zhyd.MyApplication;


/**
 * Description:      SharedPreference存储
 * Autour：          LF
 * Date：            2018/1/4 15:37
 */
public class SharedPreferenceCache {

    private static SharedPreferenceCache mInstance;
    private static SharedPreferences settings;

    /**
     * 单例
     *
     * @return SharedPreferencesUser对象
     */
    public static SharedPreferenceCache getInstance() {
        if (mInstance == null) {
            synchronized (SharedPreferenceCache.class) {
                if (mInstance == null) {
                    mInstance = new SharedPreferenceCache();
                    settings = MyApplication.getInstance().getSharedPreferences(SharedPreferenceCache.class.getName(),
                            Context.MODE_PRIVATE);
                }
            }
        }
        return mInstance;
    }

    public static void putPres(String key, String value) {
        SharedPreferences.Editor editor = settings.edit();
        // 用户ID
        if (key.equals("UserId")) {
            editor.putString("UserId", value);
        }
        // 是否登录 0-否 1-是
        else if (key.equals("IsLogin")) {
            editor.putString("IsLogin", value);
        }
        // 登录名
        else if (key.equals("LoginName")) {
            editor.putString("LoginName", value);
        }
        // 登录密码
        else if (key.equals("CustPassword")) {
            editor.putString("CustPassword", value);
        }
        // 用户姓名
        else if (key.equals("UserName")) {
            editor.putString("UserName", value);
        }
        // token
        else if (key.equals("AccessToken")) {
            editor.putString("AccessToken", value);
        }
        // 是否开启极光推送 0:关闭    1:开启
        else if (key.equals("IsOpenJPush")) {
            editor.putString("IsOpenJPush", value);
        }
        // 开启极光推送的推送类型
        else if (key.equals("JPushType")) {
            editor.putString("JPushType", value);
        }
        else {
            editor.putString(key, value);
        }
        editor.commit();
    }


    public static String getPres(String key) {
        String value = "";
        if (key.equals("UserId")) {
            value = settings.getString("UserId", "");
        } else if (key.equals("IsLogin")) {
            value = settings.getString("IsLogin", "0");
        } else if (key.equals("LoginName")) {
            value = settings.getString("LoginName", "");
        } else if (key.equals("CustPassword")) {
            value = settings.getString("CustPassword", "");
        } else if (key.equals("UserName")) {
            value = settings.getString("UserName", "");
        } else if (key.equals("AccessToken")) {
            value = settings.getString("AccessToken", "");
        }else if (key.equals("IsOpenJPush")) {
            value = settings.getString("IsOpenJPush", "1");
        }else if (key.equals("JPushType")) {
            value = settings.getString("JPushType", "");
        } else {
            value = settings.getString(key, "");
        }
        return value;
    }


    /**
     * 是否存在该字段
     *
     * @param result
     * @return
     */
    public boolean existResult(String result) {
        return settings.contains(result);
    }

    /**
     * 移除该字段
     *
     * @param preName
     */
    public void removePre(String preName) {
        settings.edit().remove(preName).commit();
    }

    /**
     * 清空用户信息
     */
    public void clearUserInfo() {
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 清空用户相关信息
     * 保留用户名和密码
     */
    public void clearExpiredInfo() {
        removePre("UserId");
        removePre("IsLogin");
        removePre("UserName");
        removePre("AccessToken");
        removePre("IsOpenJPush");
    }
}
