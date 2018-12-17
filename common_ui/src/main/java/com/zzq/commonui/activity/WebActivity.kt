package com.zzq.commonui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebSettingsImpl
import com.just.agentweb.IAgentWebSettings
import com.zzq.commonlib.base.BaseActivity
import com.zzq.commonui.model.CommonModel
import com.zzq.commonui.R
import com.zzq.netlib.utils.Logger

import kotlinx.android.synthetic.main.activity_web.*
import java.util.*

class WebActivity : BaseActivity() {

    private lateinit var agentWeb: AgentWeb
    override val useBar: Boolean = true
    override val homeUp: Boolean = true
    private var contentTitle: String? = null
    private var contentId: Int = -1
    private lateinit var contentAuthor: String
    private val commonModel by lazy { CommonModel(this) }
    private val urlList: LinkedList<String> = LinkedList()
    private val articleTitleList: LinkedList<String> = LinkedList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        intent.run {
            urlList.add(getStringExtra(CONTENT_URL))
            contentTitle = getStringExtra(CONTENT_TITLE)
            contentId = getIntExtra(CONTENT_ID, -1)
            contentAuthor = getStringExtra(CONTENT_AUTHOR)
            agentWeb = AgentWeb.with(this@WebActivity)
                    .setAgentWebParent(fl_content, ViewGroup.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .setAgentWebWebSettings(MyAgentWebSettingsImpl())
                    .setWebChromeClient(object : WebChromeClient() {
                        override fun onReceivedTitle(view: WebView?, title: String?) {
                            Logger.zzqLog().i("on receiver title ============ $title  ${TextUtils.isEmpty(contentTitle)}")
                            articleTitleList.add(title?:"")
                            if (TextUtils.isEmpty(contentTitle)) toolbar?.title = title
                        }
                    })
                    .interceptUnkownUrl()
                    .setWebViewClient(object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            url?.apply { urlList.add(url) }
                            super.onPageStarted(view, url, favicon)
                        }
                    })
                    .createAgentWeb()
                    .ready()
                    .go(urlList.last)
        }
    }

    //页面显示适配屏幕
    class MyAgentWebSettingsImpl : AgentWebSettingsImpl() {
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (agentWeb.handleKeyEvent(keyCode, event)) {
            urlList.removeLast()
            true
        } else {
            finish()
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(this, "this is click setting", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.action_collect -> {
                if (contentId != -1) {
                    commonModel.collectArticle(contentId)
                } else {
                    commonModel.collectArticle(articleTitleList.last ?: "", contentAuthor, urlList.last)
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        if (!TextUtils.isEmpty(contentTitle)) toolbar?.title = contentTitle
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
        //toolbar的title
        const val CONTENT_TITLE = "content_title"
        const val CONTENT_ID = "content_id"
        const val CONTENT_AUTHOR = "content_author"
        fun open(activity: Activity, url: String, title: String? = null, id: Int = -1, author: String = "") {
            val intent = Intent(activity, WebActivity::class.java)
            intent.putExtra(WebActivity.CONTENT_URL, url)
            intent.putExtra(WebActivity.CONTENT_TITLE, title)
            intent.putExtra(WebActivity.CONTENT_ID, id)
            intent.putExtra(WebActivity.CONTENT_AUTHOR, author)
            activity.startActivity(intent)
        }
    }

}
