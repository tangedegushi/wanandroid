package com.zzq.modulehomepage.model

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import com.zzq.modulehomepage.bean.ArticleData
import com.zzq.modulehomepage.bean.BannerData
import com.zzq.modulehomepage.bean.FriendData
import com.zzq.modulehomepage.bean.HotKeyData
import com.zzq.modulehomepage.netserver.ApiService
import com.zzq.netlib.http.model.BaseModel
import com.zzq.netlib.rxbase.BaseObserver
import com.zzq.netlib.rxbase.BaseResponse
import com.zzq.netlib.utils.UtilRx
import io.reactivex.schedulers.Schedulers

/**
 *@auther tangedegushi
 *@creat 2018/11/12
 *@Decribe
 */
class HomeModel : BaseModel() {
    lateinit var owner: LifecycleOwner

    var liveBannerData: MutableLiveData<List<BannerData>> = MutableLiveData()
    var liveHotKeyDataData: MutableLiveData<List<HotKeyData>> = MutableLiveData()
    var liveFriendData: MutableLiveData<List<FriendData>> = MutableLiveData()
    var liveArticleData: MutableLiveData<ArticleData> = MutableLiveData()

    fun requestBanner() {
        getService(ApiService::class.java)
                .getBannerData()
                .subscribeOn(Schedulers.io())
                .`as`(UtilRx.bindLifeCycle(owner))
                .subscribe(object : BaseObserver<BaseResponse<List<BannerData>>, List<BannerData>>() {
                    override fun onNextBaseResult(r: List<BannerData>) {
                        val bannerData = liveBannerData.value
                        if (bannerData == null) {
                            liveBannerData.postValue(r)
                        } else {
                            r.forEach { item ->
                                bannerData.forEach {
                                    if (!item.title.equals(it.title)) {
                                        liveBannerData.postValue(r)
                                        return@onNextBaseResult
                                    }
                                }
                            }
                        }
                    }
                })
    }

    fun requestHotKey() {
        getService(ApiService::class.java)
                .getHotKeyData()
                .subscribeOn(Schedulers.io())
                .`as`(UtilRx.bindLifeCycle(owner))
                .subscribe(object : BaseObserver<BaseResponse<List<HotKeyData>>, List<HotKeyData>>() {
                    override fun onNextBaseResult(r: List<HotKeyData>) {
                        val hotKeyData = liveHotKeyDataData.value
                        if (hotKeyData == null) {
                            liveHotKeyDataData.postValue(r)
                        } else {
                            r.forEach { item ->
                                hotKeyData.forEach {
                                    if (item.name != it.name) {
                                        liveHotKeyDataData.postValue(r)
                                        return@onNextBaseResult
                                    }
                                }
                            }
                        }
                    }
                })
    }

    fun requestFriend() {
        getService(ApiService::class.java)
                .getFriendData()
                .subscribeOn(Schedulers.io())
                .`as`(UtilRx.bindLifeCycle(owner))
                .subscribe(object : BaseObserver<BaseResponse<List<FriendData>>, List<FriendData>>() {
                    override fun onNextBaseResult(r: List<FriendData>) {
                        val friendData = liveFriendData.value
                        if (friendData == null) {
                            liveFriendData.postValue(r)
                        } else {
                            r.forEach { item ->
                                friendData.forEach {
                                    if (!item.name.equals(it.name)) {
                                        liveFriendData.postValue(r)
                                        return@onNextBaseResult
                                    }
                                }
                            }
                        }
                    }
                })
    }

    fun requestArticle(index: Int) {
        getService(ApiService::class.java)
                .getArticleData(index)
                .subscribeOn(Schedulers.io())
                .`as`(UtilRx.bindLifeCycle(owner))
                .subscribe(object : BaseObserver<BaseResponse<ArticleData>, ArticleData>() {
                    override fun onNextBaseResult(r: ArticleData) {
                        liveArticleData.postValue(r)
                    }
                })
    }

}