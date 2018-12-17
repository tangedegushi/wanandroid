package com.zzq.commonui.model

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import com.zzq.commonui.bean.CollectArticleData
import com.zzq.commonui.netserver.ApiServiceCommon
import com.zzq.netlib.http.model.BaseModel
import com.zzq.netlib.rxbase.BaseObserver
import com.zzq.netlib.rxbase.BaseResponse
import com.zzq.netlib.utils.Logger
import com.zzq.netlib.utils.UtilRx

/**
 *@auther tangedegushi
 *@creat 2018/12/14
 *@Decribe
 */
class CollectArticleModel(private val owner: LifecycleOwner): BaseModel() {

    val liveCollectArticleData = MutableLiveData<CollectArticleData>()

    fun getCollectArticles(page: Int) {
        getService(ApiServiceCommon::class.java).getCollectArticles(page)
                .compose(UtilRx.applySchedulers())
                .`as`(UtilRx.bindLifeCycle(owner))
                .subscribe(object : BaseObserver<BaseResponse<CollectArticleData>, CollectArticleData>() {
                    override fun onNextBaseResult(r: CollectArticleData) {
                        liveCollectArticleData.value = r
                        Logger.zzqLog().d("this is the collect article $r")
                    }
                })
    }

}