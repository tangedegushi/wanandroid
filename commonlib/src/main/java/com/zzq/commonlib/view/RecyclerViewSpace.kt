package com.zzq.commonlib.view

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 *@auther tangedegushi
 *@creat 2018/11/15
 *@Decribe
 */
class RecyclerViewSpace(private val bottom: Int = 30,private val left: Int = 0,private val right: Int = 0) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = bottom
        outRect.left = left
        outRect.right = right
    }
}