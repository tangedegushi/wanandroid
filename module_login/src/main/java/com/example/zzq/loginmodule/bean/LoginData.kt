package com.example.zzq.loginmodule.bean

/**
 *@auther tangedegushi
 *@creat 2018/11/27
 *@Decribe
 */
data class LoginData(var email: String? = null,
                     var icon: String? = null,
                     var id: Int = 0,
                     var password: String? = null,
                     var token: String? = null,
                     var type: Int = 0,
                     var username: String? = null,
                     var chapterTops: List<*>? = null,
                     var collectIds: List<*>? = null)