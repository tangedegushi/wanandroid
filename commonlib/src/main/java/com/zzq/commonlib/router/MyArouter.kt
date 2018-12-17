package com.zzq.commonlib.router

import android.support.v4.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter

/**
 *@auther tangedegushi
 *@creat 2019/1/2
 *@Decribe
 */
object MyArouter {

    fun getArouter(): ARouter {
        return ARouter.getInstance()
    }

    fun openActivity(pathName: String) {
        getArouter().build(pathName).navigation()
    }

    fun getFragment(pathName: String): Fragment {
        return getArouter().build(pathName).navigation() as Fragment
    }
}