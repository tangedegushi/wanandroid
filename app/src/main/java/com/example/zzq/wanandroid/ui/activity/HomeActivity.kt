package com.example.zzq.wanandroid.ui.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.zzq.wanandroid.R
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zzq.commonlib.Constants
import com.zzq.commonlib.bar.UltimateBar
import com.zzq.commonlib.router.MyArouter
import com.zzq.commonlib.utils.UtilPermission
import com.zzq.netlib.utils.Logger
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_home.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var currentIndex: Int = 0
    private val rxPermission by lazy {
        RxPermissions(this)
    }
    private val homeFragment by lazy { MyArouter.getFragment(Constants.HOME_PAGE_COMPONENT) }
    private val treeFragment by lazy { MyArouter.getFragment(Constants.TREE_COMPONENT) }
    private val naviFragment by lazy { MyArouter.getFragment(Constants.NAVI_COMPONENT) }
    private val todoFragment by lazy { MyArouter.getFragment(Constants.TODO_COMPONENT) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initStatusBar()
        initToolbarAndNai()

        supportFragmentManager.beginTransaction().add(R.id.content_fragment, homeFragment, Constants.HOME_PAGE_COMPONENT).commit()
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
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

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
