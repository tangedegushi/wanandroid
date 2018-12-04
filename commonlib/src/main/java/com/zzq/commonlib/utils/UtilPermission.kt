package com.zzq.commonlib.utils

import android.Manifest
import android.util.Log
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.*

/**
 *@auther tangedegushi
 *@creat 2018/11/16
 *@Decribe
 */
object UtilPermission {
    val TAG = "UtilPermission"

    val defaultRequestPermission:RequestPermissionResultImpl by lazy {
        RequestPermissionResultImpl()
    }

    class RequestPermissionResultImpl:RequestPermissionResult{
        override fun onRequestPermissionSuccess() {

        }

        override fun onRequestPermissionFailure(permissions: List<String>) {

        }

        override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>) {

        }

    }

    interface RequestPermissionResult {
        /**
         * 权限请求成功
         */
        fun onRequestPermissionSuccess()

        /**
         * 用户拒绝了权限请求, 权限请求失败, 但还可以继续请求该权限
         *
         * @param permissions 请求失败的权限名
         */
        fun onRequestPermissionFailure(permissions: List<String>)

        /**
         * 用户拒绝了权限请求并且用户选择了以后不再询问, 权限请求失败, 这时将不能继续请求该权限, 需要提示用户进入设置页面打开该权限
         *
         * @param permissions 请求失败的权限名
         */
        fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>)
    }

    private fun requestPermission(requestPermissionResult: RequestPermissionResult, rxPermissions: RxPermissions, vararg permissions: String) {
        if (permissions.isEmpty()) return

        val needRequest = ArrayList<String>()
        for (permission in permissions) { //过滤调已经申请过的权限
            if (!rxPermissions.isGranted(permission)) {
                needRequest.add(permission)
            }
        }

        if (needRequest.isEmpty()) {//全部权限都已经申请过，直接执行操作
            requestPermissionResult.onRequestPermissionSuccess()
        } else {//没有申请过,则开始申请
            rxPermissions
                    .requestEach(*needRequest.toTypedArray())
                    .buffer(permissions.size)
                    .subscribe { permissions ->
                        for (p in permissions) {
                            if (!p.granted) {
                                if (p.shouldShowRequestPermissionRationale) {
                                    Log.d(TAG, "Request permissions failure")
                                    requestPermissionResult.onRequestPermissionFailure(Arrays.asList(p.name))
                                    return@subscribe
                                } else {
                                    Log.d(TAG, "Request permissions failure with ask never again")
                                    requestPermissionResult.onRequestPermissionFailureWithAskNeverAgain(Arrays.asList(p.name))
                                    return@subscribe
                                }
                            }
                        }
                        Log.d(TAG, "Request permissions success")
                        requestPermissionResult.onRequestPermissionSuccess()
                    }
        }

    }

    /**
     * 请求摄像头权限
     */
    fun permissionCamera(requestPermissionResult: RequestPermissionResult, rxPermissions: RxPermissions) {
        requestPermission(requestPermissionResult, rxPermissions, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    }


    /**
     * 请求外部存储的权限
     */
    fun permissionStorage(requestPermissionResult: RequestPermissionResult, rxPermissions: RxPermissions) {
        requestPermission(requestPermissionResult, rxPermissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }


    /**
     * 请求发送短信权限
     */
    fun permissionSms(requestPermissionResult: RequestPermissionResult, rxPermissions: RxPermissions) {
        requestPermission(requestPermissionResult, rxPermissions, Manifest.permission.SEND_SMS)
    }


    /**
     * 请求打电话权限
     */
    fun permissionPhone(requestPermissionResult: RequestPermissionResult, rxPermissions: RxPermissions) {
        requestPermission(requestPermissionResult, rxPermissions, Manifest.permission.CALL_PHONE)
    }


    /**
     * 请求获取手机状态的权限
     */
    fun permissionPhonestate(requestPermissionResult: RequestPermissionResult, rxPermissions: RxPermissions) {
        requestPermission(requestPermissionResult, rxPermissions, Manifest.permission.READ_PHONE_STATE)
    }

}