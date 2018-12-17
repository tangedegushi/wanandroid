package com.zzq.moduletree

import android.os.Bundle
import com.zzq.commonlib.base.BaseActivity

class TreeActivity : BaseActivity() {
    override val useBar = true
    override val homeUp = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tree)
    }


}
