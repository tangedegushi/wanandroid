package com.zzq.netlib.http.model

import android.arch.lifecycle.ViewModel
import com.zzq.netlib.error.ExceptionHandleUtil
import com.zzq.netlib.error.ServerException
import com.zzq.netlib.http.INetManager
import com.zzq.netlib.rxbase.BaseResponse
import com.zzq.netlib.utils.UtilApp
import io.reactivex.Observable
import io.reactivex.functions.Function

/**
 *@auther tangedegushi
 *@creat 2018/11/6
 *@Decribe
 */
open class BaseModel : ViewModel() {

    val appComponent = UtilApp.obtainAppComponent()
    val errorHandler = UtilApp.obtainAppComponent().errorHandle()

    protected fun <T> getService(c: Class<T>): T = appComponent.netManager().getRetrofitService(c)

    protected fun <T> getCacheService(c: Class<T>): T = appComponent.netManager().getCacheService(c)

    protected fun <R> transBaseResponse(o: Observable<BaseResponse<R>>): Observable<R> {
        return o.map { t ->
            if (t.errorCode != 0) {
                val e = ServerException(t.errorCode, t.errorMsg ?: "the error message is undefine")
                errorHandler.processError(ExceptionHandleUtil.handleException(e))
            }
            t.data
        }
    }


}