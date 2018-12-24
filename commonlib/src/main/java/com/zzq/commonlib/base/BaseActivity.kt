package com.zzq.commonlib.base

import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.zzq.commonlib.R
import com.zzq.commonlib.bar.UltimateBar
import com.zzq.commonlib.utils.CommonDialogUtil
import com.zzq.commonlib.view.dialog.EasyDialog

/**
 *@auther tangedegushi
 *@creat 2018/11/30
 *@Decribe
 */
open class BaseActivity : AppCompatActivity() {

    private var niceDialog: EasyDialog? = null
    protected var toolbar: Toolbar? = null

    fun showLoadingDialog() {
        if (niceDialog?.dialog?.isShowing == true) return
        niceDialog = CommonDialogUtil.showLoadingDialog(supportFragmentManager)
    }

    fun hideLoadingDialog() {
        if (niceDialog?.dialog?.isShowing == true) {
            niceDialog?.dismiss()
            niceDialog = null
        }
    }

    override fun onContentChanged() {
        if (useBar) {
            toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)
            if (homeUp) supportActionBar?.setDisplayHomeAsUpEnabled(true)
            initBar()
        }
    }

    private fun initBar() {
        UltimateBar.with(this)
                .statusDrawable(ColorDrawable(resources.getColor(R.color.colorPrimary)))
                .applyNavigation(true)
                .create()
                .drawableBar()
        toolbar?.setBackgroundColor(resources.getColor(R.color.colorPrimary))
    }

    open val useBar: Boolean = false
    open val homeUp: Boolean = false

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish();true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        hideLoadingDialog()
    }

}