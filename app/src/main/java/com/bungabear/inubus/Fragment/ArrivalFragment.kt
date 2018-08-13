package com.bungabear.inubus.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bungabear.inubus.R
import com.bungabear.inubus.activity.MainActivity
import com.bungabear.inubus.adapter.ViewPagerAdapter
import com.bungabear.inubus.custom.IconPopUp
import com.bungabear.inubus.util.LocalIntent
import com.bungabear.inubus.util.Singleton.LOG_TAG
import kotlinx.android.synthetic.main.custom_tabicon.view.*
import kotlinx.android.synthetic.main.fragment_arrival_tab.*

/**
 * Created by Minjae Son on 2018-08-13.
 */

class ArrivalFragment : Fragment(){

    private lateinit var mFm: FragmentManager
    private lateinit var mContext : Context
    lateinit var mTabLaytoutWrapper : LinearLayout

    companion object {
        fun newInstance(fm : FragmentManager, context : Context) : ArrivalFragment{
            val fragment = ArrivalFragment()
            fragment.mFm = fm
            fragment.mContext = context
            return fragment
        }

    }
    private val mBroadcastManager by lazy { LocalBroadcastManager.getInstance(mContext) }
    private var mFragmentReadyCount = 0
    private val mTabIcons = listOf(
            listOf(R.drawable.ic_tab_engineer_select, R.drawable.ic_tab_science_select, R.drawable.ic_tab_gate_select, R.drawable.ic_tab_bitzon_select),
            listOf(R.drawable.ic_tab_engineer_unselect, R.drawable.ic_tab_science_unselect, R.drawable.ic_tab_gate_unselect, R.drawable.ic_tab_bitzon_unselect))
    private val mTabText = listOf("공대", "자과대", "정문", "지정단")
    private val mViewPagerAdapter by lazy { ViewPagerAdapter(mFm, mContext) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_arrival_tab, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mTabLaytoutWrapper = ll_fragment_arrival_tablayout_wrapper
        mBroadcastManager.registerReceiver(mBroadcastReceiver, IntentFilter(LocalIntent.NOTIFY_FRAGMENT_READY.value))
        // tab setting
        mTabIcons[1].forEachIndexed { index, it ->
            val v = LayoutInflater.from(mContext).inflate(R.layout.custom_tabicon, null)
            v.iv_tabicon.setImageResource(it)
            v.tv_tabicon.text = mTabText[index]
            fragment_arrival_tablayout.addTab(fragment_arrival_tablayout.newTab())
            fragment_arrival_tablayout.getTabAt(index)!!.customView = v
        }
        MainActivity.mWrSearchView.get()?.addTextChangedListener(mSearchTextWatcher)
        mViewPagerAdapter.addFragment(ArrivalFragmentTab.newInstance(mContext, "engineer"))
        mViewPagerAdapter.addFragment(ArrivalFragmentTab.newInstance(mContext, "science"))
        mViewPagerAdapter.addFragment(ArrivalFragmentTab.newInstance(mContext, "frontgate"))
        mViewPagerAdapter.addFragment(BITZonFragment.newInstance(mContext))

        fragment_arrival_viewpager.offscreenPageLimit = 4
        fragment_arrival_viewpager.adapter = mViewPagerAdapter
        fragment_arrival_viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(fragment_arrival_tablayout))
        fragment_arrival_viewpager.currentItem = 0
        setTabIcon(fragment_arrival_tablayout.getTabAt(0)!!, true)

        fragment_arrival_tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab) {}

            override fun onTabUnselected(tab: TabLayout.Tab) {
                setTabIcon(tab, false)
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                fragment_arrival_viewpager.currentItem = tab.position
                setTabIcon(tab, true)
            }
        })

        val popupView = IconPopUp(mContext)
        popupView.setBtnText("확인하였습니다")
                .setIcon(R.drawable.ic_logo, false)
                .setMessageText("This is <font color='red'>red</font>. This is <font color='blue'>blue</font>.")
                .setDimBlur(MainActivity.mWrBlurringView2.get()!!)
                .show()
    }

    private fun setTabIcon(tab : TabLayout.Tab, selected : Boolean){
        val view = tab.customView
        if(selected){
            view!!.iv_tabicon.setImageResource(mTabIcons[0][tab.position])
            view.tv_tabicon.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
        }
        else {
            view!!.iv_tabicon.setImageResource(mTabIcons[1][tab.position])
            view.tv_tabicon.setTextColor(Color.BLACK)
        }
    }

    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action){
                LocalIntent.NOTIFY_FRAGMENT_READY.value->{
                    mFragmentReadyCount++
                    Log.d(LOG_TAG, "$mFragmentReadyCount")

                    if(mFragmentReadyCount == 4){
                        mBroadcastManager.sendBroadcast(Intent(LocalIntent.FIRST_DATA_REQUEST.value))
                    }
                }
            }
        }
    }

    private val mSearchTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            (fragment_arrival_viewpager.adapter as ViewPagerAdapter).fragments.forEach {
                (it as ArrivalFragmentTab).filter(s.toString())
            }
        }
    }

}