package com.uniaip.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * 作者: ysc
 * 时间: 2017/4/20
 */

public class APPUtils {

    /**
     * 抑制软键盘弹出
     * @param act
     */
    public static void notShowInputMode(Activity act) {
        act.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * 关闭软键盘
     * @param act
     */
    public static void closeInputMode(Activity act) {
        ((InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
        if (null == str || TextUtils.equals(str, ""))
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

}
