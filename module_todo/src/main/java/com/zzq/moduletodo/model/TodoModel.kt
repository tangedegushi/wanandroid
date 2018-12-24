package com.zzq.moduletodo.model

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import com.zzq.moduletodo.R
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
class TodoModel : BaseModel() {

    val liveTodoData by lazy { MutableLiveData<TodoData>() }
    val liveRemoveData by lazy { MutableLiveData<Boolean>() }

    private var curYear: Int = 0

    init {
        val c = Calendar.getInstance()
        curYear = c.get(Calendar.YEAR)
    }

    fun removeTodo(owner: LifecycleOwner, id: Int) {
        getService(ApiTodoService::class.java)
                .removeTodo(id)
                .compose(UtilRx.applySchedulers())
                .`as`(UtilRx.bindLifeCycle(owner))
                .subscribe(object : CommonObserver<BaseResponse<String>>() {
                    override fun onNext(t: BaseResponse<String>) {
                        if (t.errorCode != 0) {
                            Logger.zzqLog().i("result = $t")
                            UtilApp.showToast(t.errorMsg ?: "删除失败")
                            liveRemoveData.value = false
                        } else {
                            UtilApp.showToast(R.string.todo_delete_success)
                            liveRemoveData.value = true
                        }
                    }

                })
    }

    fun getTODO(owner: LifecycleOwner, page: Int, status: Int = -1, type: Int = 0, priority: Int = 0, orderby: Int = 4) {
        getService(ApiTodoService::class.java)
                .getTodo(page, status, type, priority, orderby)
                .subscribeOn(Schedulers.io())
                .`as`(UtilRx.bindLifeCycle(owner))
                .subscribe(object : BaseObserver<BaseResponse<TodoData>, TodoData>() {
                    override fun onNextBaseResult(r: TodoData) {
                        dealWithTodoData(r)
                        liveTodoData.postValue(r)
                    }
                })
    }

    private fun dealWithTodoData(data: TodoData) {
        if (data.datas.isEmpty()) return
        val dataList = mutableListOf<TodoData.DatasBean>()
        var bean = TodoData.DatasBean()
        bean.isTitleType = true
        bean.customeTimeTitle = dealWithTime(data.datas[0].date)
        dataList.add(bean)
        for (item in data.datas) {
            val timeTitle = dealWithTime(item.date)
            item.customeTimeTitle = timeTitle
            if (timeTitle == dataList.last().customeTimeTitle) {
                dataList.add(item)
            } else {
                bean = TodoData.DatasBean()
                bean.isTitleType = true
                bean.customeTimeTitle = timeTitle
                dataList.add(bean)
                dataList.add(item)
            }
        }
        data.datas = dataList
    }

    private fun dealWithTime(time: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        val year = calendar.get(Calendar.YEAR)
        val moth = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val stringBuilder = StringBuilder()
        if (year == curYear) {
            stringBuilder.append(moth).append("月")
        } else {
            stringBuilder.append(year).append("年").append(moth).append("月")
        }
        return stringBuilder.toString()
    }
}