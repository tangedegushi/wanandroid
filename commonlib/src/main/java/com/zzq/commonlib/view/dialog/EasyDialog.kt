package com.zzq.commonlib.view.dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zzq.commonlib.R

/**
 *@auther tangedegushi
 *@creat 2018/12/20
 *@Decribe
 */
class EasyDialog: DialogFragment() {

    private val LAYOUT = "layout_id"
    private val DIM = "dim_amount"
    private val GRAVITY = "gravity"
    private val WIDTH = "width"
    private val HEIGHT = "height"
    private val MARGIN = "margin"
    private val LISTENER = "listener"

    private var layoutId: Int = 0
    private var dimAmount = 0.5f
    private var gravity = Gravity.CENTER
    private var width: Int = 0
    private var height: Int = 0
    private var margin: Int = 0
    private var listener: ViewListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BaseDialog)

        if (savedInstanceState != null) {
            layoutId = savedInstanceState.getInt(LAYOUT)
            dimAmount = savedInstanceState.getFloat(DIM)
            gravity = savedInstanceState.getInt(GRAVITY)
            width = savedInstanceState.getInt(WIDTH)
            height = savedInstanceState.getInt(HEIGHT)
            margin = savedInstanceState.getInt(MARGIN)
            listener = savedInstanceState.getParcelable(LISTENER)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(LAYOUT, layoutId)
        outState.putFloat(DIM, dimAmount)
        outState.putInt(GRAVITY, gravity)
        outState.putInt(WIDTH, width)
        outState.putInt(HEIGHT, height)
        outState.putInt(MARGIN, margin)
        outState.putParcelable(LISTENER, listener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(layoutId, container, false)
        listener?.convert(ViewHolder.create(mView), this)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWindowParams()
    }

    private fun initWindowParams() {
        val window = dialog.window ?: return
        val lp = window.attributes
        lp.dimAmount = dimAmount
        if (gravity != Gravity.CENTER) {
            lp.gravity = gravity
            if (gravity == Gravity.BOTTOM) {
                lp.windowAnimations = R.style.BaseDialog_Bottom_Anim
            } else if (gravity == Gravity.TOP) {
                lp.windowAnimations = R.style.BaseDialog_top_Anim
            }
        }
        if (margin != 0) {
            val marginTotal = 2 * TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin.toFloat(), resources.displayMetrics).toInt()
            val screenWidth = resources.displayMetrics.widthPixels
            lp.width = screenWidth - marginTotal
        } else {
            if (width != 0) {
                lp.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width.toFloat(), resources.displayMetrics).toInt()
            }
        }
        if (height != 0) {
            lp.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height.toFloat(), resources.displayMetrics).toInt()
        }
        window.attributes = lp
    }

    fun show(manager: FragmentManager): EasyDialog {
        show(manager, "easy_dialog")
        return this
    }

    private fun initBuild(builder: Builder) {
        layoutId = builder.layoutId
        dimAmount = builder.dimAmount
        gravity = builder.gravity
        width = builder.width
        height = builder.height
        margin = builder.margin
        listener = builder.listener
        isCancelable = builder.outCancelable
    }

    class Builder {
        var layoutId: Int = 0
        var dimAmount = 0.5f
        var gravity = Gravity.CENTER
        var width: Int = 0
        var height: Int = 0
        var margin: Int = 0
        var outCancelable = true
        var listener: ViewListener? = null
        var hasLayoutId = false

        fun setLayoutId(layoutId: Int): Builder {
            hasLayoutId = true
            this.layoutId = layoutId
            return this
        }

        fun setOutCancelable(outCancelable: Boolean): Builder {
            this.outCancelable = outCancelable
            return this
        }

        fun setDimAmount(dimAmount: Float): Builder {
            this.dimAmount = dimAmount
            return this
        }

        fun setGravity(gravity: Int): Builder {
            this.gravity = gravity
            return this
        }

        fun setWidth(width: Int): Builder {
            this.width = width
            return this
        }

        fun setHeight(height: Int): Builder {
            this.height = height
            return this
        }

        fun setMargin(margin: Int): Builder {
            this.margin = margin
            return this
        }

        fun setViewLisenter(listener: ViewListener): Builder {
            this.listener = listener
            return this
        }

        fun build(): EasyDialog {
            if (!hasLayoutId) {
                throw RuntimeException("you must call setLayoutId() method")
            }
            val dialog = EasyDialog()
            dialog.initBuild(this)
            return dialog
        }

        fun show(manager: FragmentManager): EasyDialog {
            if (!hasLayoutId) {
                throw RuntimeException("you must call setLayoutId() method")
            }
            val dialog = EasyDialog()
            dialog.initBuild(this)
            dialog.show(manager, "loading")
            return dialog
        }
    }

}