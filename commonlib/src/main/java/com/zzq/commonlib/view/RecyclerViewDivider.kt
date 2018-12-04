package com.zzq.commonlib.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import android.support.v4.content.ContextCompat
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.IntDef
import android.view.View
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.SOURCE
import android.support.v7.widget.GridLayoutManager


/**
 *@auther tangedegushi
 *@creat 2018/11/14
 *@Decribe
 */
class RecyclerViewDivider(context: Context, @RecyclerViewDivider.OrientationMode orientation: Int) : RecyclerView.ItemDecoration() {

    @IntDef(HORIZONTAL, VERTICAL, BOTH)
    @Retention(SOURCE)
    annotation class OrientationMode

    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
        const val BOTH = 3
    }

    private var mPaint: Paint? = null
    private var mDivider: Drawable? = null
    private var mDividerHeight = 2//分割线高度，默认为1px
    private var mOrientation: Int//列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
    private val ATTRS = intArrayOf(android.R.attr.listDivider)
    var isDrawFirstLine = true

    /**
     * 默认分割线：高度为2px，颜色为灰色
     *
     * @param context
     * @param orientation 列表方向
     */
    init {
        if (orientation != VERTICAL && orientation != HORIZONTAL && orientation != BOTH) {
            throw IllegalArgumentException("请输入正确的参数！")
        }
        mOrientation = orientation

        val a = context.obtainStyledAttributes(ATTRS)
//        mDivider = a.getDrawable(0)
        mDivider = ColorDrawable(0x7f282828)
        a.recycle()
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation 列表方向
     * @param drawableId  分割线图片
     */
    constructor(context: Context, orientation: Int, drawableId: Int) : this(context, orientation) {
        mDivider = ContextCompat.getDrawable(context, drawableId)
        mDividerHeight = mDivider!!.intrinsicHeight
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    constructor(context: Context, orientation: Int, dividerHeight: Int, dividerColor: Int) : this(context, orientation) {
        mDividerHeight = dividerHeight
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint?.color = Color.GRAY
        mPaint?.style = Paint.Style.FILL
    }


    //获取分割线尺寸
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, mDividerHeight)
        } else {
            outRect.set(0, 0, mDividerHeight, 0)
        }
    }

    //绘制分割线
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (mOrientation == BOTH) {
            drawVertical(c, parent)
            drawHorizontal(c, parent)
        } else {
            if (mOrientation == VERTICAL) {
                drawVertical(c, parent)
            } else {
                drawHorizontal(c, parent)
            }
        }
    }

    /**
     * 绘制纵向列表时的分隔线  这时分隔线是横着的
     * 每次 left相同，top根据child变化，right相同，bottom也变化
     * @param canvas
     * @param parent
     */
    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.measuredWidth - parent.paddingRight
        val childSize = parent.childCount
        val spanCount = getSpanCount(parent)
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + layoutParams.bottomMargin
            val bottom = top + mDividerHeight
            if (mDivider != null) {
                if (isDrawFirstLine && i < spanCount) {
                    val firstTop = child.top + layoutParams.topMargin
                    val firstBottom = firstTop + mDividerHeight
//                    Logger.zzqLog().d("firstTop = $firstTop +topMargin = ${firstBottom}+  $top   +$bottom")
                    mDivider!!.setBounds(left, firstTop, right, firstBottom)
                    mDivider!!.draw(canvas)
                }
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider!!.draw(canvas)
            }
            if (mPaint != null)
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
        }
    }

    /**
     * 绘制横向列表时的分隔线  这时分隔线是竖着的
     * l、r 变化； t、b 不变
     * @param canvas
     * @param parent
     */
    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.measuredHeight - parent.paddingBottom
        val childSize = parent.childCount
        val spanCount = getSpanCount(parent)
        var orientation = getOrientation(parent)
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + layoutParams.rightMargin
            val right = left + mDividerHeight
            if (mDivider != null) {
                if (orientation == RecyclerView.VERTICAL) {
                    if (isFirstColum(parent, spanCount, child) && isDrawFirstLine) {
                        val firstLeft = child.left + layoutParams.leftMargin
                        val firstRight = firstLeft + mDividerHeight
                        mDivider!!.setBounds(firstLeft, top, firstRight, bottom)
                        mDivider!!.draw(canvas)
                    }
                } else {
                    if (i < spanCount && isDrawFirstLine) {
                        val firstLeft = child.left + layoutParams.leftMargin
                        val firstRight = firstLeft + mDividerHeight
                        mDivider!!.setBounds(firstLeft, top, firstRight, bottom)
                        mDivider!!.draw(canvas)
                    }
                }
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider!!.draw(canvas)
            }
            if (mPaint != null) {
                if (orientation == RecyclerView.VERTICAL) {
                    if (isFirstColum(parent, spanCount, child) && isDrawFirstLine) {
                        val firstLeft = child.left + layoutParams.leftMargin
                        val firstRight = firstLeft + mDividerHeight
                        canvas.drawRect(firstLeft.toFloat(), top.toFloat(), firstRight.toFloat(), bottom.toFloat(), mPaint)
                    }
                } else {
                    if (i < spanCount && isDrawFirstLine) {
                        val firstLeft = child.left + layoutParams.leftMargin
                        val firstRight = firstLeft + mDividerHeight
                        canvas.drawRect(firstLeft.toFloat(), top.toFloat(), firstRight.toFloat(), bottom.toFloat(), mPaint)
                    }
                }
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
            }
        }
    }

    private fun isFirstColum(parent: RecyclerView, spanCount: Int, view: View): Boolean {
        val layoutManager = parent.layoutManager
        val pos = layoutManager!!.getPosition(view)
        if (layoutManager is GridLayoutManager) {
            if ((pos + 1) % spanCount == 1) {// 如果是最后一列，则不需要绘制右边
                return true
            }
        }
        return false
    }

    private fun isLastColum(parent: RecyclerView, spanCount: Int, view: View): Boolean {
        val layoutManager = parent.layoutManager
        val pos = layoutManager!!.getPosition(view)
        if (layoutManager is GridLayoutManager) {
            if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                return true
            }
        }
        return false
    }


    private fun getSpanCount(parent: RecyclerView): Int {
        // 列数
        var spanCount = -1
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            spanCount = layoutManager.spanCount
        }
        return spanCount
    }

    private fun getOrientation(parent: RecyclerView): Int {
        // 列数
        var orientation = -1
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            orientation = layoutManager.orientation
        }
        return orientation
    }

}