package com.zzq.netlib.utils

import android.arch.lifecycle.LifecycleOwner
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.AutoDisposeConverter
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *@auther tangedegushi
 *@creat 2018/11/7
 *@Decribe
 */
object UtilRx {

    fun <T> bindLifeCycle(owner: LifecycleOwner): AutoDisposeConverter<T> = AutoDispose.autoDisposable<T>(AndroidLifecycleScopeProvider.from(owner))

    fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }

    }

}