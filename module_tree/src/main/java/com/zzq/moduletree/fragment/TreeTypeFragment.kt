package com.zzq.moduletree.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.zzq.commonlib.Constants
import com.zzq.commonlib.base.BaseActivity
import com.zzq.commonlib.view.RecyclerViewDivider
import com.zzq.moduletree.R
import com.zzq.moduletree.adapter.TreeTypeAdapter
import com.zzq.moduletree.model.TreeModel
import com.zzq.netlib.utils.Logger
import com.zzq.netlib.utils.UtilApp
import kotlinx.android.synthetic.main.tree_fragment.*

/**
 *@auther tangedegushi
 *@creat 2018/12/7
 *@Decribe
 */
@Route(path = Constants.TREE_COMPONENT)
class TreeTypeFragment : Fragment() {

    private val treeTypeTitleList = mutableListOf<String>()
    private val treeModel by lazy { ViewModelProviders.of(this).get(TreeModel::class.java) }
    private val treeTypeAdapter by lazy { TreeTypeAdapter(treeTypeTitleList) }
    private val linearLayoutManager by lazy { LinearLayoutManager(context) }
    private val treeDetailFragment by lazy { TreeDetailFragment() }
    private var baseActivity: BaseActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        treeModel.getCacheTreeData(this)
        if (activity is BaseActivity) {
            baseActivity = activity as BaseActivity
            baseActivity?.showLoadingDialog()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tree_fragment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction().replace(R.id.fl_tree_detail, treeDetailFragment).commit()
        val rvTreeType = view.findViewById<RecyclerView>(R.id.rv_tree_type)
        rvTreeType.apply {
            layoutManager = linearLayoutManager
            addItemDecoration(RecyclerViewDivider(context, RecyclerViewDivider.VERTICAL))
            adapter = treeTypeAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        treeModel.liveTreeData.observe(this, Observer {
            baseActivity?.hideLoadingDialog()
            if (it != null) {
                for (item in it) {
                    treeTypeTitleList.add(item.name)
                }
                treeTypeAdapter.notifyDataSetChanged()
            }
        })
        treeModel.liveTitleData.observe(this, Observer {
            val title = it
            val index = treeTypeTitleList.indexOfFirst { it == title }
            if (index != -1) dealItemClick(index, false)
        })
        treeModel.liveErrorData.observe(this, Observer {
            baseActivity?.hideLoadingDialog()
            UtilApp.showToast(it!!)
        })
        treeTypeAdapter.setOnItemChildClickListener { adapter, view, position ->
            dealItemClick(position, true)
        }
    }

    private fun dealItemClick(position: Int, isLeft: Boolean) {
        //处理点击时背景色改变
        treeTypeAdapter.selectTypeString = treeTypeTitleList[position]
        if (isLeft) {
            treeTypeAdapter.notifyItemChanged(treeTypeAdapter.lastPosition)
            treeTypeAdapter.notifyItemChanged(position)
            //处理点击时右侧列表对应的title处于顶部
            val list = treeModel.liveTreeData.value
            var count = 0
            for (index in 0 until position) {
                list?.apply {
                    count += list[index].children?.size ?: 0
                }
            }
            count += position
            setCheck(count, isLeft)
        } else {
            setCheck(position, isLeft)
            treeTypeAdapter.notifyDataSetChanged()
        }
        treeTypeAdapter.lastPosition = position
    }

    private fun setCheck(position: Int, isLeft: Boolean) {
        if (isLeft) {
            treeDetailFragment.setSelectPosition(position)
        } else {
            moveToCenter(position)
        }
    }

    private fun moveToCenter(position: Int) {
        val itemView = rv_tree_type.getChildAt(position - linearLayoutManager.findFirstVisibleItemPosition())
        itemView?.apply {
            rv_tree_type.smoothScrollBy(0, top - rv_tree_type.height / 2)
        }
    }

}