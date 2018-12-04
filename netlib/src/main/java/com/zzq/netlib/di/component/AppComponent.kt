package com.zzq.netlib.di.component

import android.app.Application
import com.zzq.netlib.di.ActivityManager
import com.zzq.netlib.di.module.AppModule
import com.zzq.netlib.di.module.GlobalConfigurationModule
import com.zzq.netlib.di.module.NetModule
import com.zzq.netlib.di.scope.AppScope
import com.zzq.netlib.error.ErrorHandle
import com.zzq.netlib.http.INetManager
import com.google.gson.Gson
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import java.io.File


/**
 *@auther tangedegushi
 *@creat 2018/11/1
 *@Decribe
 */
@AppScope
@Component(modules = [NetModule::class, AppModule::class, GlobalConfigurationModule::class])
interface AppComponent {

    fun application(): Application

    fun netManager(): INetManager

    fun activityManager(): ActivityManager

    fun errorHandle(): ErrorHandle

    fun cacheFile(): File

    fun gson(): Gson

    fun retrofit(): Retrofit

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun globalConfigurationModule(configuration: GlobalConfigurationModule): Builder

        fun netModule(netModule: NetModule): Builder

        fun build(): AppComponent
    }

}