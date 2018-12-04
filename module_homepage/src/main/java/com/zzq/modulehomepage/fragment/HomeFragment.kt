package com.zzq.modulehomepage.fragment

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener
import com.zzq.modulehomepage.bean.*
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.zzq.commonlib.view.RecyclerViewSpace
import com.zzq.modulehomepage.R
import com.zzq.modulehomepage.activity.WebActivity
import com.zzq.modulehomepage.adapter.HomeAdapter
import com.zzq.modulehomepage.image.GlideImageLoad
import com.zzq.modulehomepage.model.HomeModel

/**
 *@auther tangedegushi
 *@creat 2018/11/9
 *@Decribe
 */
class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var homeView: View? = null
    private var banner: Banner? = null
    private var headView: View? = null
    private var articleIndex: Int = 0
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var homeData: MutableList<HomeData> = ArrayList()

    private val homeAdapter: HomeAdapter<HomeData> by lazy { HomeAdapter(activity!!, homeData) }
    private val homeModel: HomeModel by lazy { HomeModel(this) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeView ?: let {
            homeView = inflater.inflate(R.layout.fragment_home, null)
            headView = inflater.inflate(R.layout.banner_main, null)
            banner = headView?.findViewById(R.id.banner) as Banner
        }

        initUI()

        homeModel.requestBanner()
        homeModel.requestHotKey()
        homeModel.requestFriend()
//        homeModel.requestArticle(articleIndex)

        val homeRecyclerView = homeView!!.findViewById<RecyclerView>(R.id.home_recyclerView)
        homeAdapter.run {
            addHeaderView(headView)
            setOnLoadMoreListener(loadMoreListener,homeRecyclerView)
            setPreLoadNumber(3)
        }
        homeRecyclerView.run {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(RecyclerViewSpace())
            adapter = homeAdapter
        }
        swipeRefreshLayout = homeView!!.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setColorSchemeResources(R.color.swipe_refresh_color1, R.color.swipe_refresh_color2, R.color.swipe_refresh_color3)
        swipeRefreshLayout.setOnRefreshListener(this)
        return homeView
    }

    override fun onDestroy() {
        super.onDestroy()
        homeData.clear()
    }

    private fun initUI() {
        homeModel.liveBannerData.observe(this, Observer<List<BannerData>> {
            if (swipeRefreshLayout.isRefreshing) {
                swipeRefreshLayout.setRefreshing(false)
            }
            val list = mutableListOf<String?>()
            val listTitle = mutableListOf<String?>()
            it?.forEach { item ->
                list.add(item.imagePath)
                listTitle.add(item.title)
            }
            refreshBanner(list, listTitle, it)
        })

        homeModel.liveHotKeyDataData.observe(this, Observer {
            if (swipeRefreshLayout.isRefreshing) {
                swipeRefreshLayout.setRefreshing(false)
            }
            var hotKeyData = HotKeyHomeData().apply { hotKeyData = it }
            if ((homeData.size != 0) && (homeData[0] is HotKeyData)) {
                homeData[0] = hotKeyData
            } else {
                homeData.add(0, hotKeyData)
            }
        })

        homeModel.liveFriendData.observe(this, Observer {
            if (swipeRefreshLayout.isRefreshing) {
                swipeRefreshLayout.setRefreshing(false)
            }
            var friendHomeData = FriendHomeData().apply { friendData = it }
            when (homeData.size) {
                0 -> homeData.add(friendHomeData)
                1 -> if (homeData[0] is HotKeyHomeData) homeData.add(friendHomeData) else homeData.add(0, friendHomeData)
                else -> {
                    if (homeData[1] is FriendHomeData) {
                        homeData[1] = friendHomeData
                    } else {
                        homeData.add(1, friendHomeData)
                    }
                }
            }
        })

        homeModel.liveArticleData.observe(this, Observer {
            if (swipeRefreshLayout.isRefreshing) {
                swipeRefreshLayout.setRefreshing(false)
            }
            if (it != null) {
                if (it.curPage == 1) {
                    val homeData0: HomeData? = if (homeData.size >= 1) homeData[0] else null
                    val homeData1: HomeData? = if (homeData.size >= 2) homeData[1] else null
                    homeData.clear()
                    if (homeData0 != null && (homeData1 is HotKeyHomeData || homeData1 is FriendHomeData)) homeData.add(0, homeData1)
                    if (homeData1 != null && (homeData0 is HotKeyHomeData || homeData0 is FriendHomeData)) homeData.add(0, homeData0)
                    homeData.addAll(it.datas!!)
                    homeAdapter.notifyDataSetChanged()
                } else {
                    homeAdapter.addData(it.datas!!)
                }
                if (homeAdapter.isLoading) {
                    homeView?.removeCallbacks(run)
                    if (it.over) {
                        articleIndex--
                        homeAdapter.loadMoreEnd()
                    } else {
                        homeAdapter.loadMoreComplete()
                    }
                }
            }
        })
    }

    private fun refreshBanner(imgUrls: List<String?>, title: List<String?>, data: List<BannerData>?) {
        banner!!.setImages(imgUrls)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                .setBannerTitles(title)
                .setImageLoader(GlideImageLoad())
                .start()
        banner!!.setOnBannerListener {
            val intent = Intent(activity, WebActivity::class.java)
            intent.putExtra(WebActivity.CONTENT_URL, data!![it].url)
            startActivity(intent)
        }
    }

    override fun onRefresh() {
        homeModel.requestBanner()
        if (homeData[0] !is HotKeyHomeData) homeModel.requestHotKey()
        if (homeData[1] !is FriendHomeData) homeModel.requestFriend()
        articleIndex = 0
        homeModel.requestArticle(articleIndex)
    }

    private var loadMoreListener = RequestLoadMoreListener {
        homeModel.requestArticle(articleIndex++)
        homeView?.postDelayed(run,15000)
    }

    private var run = Runnable { homeAdapter.loadMoreFail() }

}