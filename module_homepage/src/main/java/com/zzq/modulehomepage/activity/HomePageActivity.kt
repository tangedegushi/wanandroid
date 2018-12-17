package com.zzq.modulehomepage.activity

import android.os.Bundle
import com.zzq.commonlib.base.BaseActivity
import com.zzq.modulehomepage.R

class HomePageActivity : BaseActivity() {

    override val homeUp: Boolean = true
    override val useBar: Boolean  = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
    }
}
