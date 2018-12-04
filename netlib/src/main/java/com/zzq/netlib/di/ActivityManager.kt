package com.zzq.netlib.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.zzq.netlib.di.scope.AppScope
import com.zzq.netlib.utils.Logger
import java.util.*
import javax.inject.Inject

/**
 *@auther tangedegushi
 *@creat 2018/11/2
 *@Decribe
 */
@AppScope
class ActivityManager @Inject
constructor() : Application.ActivityLifecycleCallbacks {

    val activityList: Stack<Activity> = Stack()


    @Inject
    lateinit var app: Application

    @Inject
    fun register() {
        Logger.zzqLog().d("register ActivityManager to manager activity")
        app.registerActivityLifecycleCallbacks(this)
    }

    fun unRegister(app: Application) {
        app.unregisterActivityLifecycleCallbacks(this)
    }

    var currentActivity: Activity? = null
        get() = if (field != null) field else null

    val topActivity: Activity?
        get() = if (activityList.size > 0) activityList.peek() else null

    fun addActivity(activity: Activity) {
        activityList.add(activity)
    }

    fun removeActivity(activity: Activity): Boolean {
        return activityList.remove(activity)
    }

    fun searchActivity(activity: Activity): Int {
        return activityList.search(activity)
    }

    fun finishActivityClass(activity: Class<Activity>?) {
        if (activity != null) {
            val iterator = activityList.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (next.javaClass == activity) {
                    iterator.remove()
                    next.finish()
                }
            }
        }
    }

    fun finishAllActivity() {
        val iterator = activityList.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            iterator.remove()
            next.finish()
        }
    }

    fun finishAllExcludeActivity(vararg activityClass: Class<Activity>) {
        val list = Arrays.asList(*activityClass)
        val iterator = activityList.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (list.contains(next.javaClass))
                continue
            iterator.remove()
            next.finish()
        }
    }


    //----------------------------弹窗显示相关方法---------------------------

    fun showSnackBar(message: String, isLong: Boolean) {
        if (currentActivity == null) {
            Logger.zzqLog().w("currentActivity = null when show SnackBar")
        }
        val decorView = currentActivity!!.window.decorView
        Snackbar.make(decorView, message, if (isLong) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT).show()
    }


    // ------------------------------ activity life -----------------------------
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        //如果 intent 包含了此字段,并且为 true 说明不加入到 list 进行统一管理
        var isNotAdd = false
        if (activity.intent != null)
            isNotAdd = activity.intent.getBooleanExtra(ActivityManager.IS_NOT_ADD_ACTIVITY_MANAGER, false)

        if (!isNotAdd)
            addActivity(activity)
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
        if (currentActivity === activity) {
            currentActivity = null
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        removeActivity(activity)
    }

    companion object {
        const val IS_NOT_ADD_ACTIVITY_MANAGER = "is_not_add_activity_manager"
    }
}
