package com.zzq.commonui.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zzq.commonlib.Constants
import com.zzq.commonlib.base.BaseActivity
import com.zzq.commonlib.view.RecyclerViewSpace
import com.zzq.commonui.R
import com.zzq.commonui.adapter.CollectArticleAdapter
import com.zzq.commonui.bean.CollectArticleData
import com.zzq.commonui.model.CollectArticleModel
import kotlinx.android.synthetic.main.activity_collect.*

@Route(path = Constants.COLLECT_ACTIVITY_COMPONENT)
class CollectActivity : BaseActivity() {
    override val useBar: Boolean = true
    override val homeUp: Boolean = true

    private var page: Int = 0
    private val collectData = ArrayList<CollectArticleData.DatasBean>()
    private val collectModel by lazy { CollectArticleModel(this) }
    private val collectAdapter by lazy { CollectArticleAdapter(this, collectData) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect)
        initUI()
    }
    //有数据返回时回调
    private fun initUI() {
        collectAdapter.apply {
            setPreLoadNumber(3)
            setOnLoadMoreListener(loadMoreListener, rv_collect_article)
        }

        rv_collect_article.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(RecyclerViewSpace())
            adapter = collectAdapter
        }

        collectModel.getCollectArticles(page)
        collectModel.liveCollectArticleData.observe(this, Observer<CollectArticleData> {
            it?.apply {
                if (collectAdapter.isLoading) {
                    if (over) collectAdapter.loadMoreEnd() else collectAdapter.loadMoreComplete()
                }
                collectAdapter.addData(datas!!)
            }
        })
    }


    val loadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        if (collectModel.liveCollectArticleData.value?.over == true) {
            collectAdapter.loadMoreEnd()
            return@RequestLoadMoreListener
        }
        page++
        collectModel.getCollectArticles(page)
        rv_collect_article.postDelayed(run,Constants.REQUEST_TIME_OUT)
    }

    val run = Runnable {
        collectAdapter.loadMoreFail()
    }

}
