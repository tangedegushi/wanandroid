package com.zzq.modulehomepage.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zzq.modulehomepage.R
import com.zzq.modulehomepage.bean.FriendData

/**
 *@auther tangedegushi
 *@creat 2018/11/9
 *@Decribe
 */
class FriendAdapter(data: List<FriendData>?) :
        BaseQuickAdapter<FriendData, BaseViewHolder>(R.layout.fragment_home_grid_fri_item, data) {

    override fun convert(helper: BaseViewHolder, item: FriendData?) {
        helper.setText(R.id.tv_item, item?.name)
    }

}