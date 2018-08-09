package com.bungabear.inubus.adapter

import android.content.Context
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.bungabear.inubus.fragment.ArrivalFragment

/**
 * Created by Minjae Son on 2018-08-07.
 */
class MainTabpagerAdapter(val mFm: FragmentManager, val mContext: Context) : FragmentStatePagerAdapter(mFm){

    private val fragments = ArrayList<ArrivalFragment>()

   init {
        // 컨텍스트와 정류장 이름을 넘겨준다. 정류장 이름은 데이터 파싱에 사용하므로, 일치하는지 꼭 확인해야한다.
        fragments.add(ArrivalFragment.newInstance(mContext, "engineer"))
        fragments.add(ArrivalFragment.newInstance(mContext, "science"))
        fragments.add(ArrivalFragment.newInstance(mContext, "frontgate"))
        fragments.add(ArrivalFragment.newInstance(mContext, "BITZon"))
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
//        re-attache crash fix
//        super.restoreState(state, loader)
    }

    override fun getItem(position: Int): Fragment = fragments[position]
    override fun getCount(): Int = fragments.size
}