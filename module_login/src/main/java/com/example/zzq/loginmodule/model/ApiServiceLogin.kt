package com.example.zzq.loginmodule.model

import com.example.zzq.loginmodule.bean.LoginData
import com.example.zzq.loginmodule.bean.RegisterData
import com.google.gson.JsonObject
import com.zzq.netlib.rxbase.BaseResponse
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 *@auther tangedegushi
 *@creat 2018/11/27
 *@Decribe
 */
interface ApiServiceLogin {

    @FormUrlEncoded
    @POST("user/register")
    fun register(@Field("username") userName: String,
                 @Field("password") password: String,
                 @Field("repassword") rePassword: String): Observable<BaseResponse<LoginData>>

    @FormUrlEncoded
    @POST("user/login")
    fun login(@Field("username") username: String, @Field("password") password: String): Observable<BaseResponse<LoginData>>

    @GET("user/logout/json")
    fun loginOut(): Observable<BaseResponse<String>>
}