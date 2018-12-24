package com.zzq.commonlib.view.dialog

import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.view.View
import android.widget.TextView

/**
 *@auther tangedegushi
 *@creat 2018/12/20
 *@Decribe
 */
class ViewHolder(val convertView: View) {

    fun <T : View> getView(@IdRes viewId: Int): T {
        val view = convertView.findViewById<View>(viewId)
        return view as T
    }

    /**
     * Will set the text of a TextView.
     *
     * @param viewId The view id.
     * @param value  The text to put in the text view.
     * @return The BaseViewHolder for chaining.
     */
    fun setText(@IdRes viewId: Int, value: CharSequence): ViewHolder {
        val textView = getView<TextView>(viewId)
        textView.text = value
        return this
    }

    fun setText(@IdRes idRes: Int, @StringRes strId: Int): ViewHolder {
        val textView = getView<TextView>(idRes)
        textView.setText(strId)
        return this
    }

    /**
     * Sets the on click listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on click listener;
     * @return The BaseViewHolder for chaining.
     */
    fun setOnClickListener(@IdRes viewId: Int, listener: View.OnClickListener): ViewHolder {
        val view = getView<View>(viewId)
        view.setOnClickListener(listener)
        return this
    }

    companion object {
        fun create(view: View): ViewHolder {
            return ViewHolder(view)
        }
    }

}