package com.zzq.netlib.error

/**
 *@auther tangedegushi
 *@creat 2018/11/6
 *@Decribe
 */
class ResponseException(val errorCode: Int, message: String? = null, cause: Throwable?) : RuntimeException(message, cause) {

    companion object {
        /**
         * 未知错误
         */
        const val UNKNOWN = 1000
        /**
         * 解析错误
         */
        const val PARSE_ERROR = 1001
        /**
         * 网络错误
         */
        const val CONNECT_ERROR = 1002
        /**
         * 协议出错
         */
        const val HTTP_ERROR = 1003

        /**
         * 证书出错
         */
        const val SSL_ERROR = 1005

        /**
         * 连接超时
         */
        const val TIMEOUT_ERROR = 1006

        /**
         * 未连接网络
         */
        const val UNKNOW_HOST_ERROR = 1007
        /**
         * 在主线程中请求网络
         */
        const val ON_MAIN_THREAD = 1008
        /**
         * 没有添加网络权限
         */
        const val PERMISSION_DENIED = 1009
    }


}