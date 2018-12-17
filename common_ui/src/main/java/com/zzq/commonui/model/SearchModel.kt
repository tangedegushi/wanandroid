package com.zzq.commonui.model

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import com.zzq.commonui.bean.SearchKeyData
import com.zzq.commonui.netserver.ApiServiceCommon
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

    var liveSearchKeyData = MutableLiveData<SearchKeyData>()

    fun getSearchKeyData(page: Int, searchKey: String) {
        getService(ApiServiceCommon::class.java).getSearchKeyData(page, searchKey)
                .compose(UtilRx.applySchedulers())
                .`as`(UtilRx.bindLifeCycle(ower))
                .subscribe(object : BaseObserver<BaseResponse<SearchKeyData>, SearchKeyData>() {
                    override fun onNextBaseResult(r: SearchKeyData) {
                        liveSearchKeyData.value = r
                    }
                })
    }
}