package com.uniaip.android.utils;


import android.os.Handler;

import org.greenrobot.eventbus.EventBus;


/**
 * EventBus封装
 * Created by Hao on 2016/1/7.
 */
public class EB {

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private static Handler H;

    /**
     * 初始化
     */
    public static void init() {
        //初始化Handler
        H = new Handler();
    }


    /**
     * 注册
     *
     * @param subscriber 订阅者
     */
    public static void register(Object subscriber) {
        if (!EVENT_BUS.isRegistered(subscriber)) { //没有注册,注册
            EVENT_BUS.register(subscriber);
        }
    }

    /**
     * 反注册
     *
     * @param subscriber 订阅者
     */
    public static void unregister(Object subscriber) {
        if (EVENT_BUS.isRegistered(subscriber)) {
            EVENT_BUS.unregister(subscriber);
        }
    }

    /**
     * 通知在UI线程
     *
     * @param event 事件
     */
    public static void postUI(Object event) {
        H.post(() -> post(event));
    }

    /**
     * 通知
     *
     * @param event 事件
     */
    public static void post(Object event) {
        EVENT_BUS.post(event);
    }
}
