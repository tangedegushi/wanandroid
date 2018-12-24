package com.zzq.commonlib.view.dialog

import android.os.Parcel
import android.os.Parcelable

/**
 *@auther tangedegushi
 *@creat 2018/12/20
 *@Decribe
 */
abstract class ViewListener(): Parcelable {

    abstract fun convert(helper: ViewHolder, dialog: EasyDialog)

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {}


    constructor(`in`: Parcel): this()

    val CREATOR: Parcelable.Creator<ViewListener> = object : Parcelable.Creator<ViewListener> {
        override fun createFromParcel(source: Parcel): ViewListener {
            return object : ViewListener(source) {
                override fun convert(helper: ViewHolder, dialog: EasyDialog) {

                }
            }
        }

        override fun newArray(size: Int): Array<ViewListener?> {
            return arrayOfNulls(size)
        }
    }

}