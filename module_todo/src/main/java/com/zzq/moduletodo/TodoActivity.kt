package com.zzq.moduletodo

import android.os.Bundle
import com.zzq.commonlib.base.BaseActivity

class TodoActivity : BaseActivity() {
    override val useBar: Boolean = true
    override val homeUp: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)
    }

}
