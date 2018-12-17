package com.zzq.commonui.adapter

import android.app.Activity
import android.arch.lifecycle.LifecycleOwner
import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zzq.commonui.R
import com.zzq.commonui.activity.TypeArticleActivity
import com.zzq.commonui.activity.WebActivity
import com.zzq.commonui.bean.CollectArticleData
import com.zzq.commonui.model.CommonModel

/**
 *@auther tangedegushi
 *@creat 2018/12/5
 *@Decribe
 */
class CollectArticleAdapter(private val activity: Activity, data: List<CollectArticleData.DatasBean>) :
        BaseQuickAdapter<CollectArticleData.DatasBean, BaseViewHolder>(R.layout.common_collect_article_item, data) {

    override fun convert(helper: BaseViewHolder?, item: CollectArticleData.DatasBean?) {
        helper?.apply {
            setText(R.id.tv_article_title, item!!.title)
            setText(R.id.tv_chapter_name, item.chapterName)
            setGone(R.id.tv_chapter_name,!TextUtils.isEmpty(item.chapterName))
            setText(R.id.tv_time, item.niceDate)
            setImageResource(R.id.iv_like, R.drawable.ic_action_like)
            setOnClickListener(R.id.cv_item) {
                WebActivity.open(activity,item.link)
            }
            setOnClickListener(R.id.iv_like) {
                val model = CommonModel(activity as LifecycleOwner)
                model.removeCollectArticle(item.id,item.originId)
            }
            setOnClickListener(R.id.tv_chapter_name) {
                TypeArticleActivity.open(activity,item.chapterName,item.chapterId)
            }
        }
    }
}