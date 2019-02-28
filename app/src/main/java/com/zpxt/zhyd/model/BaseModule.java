package com.zpxt.zhyd.model;

/**
 * Description:      接口返回基类
 * Autour：          LF
 * Date：            2018/3/26 14:38
 */

public class BaseModule {

    private String resultCode;
    private String resultDesc;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }
}
