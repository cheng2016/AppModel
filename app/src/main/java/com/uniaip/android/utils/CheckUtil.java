package com.uniaip.android.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者: ysc
 * 时间: 2016/12/28
 */

public class CheckUtil {
    /**
     * 验证手机号码是否正确
     *
     * @param tel
     * @return true:号码正确
     */
    public static Boolean checktel(String tel) {
        // 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188，182
        // 联通：130、131、132、152、155、156、185、186
        // 电信：133、153、180、189、（1349卫通）
        String s1 = "^((14[0-9])|(13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(s1);
        Matcher m = p.matcher(tel);
        final boolean is = m.find();
        return is;
    }

    /**
     * 检测是否包含中文
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }


}
