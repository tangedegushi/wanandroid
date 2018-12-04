package com.zzq.netlib.rxbase

import com.zzq.netlib.error.ErrorHandle
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 *@auther tangedegushi
 *@creat 2018/11/6
 *@Decribe 对请求数据时发生异常情况进行了处理
 */
abstract class CommonObserver<T>(val errorHandler: ErrorHandle) : Observer<T> {

    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onError(e: Throwable) {
        errorHandler.processError(e)
    }
}