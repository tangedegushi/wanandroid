package com.zzq.moduletodo.adapter

import android.text.TextUtils
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.zzq.moduletodo.R
import com.zzq.moduletodo.bean.TodoData

/**
 *@auther tangedegushi
 *@creat 2018/12/17
 *@Decribe
 */
class TodoContentProvide : BaseItemProvider<TodoData.DatasBean, BaseViewHolder>() {
    override fun layout(): Int {
        return R.layout.todo_type_content_item
    }

    override fun viewType(): Int {
        return TodoDetailAdapter.TODO_CONTENT
    }

    override fun convert(helper: BaseViewHolder?, data: TodoData.DatasBean?, position: Int) {
        helper?.apply {
            if (data == null) return
            val colorTitle: Int
            val colorContent: Int
            val colorItem: Int
            setGone(R.id.ll_todo_time_complete,data.status == 1)
            if (data.status == 1) {
                colorTitle = mContext.resources.getColor(R.color.todo_item_text_date)
                colorContent = mContext.resources.getColor(R.color.todo_item_text_date)
                val completeTime = data.completeDate as Double
                colorItem = if (data.date - completeTime >= 0) {
                    mContext.resources.getColor(R.color.todo_item_done)
                } else {
                    mContext.resources.getColor(R.color.todo_item_done_out_time)
                }
                setText(R.id.tv_todo_content_time_c,data.completeDateStr)
            } else {
                colorTitle = mContext.resources.getColor(R.color.todo_item_text_title)
                colorContent = mContext.resources.getColor(R.color.todo_item_text_content)
                colorItem = mContext.resources.getColor(R.color.todo_item_doing)
            }
            setTextColor(R.id.tv_todo_content_title, colorTitle)
            setBackgroundColor(R.id.ll_todo_item, colorItem)
            setText(R.id.tv_todo_content_title, data.title)
            setTextColor(R.id.tv_todo_content_detail, colorContent)
            setGone(R.id.tv_todo_content_detail, !TextUtils.isEmpty(data.content))
            setText(R.id.tv_todo_content_detail, data.content)
            setText(R.id.tv_todo_content_time_p, data.dateStr)
        }
    }
}