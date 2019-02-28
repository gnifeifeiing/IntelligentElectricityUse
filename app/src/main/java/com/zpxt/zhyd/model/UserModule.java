package com.zpxt.zhyd.model;

/**
 * Description:      登录返回的用户实体类
 * Autour：          LF
 * Date：            2018/3/26 11:15
 */

public class UserModule {

    private String token;
    private boolean success;
    private String url;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
