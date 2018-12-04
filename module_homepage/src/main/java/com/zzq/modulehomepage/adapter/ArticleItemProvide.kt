package com.zzq.modulehomepage.adapter

import android.app.Activity
import android.content.Intent
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.zzq.modulehomepage.R
import com.zzq.modulehomepage.activity.WebActivity
import com.zzq.modulehomepage.bean.ArticleData
import com.zzq.netlib.utils.Logger

/**
 *@auther tangedegushi
 *@creat 2018/11/12
 *@Decribe
 */
class ArticleItemProvide(val activity: Activity) : BaseItemProvider<ArticleData.DatasBean, BaseViewHolder>() {

    override fun layout(): Int {
        return R.layout.fragment_home_item
    }

    override fun viewType(): Int {
        return HomeAdapter.TYPE_ARTICLE
    }

    override fun convert(helper: BaseViewHolder?, data: ArticleData.DatasBean?, position: Int) {
        helper!!.setText(R.id.tv_article_title, data!!.title)
                .setText(R.id.tv_super_chapter_name, data.superChapterName)
                .setText(R.id.tv_chapter_name, data.chapterName)
                .setText(R.id.tv_time, data.niceDate)
                .setOnClickListener(R.id.cv_item) {
                    val intent = Intent(activity, WebActivity::class.java)
                    intent.putExtra(WebActivity.CONTENT_URL,data.link)
                    activity.startActivity(intent)
                }
                .setOnClickListener(R.id.iv_like) {
                    //TODO
                    Logger.zzqLog().d("this is the click like article title name = ${data.title}")
                }
                .setOnClickListener(R.id.tv_chapter_name) {
                    //TODO
                    Logger.zzqLog().d("this is the click name = ${data.chapterName}")
                }


    }
}