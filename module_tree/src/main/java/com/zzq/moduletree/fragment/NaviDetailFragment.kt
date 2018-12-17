package com.zzq.moduletree.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zzq.moduletree.bean.NavigationData
import com.zzq.commonlib.view.RecyclerViewSpace
import com.zzq.commonui.activity.WebActivity
import com.zzq.moduletree.R
import com.zzq.moduletree.adapter.ItemHeaderDecoration
import com.zzq.moduletree.adapter.NaviDetailItemAdapter
import com.zzq.moduletree.model.NavigationModel
import com.zzq.netlib.utils.Logger
import kotlinx.android.synthetic.main.tree_detail_fragment.*

/**
 *@auther tangedegushi
 *@creat 2018/12/7
 *@Decribe
 */
class NaviDetailFragment : Fragment(),ItemHeaderDecoration.DetailTitleCallback {

    private val naviTypeDetailList = mutableListOf<NavigationData.ArticlesBean>()
    private val naviModel by lazy { ViewModelProviders.of(parentFragment!!).get(NavigationModel::class.java) }
    private val naviDetailItemAdapter by lazy { NaviDetailItemAdapter(naviTypeDetailList) }
    private val gridLayoutManager by lazy { GridLayoutManager(context, 3) }
    private val headerDecoration by lazy { ItemHeaderDecoration() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tree_detail_fragment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvDetail = view.findViewById<RecyclerView>(R.id.rv_detail_fragment)
        rvDetail.apply {
            layoutManager = gridLayoutManager
            addItemDecoration(RecyclerViewSpace())
            addItemDecoration(headerDecoration)
            headerDecoration.callback = this@NaviDetailFragment
            headerDecoration.isShowNextIcon = false
            adapter = naviDetailItemAdapter
            addOnScrollListener(RecyclerViewScrollListener())
        }
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return naviDetailItemAdapter.getItemViewType(position)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        naviModel.liveNaviData.observe(this, Observer {
            if (it != null) {
                for (item in it) {
                    if (item.articles != null) {
                        val childrenBean = NavigationData.ArticlesBean()
                        childrenBean.isTitle = true
                        childrenBean.titleName = item.name
                        naviTypeDetailList.add(childrenBean)
                        for (childItem in item.articles!!) {
                            childItem.titleName = item.name
                            naviTypeDetailList.add(childItem)
                        }
                    }
                }
                headerDecoration.data = naviTypeDetailList
            }
            naviDetailItemAdapter.notifyDataSetChanged()
        })
    }

    override fun onStart() {
        super.onStart()
        naviDetailItemAdapter.setOnItemClickListener { adapter, view, position ->
            val data = naviTypeDetailList[position]
            if (!data.isTitle) {
                WebActivity.open(activity as Activity,data.link,data.title)
            }
        }
    }

    fun setSelectPosition(position: Int) {
        smoothMoveToPosition(position)
    }

    private var isMove = false
    private var mIndex = 0
    private fun smoothMoveToPosition(position: Int) {
        mIndex = position
        val firstItem = gridLayoutManager.findFirstVisibleItemPosition()
        val lastItem = gridLayoutManager.findLastVisibleItemPosition()
        when {
            position <= firstItem -> rv_detail_fragment.scrollToPosition(position)
            position <= lastItem -> {
                val top = rv_detail_fragment.getChildAt(position - firstItem).top
                rv_detail_fragment.scrollBy(0, top)
            }
            else -> {
                isMove = true
                rv_detail_fragment.scrollToPosition(position)
            }
        }
    }

    inner class RecyclerViewScrollListener: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (isMove) {
                isMove = false
                smoothMoveToPosition(mIndex)
            }
            Logger.zzqLog().d("onScrolled dx = $dx  dy = $dy")
        }
    }

    override fun onTitlePosition(title: String) {
        naviModel.liveTitleData.value = title
    }
}