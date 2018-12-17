package com.zzq.moduletree.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.zzq.moduletree.bean.TreeData
import com.zzq.commonui.fragment.TypeArticleFragment

/**
 *@auther tangedegushi
 *@creat 2018/12/12
 *@Decribe
 */
class TreeDetailContentMoreVPAdapter(private val data: List<TreeData.ChildrenBean>, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = TypeArticleFragment.newInstance(data[position].id)

    override fun getCount(): Int = data.size

    override fun getPageTitle(position: Int): CharSequence? = data[position].name

}