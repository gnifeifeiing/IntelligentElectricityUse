package com.zpxt.zhyd.common.cache;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Description:      三级缓存工具类
 * Autour：          LF
 * Date：            2017/7/19 16:43
 */

public class BitmapUtils {
    private NetCacheUtils mNetCacheUtils;
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;

    public BitmapUtils(){
        mMemoryCacheUtils=new MemoryCacheUtils();
        mLocalCacheUtils=new LocalCacheUtils();
        mNetCacheUtils=new NetCacheUtils(mLocalCacheUtils,mMemoryCacheUtils);
    }

    /**
     * 设置imageview图片
     * @param ivPic
     * @param url
     */
    public void disPlay(ImageView ivPic, String url) {
//        ivPic.setImageResource(R.mipmap.pic_item_list_default);
        Bitmap bitmap;
        //内存缓存
        bitmap=mMemoryCacheUtils.getBitmapFromMemory(url);
        if (bitmap!=null){
            ivPic.setImageBitmap(bitmap);
            System.out.println("从内存获取图片啦.....");
            return;
        }

        //本地缓存
        bitmap = mLocalCacheUtils.getBitmapFromLocal(url);
        if(bitmap !=null){
            ivPic.setImageBitmap(bitmap);
            System.out.println("从本地获取图片啦.....");
            //从本地获取图片后,保存至内存中
            mMemoryCacheUtils.setBitmapToMemory(url,bitmap);
            return;
        }
        //网络缓存
        mNetCacheUtils.getBitmapFromNet(ivPic,url);
    }

    /**
     * 三级策略获取bitmap
     * @param url
     */
    public void getBitmap(String url, final IBitmapPostExecute iBitmapPostExecute) {
        Bitmap bitmap;
        //内存缓存
        bitmap=mMemoryCacheUtils.getBitmapFromMemory(url);
        if (bitmap!=null){
            iBitmapPostExecute.onPostExecuteSuccess(bitmap);
            return;
        }

        //本地缓存
        bitmap = mLocalCacheUtils.getBitmapFromLocal(url);
        if(bitmap !=null){
            //从本地获取图片后,保存至内存中
            mMemoryCacheUtils.setBitmapToMemory(url,bitmap);
            iBitmapPostExecute.onPostExecuteSuccess(bitmap);
            return;
        }
        //网络缓存
        mNetCacheUtils.getBitmapFromNet(url, new IBitmapPostExecute() {
            @Override
            public void onPostExecuteSuccess(Bitmap result) {
                iBitmapPostExecute.onPostExecuteSuccess(result);
            }
        });
    }
}
