package com.zpxt.zhyd.retrofit;

/**
 * Description:      token过期一场
 * Autour：          LF
 * Date：            2018/5/16 16:00
 */

public class TokenExpiredException extends Exception {

    public TokenExpiredException() {
    }

    public TokenExpiredException(String message) {
        throw new RuntimeException("Stub!");
    }
}
