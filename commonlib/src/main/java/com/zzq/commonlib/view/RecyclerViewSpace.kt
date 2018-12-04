package com.zzq.commonlib.view

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 *@auther tangedegushi
 *@creat 2018/11/15
 *@Decribe
 */
class RecyclerViewSpace(private val space: Int = 30) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect?.bottom = space
    }
}