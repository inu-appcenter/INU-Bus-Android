package com.bungabear.inubus.activity

import android.arch.persistence.room.Room
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.bungabear.inubus.R
import com.bungabear.inubus.adapter.MainViewPagerAdapter
import com.bungabear.inubus.adapter.SearchHistoryAdapter
import com.bungabear.inubus.model.AppDatabase
import com.bungabear.inubus.model.SearchHistoryItem
import com.bungabear.inubus.util.LocalIntent
import com.ms_square.etsyblur.BlurSupport
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_actionbar.*
import kotlinx.android.synthetic.main.custom_info_drawer.*
import kotlinx.android.synthetic.main.custom_tabicon.view.*
import android.R.attr.versionName
import android.content.pm.PackageInfo




/**
 * Created by Minjae Son on 2018-08-07.
 */

class MainActivity : AppCompatActivity(){

    companion object {
        lateinit var DB : AppDatabase
    }

    private val mBroadcastManager by lazy { LocalBroadcastManager.getInstance(this) }
    private var mFragmentReadyCount = 0
    private val mTabIcons = listOf(
            listOf(R.drawable.ic_tab_engineer_select, R.drawable.ic_tab_science_select, R.drawable.ic_tab_gate_select, R.drawable.ic_tab_bitzon_select),
            listOf(R.drawable.ic_tab_engineer_unselect, R.drawable.ic_tab_science_unselect, R.drawable.ic_tab_gate_unselect, R.drawable.ic_tab_bitzon_unselect))
    private val mTabText = listOf("공대", "자과대", "정문", "지정단")
    private val mSearchAdapter : SearchHistoryAdapter by lazy { SearchHistoryAdapter(this, R.layout.search_history_list_item) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBroadcastManager.registerReceiver(broadcastReceiver, IntentFilter(LocalIntent.NOTIFY_FRAGMENT_READY.value))
        DB =  Room.databaseBuilder(this, AppDatabase::class.java, "db").allowMainThreadQueries().build()
        setContentView(R.layout.activity_main)

        // actionbar setting
        actionbar_searchView.setAdapter(mSearchAdapter)
        actionbar_searchView.addTextChangedListener(searchTextWatcher)

        // tab setting
        mTabIcons[1].forEachIndexed { index, it ->
            val v = LayoutInflater.from(this).inflate(R.layout.custom_tabicon, null)
            v.iv_tabicon.setImageResource(it)
            v.tv_tabicon.text = mTabText[index]
            activity_main_tablayout.addTab(activity_main_tablayout.newTab())
            activity_main_tablayout.getTabAt(index)!!.customView = v
        }

        activity_main_viewpager.offscreenPageLimit = 4
        activity_main_viewpager.adapter = MainViewPagerAdapter(supportFragmentManager, applicationContext)
        activity_main_viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(activity_main_tablayout))
        activity_main_viewpager.currentItem = 0
        setTabIcon(activity_main_tablayout.getTabAt(0)!!, true)

        activity_main_tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab) {}

            override fun onTabUnselected(tab: TabLayout.Tab) {
                setTabIcon(tab, false)
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                activity_main_viewpager.currentItem = tab.position
                setTabIcon(tab, true)
            }
        })
        actionbar_searchView.setOnEditorActionListener( TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                DB.searchHistoryDAO().insert(SearchHistoryItem(name = v.text.toString()))
                mSearchAdapter.refreshHistory()
                return@OnEditorActionListener true
            }
            false
        })


        // drawer settng
        btn_actionbar_info.setOnClickListener {
            drawer_layout.openDrawer(Gravity.RIGHT)
        }
        drawer_btn_ask.setOnClickListener {
            startActivity(Intent(this, InquireActivity::class.java))
        }
        val pInfo = this.packageManager.getPackageInfo(packageName, 0)
        tv_drawer_version.text = pInfo.versionName
        BlurSupport.addTo(drawer_layout)

        // toggle setting
        activity_main_toggle.setOnPositionChangedListener {
            when(it){
                // 도착정보
                0->{

                }
                // 목적지 정보
                1->{

                }
            }
        }

    }

    private val searchTextWatcher = object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            (activity_main_viewpager.adapter as MainViewPagerAdapter).fragments.forEach {
                it.filter(s.toString())
            }
        }
    }

    private fun setTabIcon(tab : TabLayout.Tab, selected : Boolean){
        val view = tab.customView
        if(selected){
            view!!.iv_tabicon.setImageResource(mTabIcons[0][tab.position])
            view.tv_tabicon.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
        }
        else {
            view!!.iv_tabicon.setImageResource(mTabIcons[1][tab.position])
            view.tv_tabicon.setTextColor(Color.BLACK)
        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
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

    override fun onBackPressed() {
        if(actionbar_searchView.text.toString() != ""){
            actionbar_searchView.text.clear()
        }
        else {
            super.onBackPressed()
        }
    }
}