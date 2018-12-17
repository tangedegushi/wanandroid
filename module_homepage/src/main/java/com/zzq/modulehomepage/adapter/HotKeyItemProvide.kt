package com.zzq.modulehomepage.adapter

import android.app.Activity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.zzq.commonlib.Constants
import com.zzq.commonlib.router.MyArouter
import com.zzq.commonlib.view.RecyclerViewDivider
import com.zzq.commonui.activity.SearchActivity
import com.zzq.modulehomepage.R
import com.zzq.modulehomepage.bean.HotKeyData
import com.zzq.modulehomepage.bean.HotKeyHomeData

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
        helper.setText(R.id.tv_title, R.string.ui_search_hot_key)
        rvHotKey.layoutManager = GridLayoutManager(rvHotKey.context, 3)
        rvHotKey.addItemDecoration(RecyclerViewDivider(rvHotKey.context, RecyclerViewDivider.BOTH))
        val adapter = HotKeyAdapter(data!!.hotKeyData)
        rvHotKey.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position ->
            val itemData = adapter.data[position] as HotKeyData
            MyArouter.getArouter().build(Constants.SEARCH_ACTIVITY_COMPONENT)
                    .withString(Constants.SEARCH_ACTIVITY_KEY,itemData.name)
                    .navigation()
        }
    }
}