package com.uniaip.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import static com.uniaip.android.root.SHDApplication.AppCtx;

/**
 * 作者: ysc
 * 时间: 2017/1/6
 */

public class SP {

    private SharedPreferences mSp;

    public SP() {
        this("default");
    }

    public SP(String name) {
        mSp = AppCtx.getSharedPreferences(AppCtx.getPackageName() + "." + name, Context.MODE_PRIVATE);
    }

    /**
     * 清空数据
     *
     * @return true 成功
     */
    public boolean clear() {
        return mSp.edit().clear().commit();
    }

    /**
     * 保存数据
     *
     * @param key   key
     * @param value value
     */
    public boolean save(String key, String value) {
        return mSp.edit().putString(key, value).commit();
    }

    /**
     * 获取保存的数据
     *
     * @param key      键
     * @param defValue 默认返回的value
     * @return value
     */
    public String load(String key, String defValue) {
        return mSp.getString(key, defValue);
    }

    /**
     * 保存数据
     *
     * @param key   key
     * @param value value
     */
    public boolean save(String key, boolean value) {
        return mSp.edit().putBoolean(key, value).commit();
    }

    /**
     * 获取保存的数据
     *
     * @param key      键
     * @param defValue 默认返回的value
     * @return value
     */
    public boolean load(String key, boolean defValue) {
        return mSp.getBoolean(key, defValue);
    }

    /**
     * 保存数据
     *
     * @param key   key
     * @param value value
     */
    public boolean save(String key, int value) {
        return mSp.edit().putInt(key, value).commit();
    }

    /**
     * 获取保存的数据
     *
     * @param key      键
     * @param defValue 默认返回的value
     * @return value
     */
    public int load(String key, int defValue) {
        return mSp.getInt(key, defValue);
    }

    /**
     * 保存数据
     *
     * @param key   key
     * @param value value
     */
    public boolean save(String key, long value) {
        return mSp.edit().putLong(key, value).commit();
    }

    /**
     * 获取保存的数据
     *
     * @param key      键
     * @param defValue 默认返回的value
     * @return value
     */
    public long load(String key, long defValue) {
        return mSp.getLong(key, defValue);
    }
}
