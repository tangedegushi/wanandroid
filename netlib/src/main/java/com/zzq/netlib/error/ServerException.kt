package com.zzq.netlib.error

/**
 *@auther tangedegushi
 *@creat 2018/11/6
 *@Decribe
 */
class ServerException(val errorCode:Int,message: String): RuntimeException(message)