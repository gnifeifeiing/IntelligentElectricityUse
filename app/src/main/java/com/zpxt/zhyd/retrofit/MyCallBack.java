package com.zpxt.zhyd.retrofit;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.zpxt.zhyd.MyApplication;
import com.zpxt.zhyd.common.cache.SharedPreferenceCache;
import com.zpxt.zhyd.common.utils.Constants;
import com.zpxt.zhyd.common.utils.NetworkUtils;
import com.zpxt.zhyd.ui.login.LoginActivity;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Description:      retrofit封装的回调
 * Autour：          LF
 * Date：            2017/7/5 17:37
 */
public abstract class MyCallBack<T> implements Callback<T> {

    private Context mContext;
    private String mLoginPs="noLogin";

    public MyCallBack(Context context){
        this.mContext=context;
    }

    public MyCallBack(Context context,String loginPs){
        this.mContext=context;
        this.mLoginPs=loginPs;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
//        Log.e("Retrofit-response-info",response.body().toString());
//        200是服务器有合理响应
        if (response.raw().code() == 200) {
            Log.i("Retrofit-response-info",response.raw().request().toString());
            if(response.raw().header("token","false").equals("true")){
                //未过期
                onSuccess(response.body());
            }else{
                //过期
                if(mLoginPs.equals("login")){
                    onSuccess(response.body());
                }else{
                    onFailure(call, new TokenExpiredException());
                }
            }
        } else {//失败响应
            Log.e("Retrofit"," code : "+ String.valueOf(response.raw().code())+" ; "+response.raw().request().toString());
            onFailure(call, new RuntimeException("response error,detail = " + response.raw().toString()));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        //失败先判断网络状态
        if(!NetworkUtils.isConnected(MyApplication.getInstance())){
            onFail(Constants.NETWORK_NO);
            return;
        }
        if(!NetworkUtils.isAvailable(MyApplication.getInstance())){
            onFail(Constants.NETWORK_NOT_WORK);
            return;
        }
        //判断过期登录
        if(t instanceof TokenExpiredException){
            onFail(Constants.TOKEN_EXPIRED_FAIL);
            SharedPreferenceCache.getInstance().clearExpiredInfo();
            Intent loginIntent = new Intent(mContext,LoginActivity.class);
            mContext.startActivity(loginIntent);
            Toast.makeText(mContext,"登录过期，请重新登录",Toast.LENGTH_SHORT).show();
        }
        //再判断失败原因
        if(t instanceof SocketTimeoutException){
            onFail(Constants.TIMEOUT_ERROR);
        }else if(t instanceof ConnectException){
            onFail(Constants.CONNECT_NOT_ERROR);
        }else if(t instanceof JsonSyntaxException){
            onFail(Constants.JSON_SYNTAX_ERROR);
        }else if(t instanceof IllegalStateException){
            onFail(Constants.JSON_SYNTAX_ERROR);
        }else if(t instanceof RuntimeException){
            onFail(Constants.CONNECT_FAIL);
        }else{
            onFail(Constants.CONNECT_FAIL);
        }
        Log.e(MyCallBack.class.getName(),t.toString());
    }

    public abstract void onSuccess(T response);

    public abstract void onFail(String message);
}
