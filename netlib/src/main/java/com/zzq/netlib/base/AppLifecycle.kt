package com.zzq.netlib.base

import android.app.Application
import android.content.Context

/**
 *@auther tangedegushi
 *@creat 2018/11/2
 *@Decribe
 */
interface AppLifecycle {

    fun attachBaseContext(context: Context)

    fun onCreate(application: Application)

    fun onTerminate()

}