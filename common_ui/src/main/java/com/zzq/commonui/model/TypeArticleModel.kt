package com.zzq.commonui.model

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import com.zzq.commonui.bean.TypeArticleData
import com.zzq.commonui.netserver.ApiCacheServiceCommon
import com.zzq.commonui.netserver.ApiServiceCommon
import com.zzq.netlib.error.ServerException
import com.zzq.netlib.http.model.BaseModel
import com.zzq.netlib.rxbase.CommonObserver
import com.zzq.netlib.utils.UtilRx
import io.reactivex.Observable
import io.rx_cache2.DynamicKeyGroup
import io.rx_cache2.EvictDynamicKey

/**
 *@auther tangedegushi
 *@creat 2018/12/5
 *@Decribe
 */
class TypeArticleModel(private val owner:LifecycleOwner) : BaseModel() {
    val liveTypeArticleData = MutableLiveData<TypeArticleData>()
    val liveErrorData = MutableLiveData<String>()

    fun getCacheTypeArticleData(page:Int,cid:Int){
        getCacheService(ApiCacheServiceCommon::class.java)
                .getArticleList(getTypeArticleData(page,cid), DynamicKeyGroup(cid,page), EvictDynamicKey(false))
                .compose(UtilRx.applySchedulers())
                .`as`(UtilRx.bindLifeCycle(owner))
                .subscribe(object : CommonObserver<TypeArticleData>(){
                    override fun onNext(t: TypeArticleData) {
                        liveTypeArticleData.value = t
                    }
                })
    }

    //缓存时去掉BaseResponse，不然在解析缓存时会有问题
    private fun getTypeArticleData(page:Int,cid:Int): Observable<TypeArticleData> {
        return getService(ApiServiceCommon::class.java).getArticleList(page,cid).map{ t ->
            if (t.errorCode != 0) {
                liveErrorData.postValue(if (TextUtils.isEmpty(t.errorMsg)) "获取数据失败" else t.errorMsg)
                throw ServerException(t.errorCode, t.errorMsg ?: "the error message is undefine")
            }
            return@map t.data
        }
    }

}