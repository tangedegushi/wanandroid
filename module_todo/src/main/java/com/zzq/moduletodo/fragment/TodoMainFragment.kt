package com.zzq.moduletodo.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zzq.commonlib.Constants
import com.zzq.moduletodo.RvItemTouchHelper
import com.zzq.moduletodo.R
import com.zzq.moduletodo.TodoAddActivity
import com.zzq.moduletodo.adapter.TodoDetailAdapter
import com.zzq.moduletodo.bean.TodoData
import com.zzq.moduletodo.model.TodoModel
import com.zzq.netlib.utils.Logger
import kotlinx.android.synthetic.main.fragment_todo.*

/**
 * A placeholder fragment containing a simple view.
 */
class TodoMainFragment : Fragment() {

    private val mParent by lazy { parentFragment!! as TodoDelegateFragment }
    private val todoModel by lazy { ViewModelProviders.of(mParent).get(TodoModel::class.java) }
    private val todoDataList by lazy { mParent.todoDataList }
    private val todoAdapter by lazy { TodoDetailAdapter(todoDataList) }
    private var isGridLayout = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isGridLayout = arguments?.getBoolean("isGridLayout") ?: false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_todo_detail.apply {
            layoutManager = if (isGridLayout) {
                GridLayoutManager(context, 2)
            } else {
                LinearLayoutManager(context)
            }
            adapter = todoAdapter
            if (isGridLayout) {
                (layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(p0: Int): Int {
                        if (p0 >= todoDataList.size) return 2  //加载更多的时候添加了一个item，这里就是处理这个item
                        return todoAdapter.getItemViewType(p0)
                    }

                }
            }
        }
        todoAdapter.setPreLoadNumber(3)
        val rvItemTouchHelper = RvItemTouchHelper(todoDataList, todoAdapter, activity!!)
        //处理删除是时回调
        rvItemTouchHelper.swipeDeleteCallback = object : RvItemTouchHelper.SwipeDeleteCallback {
            override fun onDelete(position: Int) {
                todoModel.removeTodo(this@TodoMainFragment, todoDataList[position].id)
                deleteDataCallback.onDeleteData(position)
                todoAdapter.notifyItemRemoved(position)
            }

        }
        ItemTouchHelper(rvItemTouchHelper).attachToRecyclerView(rv_todo_detail)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        todoModel.liveTodoData.observe(this, Observer<TodoData> {
            if (todoAdapter.isLoading) {
                rv_todo_detail.removeCallbacks(run)
            }
            it?.apply {
                if (pageCount == 1) {
                    todoAdapter.setEnableLoadMore(false)
                }
                if (over) todoAdapter.loadMoreEnd() else todoAdapter.loadMoreComplete()
                notifyUI()
            }
        })
        initClickEvent()
    }

    fun notifyUI(){
        todoAdapter.notifyDataSetChanged()
    }

    private fun initClickEvent() {
        Logger.zzqLog().i("setOnItemClickListener is grid = $isGridLayout   ")
        todoAdapter.setOnItemClickListener { adapter, _, position ->
            Logger.zzqLog().i("setOnItemClickListener is grid = $isGridLayout   position = $position")
            if (adapter.getItemViewType(position) == TodoDetailAdapter.TODO_CONTENT) {
                val todoItemData = adapter.data[position] as TodoData.DatasBean
                TodoAddActivity.open(mParent, 100, todoItemData)
            }
        }
        todoAdapter.setOnLoadMoreListener(loadMoreListener, rv_todo_detail)
    }

    private var loadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        if (!mParent.hasData) return@RequestLoadMoreListener
        val todoData = todoModel.liveTodoData.value
        todoData?.apply {
            if (total > pageCount) {
                pageCount++
                todoModel.getTODO(this@TodoMainFragment, pageCount,status = mParent.curStatus, type = mParent.curType, orderby = mParent.curOrder)
                rv_todo_detail.postDelayed(run, Constants.REQUEST_TIME_OUT)
            } else {
                todoAdapter.loadMoreEnd()
            }
        }
    }

    private var run = Runnable { todoAdapter.loadMoreFail() }

    override fun onDestroy() {
        super.onDestroy()
        rv_todo_detail?.removeCallbacks(run)
    }

    companion object {
        fun newInstance(isGridLayout: Boolean): TodoMainFragment {
            val fragment = TodoMainFragment()
            val args = Bundle()
            args.putBoolean("isGridLayout", isGridLayout)
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var deleteDataCallback: DeleteDataCallback
    interface DeleteDataCallback {
        fun onDeleteData(position: Int)
    }
}
