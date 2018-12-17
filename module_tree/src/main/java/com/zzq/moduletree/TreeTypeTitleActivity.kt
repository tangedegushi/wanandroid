package com.zzq.moduletree

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import com.zzq.commonlib.base.BaseActivity
import com.zzq.moduletree.bean.TreeData
import com.zzq.moduletree.adapter.TreeDetailContentMoreVPAdapter
import com.zzq.moduletree.adapter.TreeDetailContentVPAdapter
import kotlinx.android.synthetic.main.activity_tree_type_title.*

class TreeTypeTitleActivity : BaseActivity() {

    override val useBar: Boolean = true
    override val homeUp: Boolean = true
    private lateinit var treeTitle: String
    private lateinit var treeData: TreeData
    private lateinit var vpAdapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tree_type_title)

        intent.extras.apply {
            treeData = getSerializable(TREE_DATA) as TreeData
            treeTitle = treeData.name
            vpAdapter = treeData.children!!.run {
                //数量多使用FragmentStatePagerAdapter，数量少使用FragmentPagerAdapter
                if (size() > 5) {
                    TreeDetailContentMoreVPAdapter(this,supportFragmentManager)
                } else {
                    TreeDetailContentVPAdapter(this,supportFragmentManager)
                }
            }
        }
        initTabLayout()
    }

    private fun initTabLayout() {
        tl_tree_detail.setupWithViewPager(vp_tree_detail_content)
        vp_tree_detail_content.apply { adapter = vpAdapter }
    }

    override fun onResume() {
        super.onResume()
        toolbar?.title = treeTitle
    }

    companion object {
        const val TREE_DATA = "childData"
        fun open(activity: Activity, treeData: TreeData) {
            val intent = Intent(activity, TreeTypeTitleActivity::class.java)
            intent.putExtra(TREE_DATA,treeData)
            activity.startActivity(intent)
        }
    }
}
