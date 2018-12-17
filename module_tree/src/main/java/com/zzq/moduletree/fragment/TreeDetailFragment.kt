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
import com.zzq.moduletree.bean.TreeData
import com.zzq.commonlib.view.RecyclerViewSpace
import com.zzq.commonui.activity.TypeArticleActivity
import com.zzq.moduletree.R
import com.zzq.moduletree.TreeTypeTitleActivity
import com.zzq.moduletree.adapter.ItemHeaderDecoration
import com.zzq.moduletree.adapter.TreeDetailItemAdapter
import com.zzq.moduletree.model.TreeModel
import com.zzq.netlib.utils.Logger
import kotlinx.android.synthetic.main.tree_detail_fragment.*

/**
 *@auther tangedegushi
 *@creat 2018/12/7
 *@Decribe
 */
class TreeDetailFragment : Fragment(),ItemHeaderDecoration.DetailTitleCallback {

    private val treeTypeDetailList = mutableListOf<TreeData.ChildrenBean>()
    private val treeModel by lazy { ViewModelProviders.of(parentFragment!!).get(TreeModel::class.java) }
    private val treeDetailItemAdapter by lazy { TreeDetailItemAdapter(treeTypeDetailList) }
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
            headerDecoration.callback = this@TreeDetailFragment
            adapter = treeDetailItemAdapter
            addOnScrollListener(RecyclerViewScrollListener())
        }
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return treeDetailItemAdapter.getItemViewType(position)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        treeModel.liveTreeData.observe(this, Observer {
            if (it != null) {
                for (item in it) {
                    if (item.children != null) {
                        val childrenBean = TreeData.ChildrenBean()
                        childrenBean.isTitle = true
                        childrenBean.titleName = item.name
                        treeTypeDetailList.add(childrenBean)
                        for (childItem in item.children!!) {
                            childItem.titleName = item.name
                            treeTypeDetailList.add(childItem)
                        }
                    }
                }
                headerDecoration.data = treeTypeDetailList
            }
            treeDetailItemAdapter.notifyDataSetChanged()
        })
    }

    override fun onStart() {
        super.onStart()
        treeDetailItemAdapter.setOnItemClickListener { adapter, view, position ->
            val data = treeTypeDetailList[position]
            if (data.isTitle) {
                val treeDataList = treeModel.liveTreeData.value?: mutableListOf()
                val titlePosition = treeDataList.indexOfFirst { data.titleName == it.name }
                if (titlePosition != -1 && treeDataList[titlePosition].children != null) {
                    TreeTypeTitleActivity.open(activity as Activity,treeDataList[titlePosition])
                }
            } else {
                TypeArticleActivity.open(activity as Activity,data.titleName,data.id)
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
        treeModel.liveTitleData.value = title
    }
}