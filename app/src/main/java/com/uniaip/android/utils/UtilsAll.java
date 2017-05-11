package com.uniaip.android.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 作者: ysc
 * 时间: 2017/1/11
 */

public class UtilsAll {

    /**
     * 进行IO流读写
     *
     * @param inputStream txt文件流
     */
    private static String read(InputStream inputStream) {
        try {
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            int length;
            while ((length = inputStream.read()) != -1) {
                oStream.write(length);
            }
            String str = new String(oStream.toByteArray(), "utf-8");
            String mString = "\ufeff";
            if (str != null && str.startsWith(mString)) {
                str = str.substring(1);
            }
            return str;
        } catch (IOException e) {
            return null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key))
            return null;
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resultData;
    }

    /**
     * dip转换为px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context mContext) {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 删除本地压缩后的图片
     */
    public static void deleteImages(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteImages(f);
            }
            file.delete();
        }
    }

    /**
     * 获取版本号
     *
     * @param mContext
     * @return
     */
    public static String getVersionName(Context mContext) {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 保留第一个小数点，去除多余的小数点
     *
     * @return
     */
    public static double removeStrDecimalPoint(String str) {
        if (isNullOrEmpty(str))
            return 0;
        String strs[] = str.split("\\.");
        String mStr = "";
        for (int i = 0; i < strs.length; i++) {
            if (i == 1) {
                mStr += "." + strs[i];
            } else {
                mStr += strs[i];
            }
        }
        return Double.parseDouble(mStr);
    }

    public static boolean isNullOrEmpty(String text) {
        if (text == null || text.length() == 0) {
            return true;
        }
        return false;
    }


    /**
     * 进入应用详情页
     *
     * @param mContext
     */
    public static void goAPPDetails(Context mContext) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", mContext.getPackageName());
        }
        mContext.startActivity(localIntent);
    }
}
