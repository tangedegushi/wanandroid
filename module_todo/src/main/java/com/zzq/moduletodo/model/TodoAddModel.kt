package com.zzq.moduletodo.model

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import com.zzq.moduletodo.R
import com.zzq.moduletodo.TodoAddActivity
import com.zzq.moduletodo.bean.TodoData
import com.zzq.moduletodo.netserver.ApiTodoService
import com.zzq.netlib.http.model.BaseModel
import com.zzq.netlib.rxbase.BaseObserver
import com.zzq.netlib.rxbase.BaseResponse
import com.zzq.netlib.rxbase.CommonObserver
import com.zzq.netlib.utils.Logger
import com.zzq.netlib.utils.UtilApp
import com.zzq.netlib.utils.UtilRx
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 *@auther tangedegushi
 *@creat 2018/12/17
 *@Decribe
 */
class TodoAddModel(private val owner: LifecycleOwner) : BaseModel() {
    val liveTodoAddData by lazy { MutableLiveData<Int>() }

    fun addTodo(title: String, content: String?, date: String?, type: Int = 0, priority: Int = 0) {
        getService(ApiTodoService::class.java)
                .addTodo(title, content, date, type, priority)
                .compose(UtilRx.applySchedulers())
                .`as`(UtilRx.bindLifeCycle(owner))
                .subscribe(object : CommonObserver<BaseResponse<TodoData.DatasBean>>() {
                    override fun onNext(t: BaseResponse<TodoData.DatasBean>) {
                        if (t.errorCode != 0) {
                            Logger.zzqLog().i("result = $t")
                            UtilApp.showToast(t.errorMsg ?: "添加失败")
                        } else {
                            UtilApp.showToast("添加成功")
                            liveTodoAddData.value = TodoAddActivity.BACK_ADD
                        }
                    }
                })
    }

    fun updateTodo(id: Int, title: String, content: String?, date: String?, status: Int = 0, type: Int = 0, priority: Int = 0) {
        getService(ApiTodoService::class.java)
                .updateTodo(id, title, content, date, status, type, priority)
                .compose(UtilRx.applySchedulers())
                .`as`(UtilRx.bindLifeCycle(owner))
                .subscribe(object : CommonObserver<BaseResponse<TodoData.DatasBean>>() {
                    override fun onNext(t: BaseResponse<TodoData.DatasBean>) {
                        if (t.errorCode != 0) {
                            Logger.zzqLog().i("result = $t")
                            UtilApp.showToast(t.errorMsg ?: "更新失败")
                        } else {
                            UtilApp.showToast(R.string.todo_update_success)
                            liveTodoAddData.value = TodoAddActivity.BACK_UPDATE_DONE
                        }
                    }

                })
    }

    fun updateTodoDone(id: Int, status: Int) {
        getService(ApiTodoService::class.java)
                .updateTodoDone(id, status)
                .compose(UtilRx.applySchedulers())
                .`as`(UtilRx.bindLifeCycle(owner))
                .subscribe(object : CommonObserver<BaseResponse<TodoData.DatasBean>>() {
                    override fun onNext(t: BaseResponse<TodoData.DatasBean>) {
                        if (t.errorCode != 0) {
                            Logger.zzqLog().i("result = $t")
                            UtilApp.showToast(t.errorMsg ?: "更新状态失败")
                        } else {
                            UtilApp.showToast(R.string.todo_done_success)
                            liveTodoAddData.value = TodoAddActivity.BACK_UPDATE
                        }
                    }

                })
    }
}