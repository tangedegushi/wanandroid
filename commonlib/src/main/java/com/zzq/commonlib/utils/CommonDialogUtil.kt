package com.zzq.commonlib.utils

import android.support.v4.app.FragmentManager
import android.view.View
import com.zzq.commonlib.R
import com.zzq.commonlib.view.dialog.EasyDialog
import com.zzq.commonlib.view.dialog.ViewHolder
import com.zzq.commonlib.view.dialog.ViewListener

/**
 *@auther tangedegushi
 *@creat 2018/12/20
 *@Decribe
 */
object CommonDialogUtil {

    fun showLoadingDialog(supportFragmentManager: FragmentManager): EasyDialog {
        return EasyDialog.Builder()
                .setLayoutId(R.layout.common_loading)
                .setWidth(80)
                .setHeight(80)
                .setOutCancelable(false)
                .show(supportFragmentManager)

    }

    fun showConfirmDialog(message: String, supportFragmentManager: FragmentManager, callback: ConfirmDialogCallback): EasyDialog {
        return EasyDialog.Builder().setLayoutId(R.layout.common_confirm_layout)
                .setViewLisenter(object : ViewListener() {
                    override fun convert(helper: ViewHolder, dialog: EasyDialog) {
                        helper.apply {
                            setText(R.id.tv_confirm_message,message)
                            setOnClickListener(R.id.tv_common_confirm_cancel, View.OnClickListener {
                                callback.cancel(it)
                                dialog.dismiss()
                            })
                            setOnClickListener(R.id.tv_common_confirm_ok,View.OnClickListener{
                                callback.confirm(it)
                                dialog.dismiss()
                            })
                        }

                    }
                })
                .setMargin(60)
                .setOutCancelable(false)
                .show(supportFragmentManager)
    }

    interface ConfirmDialogCallback {
        fun cancel(view: View)
        fun confirm(view: View)
    }

}