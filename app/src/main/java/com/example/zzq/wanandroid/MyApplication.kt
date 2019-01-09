package com.example.zzq.wanandroid

import com.alibaba.android.arouter.launcher.ARouter
import com.zzq.netlib.BuildConfig
import com.zzq.netlib.base.BaseApplication

/**
 *@auther tangedegushi
 *@creat 2019/1/2
 *@Decribe
 */
class MyApplication: BaseApplication() {

    override fun onCreate() {
        if (BuildConfig.isDebug) {
            ARouter.openLog()
            ARouter.openDebug()
            ARouter.printStackTrace()
        }
        ARouter.init(this)
        super.onCreate()
    }

}