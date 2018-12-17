package com.zzq.moduletree.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.zzq.moduletree.bean.TreeData
import com.zzq.moduletree.R

/**
 *@auther tangedegushi
 *@creat 2018/12/7
 *@Decribe
 */
class TreeDetailItemProvide: BaseItemProvider<TreeData.ChildrenBean, BaseViewHolder>() {

    override fun layout(): Int = R.layout.tree_detail_item

    override fun viewType(): Int = TreeDetailItemAdapter.TREE_DETAIL

    override fun convert(helper: BaseViewHolder?, data: TreeData.ChildrenBean?, position: Int) {
        helper?.apply {
            setText(R.id.tv_tree_detail,data?.name)
        }
    }
}