package com.zzq.netlib.base

import android.app.Application
import android.content.Context
import com.zzq.netlib.di.component.AppComponent
import com.zzq.netlib.di.component.DaggerAppComponent
import com.zzq.netlib.di.module.GlobalConfigurationModule
import com.zzq.netlib.di.module.NetModule

/**
 *@auther tangedegushi
 *@creat 2018/11/2
 *@Decribe
 */
class AppDelegate : AppLifecycle {
    lateinit var appComponent: AppComponent

    override fun attachBaseContext(context: Context) {

    }

    override fun onCreate(application: Application) {
        appComponent = DaggerAppComponent.builder()
                .globalConfigurationModule(initGlobalConfigurationModule())
                .netModule(netModule = NetModule)
                .application(application).build()
        initActivityManager()
    }

    override fun onTerminate() {

    }

    private fun initActivityManager() {
        appComponent.activityManager()
    }

    private fun initGlobalConfigurationModule(): GlobalConfigurationModule {
        return GlobalConfigurationModule.getDefaultInstance()
    }

}
