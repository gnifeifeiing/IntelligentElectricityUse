package com.zpxt.zhyd.retrofit;

import android.content.Intent;

import com.zpxt.zhyd.MyApplication;
import com.zpxt.zhyd.common.cache.SharedPreferenceCache;
import com.zpxt.zhyd.common.utils.Constants;
import com.zpxt.zhyd.ui.login.LoginActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description:      基础网络请求配置
 * Autour：          LF
 * Date：            2017/7/5 16:36
 */

public class BaseAction {

    public static IApiSeivice mService;

    /**
     * 是否需要登陆逻辑判断
     * @param needLogin 是否需要登陆
     * @param isAuth    是否添加AccessToken
     * @return
     */
    public static IApiSeivice newInstance(boolean needLogin, boolean isAuth) {
        if (!needLogin) {
            setService(isAuth);
            return mService;
        } else {
            if(SharedPreferenceCache.getPres("IsLogin").equals("1")){
                setService(isAuth);
                return mService;
            }else{
                MyApplication.getInstance().startActivity(new Intent(MyApplication.getInstance(),LoginActivity.class));
                return null;
            }
        }
    }

    /**
     * 创建请求单例
     * @param isAuth    是否添加AccessToken
     */
    public static void setService(boolean isAuth){
        if (isAuth) {
            setAuthService();
        } else {
            OkHttpClient client = new OkHttpClient.Builder().
                    connectTimeout(60, TimeUnit.SECONDS).
                    readTimeout(60, TimeUnit.SECONDS).
                    writeTimeout(60, TimeUnit.SECONDS).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.SERVER_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mService = retrofit.create(IApiSeivice.class);
        }
    }

    /**
     * 设置AccessToken
     */
    public static void setAuthService() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(60, TimeUnit.SECONDS).
                readTimeout(60, TimeUnit.SECONDS).
                writeTimeout(60, TimeUnit.SECONDS).build();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .addHeader("Authorization", SharedPreferenceCache.getInstance().getPres("AccessToken"))
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        mService = retrofit.create(IApiSeivice.class);
    }
}
