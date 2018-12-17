package com.zzq.moduletree.adapter

import android.graphics.Canvas
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.zzq.moduletree.bean.DecorationTitleName
import com.zzq.moduletree.R

/**
 *@auther tangedegushi
 *@creat 2018/12/10
 *@Decribe
 */
class ItemHeaderDecoration : RecyclerView.ItemDecoration() {
    private lateinit var inflater: LayoutInflater
    private lateinit var topTitleView: View
    private lateinit var tvTitle: TextView
    private lateinit var llRoot: LinearLayout
    private var heightSpec: Int = 0
    private var widthSpec: Int = 0
    var data: List<DecorationTitleName>? = null
    var callback: DetailTitleCallback? = null

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (data == null) return
        val firstVisibleItemPosition = (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        initTopTitleView(parent, firstVisibleItemPosition)
        translateTitleView(c, parent, firstVisibleItemPosition)
        drawHeader(c, parent)
    }

    var isInit = false
    var isShowNextIcon = true
    private fun initTopTitleView(parent: RecyclerView, firstVisibleItemPosition: Int) {
        if (!isInit) {
            isInit = true
            inflater = LayoutInflater.from(parent.context)
            topTitleView = inflater.inflate(R.layout.tree_detail_item_title, null)
            if (!isShowNextIcon) topTitleView.findViewById<ImageView>(R.id.img_next).visibility = View.GONE
            topTitleView.apply {
                tvTitle = findViewById(R.id.tv_tree_detail_title)
                llRoot = findViewById(R.id.root)
                val lp = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParams = lp
                widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width - parent.paddingLeft - parent.paddingRight, View.MeasureSpec.EXACTLY)
                heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height - parent.paddingTop - parent.paddingBottom, View.MeasureSpec.AT_MOST)
            }
        }
        val title = data?.get(firstVisibleItemPosition)?.titleName
        if (tvTitle.text != title ) {
            tvTitle.text = title
            callback?.onTitlePosition(title?:"")
        }
    }


    private fun translateTitleView(canvas: Canvas, parent: RecyclerView, firstVisibleItemPosition: Int) {
        var isTranslate = false
        var firstTitleIndex = 0
        for (index in 1..3) {
            isTranslate = data?.get(firstVisibleItemPosition + index)?.isTitle ?: false
            firstTitleIndex = firstVisibleItemPosition + index
            if (isTranslate) break
        }
        if (!isTranslate) return
        val itemTitleView = parent.findViewHolderForLayoutPosition(firstTitleIndex)?.itemView
        itemTitleView?.apply {
            var distance = top - topTitleView.height
            if (distance < 0) {
                canvas.translate(0f, distance.toFloat())
            }
        }
//        val itemView = parent.findViewHolderForLayoutPosition(firstVisibleItemPosition)?.itemView
//        val manager = parent.layoutManager as LinearLayoutManager
//        itemView?.apply {
//            val decorationHeight = manager.getBottomDecorationHeight(itemView)
//            val distance = top + height + decorationHeight - topTitleView.height
//            if (distance < 0) {
//                canvas.translate(0f, distance.toFloat())
//            }
//        }
    }

    private fun drawHeader(canvas: Canvas, parent: RecyclerView) {
        topTitleView.apply {
            measure(widthSpec, heightSpec)
            layout(parent.paddingLeft, parent.paddingTop, parent.paddingRight, parent.paddingTop + measuredHeight)
            draw(canvas)
        }
    }

    interface DetailTitleCallback {

        fun onTitlePosition(title: String)
    }
}