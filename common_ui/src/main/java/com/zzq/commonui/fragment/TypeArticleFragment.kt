package com.zzq.commonui.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zzq.commonlib.Constants
import com.zzq.commonui.model.TypeArticleModel
import com.zzq.commonlib.view.RecyclerViewSpace
import com.zzq.commonui.R
import com.zzq.commonui.adapter.TypeArticleAdapter
import com.zzq.commonui.bean.TypeArticleData
import com.zzq.netlib.utils.UtilApp

/**
 *@auther tangedegushi
 *@creat 2018/12/12
 *@Decribe
 */
class TypeArticleFragment: Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var page: Int = 0
    private var typeCid: Int = 0
    private val typeData = ArrayList<TypeArticleData.DatasBean>()
    private val typeModel: TypeArticleModel by lazy { TypeArticleModel(this) }
    private val typeAdapter: TypeArticleAdapter by lazy { TypeArticleAdapter(activity as Activity, typeData) }
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var rv_type_article: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        typeCid = arguments!!.getInt(ARTICLE_CID)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ui_fragment_type_article,null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        rv_type_article = view.findViewById(R.id.rv_type_article)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUI()
    }

    //有数据返回时回调
    private fun initUI() {
        swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.swipe_refresh_color1, R.color.swipe_refresh_color2, R.color.swipe_refresh_color3)
            setOnRefreshListener(this@TypeArticleFragment)
            isRefreshing = true
        }

        typeAdapter.apply {
            setPreLoadNumber(3)
            setOnLoadMoreListener(loadMoreListener, rv_type_article)
        }

        rv_type_article.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(RecyclerViewSpace())
            adapter = typeAdapter
        }

        typeModel.getCacheTypeArticleData(page, typeCid)
        swipeRefreshLayout.postDelayed(run, Constants.REQUEST_TIME_OUT)
        typeModel.liveTypeArticleData.observe(this, Observer<TypeArticleData> {
            swipeRefreshLayout.removeCallbacks(run)
            it?.apply {
                if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
                if (typeAdapter.isLoading) {
                    if (over) typeAdapter.loadMoreEnd() else typeAdapter.loadMoreComplete()
                }
                if (curPage == 1 && !typeData.isEmpty() && (datas?.get(0)?.title == typeData[0].title)) return@Observer
                typeAdapter.addData(datas!!)
            }
        })
        typeModel.liveErrorData.observe(this, Observer {
            swipeRefreshLayout.isRefreshing = false
            if (typeAdapter.isLoading) typeAdapter.loadMoreFail()
            UtilApp.showToast(it!!)
        })
    }

    override fun onRefresh() {
        page = 0
        typeModel.getCacheTypeArticleData(page, typeCid)
        swipeRefreshLayout.removeCallbacks(run)
    }

    val loadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        if (typeModel.liveTypeArticleData.value?.over == true) {
            typeAdapter.loadMoreEnd()
            return@RequestLoadMoreListener
        }
        page++
        typeModel.getCacheTypeArticleData(page, typeCid)
        swipeRefreshLayout.postDelayed(run, Constants.REQUEST_TIME_OUT)
    }

    val run = Runnable {
        swipeRefreshLayout.isRefreshing = false
        typeAdapter.loadMoreFail()
    }

    companion object {
        const val ARTICLE_CID = "article_cid"
        fun newInstance(cid: Int): Fragment {
            val fragment = TypeArticleFragment()
            val args = Bundle()
            args.putInt(ARTICLE_CID,cid)
            fragment.arguments = args
            return fragment
        }
    }
}