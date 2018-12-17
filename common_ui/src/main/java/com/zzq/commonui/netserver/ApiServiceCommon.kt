package com.zzq.commonui.netserver

import com.zzq.commonui.bean.CollectArticleData
import com.zzq.commonui.bean.CollectOutArticleData
import com.zzq.commonui.bean.SearchKeyData
import com.zzq.commonui.bean.TypeArticleData
import com.zzq.netlib.rxbase.BaseResponse
import io.reactivex.Observable
import retrofit2.http.*

/**
 *@auther tangedegushi
 *@creat 2018/12/5
 *@Decribe
 */
interface ApiServiceCommon {

    /**
     * 知识体系下的文章
     * http://www.wanandroid.com/article/list/0/json?cid=168
     * @param page page
     * @param cid cid
     */
    @GET("/article/list/{page}/json")
    fun getArticleList(@Path("page") page: Int,
                       @Query("cid") cid: Int):Observable<BaseResponse<TypeArticleData>>

    /**
     * 根据关键字查询文章
     */
    @FormUrlEncoded
    @POST("article/query/{page}/json")
    fun getSearchKeyData(@Path("page") page: Int,
                         @Field("k") searchKey: String): Observable<BaseResponse<SearchKeyData>>

    /**
     * 收藏站内文章
     */
    @POST("lg/collect/{id}/json")
    fun collectArticle(@Path("id") id: Int): Observable<BaseResponse<String>>

    /**
     * 收藏站外文章
     */
    @FormUrlEncoded
    @POST("lg/collect/add/json")
    fun collectOutArticle(@Field("title")articleTitle: String,
                          @Field("author")author: String,
                          @Field("link")link: String): Observable<BaseResponse<CollectOutArticleData>>

    /**
     * 获取收藏的文章
     */
    @GET("lg/collect/list/{page}/json")
    fun getCollectArticles(@Path("page") page: Int): Observable<BaseResponse<CollectArticleData>>

    /**
     * 删除收藏文章(不是收藏页面)
     * @param id id
     */
    @POST("lg/uncollect_originId/{id}/json")
    fun removeCollectArticleCurrent(@Path("id") id: Int): Observable<BaseResponse<String>>

    /**
     * 删除收藏文章(我的收藏页面)
     * @param id id
     * @param originId -1
     */
    @POST("/lg/uncollect/{id}/json")
    @FormUrlEncoded
    fun removeCollectArticle(@Path("id") id: Int,
                             @Field("originId") originId: Int = -1): Observable<BaseResponse<String>>


}