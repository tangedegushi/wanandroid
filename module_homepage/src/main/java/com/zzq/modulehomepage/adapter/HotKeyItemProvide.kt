package com.zzq.modulehomepage.adapter

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.zzq.commonlib.view.RecyclerViewDivider
import com.zzq.modulehomepage.R
import com.zzq.modulehomepage.activity.SearchActivity
import com.zzq.modulehomepage.activity.WebActivity
import com.zzq.modulehomepage.bean.HotKeyData
import com.zzq.modulehomepage.bean.HotKeyHomeData
import com.zzq.netlib.utils.Logger

/**
 *@auther tangedegushi
 *@creat 2018/11/12
 *@Decribe
 */
class HotKeyItemProvide(val activity: Activity) : BaseItemProvider<HotKeyHomeData, BaseViewHolder>() {

    override fun layout(): Int {
        return R.layout.fragment_home_grid_item
    }

    override fun viewType(): Int {
        return HomeAdapter.TYPE_HOTKEY
    }

    override fun convert(helper: BaseViewHolder?, data: HotKeyHomeData?, position: Int) {
        val rvHotKey = helper!!.getView<RecyclerView>(R.id.rv_grid)
        helper.setText(R.id.tv_title, R.string.search_hot_key)
        rvHotKey.layoutManager = GridLayoutManager(rvHotKey.context, 3)
        rvHotKey.addItemDecoration(RecyclerViewDivider(rvHotKey.context, RecyclerViewDivider.BOTH))
        val adapter = HotKeyAdapter(data!!.hotKeyData)
        rvHotKey.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(activity, WebActivity::class.java)
            val itemData = adapter.data[position] as HotKeyData
            SearchActivity.open(activity,itemData.name)
            Logger.zzqLog().d("this is click grid item name = ${data.hotKeyData!![position].name}")
        }
    }
}