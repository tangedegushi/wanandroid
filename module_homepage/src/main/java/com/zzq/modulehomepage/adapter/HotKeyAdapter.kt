package com.zzq.modulehomepage.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zzq.modulehomepage.R
import com.zzq.modulehomepage.bean.HotKeyData

/**
 *@auther tangedegushi
 *@creat 2018/11/12
 *@Decribe
 */
class HotKeyAdapter(data: List<HotKeyData>?) : BaseQuickAdapter<HotKeyData, BaseViewHolder>(R.layout.fragment_home_grid_hk_item, data) {
    override fun convert(helper: BaseViewHolder?, item: HotKeyData?) {
        helper!!.setText(R.id.tv_item, item?.name)
    }
}