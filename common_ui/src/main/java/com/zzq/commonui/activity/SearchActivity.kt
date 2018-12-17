package com.zzq.commonui.activity

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.zzq.commonlib.base.BaseActivity
import com.zzq.commonlib.view.RecyclerViewSpace
import com.zzq.commonui.model.SearchModel
import com.zzq.commonui.R
import com.zzq.commonui.adapter.SearchKeyAdapter
import com.zzq.commonui.bean.SearchKeyData
import com.zzq.netlib.utils.Logger
import kotlinx.android.synthetic.main.fragment_home.*

class SearchActivity : BaseActivity() {
    override val useBar: Boolean = true
    override val homeUp: Boolean = true

    private var searchKeyData: MutableList<SearchKeyData.Datas> = mutableListOf()
    private val searchModel by lazy { SearchModel(this) }
    private val searchKeyAdapter by lazy { SearchKeyAdapter(this@SearchActivity, searchKeyData) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchKey = intent.getStringExtra("searchKey")

        setSupportActionBar(toolbar)
        toolbar?.title = searchKey

        initUI()

        searchModel.getSearchKeyData(0, searchKey)
        home_recyclerView.run {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            addItemDecoration(RecyclerViewSpace())
            adapter = searchKeyAdapter
        }
    }

    private fun initUI() {
        searchModel.liveSearchKeyData.observe(this@SearchActivity, Observer<SearchKeyData> {
            Logger.zzqLog().d("the data is come back????")
            searchKeyAdapter.addData(searchKeyData.size,it?.datas!!)
        })
    }

    companion object {
        fun open(context: Context, searchKey: String) {
            val intent = Intent(context, SearchActivity::class.java)
            intent.putExtra("searchKey", searchKey)
            context.startActivity(intent)
        }
    }
}
