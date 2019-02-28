package com.zpxt.zhyd.common.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.zpxt.zhyd.MyApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 维护 所有缓存的路径
 *
 * @author WD
 */
public class FileUtil {
    /**
     * 缓存数据的目录
     */
    private static final String CACHE = "cache";
    private static final String ERROR = "cache" + File.separator + ".error";//隐藏目录
    /**
     * 缓存图片的目录
     */
    private static final String ICON = "icon";
    private static final String ICON_CLIP = "icon" + File.separator + "clip";
    /**
     * 下载目录
     **/
    private static final String Down = "down";
    /**
     * 缓存路径的根目录
     **/
    private static final String ROOT = "ZPXT";

    public static File getDir(String dir) {
        StringBuilder path = new StringBuilder();
        // sd卡可用
        if (isSDAvailable()) {
            path.append(Environment.getExternalStorageDirectory()
                    .getAbsolutePath());// /mnt/sdcard
            // linux /
            // windows \
            path.append(File.separator); // /mnt/sdcard/
            path.append(ROOT);// /mnt/sdcard/googleplayz12
            path.append(File.separator);
            path.append(dir);// /mnt/sdcard/googleplayz12/cache
        } else {
            //  /data/data/包名/cache/cache
            path.append(MyApplication.getInstance().getCacheDir().getAbsolutePath());///data/data/包名/cache
            path.append(File.separator);
            path.append(dir); //  /data/data/包名/cache/cache
        }
        File file = new File(path.toString());
        if (!file.exists() || !file.isDirectory()) {
            // 创建文件夹
            file.mkdirs();
        }
        return file;
    }

	/*private static boolean isSDAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}*/

    /**
     * SD卡是否可用.
     */
    public static boolean isSDAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sd = new File(Environment.getExternalStorageDirectory().getPath());
            return sd.canWrite();
        } else
            return false;
    }

    /**
     * 得到SD卡根目录.
     */
    public static File getRootPath() {
        File path = null;
        if (FileUtil.isSDAvailable()) {
            path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        } else {
            path = Environment.getDataDirectory();
        }
        return path;
    }

    /**
     * 得到SD卡根目录.
     */
    public static File getRootPath1(Context context) {
        if (FileUtil.sdCardIsAvailable()) {
            return Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        } else {
            return context.getFilesDir();
        }
    }

    /**
     * SD卡是否可用.
     */
    public static boolean sdCardIsAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sd = new File(Environment.getExternalStorageDirectory().getPath());
            return sd.canWrite();
        } else
            return false;
    }

    /**
     * 文件或者文件夹是否存在.
     */
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static void deleleFile(String path) {
        deleteFile(new File(path));
    }

    public static void deleteFile(File file) {
        if (file != null && file.exists())
            file.delete();
    }

    /**
     * 删除指定文件夹下所有文件, 保留文件夹.
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (file.isFile()) {
            file.delete();
            return true;
        }
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File exeFile = files[i];
            if (exeFile.isDirectory()) {
                delAllFile(exeFile.getAbsolutePath());
            } else {
                exeFile.delete();
            }
        }
        return flag;
    }

    /**
     * 获取数据缓存的目录
     */
    public static File getCache() {
        return getDir(CACHE);
    }

    public static File getCacheError() {
        return getDir(ERROR);
    }

    /*** 缓存图标位置**/
    public static File getIcon() {
        return getDir(ICON);//  /mnt/sdcard/ylb/icon
    }

    public static File getIconClip() {
        return getDir(ICON_CLIP);
    }

    /**
     * 下载文件位置
     **/
    public static File getDown() {
        return getDir(Down);//  /mnt/sdcard/googleplayz12/icon
    }

    /**
     * 将图片（Bitmap）保存到本地
     *
     * @param context
     * @param bmp
     * @param saveResultCallback
     */
    public static void savePhoto(final Context context, final Bitmap bmp, final String fileName, final SaveResultCallback saveResultCallback) {
        final File sdDir = getDir(Down);
        if (sdDir == null) {
            android.widget.Toast.makeText(context, "设备存储不可用", android.widget.Toast.LENGTH_LONG).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                //File appDir = new File(sdDir, "out_photo");
                if (!sdDir.exists()) {
                    sdDir.mkdir();
                }
                //fileName=getFileNameByTime();
                File file = new File(sdDir, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                    saveResultCallback.onSavedSuccess(file.getPath());
                } catch (FileNotFoundException e) {
                    saveResultCallback.onSavedFailed();
                    e.printStackTrace();
                } catch (IOException e) {
                    saveResultCallback.onSavedFailed();
                    e.printStackTrace();
                }

                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(file);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            }
        }).start();
    }

    public interface SaveResultCallback {
        void onSavedSuccess(String imgPath);

        void onSavedFailed();
    }

    /**
     * 有路径获取文件名
     */
    public static String getFileNameByPath(String path) {
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        String[] split = path.split("/");
        String nameTable = split[split.length - 1];
        /*File file=new File(path);
        String fileName=file.getName();
        if (MyUtil.isEmpty(fileName)) {
            return "";
        }*/
		/*if (nameTable.contains(".")){
			String[] nameString = nameTable.split(".");
			LogUtils.i(nameTable+"图片路径："+nameString.length);
			nameTable=nameString[0];
		}*/
        return nameTable;
    }

    /**
     * 根据时间设置文件名称
     */
    public static String getFileNameByTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置以当前时间格式为图片名称
        String fileName = df.format(new Date()) + ".png";
        return fileName;
    }
}
