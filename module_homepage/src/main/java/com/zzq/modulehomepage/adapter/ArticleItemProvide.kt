package com.zzq.modulehomepage.adapter

import android.app.Activity
import android.arch.lifecycle.LifecycleOwner
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.example.zzq.loginmodule.LoginActivity
import com.zzq.commonui.activity.CollectActivity
import com.zzq.commonui.model.CommonModel
import com.zzq.commonui.activity.TypeArticleActivity
import com.zzq.commonui.activity.WebActivity
import com.zzq.modulehomepage.R
import com.zzq.modulehomepage.bean.ArticleData

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
        helper?.apply {
            setText(R.id.tv_article_title, data!!.title)
            setText(R.id.tv_super_chapter_name, data.superChapterName)
            setText(R.id.tv_chapter_name, data.chapterName)
            setText(R.id.tv_time, data.niceDate)
            setImageResource(R.id.iv_like,if (data.collect) R.drawable.ic_action_like else R.drawable.ic_action_no_like)
            setOnClickListener(R.id.cv_item) {
                WebActivity.open(activity,data.link,title = data.superChapterName,id = data.id,author = data.author?:"")
            }
            setOnClickListener(R.id.iv_like) {
                if (LoginActivity.open(activity)) {
                    data.collect = !data.collect
                    setImageResource(R.id.iv_like,if (data.collect) R.drawable.ic_action_like else R.drawable.ic_action_no_like)
                    val model = CommonModel(activity as LifecycleOwner)
                    if (data.collect) model.collectArticle(data.id)else model.removeCollectArticle(data.id)
                }
            }
            setOnClickListener(R.id.tv_chapter_name) {
//                TypeArticleActivity.open(activity,data.chapterName,data.chapterId)
                CollectActivity.open(activity)
            }
        }
    }
}