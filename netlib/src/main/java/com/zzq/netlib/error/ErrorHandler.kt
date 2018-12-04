package com.zzq.netlib.error

import com.zzq.netlib.BuildConfig
import com.zzq.netlib.utils.Logger
import com.zzq.netlib.utils.UtilApp

/**
 *@auther tangedegushi
 *@creat 2018/11/6
 *@Decribe
 */
interface ErrorHandle {

    fun processError(e: Throwable)

    companion object {
        var DEFAULT_HANDLE = object : ErrorHandle {
            override fun processError(e: Throwable) {
                if (e is ServerException) {
                    if (BuildConfig.isDebug) {
                        UtilApp.showToast("the server error code = ${e.errorCode}", true)
                    }
                    Logger.zzqLog().e("the server is error,the error code = ${e.errorCode},this error message is = ${e.message}")
                } else if (e is ResponseException) {
                    e.printStackTrace()
                    val errorCode = e.errorCode
                    when (errorCode) {
                        ResponseException.HTTP_ERROR -> UtilApp.showToast(e.message
                                ?: "ResponseException", true)
                        ResponseException.CONNECT_ERROR,
                        ResponseException.TIMEOUT_ERROR -> UtilApp.showToast(e.message
                                ?: "ResponseException")
                        ResponseException.UNKNOW_HOST_ERROR,
                        ResponseException.PARSE_ERROR,
                        ResponseException.SSL_ERROR,
                        ResponseException.UNKNOWN,
                        ResponseException.ON_MAIN_THREAD,
                        ResponseException.PERMISSION_DENIED
                        -> if (BuildConfig.isDebug) {
                            Logger.zzqLog().e("custom: error code = ${e.errorCode},system: error message is = ${e.message}")
                            UtilApp.showToast(e.message ?: "ResponseException", true)
                        }
                        else -> {
                            Logger.zzqLog().e("undefine error")
                        }
                    }
                }
            }
        }
    }
}