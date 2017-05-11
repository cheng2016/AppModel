package com.uniaip.android.root;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.baidu.mapapi.SDKInitializer;
import com.uniaip.android.http.SHDAPI;
import com.uniaip.android.utils.EB;

import java.io.File;

import static com.uniaip.android.utils.Toast.show;

/**
 * 作者: ysc
 * 时间: 2017/4/18
 */

public class SHDApplication extends Application {
    public static final File fileImage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/shdian");
    public static final boolean ONLINE = false;//是否是线上模式
    public static final boolean DEBUG = true;//是否打印Log日志
    public static Context AppCtx;//APP上下文实例缓存
    public static WindowManager mManager = null; // 窗口管理
    public static String photoPath;//存放位置

    @Override
    public void onCreate() {
        super.onCreate();
//        SDKInitializer.initialize(getApplicationContext());
        AppCtx = getApplicationContext();
        photoPath = Environment.getExternalStorageDirectory().getPath() + "/shdian/";
        if (!ONLINE)
            show("当前是测试服务器");
        if (DEBUG)
            show("当前是测试模式");

        //初始化网络工具类
        SHDAPI.init();
        //初始化工具类
        EB.init();


        mManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mManager.getDefaultDisplay().getMetrics(new DisplayMetrics());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

    }
}
