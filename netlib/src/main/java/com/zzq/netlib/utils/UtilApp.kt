package com.zzq.netlib.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Environment
import android.support.annotation.StringRes
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.zzq.netlib.base.App
import com.zzq.netlib.base.BaseApplication
import com.zzq.netlib.di.ActivityManager
import com.zzq.netlib.di.component.AppComponent
import java.security.MessageDigest
import kotlin.experimental.and

/**
 *@auther tangedegushi
 *@creat 2018/11/6
 *@Decribe
 */
object UtilApp {

    fun MD5(s: String): String {
        try {
            val md = MessageDigest.getInstance("MD5")
            val bytes = md.digest(s.toByteArray(charset("utf-8")))
            return toHex(bytes)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    private fun toHex(bytes: ByteArray): String {
        val hex_digits = "0123456789ABCDEF".toCharArray()
        val ret = StringBuilder(bytes.size * 2)
        for (i in bytes.indices) {
            ret.append(hex_digits[bytes[i].toInt() shr 4 and 0x0f])
            ret.append(hex_digits[(bytes[i] and 0x0f).toInt()])
        }
        return ret.toString()
    }

    fun md5Encode(string: String): String {
        var hash = ByteArray(0)
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.toByteArray(charset("UTF-8")))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val hex = StringBuilder(hash.size * 2)
        for (b in hash) {
            if ((b and 0x0f) < 0x10) {
                hex.append("0")
            }
            hex.append(Integer.toHexString((b and 0x0f).toInt()))
        }
        return hex.toString()
    }

    fun obtainAppComponent(context: Context): AppComponent {
        UtilCheck.checkNotNull(context, "%s cannot be null", Context::class.java.name)
        return if (context.applicationContext is App) {
            (context.applicationContext as App).getAppComponent()
        } else {
            throw RuntimeException("your Application does not implements App")
        }
    }

    fun obtainAppComponent(): AppComponent {
        val application = BaseApplication.application
        return (application as? App)?.getAppComponent()
                ?: throw RuntimeException("your Application does not implements App")
    }

    //------------------------弹窗相关-----------------------------

    private fun getActivityManager(): ActivityManager {
        return obtainAppComponent().activityManager()
    }

    private var toast: Toast? = null
    fun showToast(@StringRes resId: Int) {
        showToast(resId, false)
    }

    fun showToast(message: String) {
        showToast(message, false)
    }

    fun showToast(message: String, isLong: Boolean) {
        if ("main" != (Thread.currentThread().name)) {
            getActivityManager().currentActivity?.window?.decorView?.post({
                if (toast == null) {
                    toast = Toast.makeText(obtainAppComponent().application(), message, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
                } else {
                    toast?.setText(message)
                }
                toast?.show()
            })
            return
        }
        if (toast == null) {
            toast = Toast.makeText(obtainAppComponent().application(), message, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
        } else {
            toast?.setText(message)
        }
        toast?.show()
    }

    fun showToast(@StringRes resId: Int, isLong: Boolean) {
        if ("main" != (Thread.currentThread().name)) {
            getActivityManager().currentActivity?.window?.decorView?.post({
                if (toast == null) {
                    toast = Toast.makeText(obtainAppComponent().application(), resId, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
                } else {
                    toast?.setText(resId)
                }
                toast?.show()
            })
            return
        }
        if (toast == null) {
            toast = Toast.makeText(obtainAppComponent().application(), resId, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
        } else {
            toast?.setText(resId)
        }
        toast?.show()
    }

    fun showSnackbar(message: String) {
        getActivityManager().showSnackBar(message, false)
    }

    fun showSnackbar(message: String, isLong: Boolean) {
        getActivityManager().showSnackBar(message, isLong)
    }

    fun dp2px(context: Context, value: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), context.resources.displayMetrics).toInt()
    }

    fun showSoftKeyboard(dialog: Dialog) {
        dialog.window!!.setSoftInputMode(4)
    }

    fun showSoftKeyboard(context: Context, view: View) {
        (context.getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(view,
                InputMethodManager.SHOW_FORCED)
    }

    fun toogleSoftKeyboard(context: Context, view: View) {
        (context.getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(0,
                InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun hideSoftKeyboard(context: Context, view: View?) {
        if (view == null)
            return
        val inputMethodManager = context.getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isActive)
            inputMethodManager.hideSoftInputFromWindow(
                    view.windowToken, 0)
    }


    fun isSdcardReady(): Boolean {
        return Environment.MEDIA_MOUNTED == Environment
                .getExternalStorageState()
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     * @param activity
     * @return bp
     */
    fun snapShotWithoutStatusBar(activity: Activity): Bitmap? {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache ?: return null
        val frame = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(frame)
        val statusBarHeight = frame.top
        val bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, bmp.width, bmp.height - statusBarHeight)
        view.destroyDrawingCache()
        view.isDrawingCacheEnabled = false

        return bp
    }

}