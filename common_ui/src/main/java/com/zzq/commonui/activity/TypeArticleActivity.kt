package com.zzq.commonui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.zzq.commonlib.base.BaseActivity
import com.zzq.commonui.R
import com.zzq.commonui.fragment.TypeArticleFragment

class TypeArticleActivity : BaseActivity() {

    override val useBar: Boolean = true
    override val homeUp: Boolean = true
    private lateinit var typeTitle: String
    private var typeCid: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_article)
        intent.apply {
            typeTitle = getStringExtra(TYPE_TITLE)?:""
            typeCid = getIntExtra(TYPE_CID,0)
        }
        supportFragmentManager.beginTransaction().replace(R.id.fl_content_detail,TypeArticleFragment.newInstance(typeCid)).commit()
    }

    override fun onResume() {
        super.onResume()
        toolbar?.title = typeTitle
    }

    companion object {
        const val TYPE_TITLE = "typeTitle"
        const val TYPE_CID = "typecid"
        fun open(context: Context, title: String?, cid: Int) {
            val intent = Intent(context, TypeArticleActivity::class.java)
            intent.putExtra(TYPE_TITLE, title)
            intent.putExtra(TYPE_CID, cid)
            context.startActivity(intent)
        }
    }
}
