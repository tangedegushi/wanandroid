package com.zzq.netlib.di.module

import android.content.Context
import com.zzq.netlib.BuildConfig
import com.zzq.netlib.di.scope.AppScope
import com.zzq.netlib.error.ErrorHandle
import com.zzq.netlib.utils.UtilCheck
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.Interceptor

/**
 *@auther tangedegushi
 *@creat 2018/11/1
 *@Decribe
 */
@Module
class GlobalConfigurationModule(builder: Builder) {

    private val baseUrl: HttpUrl = builder.baseUrl
    private val configGson: NetModule.ConfigurationGson = builder.configGson
    private val configOkhttp: NetModule.ConfigurationOkhttp? = builder.configOkhttp
    private val configRetrofit: NetModule.ConfigurationRetrofit? = builder.configRetrofit
    private val configRxcache: NetModule.ConfigurationRxcache? = builder.configRxcache
    private val configLogging: NetModule.ConfigurationLoggingInterceptor? = builder.configLogging
    private val configCacheFile: NetModule.ConfigurationCacheFileDir? = builder.configCacheFile
    private val errorHandle: ErrorHandle? = null

    @AppScope
    @Provides
    fun provideBaseUrl(): HttpUrl {
        return baseUrl
    }

    @AppScope
    @Provides
    fun provideConfigGson(): NetModule.ConfigurationGson? {
        return configGson
    }

    @AppScope
    @Provides
    fun provideConfigOkhttp(): NetModule.ConfigurationOkhttp? {
        return configOkhttp
    }

    @AppScope
    @Provides
    fun provideConfigRetrofit(): NetModule.ConfigurationRetrofit? {
        return configRetrofit
    }

    @AppScope
    @Provides
    fun provideConfigRxcache(): NetModule.ConfigurationRxcache? {
        return configRxcache
    }

    @AppScope
    @Provides
    fun provideConfigLogging(): NetModule.ConfigurationLoggingInterceptor? {
        return configLogging
    }

    @AppScope
    @Provides
    fun provideInterceptor(): java.util.List<Interceptor>? {
        return null
    }

    @AppScope
    @Provides
    fun provideConfigCacheFile(): NetModule.ConfigurationCacheFileDir? {
        return configCacheFile
    }

    @AppScope
    @Provides
    fun provideErrorHandle(): ErrorHandle {
        return errorHandle ?: ErrorHandle.DEFAULT_HANDLE
    }

    class Builder {
        var baseUrl: HttpUrl
        var configGson: NetModule.ConfigurationGson
        var configOkhttp: NetModule.ConfigurationOkhttp? = null
        var configRetrofit: NetModule.ConfigurationRetrofit? = null
        var configRxcache: NetModule.ConfigurationRxcache? = null
        var configLogging: NetModule.ConfigurationLoggingInterceptor? = null
        var configCacheFile: NetModule.ConfigurationCacheFileDir? = null
        var errorHandle: ErrorHandle? = null

        init {
            configGson = object : NetModule.ConfigurationGson {
                override fun configGson(context: Context, builder: GsonBuilder) {
                    builder.serializeNulls()
                }
            }
            baseUrl = HttpUrl.parse(BuildConfig.baseUrl)!!
        }

        /**
         * @param baseUrl 网络请求的基url
         */
        fun baseUrl(baseUrl: String): Builder {
            val url = UtilCheck.checkNotNull(baseUrl, "baseUrl can not be null")
            this.baseUrl = HttpUrl.parse(url)!!
            return this
        }


        /**
         * @param baseUrl 网络请求的基url
         */
        fun baseUrl(baseUrl: HttpUrl): Builder {
            this.baseUrl = UtilCheck.checkNotNull(baseUrl, "%s can not be null", HttpUrl::class.java.canonicalName)
            return this
        }

        /**
         * @param configGson 配置Gson
         */
        fun configGson(configGson: NetModule.ConfigurationGson): Builder {
            this.configGson = configGson
            return this
        }

        /**
         * @param configOkhttp 配置Okhttp
         */
        fun configOkhttp(configOkhttp: NetModule.ConfigurationOkhttp): Builder {
            this.configOkhttp = configOkhttp
            return this
        }

        /**
         * @param configRetrofit 配置Retrofit
         * @return
         */
        fun configRetrofit(configRetrofit: NetModule.ConfigurationRetrofit): Builder {
            this.configRetrofit = configRetrofit
            return this
        }

        /**
         * @param configRxcache 配置RxCache
         */
        fun configRxcache(configRxcache: NetModule.ConfigurationRxcache): Builder {
            this.configRxcache = configRxcache
            return this
        }

        /**
         * @param configLogging 设置网络请求的相关信息,默认情况下
         */
        fun configRequestLogging(configLogging: NetModule.ConfigurationLoggingInterceptor): Builder {
            this.configLogging = configLogging
            return this
        }


        /**
         * @param configCacheFile 设置缓存的目录
         */
        fun configCacheFile(configCacheFile: NetModule.ConfigurationCacheFileDir): Builder {
            this.configCacheFile = configCacheFile
            return this
        }

        fun processErrorHandle(errorHandle: ErrorHandle): Builder {
            this.errorHandle = errorHandle
            return this
        }

        fun build(): GlobalConfigurationModule {
            return GlobalConfigurationModule(this)
        }
    }

    companion object {
        fun getDefaultInstance(): GlobalConfigurationModule {
            return GlobalConfigurationModule.Builder().build()
        }
    }
}