package com.zzq.modulehomepage.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebSettingsImpl
import com.just.agentweb.IAgentWebSettings
import com.zzq.commonlib.bar.UltimateBar
import com.zzq.modulehomepage.R

import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {

    private lateinit var agentWeb: AgentWeb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        initStatusBar()
        toolbar.run {
            setSupportActionBar(this)
            supportActionBar?.run{
                setDisplayHomeAsUpEnabled(true)
            }
        }
        intent.run {
            val contentUrl = getStringExtra(CONTENT_URL)
            agentWeb = AgentWeb.with(this@WebActivity)
                    .setAgentWebParent(fl_content, ViewGroup.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .setAgentWebWebSettings(MyAgentWebSettingsImpl())
                    .setWebChromeClient(object: WebChromeClient(){
                        override fun onReceivedTitle(view: WebView?, title: String?) {
                            toolbar.title = title
                        }
                    })
                    .createAgentWeb()
                    .ready()
                    .go(contentUrl)
        }
    }

    //页面显示适配屏幕
    class MyAgentWebSettingsImpl:AgentWebSettingsImpl(){
        override fun toSetting(webView: WebView?): IAgentWebSettings<*> {
            val iAgentWebSettings = super.toSetting(webView)
            webView?.settings?.run {
                builtInZoomControls = true
                displayZoomControls = false
                setSupportZoom(true)
                loadWithOverviewMode = true
                useWideViewPort = true
            }
            return iAgentWebSettings
        }
    }

    private fun initStatusBar() {
        UltimateBar.with(this)
                .statusDrawable(ColorDrawable(resources.getColor(R.color.colorPrimary)))
                .applyNavigation(true)
                .create()
                .drawableBar()
        toolbar.setBackgroundColor(resources.getColor(R.color.colorPrimary))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (agentWeb.handleKeyEvent(keyCode, event)) {
            true
        } else {
            finish()
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> { finish();true }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onResume() {
        agentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onPause() {
        agentWeb.webLifeCycle.onPause()
        super.onPause()

    }

    override fun onDestroy() {
        agentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }

    companion object {
        const val CONTENT_URL = "content_url"
    }

}
