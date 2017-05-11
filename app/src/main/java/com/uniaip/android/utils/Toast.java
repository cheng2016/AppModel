package com.uniaip.android.utils;


import android.text.TextUtils;

import static com.uniaip.android.root.SHDApplication.AppCtx;


/**
 * Toast工具类
 */
public class Toast {
    private static android.widget.Toast mToast;
    /**
     * Toast
     * @param res 资源ID
     */
    public static void show(int res) {
        show(AppCtx.getString(res));
    }

    /**
     * Toast
     * @param text 文本
     */
    public static void show(String text) {
        if (TextUtils.isEmpty(text)) //空文本不显示
            return;
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = android.widget.Toast.makeText(AppCtx, text, android.widget.Toast.LENGTH_SHORT);
        mToast.show();
    }
}
