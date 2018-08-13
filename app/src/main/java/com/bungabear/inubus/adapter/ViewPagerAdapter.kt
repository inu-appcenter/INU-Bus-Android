package com.bungabear.inubus.adapter

import android.content.Context
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * Created by Minjae Son on 2018-08-07.
 */
class ViewPagerAdapter(mFm: FragmentManager, mContext: Context) : FragmentStatePagerAdapter(mFm){

    val fragments = ArrayList<Fragment>()

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
//        re-attache crash fix
//        super.restoreState(state, loader)
    }

    override fun getItem(position: Int): Fragment = fragments[position]
    override fun getCount(): Int = fragments.size

    fun addFragment(fragment : Fragment){
        fragments.add(fragment)
    }
}