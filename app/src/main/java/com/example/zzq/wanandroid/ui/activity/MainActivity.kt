package com.example.zzq.wanandroid.ui.activity

import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.zzq.wanandroid.R
import com.zzq.commonlib.bar.UltimateBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textview.setOnClickListener { _ ->
            textview.pivotX = 0f
            textview.scaleX = 0.8f
        }
        UltimateBar.with(this).statusDrawable(ColorDrawable(resources.getColor(R.color.colorPrimary)))
                .applyNavigation(true)
                .create()
                .drawableBar()
    }
}
