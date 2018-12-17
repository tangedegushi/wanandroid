package com.example.zzq.wanandroid.ui.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.example.zzq.loginmodule.model.LoginModel
import com.example.zzq.wanandroid.R
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zzq.commonlib.Constants
import com.zzq.commonlib.bar.UltimateBar
import com.zzq.commonlib.router.MyArouter
import com.zzq.commonlib.utils.UtilPermission
import com.zzq.netlib.utils.Logger
import com.zzq.netlib.utils.UtilSp
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_home.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var currentIndex: Int = 0
    private val rxPermission by lazy { RxPermissions(this) }
    private val homeFragment by lazy { MyArouter.getFragment(Constants.HOME_PAGE_COMPONENT) }
    private val treeFragment by lazy { MyArouter.getFragment(Constants.TREE_COMPONENT) }
    private val naviFragment by lazy { MyArouter.getFragment(Constants.NAVI_COMPONENT) }
    private val todoFragment by lazy { MyArouter.getFragment(Constants.TODO_COMPONENT) }

    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initStatusBar()
        initToolbarAndNai()

        var homeFragment1 = supportFragmentManager.findFragmentByTag(Constants.HOME_PAGE_COMPONENT)
        homeFragment1?.apply {
            supportFragmentManager.beginTransaction().show(homeFragment1).commit()
        } ?: apply {
            supportFragmentManager.beginTransaction().add(R.id.content_fragment, homeFragment, Constants.HOME_PAGE_COMPONENT).commit()
        }
        currentIndex = R.id.navigation_home

        //请求sd卡权限
        UtilPermission.permissionStorage(UtilPermission.defaultRequestPermission,rxPermission)

    }

    private fun initToolbarAndNai() {
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

        setSupportActionBar(toolbar)

        nav_view.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        bottomNavigation.run {
            onNavigationItemSelectedListener = navigationBottomItemSelectListener
            labelVisibilityMode = 1
            currentIndex = R.id.navigation_home
        }
    }

    private fun initStatusBar() {
        UltimateBar.with(this).statusDrawable(ColorDrawable(resources.getColor(R.color.colorPrimary)))
                .applyNavigation(true)
                .create()
                .drawableBarDrawer(drawer_layout, content, nav_view)
        toolbar.setBackgroundColor(resources.getColor(R.color.colorPrimary))
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        searchView = menu.findItem(com.zzq.commonui.R.id.menuSearch).actionView as SearchView
        searchView?.setOnQueryTextListener(serchViewListener)
        return true
    }

    private var serchViewListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(searchKey: String?): Boolean {
            searchKey?.apply {
                MyArouter.getArouter().build(Constants.SEARCH_ACTIVITY_COMPONENT)
                        .withString(Constants.SEARCH_ACTIVITY_KEY,searchKey)
                        .navigation()
            }
            searchView?.clearFocus()
            searchView?.setQuery("",false)
            searchView?.isIconified = true
            return true
        }

        override fun onQueryTextChange(p0: String?): Boolean {
            return false
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_chapter_save -> {
                if (UtilSp.hadLogin()) {
                    MyArouter.openActivity(Constants.COLLECT_ACTIVITY_COMPONENT)
                } else {
                    MyArouter.openActivity(Constants.LOGIN_COMPONENT)
                }
            }
            R.id.nav_login_out -> {
                LoginModel(this).loginOut()
                UtilSp.clearLoginData()
                finish()
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private val navigationBottomItemSelectListener = BottomNavigationView.OnNavigationItemSelectedListener {
        return@OnNavigationItemSelectedListener when (it.itemId) {
            R.id.navigation_home -> {
                if (currentIndex == R.id.navigation_home) return@OnNavigationItemSelectedListener false
                dealFragment(Constants.HOME_PAGE_COMPONENT)
                currentIndex = R.id.navigation_home
                true
            }
            R.id.navigation_tree -> {
                if (currentIndex == R.id.navigation_tree) return@OnNavigationItemSelectedListener false
                dealFragment(Constants.TREE_COMPONENT)
                currentIndex = R.id.navigation_tree
                true
            }
            R.id.navigation_navi -> {
                if (currentIndex == R.id.navigation_navi) return@OnNavigationItemSelectedListener false
                dealFragment(Constants.NAVI_COMPONENT)
                currentIndex = R.id.navigation_navi
                true
            }
            R.id.navigation_todo -> {
                if (currentIndex == R.id.navigation_todo) return@OnNavigationItemSelectedListener false
                dealFragment(Constants.TODO_COMPONENT)
                currentIndex = R.id.navigation_todo
                true
            }
            else -> {
                false
            }
        }
    }

    private fun dealFragment(tag: String) {
        Logger.zzqLog().d("current index = ${currentIndex}")
        val tempFragment = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.beginTransaction().apply {
            when (currentIndex) {
                R.id.navigation_home -> hide(homeFragment)
                R.id.navigation_tree -> hide(treeFragment)
                R.id.navigation_navi -> hide(naviFragment)
                R.id.navigation_todo -> hide(todoFragment)
            }
            if (tempFragment == null) {
                when(tag){
                    Constants.TREE_COMPONENT -> add(R.id.content_fragment,treeFragment,Constants.TREE_COMPONENT)
                    Constants.NAVI_COMPONENT -> add(R.id.content_fragment,naviFragment,Constants.NAVI_COMPONENT)
                    Constants.TODO_COMPONENT -> add(R.id.content_fragment,todoFragment,Constants.TODO_COMPONENT)
                }
            } else {
                when(tag){
                    Constants.HOME_PAGE_COMPONENT -> show(homeFragment)
                    Constants.TREE_COMPONENT -> show(treeFragment)
                    Constants.NAVI_COMPONENT -> show(naviFragment)
                    Constants.TODO_COMPONENT -> show(todoFragment)
                }
            }
            commit()
        }
    }
}
