package com.zzq.commonui.adapter

import android.app.Activity
import android.arch.lifecycle.LifecycleOwner
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.zzq.loginmodule.LoginActivity
import com.zzq.commonui.R
import com.zzq.commonui.activity.TypeArticleActivity
import com.zzq.commonui.activity.WebActivity
import com.zzq.commonui.bean.TypeArticleData
import com.zzq.commonui.model.CommonModel

/**
 *@auther tangedegushi
 *@creat 2018/12/5
 *@Decribe
 */
class TypeArticleAdapter(private val activity: Activity, data: List<TypeArticleData.DatasBean>) : BaseQuickAdapter<TypeArticleData.DatasBean, BaseViewHolder>(R.layout.fragment_home_item, data) {

    override fun convert(helper: BaseViewHolder?, item: TypeArticleData.DatasBean?) {
        helper?.apply {
            setText(R.id.tv_article_title, item!!.title)
            setText(R.id.tv_super_chapter_name, item.superChapterName)
//            setText(R.id.tv_chapter_name, item.chapterName)
            setGone(R.id.tv_chapter_name,false)
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
        }
    }
}