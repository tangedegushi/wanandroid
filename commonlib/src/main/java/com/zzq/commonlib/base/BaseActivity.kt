package com.zzq.commonlib.base

import android.support.v7.app.AppCompatActivity
import com.shehuan.nicedialog.BaseNiceDialog
import com.shehuan.nicedialog.NiceDialog
import com.zzq.commonlib.R

/**
 *@auther tangedegushi
 *@creat 2018/11/30
 *@Decribe
 */
open class BaseActivity:AppCompatActivity() {

    private lateinit var niceDialog: BaseNiceDialog

    fun showLoadingDialog(){
        niceDialog = NiceDialog.init().
                setLayoutId(R.layout.common_loading)
                .setWidth(80)
                .setHeight(80)
                .setOutCancel(false)
                .show(supportFragmentManager)
    }

    fun hideLoadingDialog() {
        if (niceDialog.dialog.isShowing) {
            niceDialog.dismiss()
        }
    }


}