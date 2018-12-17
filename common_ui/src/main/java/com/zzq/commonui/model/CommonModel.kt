package com.zzq.commonui.model

import android.arch.lifecycle.LifecycleOwner
import com.zzq.commonlib.R
import com.zzq.commonui.bean.CollectOutArticleData
import com.zzq.commonui.netserver.ApiServiceCommon
import com.zzq.netlib.http.model.BaseModel
import com.zzq.netlib.rxbase.BaseResponse
import com.zzq.netlib.rxbase.CommonObserver
import com.zzq.netlib.utils.UtilApp
import com.zzq.netlib.utils.UtilRx
import io.reactivex.schedulers.Schedulers

/**
 *@auther tangedegushi
 *@creat 2018/12/6
 *@Decribe
 */
class CommonModel(val owner: LifecycleOwner) : BaseModel() {


    //收藏站内文章
    fun collectArticle(id: Int) {
        getService(ApiServiceCommon::class.java).collectArticle(id)
                .subscribeOn(Schedulers.io())
                .`as`(UtilRx.bindLifeCycle(owner))
                .subscribe(object : CommonObserver<BaseResponse<String>>() {
                    override fun onNext(t: BaseResponse<String>) {
                        if (t.errorCode == 0) {
                            UtilApp.showToast(R.string.common_collect_article)
                        }
                    }
                })
    }

    //收藏站外文章
    fun collectArticle(articleTitle: String, author: String, link: String) {
        getService(ApiServiceCommon::class.java).collectOutArticle(articleTitle, author, link)
                .subscribeOn(Schedulers.io())
                .subscribe(object : CommonObserver<BaseResponse<CollectOutArticleData>>() {
                    override fun onNext(r: BaseResponse<CollectOutArticleData>) {
                        if (r.errorCode == 0) {
                            UtilApp.showToast(R.string.common_collect_article)
                        }
                    }
                })
    }

    //取消收藏文章(不是在收藏页面)
    fun removeCollectArticle(id: Int) {
        getService(ApiServiceCommon::class.java).removeCollectArticleCurrent(id)
                .subscribeOn(Schedulers.io())
                .`as`(UtilRx.bindLifeCycle(owner))
                .subscribe(object : CommonObserver<BaseResponse<String>>() {
                    override fun onNext(t: BaseResponse<String>) {
                        if (t.errorCode == 0) {
                            UtilApp.showToast(R.string.common_collect_article_cancel)
                        }
                    }
                })
    }

    //取消收藏文章(在收藏页面)
    fun removeCollectArticle(id: Int, originId: Int) {
        getService(ApiServiceCommon::class.java).removeCollectArticle(id = id, originId = originId)
                .subscribeOn(Schedulers.io())
                .`as`(UtilRx.bindLifeCycle(owner))
                .subscribe(object : CommonObserver<BaseResponse<String>>() {
                    override fun onNext(t: BaseResponse<String>) {
                        if (t.errorCode == 0) {
                            UtilApp.showToast(R.string.common_collect_article_cancel)
                        }
                    }
                })
    }


}