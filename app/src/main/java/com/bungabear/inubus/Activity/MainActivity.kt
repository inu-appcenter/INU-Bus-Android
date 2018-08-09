package com.bungabear.inubus.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import com.bungabear.inubus.R
import com.bungabear.inubus.adapter.MainTabpagerAdapter
import com.bungabear.inubus.util.LocalIntent
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Minjae Son on 2018-08-07.
 */

class MainActivity : AppCompatActivity(){

    private val mBroadcastManager by lazy { LocalBroadcastManager.getInstance(this) }
    private var mFragmentReadyCount = 0;
    private val tabIcons = listOf(
            listOf(R.drawable.ic_tab_engineer_select, R.drawable.ic_tab_science_select, R.drawable.ic_tab_gate_select, R.drawable.ic_tab_bitzon_select),
            listOf(R.drawable.ic_tab_engineer_unselect, R.drawable.ic_tab_science_unselect, R.drawable.ic_tab_gate_unselect, R.drawable.ic_tab_bitzon_unselect))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        activity_main_tablayout.addTab(activity_main_tablayout.newTab())
//        activity_main_tablayout.addTab(activity_main_tablayout.newTab())
//        activity_main_tablayout.addTab(activity_main_tablayout.newTab())
//        activity_main_tablayout.addTab(activity_main_tablayout.newTab())
//        activity_main_tablayout.getTabAt(0)!!.setText("공대").setIcon(R.drawable.ic_tab_spanner).icon!!.alpha = 100
//        activity_main_tablayout.getTabAt(1)!!.setText("자과대").setIcon(R.drawable.ic_tab_flask).icon!!.alpha = 100
//        activity_main_tablayout.getTabAt(2)!!.setText("정문").setIcon(R.drawable.ic_tab_gate).icon!!.alpha = 100
//        activity_main_tablayout.getTabAt(3)!!.setText("지정단").setIcon(R.drawable.ic_tab_spanner).icon!!.alpha = 100
        activity_main_tablayout.getTabAt(0)!!.customView = LayoutInflater.from(this).inflate(R.layout.custom_tabicon, activity_main_tablayout, false)
        activity_main_tablayout.getTabAt(1)!!.customView = LayoutInflater.from(this).inflate(R.layout.custom_tabicon, activity_main_tablayout, false)
        activity_main_tablayout.getTabAt(2)!!.customView = LayoutInflater.from(this).inflate(R.layout.custom_tabicon, activity_main_tablayout, false)
        activity_main_tablayout.getTabAt(3)!!.customView = LayoutInflater.from(this).inflate(R.layout.custom_tabicon, activity_main_tablayout, false)

        activity_main_viewpager.offscreenPageLimit = 4;
        activity_main_viewpager.adapter = MainTabpagerAdapter(supportFragmentManager, applicationContext)
        activity_main_viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(activity_main_tablayout))
        activity_main_viewpager.currentItem = 0


        activity_main_tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
//                tab.icon = ResourcesCompat.getDrawable(resources, tabIcons[1][tab.position], null)
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                activity_main_viewpager.currentItem = tab.position
//                tab.icon = ResourcesCompat.getDrawable(resources, tabIcons[0][tab.position], null)
            }

        })


        mBroadcastManager.registerReceiver(broadcastReceiver, IntentFilter(LocalIntent.NOTIFY_FRAGMENT_READY.value))
    }

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action){
                LocalIntent.NOTIFY_FRAGMENT_READY.value->{
                    mFragmentReadyCount++
                    if(mFragmentReadyCount == 4){
                        mBroadcastManager.sendBroadcast(Intent(LocalIntent.FIRST_DATA_REQUEST.value))
                    }
                }
            }
        }
    }
}