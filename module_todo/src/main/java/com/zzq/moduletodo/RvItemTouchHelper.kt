package com.zzq.moduletodo

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.animation.DecelerateInterpolator
import android.animation.ObjectAnimator
import android.app.Activity
import android.support.v4.app.FragmentActivity
import android.view.View
import com.zzq.commonlib.R
import com.zzq.commonlib.utils.CommonDialogUtil
import com.zzq.moduletodo.bean.TodoData
import com.zzq.netlib.utils.Logger
import java.util.*


/**
 *@auther tangedegushi
 *@creat 2018/12/19
 *@Decribe
 */
class RvItemTouchHelper(data1: MutableList<TodoData.DatasBean>,
                           adapter1: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
                           activity: Activity) : ItemTouchHelper.Callback() {
    private var adapter = adapter1
    private var holder: RecyclerView.ViewHolder? = null
    private val data = data1
    private val mActivity = activity

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        // 拖拽的标记，这里允许上下左右四个方向
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or
                ItemTouchHelper.RIGHT
        // 滑动的标记，这里允许左右滑动
        var swipeFlags = 0
        if (viewHolder.layoutPosition < data.size && !data[viewHolder.layoutPosition].isTitleType) {
            swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        }
        Logger.zzqLog().d("getMovementFlags...........")
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    /**
     * 这个方法会在某个Item被拖动和移动的时候回调，这里我们用来播放动画，当viewHolder不为空时为选中状态
     * 否则为释放状态
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (viewHolder != null) {
            holder = viewHolder
            pickUpAnimation(viewHolder.itemView)
        } else {
            if (holder != null) {
                putDownAnimation(holder!!.itemView)
            }
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        // 移动时更改列表中对应的位置并返回true
        Logger.zzqLog().i("on move old position = ${viewHolder.adapterPosition} new postion = ${target.adapterPosition}")
        Collections.swap(data, viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    /**
     * 当onMove返回true时调用
     */
    override fun onMoved(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, fromPos: Int, target: RecyclerView.ViewHolder, toPos: Int, x: Int, y: Int) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
        adapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        Logger.zzqLog().i("onSwiped old position = ${viewHolder.adapterPosition} new direction = $direction")
        CommonDialogUtil.showConfirmDialog(mActivity.getString(R.string.common_confirm_content_del),
                (mActivity as FragmentActivity).supportFragmentManager, object : CommonDialogUtil.ConfirmDialogCallback {

            override fun cancel(view: View) {
                adapter.notifyDataSetChanged()
            }

            override fun confirm(view: View) {
                swipeDeleteCallback?.onDelete(viewHolder.adapterPosition)
            }
        })
    }

    private fun pickUpAnimation(card: View) {
        val animator = ObjectAnimator.ofFloat(card, "translationZ", 1f, 30f)
        animator.interpolator = DecelerateInterpolator()
        animator.duration = 300
        animator.start()
    }

    private fun putDownAnimation(card: View) {
        val animator = ObjectAnimator.ofFloat(card, "translationZ", 30f, 1f)
        animator.interpolator = DecelerateInterpolator()
        animator.duration = 300
        animator.start()
    }

    var swipeDeleteCallback: SwipeDeleteCallback? = null
    interface SwipeDeleteCallback{
        fun onDelete(position: Int)
    }
}