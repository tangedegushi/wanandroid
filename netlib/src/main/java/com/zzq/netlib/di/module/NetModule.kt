package com.zzq.netlib.di.module

import android.app.Application
import android.content.Context
import com.zzq.netlib.BuildConfig
import com.zzq.netlib.di.scope.AppScope
import com.zzq.netlib.utils.UtilFile
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.zzq.netlib.http.CookieManager
import dagger.Module
import dagger.Provides
import io.rx_cache2.internal.RxCache
import io.victoralbertos.jolyglot.GsonSpeaker
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

/**
 *@auther tangedegushi
 *@creat 2018/11/1
 *@Decribe
 */
@Module
object NetModule {

    @AppScope
    @Provides
    internal fun provideGsonBuilder(): GsonBuilder {
        return GsonBuilder()
    }

    @AppScope
    @Provides
    internal fun provideGson(application: Application, builder: GsonBuilder, config: ConfigurationGson?): Gson {
        when {
            config != null -> config.configGson(application, builder)
        }
        return builder.create()
    }

    @AppScope
    @Provides
    internal fun provideLoggingInterceptor(config: ConfigurationLoggingInterceptor?): HttpLoggingInterceptor {
        var interceptor: HttpLoggingInterceptor = when {
            BuildConfig.isDebug -> HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            else -> HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        when {
            config != null -> config.configLoggingInterceptor(interceptor)
        }
        return interceptor
    }

    @AppScope
    @Provides
    internal fun provideOkhttpBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }

    @AppScope
    @Provides
    internal fun provideOkhttp(application: Application, builder: OkHttpClient.Builder, config: ConfigurationOkhttp?,
                               loggingInterceptor: HttpLoggingInterceptor, interceptors: java.util.List<Interceptor>?): OkHttpClient {
        builder.addInterceptor(loggingInterceptor)
        when {
            interceptors != null -> {
                interceptors.forEach { item -> builder.addInterceptor(item) }
            }
            config != null -> config.configOkhttp(application, builder)
        }
        builder.cookieJar(CookieManager())
        return builder.build()
    }

    @AppScope
    @Provides
    internal fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
    }

    @AppScope
    @Provides
    internal fun provideRetrofit(application: Application, builder: Retrofit.Builder, config: ConfigurationRetrofit?,
                                 client: OkHttpClient, baseUrl: HttpUrl, gson: Gson): Retrofit {
        when {
            config != null -> config.configRetrofit(application, builder)
        }
        builder.baseUrl(baseUrl).client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))

        return builder.build()
    }

    @AppScope
    @Provides
    internal fun provideRxcache(application: Application, config: ConfigurationRxcache?, cacheFile: File): RxCache {
        var builder: RxCache.Builder = RxCache.Builder()
        return when {
            config != null -> config.configRxcache(application, builder)
            else -> {
                builder.persistence(cacheFile, GsonSpeaker())
            }
        }
    }

    @AppScope
    @Provides
    internal fun provideFile(application: Application, config: ConfigurationCacheFileDir?): File {
        var file = when {
            config != null -> File(config.configCacheFile(application), "RxCache")
            else -> File(UtilFile.getCacheFile(application), "RxCache")
        }
        return UtilFile.makeDirs(file)
    }

    interface ConfigurationGson {
        fun configGson(context: Context, builder: GsonBuilder)
    }

    interface ConfigurationOkhttp {
        fun configOkhttp(context: Context, builder: OkHttpClient.Builder)
    }

    interface ConfigurationRetrofit {
        fun configRetrofit(context: Context, builder: Retrofit.Builder)
    }

    interface ConfigurationRxcache {
        fun configRxcache(context: Context, builder: RxCache.Builder): RxCache
    }

    interface ConfigurationLoggingInterceptor {
        fun configLoggingInterceptor(interceptor: HttpLoggingInterceptor)
    }

    interface ConfigurationCacheFileDir {
        fun configCacheFile(application: Application): File
    }
}