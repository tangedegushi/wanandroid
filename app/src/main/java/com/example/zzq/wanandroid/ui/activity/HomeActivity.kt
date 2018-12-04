package com.example.zzq.wanandroid.ui.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.zzq.wanandroid.R
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zzq.commonlib.bar.UltimateBar
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
    private val homeFragment by lazy {
//        HomeFragment()
//        Fragment.instantiate(this,"")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initStatusBar()
        initToolbarAndNai()

//        supportFragmentManager.beginTransaction().replace(R.id.content_fragment,homeFragment).show(homeFragment).commit()

        //请求sd卡权限
        UtilPermission.permissionStorage(UtilPermission.defaultRequestPermission,rxPermission)

    }

    private fun initToolbarAndNai() {
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        setSupportActionBar(toolbar)

        nav_view.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        bottomNavigation.run {
            setOnNavigationItemSelectedListener(navigationBottomItemSelectListener)
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
        // Inflate the menu; this adds items to the action bar if it is present.
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
                if (currentIndex == R.id.navigation_home) {

                }
                currentIndex = R.id.navigation_home
                Logger.zzqLog().d("click home")
                true
            }

            R.id.navigation_sys -> {
                if (currentIndex == R.id.navigation_sys) {

                }
                currentIndex = R.id.navigation_sys
                Logger.zzqLog().d("click sys")
                true
            }
            else -> {
                false
            }
        }
    }
}
