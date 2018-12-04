package com.zzq.modulehomepage.netserver

import com.zzq.modulehomepage.bean.*
import com.zzq.netlib.rxbase.BaseResponse
import io.reactivex.Observable
import retrofit2.http.*

/**
 *@auther tangedegushi
 *@creat 2018/11/6
 *@Decribe
 */
interface ApiService {

    @GET("banner/json")
    fun getBannerData(): Observable<BaseResponse<List<BannerData>>>

    @GET("friend/json")
    fun getFriendData(): Observable<BaseResponse<List<FriendData>>>

    @GET("article/list/{page}/json")
    fun getArticleData(@Path("page") page: Int): Observable<BaseResponse<ArticleData>>

    @GET("hotkey/json")
    fun getHotKeyData(): Observable<BaseResponse<List<HotKeyData>>>

    @FormUrlEncoded
    @POST("article/query/{page}/json")
    fun getSearchKeyData(@Path("page") page: Int, @Field("k") searchKey: String): Observable<BaseResponse<SearchKeyData>>

}