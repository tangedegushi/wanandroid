package com.zzq.modulehomepage.adapter

import android.app.Activity
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.MultipleItemRvAdapter
import com.zzq.modulehomepage.bean.ArticleData
import com.zzq.modulehomepage.bean.FriendHomeData
import com.zzq.modulehomepage.bean.HomeData
import com.zzq.modulehomepage.bean.HotKeyHomeData

/**
 *@auther tangedegushi
 *@creat 2018/11/9
 *@Decribe
 */
class HomeAdapter<T : HomeData>(val activity: Activity, homeData: List<T>?) : MultipleItemRvAdapter<T, BaseViewHolder>(homeData) {

    init {
        finishInitialize()
    }

    override fun registerItemProvider() {
        mProviderDelegate.registerProvider(HotKeyItemProvide(activity))
        mProviderDelegate.registerProvider(FriendItemProvide(activity))
        mProviderDelegate.registerProvider(ArticleItemProvide(activity))
    }

    override fun getViewType(t: T?): Int {
        return when (t) {
            is HotKeyHomeData -> TYPE_HOTKEY
            is FriendHomeData -> TYPE_FRIEND
            is ArticleData.DatasBean -> TYPE_ARTICLE

            else -> {
                0
            }
        }
    }


    companion object {
        const val TYPE_HOTKEY = 1
        const val TYPE_FRIEND = 2
        const val TYPE_ARTICLE = 3
    }
}