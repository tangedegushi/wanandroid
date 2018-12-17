package com.zzq.commonui.adapter

import android.app.Activity
import android.arch.lifecycle.LifecycleOwner
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.zzq.loginmodule.LoginActivity
import com.zzq.commonui.model.CommonModel
import com.zzq.commonui.R
import com.zzq.commonui.activity.TypeArticleActivity
import com.zzq.commonui.activity.WebActivity
import com.zzq.commonui.bean.SearchKeyData

/**
 *@auther tangedegushi
 *@creat 2018/11/19
 *@Decribe
 */
class SearchKeyAdapter(val activity:Activity,data: List<SearchKeyData.Datas>) : BaseQuickAdapter<SearchKeyData.Datas,BaseViewHolder>(R.layout.fragment_home_item, data) {

    override fun convert(helper: BaseViewHolder?, item: SearchKeyData.Datas) {
        helper?.apply {
            setText(R.id.tv_article_title, item.title)
            setText(R.id.tv_chapter_name, item.chapterName)
            setText(R.id.tv_time, item.niceDate)
            setOnClickListener(R.id.cv_item) {
                WebActivity.open(activity,item.link)
            }
            setOnClickListener(R.id.iv_like) {
                if (LoginActivity.open(activity)) {
                    item.collect = !item.collect
                    setImageResource(R.id.iv_like,if (item.collect) R.drawable.ic_action_like else R.drawable.ic_action_no_like)
                    val model = CommonModel(activity as LifecycleOwner)
                    if (item.collect) model.collectArticle(item.id)else model.removeCollectArticle(item.id)
                }
            }
            setOnClickListener(R.id.tv_chapter_name) {
                TypeArticleActivity.open(activity,item.chapterName,item.chapterId)
            }
        }
    }
}