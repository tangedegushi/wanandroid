package com.zzq.moduletree.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.MultipleItemRvAdapter
import com.zzq.moduletree.bean.NavigationData

/**
 *@auther tangedegushi
 *@creat 2018/12/7
 *@Decribe
 */
class NaviDetailItemAdapter(data: List<NavigationData.ArticlesBean>):MultipleItemRvAdapter<NavigationData.ArticlesBean,BaseViewHolder>(data) {

    init {
        finishInitialize()
    }

    override fun registerItemProvider() {
        mProviderDelegate.registerProvider(NaviDetailTitleProvide())
        mProviderDelegate.registerProvider(NaviDetailItemProvide())
    }

    override fun getViewType(t: NavigationData.ArticlesBean?): Int {
        t?.apply {
            return if (isTitle) TREE_TITILE else TREE_DETAIL
        }
        return 0
    }

    companion object {
        const val TREE_TITILE = 3
        const val TREE_DETAIL = 1
    }
}