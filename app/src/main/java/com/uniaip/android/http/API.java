package com.uniaip.android.http;

import com.uniaip.android.root.login.model.UserModel;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 作者: ysc
 * 时间: 2017/4/18
 */

public interface API {

    @POST("login/login")
    @FormUrlEncoded
    Observable<UserModel> login(@Field("phone") String phone, @Field("password") String password);

}
