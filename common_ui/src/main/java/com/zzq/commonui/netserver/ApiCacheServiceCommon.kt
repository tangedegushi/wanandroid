package com.zzq.commonui.netserver

import com.zzq.commonui.bean.TypeArticleData
import io.reactivex.Observable
import io.rx_cache2.*
import java.util.concurrent.TimeUnit

/**
 *@auther tangedegushi
 *@creat 2018/12/11
 *@Decribe
 */
interface ApiCacheServiceCommon {

    /**
     * 知识体系下的文章
     * http://www.wanandroid.com/article/list/0/json?cid=168
     * @param page page
     * @param cid cid
     */
    @LifeCache(duration = 12, timeUnit = TimeUnit.HOURS)
    @ProviderKey("type_article_data")
    fun getArticleList(observable: Observable<TypeArticleData>,
                       groupKey: DynamicKeyGroup,
                       evict: EvictDynamicKey):Observable<TypeArticleData>

}