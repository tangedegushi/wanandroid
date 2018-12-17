package com.zzq.moduletree.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zzq.moduletree.R
import com.zzq.netlib.utils.Logger

/**
 *@auther tangedegushi
 *@creat 2018/12/7
 *@Decribe
 */
class TreeTypeAdapter(data: List<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.tree_type_item, data) {
    var selectTypeString: String? = null
    var lastPosition: Int = 0
    override fun convert(helper: BaseViewHolder?, item: String?) {
        helper?.apply {
            setBackgroundRes(R.id.tv_tree_type,if (item == selectTypeString) R.color.tree_item_select else R.color.tree_item_normal)
            setText(R.id.tv_tree_type, item)
            addOnClickListener(R.id.tv_tree_type)
        }
    }
}