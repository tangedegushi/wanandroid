package com.zzq.modulehomepage.model

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import com.zzq.modulehomepage.bean.SearchKeyData
import com.zzq.modulehomepage.netserver.ApiService
import com.zzq.netlib.http.model.BaseModel
import com.zzq.netlib.rxbase.BaseObserver
import com.zzq.netlib.rxbase.BaseResponse
import com.zzq.netlib.utils.UtilRx

/**
 *@auther tangedegushi
 *@creat 2018/11/19
 *@Decribe
 */
class SearchModel(val ower: LifecycleOwner) : BaseModel() {

    var searchKeyData = MutableLiveData<SearchKeyData>()

    fun getSearchKeyData(page: Int, searchKey: String) {
        getService(ApiService::class.java).getSearchKeyData(page, searchKey)
                .compose(UtilRx.applySchedulers())
                .`as`(UtilRx.bindLifeCycle(ower))
                .subscribe(object : BaseObserver<BaseResponse<SearchKeyData>, SearchKeyData>(appComponent.errorHandle()) {
                    override fun onNextBaseResult(r: SearchKeyData) {
                        searchKeyData.value = r
                    }
                })
    }
}