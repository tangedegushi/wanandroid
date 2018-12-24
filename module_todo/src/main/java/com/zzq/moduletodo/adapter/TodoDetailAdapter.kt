package com.zzq.moduletodo.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.MultipleItemRvAdapter
import com.zzq.moduletodo.bean.TodoData

/**
 *@auther tangedegushi
 *@creat 2018/12/17
 *@Decribe
 */
class TodoDetailAdapter(data:List<TodoData.DatasBean>): MultipleItemRvAdapter<TodoData.DatasBean,BaseViewHolder>(data) {

    init {
        finishInitialize()
    }

    override fun registerItemProvider() {
        mProviderDelegate.registerProvider(TodoTitleProvide())
        mProviderDelegate.registerProvider(TodoContentProvide())
    }

    override fun getViewType(t: TodoData.DatasBean?): Int {
        return  if (t!!.isTitleType) TODO_TITLE else TODO_CONTENT
    }

    companion object {
        const val TODO_TITLE = 2
        const val TODO_CONTENT = 1
    }

}