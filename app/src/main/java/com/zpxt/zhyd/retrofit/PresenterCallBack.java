package com.zpxt.zhyd.retrofit;

/**
 * Description:      描述
 * Autour：          LF
 * Date：            2017/7/5 17:56
 */

public interface PresenterCallBack<T> {
    //成功
    void onSuccess(T result);

    //失败
    void onFail(String errMsg);
}
