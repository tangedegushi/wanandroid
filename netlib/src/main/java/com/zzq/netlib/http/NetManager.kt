package com.zzq.netlib.http

import android.app.Application
import android.content.Context
import android.util.LruCache
import com.zzq.netlib.di.scope.AppScope
import dagger.Lazy
import io.rx_cache2.internal.RxCache
import retrofit2.Retrofit
import javax.inject.Inject

/**
 *@auther tangedegushi
 *@creat 2018/11/2
 *@Decribe 主要用来网络请求及请求的数据缓存
 */
@AppScope
class NetManager @Inject constructor() : INetManager {

    private val cacheServiceCount = 100

    @Inject
    internal lateinit var mRetrofit: Lazy<Retrofit>

    @Inject
    internal lateinit var mRxCache: Lazy<RxCache>

    @Inject
    internal lateinit var application: Application

    internal var lruRetrofitCacheService: LruCache<String, Any>? = null
    internal var lruRxCacheService: LruCache<String, Any>? = null

    init {
        lruRetrofitCacheService = LruCache(cacheServiceCount)
        lruRxCacheService = LruCache(cacheServiceCount)
    }

    override fun <T> getRetrofitService(service: Class<T>): T {
        var retrofitService: T? = lruRetrofitCacheService?.get(service.canonicalName) as T
        if (retrofitService == null) {
            retrofitService = mRetrofit.get()?.create(service)
            lruRetrofitCacheService?.put(service.canonicalName, retrofitService)
        }
        return retrofitService!!
    }

    override fun <T> getCacheService(cache: Class<T>): T {
        var rxCacheService: T? = lruRxCacheService?.get(cache.canonicalName) as T
        if (rxCacheService == null) {
            rxCacheService = mRxCache.get()?.using<T>(cache)
            lruRxCacheService?.put(cache.canonicalName, rxCacheService)
        }
        return rxCacheService!!
    }

    override fun clearAllCache() {
        mRxCache.get()?.evictAll()?.subscribe()
    }

    override fun getContext(): Context {
        return application
    }
}