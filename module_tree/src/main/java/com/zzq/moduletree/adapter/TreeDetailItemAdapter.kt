package com.zzq.moduletree.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.MultipleItemRvAdapter
import com.zzq.moduletree.bean.TreeData

/**
 *@auther tangedegushi
 *@creat 2018/12/7
 *@Decribe
 */
class TreeDetailItemAdapter(data: List<TreeData.ChildrenBean>):MultipleItemRvAdapter<TreeData.ChildrenBean,BaseViewHolder>(data) {

    init {
        finishInitialize()
    }

    override fun registerItemProvider() {
        mProviderDelegate.registerProvider(TreeDetailTitleProvide())
        mProviderDelegate.registerProvider(TreeDetailItemProvide())
    }

    override fun getViewType(t: TreeData.ChildrenBean?): Int {
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