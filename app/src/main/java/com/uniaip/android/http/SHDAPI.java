package com.uniaip.android.http;

import com.uniaip.android.root.login.model.UserModel;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.uniaip.android.root.SHDApplication.DEBUG;
import static com.uniaip.android.root.SHDApplication.ONLINE;

/**
 * 作者: ysc
 * 时间: 2017/4/18
 */

public class SHDAPI {
    //初始化================
    /**
     * api实例
     */
    private static API API;
    public static String url = ONLINE ? "http://api.pengjipay.com/spendls/" : "http://192.168.1.174:1020/spendls/";
    /**
     * 初始化网络请求，在Application创建的时候调用,确保只初始化一次
     */
    public static void init() {


        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        if (DEBUG) { //DEBUG模式
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            builder.client(client);
        }
        API = builder.build().create(API.class);

    }

    //接口=======================

    /**
     * 登录
     *
     * @param phone    账号
     * @param password 密码
     */
    public static Observable<UserModel> login(String phone, String password) {
        return thread(API.login(phone, password));
    }

    //其它封装===============

    /**
     * 后台网络,前台UI线程
     */
    private static <T> Observable<T> thread(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 错误空拦截
     */
    private static <T> Observable<T> onErrorEmpty(Observable<T> observable) {
        return observable.onErrorResumeNext(Observable.empty());
    }
}
