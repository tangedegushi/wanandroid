package com.zzq.moduletree.netserver

import com.zzq.moduletree.bean.NavigationData
import com.zzq.moduletree.bean.TreeData
import com.zzq.netlib.rxbase.BaseResponse
import io.reactivex.Observable
import retrofit2.http.GET

/**
 *@auther tangedegushi
 *@creat 2018/12/14
 *@Decribe
 */
interface ApiTreeService {

    /**
     * 获取体系数据
     */
    @GET("tree/json")
    fun getTreeData(): Observable<BaseResponse<List<TreeData>>>

    /**
     * 获取导航数据
     */
    @GET("navi/json")
    fun getNavigationData(): Observable<BaseResponse<List<NavigationData>>>
}