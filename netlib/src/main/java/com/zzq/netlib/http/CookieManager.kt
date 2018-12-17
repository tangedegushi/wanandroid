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
class CookieManager : CookieJar {

    private var list = mutableListOf<Cookie>()

    init {
        if (UtilSp.getBoolean(UtilSp.KEY_HAD_LOGIN, false, UtilSp.SP_NAME_LOGIN)) {
            list.add(Cookie.Builder()
                    .name(UtilSp.KEY_COOKIE_USERNAME)
                    .value(UtilSp.getObjectBase64<String>(UtilSp.KEY_COOKIE_USERNAME, UtilSp.SP_NAME_LOGIN)
                            ?: "")
                    .domain("www.wanandroid.com")
                    .build())
            list.add(Cookie.Builder()
                    .name(UtilSp.KEY_COOKIE_TOKEN_PASS)
                    .value(UtilSp.getObjectBase64<String>(UtilSp.KEY_COOKIE_TOKEN_PASS, UtilSp.SP_NAME_LOGIN)
                            ?: "")
                    .domain("www.wanandroid.com")
                    .build())
        }
    }

    override fun saveFromResponse(url: HttpUrl?, cookies: MutableList<Cookie>?) {
        if ("http://www.wanandroid.com/user/login" == url?.url().toString()) {
            cookies?.forEach {
                UtilSp.saveObjectBase64(it.name(), it.value(), UtilSp.SP_NAME_LOGIN)
                list.add(Cookie.Builder()
                        .name(it.name())
                        .value(it.value())
                        .domain("www.wanandroid.com")
                        .build())
            }
        }
    }

    override fun loadForRequest(url: HttpUrl?): MutableList<Cookie> {
        if (!UtilSp.getBoolean(UtilSp.KEY_HAD_LOGIN, false, UtilSp.SP_NAME_LOGIN)) {
            list.clear()
        }
        return list
    }
}