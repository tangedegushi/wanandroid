package com.zzq.moduletree.model

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import com.zzq.moduletree.bean.NavigationData
import com.zzq.moduletree.netserver.ApiTreeCacheService
import com.zzq.moduletree.netserver.ApiTreeService
import com.zzq.netlib.error.ServerException
import com.zzq.netlib.http.model.BaseModel
import com.zzq.netlib.rxbase.CommonObserver
import com.zzq.netlib.utils.UtilRx
import io.reactivex.Observable
import io.rx_cache2.DynamicKey
import io.rx_cache2.EvictDynamicKey

/**
 *@auther tangedegushi
 *@creat 2018/12/7
 *@Decribe
 */
class NavigationModel : BaseModel() {

    val liveNaviData = MutableLiveData<List<NavigationData>>()
    //右边滑动时左边对应title，在两个fragment之间传递数据
    val liveTitleData = MutableLiveData<String>()
    val liveErrorData = MutableLiveData<String>()

    //数据基本不变，使用缓存，缓存时间为一天
    fun getCacheNavigationData(owner: LifecycleOwner) {
        getCacheService(ApiTreeCacheService::class.java)
                .getNavigationData(getNavigationData(), DynamicKey("navi"), EvictDynamicKey(false))
                .compose(UtilRx.applySchedulers())
                .`as`(UtilRx.bindLifeCycle(owner))
                .subscribe(object : CommonObserver<List<NavigationData>>() {
                    override fun onNext(r: List<NavigationData>) {
                        liveNaviData.value = r
                    }

                })
    }

    //缓存时去掉BaseResponse，不然在解析缓存时会有问题
    private fun getNavigationData(): Observable<List<NavigationData>>{
        return getService(ApiTreeService::class.java).getNavigationData().map{ t ->
            if (t.errorCode != 0) {
                liveErrorData.postValue(if (TextUtils.isEmpty(t.errorMsg)) "获取数据失败" else t.errorMsg)
                throw ServerException(t.errorCode, t.errorMsg ?: "the error message is undefine")
            }
            return@map t.data
        }
    }

}