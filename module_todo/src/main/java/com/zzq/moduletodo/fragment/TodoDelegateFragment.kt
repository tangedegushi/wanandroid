package com.zzq.moduletodo.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.PopupMenu
import android.view.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.zzq.commonlib.Constants
import com.zzq.commonlib.router.MyArouter
import com.zzq.commonlib.utils.CommonDialogUtil
import com.zzq.commonlib.view.dialog.EasyDialog
import com.zzq.moduletodo.R
import com.zzq.moduletodo.TodoAddActivity
import com.zzq.moduletodo.bean.TodoData
import com.zzq.moduletodo.model.TodoModel
import com.zzq.netlib.utils.Logger
import com.zzq.netlib.utils.UtilSp
import kotlinx.android.synthetic.main.todo_delegate_fragment.*

/**
 *@auther tangedegushi
 *@creat 2018/12/18
 *@Decribe
 */
@Route(path = Constants.TODO_COMPONENT)
class TodoDelegateFragment : Fragment() {

    private var page = 0
    private var isGridLayout = false
    private val linearLayoutFragment by lazy { TodoMainFragment.newInstance(false) }
    private val gridLayoutFragment by lazy { TodoMainFragment.newInstance(true) }
    private val todoModel by lazy { ViewModelProviders.of(this).get(TodoModel::class.java) }
    //放在这里是为了删除记录时两个列表能同步数据
    val todoDataList = mutableListOf<TodoData.DatasBean>()
    var curType = 0
    var curStatus = -1
    var curOrder = 4
    private var easyDialog: EasyDialog? = null
    private var currentRemoveItemdata: TodoData.DatasBean? = null
    var hasData = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        linearLayoutFragment.deleteDataCallback = deteleCallback
        gridLayoutFragment.deleteDataCallback = deteleCallback
        todoModel.liveTodoData.observe(this, Observer<TodoData> {
            hasData = true
            tv_filter.removeCallbacks(run)
            destroyDialog()
            it?.apply {
                if (curPage == 0) {
                    todoDataList.clear()
                }
                todoDataList.addAll(datas)
            }
            Logger.zzqLog().d("todo delegate todoDataList size = ${todoDataList.size}")
        })
        todoModel.liveRemoveData.observe(this, Observer {
            if (it != null && !it && currentRemoveItemdata != null) {
                //删除数据失败
                todoDataList.add(currentRemovePosition, currentRemoveItemdata!!)
                currentRemoveItemdata = null
            } else {
                //处理item是title时，下面没有子item，那么就去掉这个item
                if (todoDataList[currentRemovePosition - 1].isTitleType && todoDataList[currentRemovePosition].isTitleType) {
                    todoDataList.removeAt(currentRemovePosition - 1)
                    if (isGridLayout) {
                        gridLayoutFragment.notifyUI()
                    } else {
                        linearLayoutFragment.notifyUI()
                    }
                }
            }
        })
    }

    private var deteleCallback = object : TodoMainFragment.DeleteDataCallback {
        override fun onDeleteData(position: Int) {
            currentRemovePosition = position
            currentRemoveItemdata = todoDataList[position]
            todoDataList.removeAt(position)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.todo_delegate_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_filter.postDelayed(run,Constants.REQUEST_TIME_OUT)
        tv_filter.setOnClickListener { showFilterPopMenu() }
        tv_todo_sort.setOnClickListener { showSortPopMenu() }
        btn_goto_login.setOnClickListener { MyArouter.openActivity(Constants.LOGIN_COMPONENT) }
        childFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.rv_enter_transition, R.anim.rv_exit_transition)
                .add(R.id.fl_delegate_content, gridLayoutFragment)
                .add(R.id.fl_delegate_content, linearLayoutFragment)
                .hide(gridLayoutFragment)
                .show(linearLayoutFragment)
                .commit()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tv_change.setOnClickListener { _ ->
            if (isGridLayout) {
                isGridLayout = false
                childFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.rv_enter_transition, R.anim.rv_exit_transition)
                        .hide(gridLayoutFragment)
                        .show(linearLayoutFragment)
                        .commit()
            } else {
                isGridLayout = true
                childFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.rv_enter_transition, R.anim.rv_exit_transition)
                        .hide(linearLayoutFragment)
                        .show(gridLayoutFragment)
                        .commit()
            }
        }

        fab.setOnClickListener { _ ->
            TodoAddActivity.open(this@TodoDelegateFragment, ADD_REQUEST_CODE)
        }
    }

    private val ADD_REQUEST_CODE = 100
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_REQUEST_CODE && resultCode == TodoAddActivity.ADD_SUCCESS) {
            page = 0
            todoModel.getTODO(this, page)
        }
    }

    private fun showFilterPopMenu() {
        val popMenu = PopupMenu(context!!, tv_filter)
        popMenu.setOnMenuItemClickListener(menuPopmenuListener)
        popMenu.inflate(R.menu.menu_todo_filter)
        popMenu.show()
    }

    private fun showSortPopMenu() {
        val popMenu = PopupMenu(context!!, tv_todo_sort)
        popMenu.setOnMenuItemClickListener(menuPopmenuListener)
        popMenu.inflate(R.menu.menu_todo_sort)
        popMenu.show()
    }

    private val menuPopmenuListener = PopupMenu.OnMenuItemClickListener { menu ->
        menu.run {
            when (itemId) {
                R.id.todo_menu_all -> {
                    curType = 0
                    tv_filter.text = "过滤（默认）"
                }
                R.id.todo_menu_work -> {
                    curType = 1
                    tv_filter.text = "过滤（工作）"
                }
                R.id.todo_menu_study -> {
                    curType = 2
                    tv_filter.text = "过滤（学习）"
                }
                R.id.todo_menu_life -> {
                    curType = 3
                    tv_filter.text = "过滤（生活）"
                }
                R.id.todo_menu_complete -> curStatus = 1
                R.id.todo_menu_complete_no -> curStatus = 0
                R.id.todo_menu_complete_or_no -> curStatus = -1
                R.id.todo_menu_complete_l -> curOrder = 1
                R.id.todo_menu_complete_r -> curOrder = 2
                R.id.todo_menu_create -> curOrder = 3
                R.id.todo_menu_create_r -> curOrder = 4
            }
        }
        page = 0
        todoModel.getTODO(this@TodoDelegateFragment, page, status = curStatus, type = curType, orderby = curOrder)
        return@OnMenuItemClickListener true
    }

    private var run = Runnable {
        destroyDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        tv_filter?.removeCallbacks(run)
        destroyDialog()
    }

    private fun destroyDialog(){
        easyDialog?.apply {
            if (dialog?.isShowing == true) {
                dismiss()
            }
        }
        easyDialog = null
    }

    companion object {
        var currentRemovePosition = 0
    }

    override fun onStart() {
        super.onStart()
        Logger.zzqLog().d("onStart-----------------------")
    }

    override fun onResume() {
        super.onResume()
        Logger.zzqLog().d("onResume-----------------------")
        if (UtilSp.hadLogin()) {
            ll_no_login.visibility = View.GONE
            if (!hasData) {
                todoModel.getTODO(this,page)
                easyDialog = CommonDialogUtil.showLoadingDialog(supportFragmentManager = fragmentManager!!)
            }
        } else {
            ll_no_login.visibility = View.VISIBLE
        }

    }

    override fun onPause() {
        super.onPause()
        Logger.zzqLog().d("onPause-----------------------")
    }

    override fun onStop() {
        super.onStop()
        Logger.zzqLog().d("onStop-----------------------")
    }


}