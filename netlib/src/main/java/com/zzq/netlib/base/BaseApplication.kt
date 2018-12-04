package com.zzq.netlib.base

import android.app.Application
import android.content.Context
import com.zzq.netlib.di.component.AppComponent
import com.zzq.netlib.utils.UtilCheck
import com.squareup.leakcanary.LeakCanary

/**
 *@auther tangedegushi
 *@creat 2018/11/2
 *@Decribe
 */
class BaseApplication : Application(), App {

    private var appDelegate: AppDelegate? = null

    override fun getAppComponent(): AppComponent {
        return appDelegate?.appComponent!!
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        if (UtilCheck.isNull(appDelegate)) {
            appDelegate = AppDelegate()
        }
        appDelegate?.attachBaseContext(this)

    }

    override fun onCreate() {
        super.onCreate()
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this)
        }
        application = this
        appDelegate?.onCreate(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        appDelegate?.onTerminate()
    }

    companion object {
        var application: BaseApplication? = null
    }
}
