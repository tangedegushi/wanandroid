package com.zzq.moduletodo.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.zzq.moduletodo.R
import com.zzq.moduletodo.bean.TodoData

/**
 *@auther tangedegushi
 *@creat 2018/12/17
 *@Decribe
 */
class TodoTitleProvide : BaseItemProvider<TodoData.DatasBean, BaseViewHolder>() {
    override fun layout(): Int {
        return R.layout.todo_type_title_item
    }

    override fun viewType(): Int {
        return TodoDetailAdapter.TODO_TITLE
    }

    override fun convert(helper: BaseViewHolder?, data: TodoData.DatasBean?, position: Int) {
        helper?.apply {
            setText(R.id.tv_todo_title, data?.customeTimeTitle)
        }
    }
}