package com.zzq.netlib.rxbase

import com.zzq.netlib.error.ErrorHandle
import com.zzq.netlib.error.ExceptionHandleUtil
import com.zzq.netlib.error.ServerException
import com.zzq.netlib.utils.Logger
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 *@auther tangedegushi
 *@creat 2018/11/6
 *@Decribe 主要用于内容是由 {@link BaseResponse<T>}包裹的,如果不是，
 * 则使用 {@link CommonObserver<T>},这里主要是对异常情况进行了处理
 */
abstract class BaseObserver<T, in R>(val errorHandler: ErrorHandle) : Observer<T> {

    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(t: T) {
        if (t is BaseResponse<*>) {
            if (t.errorCode == 0) {
                onNextBaseResult(t.data as R)
            } else {
                onError(ServerException(t.errorCode, t.errorMsg ?: "the error message is undefine"))
            }
        } else {
            Logger.zzqLog().e("the instance should not  ${BaseResponse::class.java.canonicalName},the onNextBaseResult() is not called," +
                    "you should implement your logic in onNext() or you can use ${CommonObserver::class.java!!.getCanonicalName()}")
        }
    }

    abstract fun onNextBaseResult(r: R)

    override fun onError(e: Throwable) {
        errorHandler.processError(ExceptionHandleUtil.handleException(e))
    }
}