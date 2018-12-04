package com.zzq.netlib.http

import android.content.Context

/**
 *@auther tangedegushi
 *@creat 2018/11/2
 *@Decribe
 */
interface INetManager {

    fun <T> getRetrofitService(service: Class<T>): T

    fun <T> getCacheService(cache: Class<T>): T

    fun clearAllCache()

    fun getContext(): Context

}