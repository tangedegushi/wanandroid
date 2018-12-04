package com.zzq.modulehomepage.image

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.youth.banner.loader.ImageLoader

/**
 *@auther tangedegushi
 *@creat 2018/11/8
 *@Decribe 用于banner加载图片
 */
class GlideImageLoad : ImageLoader() {
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        Glide.with(context!!).load(path).into(imageView!!)
    }
}