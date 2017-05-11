package com.uniaip.android.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者: ysc
 * 时间: 2017/1/2
 */

public class TextUtil {

    /**
     * 关键字高亮变色
     *
     * @param color   变化的色值
     * @param text    文字
     * @param keyword 文字中的关键字
     * @return
     */
    public static SpannableString matcherSearchTitle(int color, String text, String keyword) {
        SpannableString s = new SpannableString(text);
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(color), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

    /**
     * 随机生成字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) { // length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789"; // 生成字符串从此序列中取
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    public static String getImgName() {
        String time = new Date().getTime() + "";
        time = time + ((int) (Math.random() * 1000));
        return time;
    }


    /**
     * 将时间戳转为字符串
     */
    public static String getStrTime(String time) {
        if (null == time || "null".equals(time) || "0".equals(time))
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(Long.valueOf(time) * 1000L));
    }

    public static String getStrCompleteTime(String time) {
        if (null == time || "null".equals(time) || "0".equals(time))
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(Long.valueOf(time) * 1000L));
    }

}


