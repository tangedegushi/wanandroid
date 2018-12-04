package com.zzq.netlib.error

import android.net.ParseException
import android.os.NetworkOnMainThreadException
import com.zzq.netlib.utils.Logger
import com.google.gson.JsonParseException
import io.reactivex.exceptions.CompositeException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException

/**
 *@auther tangedegushi
 *@creat 2018/11/6
 *@Decribe
 */
object ExceptionHandleUtil {

    fun handleException(e: Throwable): ResponseException {
        var e = e
        val ex: ResponseException
        if (e is CompositeException) {
            val exceptions = e.exceptions
            e = exceptions[0]
        }
        Logger.zzqLog().e("the throwable = $e")
        if (e is HttpException || e is retrofit2.adapter.rxjava2.HttpException) {
            val httpException = e as HttpException
            val message = "the http error code is = ${httpException.code()}"
            ex = ResponseException(ResponseException.HTTP_ERROR, message, e)
            return ex
        } else if (e is JsonParseException
                || e is JSONException
                || e is ParseException) {
            val message = "解析错误"
            ex = ResponseException(ResponseException.PARSE_ERROR, message, e)
            return ex
        } else if (e is ConnectException || e is SocketException) {
            val message = "连接失败"
            ex = ResponseException(ResponseException.CONNECT_ERROR, message, e)
            return ex
        } else if (e is javax.net.ssl.SSLHandshakeException) {
            val message = "证书验证失败"
            ex = ResponseException(ResponseException.SSL_ERROR, message, e)
            return ex
        } else if (e is ConnectTimeoutException) {
            val message = "请求连接超时"
            ex = ResponseException(ResponseException.TIMEOUT_ERROR, message, e)
            return ex
        } else if (e is java.net.SocketTimeoutException) {
            val message = "服务器响应超时"
            ex = ResponseException(ResponseException.TIMEOUT_ERROR, message, e)
            return ex
        } else if (e is UnknownHostException) {
            val message = "UnknownHostException"
            ex = ResponseException(ResponseException.UNKNOW_HOST_ERROR, message, e)
            return ex
        } else if (e is NetworkOnMainThreadException) {
            val message = "在主线程中请求网络"
            ex = ResponseException(ResponseException.ON_MAIN_THREAD, message, e)
            return ex
        } else if (e is SecurityException) {
            val message = "没有添加网络权限"
            ex = ResponseException(ResponseException.PERMISSION_DENIED, message, e)
            return ex
        } else {
            ex = ResponseException(ResponseException.UNKNOWN, e.message, e)
            return ex
        }
    }

}