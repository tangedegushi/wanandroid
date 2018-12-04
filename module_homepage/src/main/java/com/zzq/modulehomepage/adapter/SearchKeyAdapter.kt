package com.zzq.modulehomepage.adapter

import android.content.Context
import android.content.Intent
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zzq.modulehomepage.R
import com.zzq.modulehomepage.activity.WebActivity
import com.zzq.modulehomepage.bean.SearchKeyData
import com.zzq.netlib.utils.Logger

/**
 *@auther tangedegushi
 *@creat 2018/11/19
 *@Decribe
 */
class SearchKeyAdapter(val context:Context,data: List<SearchKeyData.Datas>) : BaseQuickAdapter<SearchKeyData.Datas,BaseViewHolder>(R.layout.fragment_home_item, data) {
    override fun convert(helper: BaseViewHolder?, item: SearchKeyData.Datas) {
        helper!!.setText(R.id.tv_article_title, item.title)
                .setText(R.id.tv_chapter_name, item.chapterName)
                .setText(R.id.tv_time, item.niceDate)
                .setOnClickListener(R.id.cv_item) {
                    val intent = Intent(context, WebActivity::class.java)
                    intent.putExtra(WebActivity.CONTENT_URL,item.link)
                    context.startActivity(intent)
                }
                .setOnClickListener(R.id.iv_like) {
                    //TODO
                    Logger.zzqLog().d("this is the click like article title name = ${item.title}")
                }
                .setOnClickListener(R.id.tv_chapter_name) {
                    //TODO
                    Logger.zzqLog().d("this is the click name = ${item.chapterName}")
                }
    }
}