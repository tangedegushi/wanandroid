package com.zzq.modulehomepage.adapter

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.zzq.commonlib.view.RecyclerViewDivider
import com.zzq.modulehomepage.R
import com.zzq.modulehomepage.activity.WebActivity
import com.zzq.modulehomepage.bean.FriendData
import com.zzq.modulehomepage.bean.FriendHomeData

/**
 *@auther tangedegushi
 *@creat 2018/11/12
 *@Decribe 常用网站item提供者
 */
class FriendItemProvide(val activity: Activity) : BaseItemProvider<FriendHomeData, BaseViewHolder>() {

    override fun layout(): Int {
        return R.layout.fragment_home_grid_item
    }

    override fun viewType(): Int {
        return HomeAdapter.TYPE_FRIEND
    }

    override fun convert(helper: BaseViewHolder?, data: FriendHomeData?, position: Int) {
        val rvFriend = helper!!.getView<RecyclerView>(R.id.rv_grid)
        helper.setText(R.id.tv_title, R.string.common_web)
        rvFriend.layoutManager = GridLayoutManager(rvFriend.context, 3, RecyclerView.HORIZONTAL, false)
        rvFriend.addItemDecoration(RecyclerViewDivider(rvFriend.context, RecyclerViewDivider.BOTH))
        var adapter = FriendAdapter(data!!.friendData)
        rvFriend.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(activity, WebActivity::class.java)
            val itemData = adapter.data[position] as FriendData
            intent.putExtra(WebActivity.CONTENT_URL,itemData.link)
            activity.startActivity(intent)
        }
    }
}