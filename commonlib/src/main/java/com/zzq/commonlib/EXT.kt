package com.zzq.commonlib

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

/**
 *@auther tangedegushi
 *@creat 2019/3/6
 *@Decribe
 */
class CoroutineLifeListener(private val deffer: Deferred<*>) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun release() {
        if (deffer.isCancelled) {
            deffer.cancel()
        }
    }
}

fun <T> LifecycleOwner.loader(task: () -> T): Deferred<T> {
    val deferred = async {
        task()
    }
    lifecycle.addObserver(CoroutineLifeListener(deferred))
    return deferred
}

infix fun <T> Deferred<T>.then(result: (T) -> Unit): Job {
    return launch(UI) {
        result(this@then.await())
    }
}