package com.zzq.moduletree.netserver

import com.zzq.moduletree.bean.NavigationData
import com.zzq.moduletree.bean.TreeData
import io.reactivex.Observable
import io.rx_cache2.DynamicKey
import io.rx_cache2.EvictDynamicKey
import io.rx_cache2.LifeCache
import io.rx_cache2.ProviderKey
import java.util.concurrent.TimeUnit

/**
 *@auther tangedegushi
 *@creat 2018/12/14
 *@Decribe
 */
interface ApiTreeCacheService {

    /**
     * 获取体系数据
     */
    @LifeCache(duration = 1, timeUnit = TimeUnit.DAYS)
    @ProviderKey("tree_data")
    fun getTreeData(observable: Observable<List<TreeData>>,
                    key: DynamicKey,
                    evict: EvictDynamicKey): Observable<List<TreeData>>

    /**
     * 获取导航数据
     */
    @LifeCache(duration = 1, timeUnit = TimeUnit.DAYS)
    @ProviderKey("navigation_data")
    fun getNavigationData(observable: Observable<List<NavigationData>>,
                          key: DynamicKey,
                          evict: EvictDynamicKey): Observable<List<NavigationData>>
}