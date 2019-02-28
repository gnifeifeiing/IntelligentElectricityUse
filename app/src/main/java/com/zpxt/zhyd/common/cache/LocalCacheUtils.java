package com.zpxt.zhyd.common.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.zpxt.zhyd.common.utils.Constants;
import com.zpxt.zhyd.common.utils.MD5Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Description:      三级缓存之本地缓存
 * Autour：          LF
 * Date：            2017/7/19 16:50
 */
public class LocalCacheUtils {

    private static String IMAGE_PATH = "/image_cache";

    /**
     * 从本地读取图片
     *
     * @param url
     * @return
     */
    public Bitmap getBitmapFromLocal(String url) {
        String fileName = null;//把图片的url当做文件名,并进行MD5加密
        try {
            fileName = MD5Encoder.encode(url);
            File file = new File(Constants.APP_SDCARD_FILE_PATH + IMAGE_PATH, fileName);

            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 从网络获取图片后,保存至本地缓存
     *
     * @param url
     * @param bitmap
     */
    public void setBitmapToLocal(String url, Bitmap bitmap) {
        try {
            String fileName = MD5Encoder.encode(url);//把图片的url当做文件名,并进行MD5加密
            File file = new File(Constants.APP_SDCARD_FILE_PATH + IMAGE_PATH, fileName);

            //通过得到文件的父文件,判断父文件是否存在
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }

            //把图片保存至本地
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
