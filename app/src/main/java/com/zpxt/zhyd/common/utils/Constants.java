package com.zpxt.zhyd.common.utils;

import android.os.Environment;

/**
 * Description:      常量配置类
 * Autour：          LF
 * Date：            2018/3/14 17:07
 */

public class Constants {

    /******************** 生产环境配置 -- 服务器地址 ******************/
    public static String SERVER_URL = "http://zhyd.pmcc.cn/app/";
    public static String SERVER_SOCKET_URL = "ws://zhyd.pmcc.cn/websocket/";

    /******************* 测试环境配置 -- 测试地址 **********************/
//    public static String SERVER_URL = "http://b8652176.ngrok.io/app/";
//    public static String SERVER_SOCKET_URL = "ws://b8652176.ngrok.io/websocket/";
//    public static String SERVER_URL = "http://192.168.1.115:8080/app/";
//    public static String SERVER_SOCKET_URL = "ws://192.168.1.115:8080/";


    /*************************** 华丽分割线 *****************************/

    /**
     * 请求失败分类
     */
    public static String TOKEN_EXPIRED_FAIL = "登录过期，请重新登录";
    public static String NETWORK_NO = "无网络可用";
    public static String NETWORK_NOT_WORK = "当前网络不可用";
    public static String TIMEOUT_ERROR = "链接超时";
    public static String CONNECT_NOT_ERROR = "无法链接网路";
    public static String JSON_SYNTAX_ERROR = "Json解析失败";
    public static String CONNECT_FAIL = "请求失败";
    /**
     * 默认文件夹路径
     */
    public static String APP_SDCARD_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zpxt";
    /**
     * 分页数
     */
    public static String PAGE_SIZE = "10";
    public static String PAGE_SIZE_LARGE = "20";
    /**
     * 分页--显示没有更多数据
     */
    public static String NO_MORE_DATA = "没有更多数据";

    public static final int REQUEST_CODE_PERMISSION_SINGLE = 100;
    public static final int REQUEST_CODE_PERMISSION_MULTI = 101;
    public static final int REQUEST_CODE_SETTING = 300;
}
