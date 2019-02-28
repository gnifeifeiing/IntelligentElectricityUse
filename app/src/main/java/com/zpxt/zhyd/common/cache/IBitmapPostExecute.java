package com.zpxt.zhyd.common.cache;

import android.graphics.Bitmap;

/**
 * Description:      三级缓存获取网络图片接口
 * Autour：          LF
 * Date：            2017/7/21 12:02
 */

public interface IBitmapPostExecute {
    void onPostExecuteSuccess(Bitmap bitmap);
}
