package com.zzq.commonui.activity

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zzq.commonlib.Constants
import com.zzq.commonlib.base.BaseActivity
import com.zzq.commonlib.view.RecyclerViewSpace
import com.zzq.commonui.model.SearchModel
import com.zzq.commonui.R
import com.zzq.commonui.adapter.SearchKeyAdapter
import com.zzq.commonui.bean.SearchKeyData
import com.zzq.netlib.utils.UtilApp
import kotlinx.android.synthetic.main.activity_search.*

@Route(path = Constants.SEARCH_ACTIVITY_COMPONENT)
class SearchActivity : BaseActivity() {
    override val useBar: Boolean = true
    override val homeUp: Boolean = true

    private var searchView: SearchView? = null

    private var searchKeyData: MutableList<SearchKeyData.Datas> = mutableListOf()
    private val searchModel by lazy { SearchModel(this) }
    private val searchKeyAdapter by lazy { SearchKeyAdapter(this@SearchActivity, searchKeyData) }

    private var searchKey: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchKey = intent.getStringExtra(Constants.SEARCH_ACTIVITY_KEY)?:""

        setSupportActionBar(toolbar)
        searchKey.apply {
            toolbar?.title = this
            searchModel.getSearchKeyData(0, this)
        }

        initUI()

        searchKeyAdapter.apply {
            setPreLoadNumber(3)
            setOnLoadMoreListener(loadMoreListener, search_recyclerView)
        }
        search_recyclerView.run {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            addItemDecoration(RecyclerViewSpace())
            adapter = searchKeyAdapter
        }
    }

    private fun initUI() {
        searchModel.liveSearchKeyData.observe(this@SearchActivity, Observer<SearchKeyData> {
            it?.apply {
                if (curPage == 1) {
                    searchKeyAdapter.apply {
                        toolbar?.title = searchKey
                        setNewData(it.datas)
                        setEnableLoadMore(it.pageCount != 1)
                    }
                } else {
                    search_recyclerView.removeCallbacks(run)
                    searchKeyAdapter.apply {
                        addData(searchKeyData.size, it.datas!!)
                        if (it.over) loadMoreEnd() else loadMoreComplete()
                    }
                }
            }
        })
        searchModel.liveSeatchKeyNoData.observe(this@SearchActivity, Observer {
            UtilApp.showToast("未找到相关内容")
        })
    }

    private val loadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        searchModel.liveSearchKeyData.value?.apply {
            searchModel.getSearchKeyData(curPage, searchKey)
            search_recyclerView.postDelayed(run,10_000)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        searchView = menu.findItem(R.id.menuSearch).actionView as SearchView
        searchView?.setOnQueryTextListener(searchViewListener)
        return super.onCreateOptionsMenu(menu)
    }

    private var searchViewListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(sKey: String?): Boolean {
            sKey?.apply {
                searchKey = sKey
                searchModel.getSearchKeyData(0, sKey)
            }
            searchView?.clearFocus()
            searchView?.setQuery("", false)
            searchView?.isIconified = true
            return true
        }

        override fun onQueryTextChange(p0: String?): Boolean {
            return false
        }

    }

    private var run = Runnable {
        UtilApp.showToast("请求超时")
        searchKeyAdapter.loadMoreFail()
    }
}
