package com.zzq.netlib.http

import com.zzq.netlib.utils.UtilSp
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 *@auther tangedegushi
 *@creat 2018/11/29
 *@Decribe 主要用于自己管理cookie
 */
class CookieManager:CookieJar {
    override fun saveFromResponse(url: HttpUrl?, cookies: MutableList<Cookie>?) {
        if ("http://www.wanandroid.com/user/login" == url?.url().toString()) {
            cookies?.forEach {
                UtilSp.saveObjectBase64(it.name(),it.value(),UtilSp.SP_NAME_LOGIN)
            }
        }
    }

    override fun loadForRequest(url: HttpUrl?): MutableList<Cookie> {
        return mutableListOf()
    }
}