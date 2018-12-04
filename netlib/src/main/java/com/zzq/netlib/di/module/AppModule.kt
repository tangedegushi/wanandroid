package com.zzq.netlib.di.module

import com.zzq.netlib.http.INetManager
import com.zzq.netlib.http.NetManager
import dagger.Binds
import dagger.Module

/**
 *@auther tangedegushi
 *@creat 2018/11/5
 *@Decribe
 */
@Module
abstract class AppModule {

    @Binds
    abstract fun bindNetManger(netManager: NetManager): INetManager

}