package com.bungabear.inubus.activity

import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView
import android.widget.TextView
import com.bungabear.inubus.R
import com.bungabear.inubus.adapter.SearchHistoryAdapter
import com.bungabear.inubus.adapter.ViewPagerAdapter
import com.bungabear.inubus.fragment.ArrivalFragment
import com.bungabear.inubus.fragment.DestinationFragment
import com.bungabear.inubus.model.AppDatabase
import com.bungabear.inubus.model.SearchHistoryItem
import com.ms_square.etsyblur.BlurSupport
import com.ms_square.etsyblur.BlurringView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_actionbar.*
import kotlinx.android.synthetic.main.custom_info_drawer.*
import java.lang.ref.WeakReference


/**
 * Created by Minjae Son on 2018-08-07.
 */

class MainActivity : AppCompatActivity(){

    companion object {
        lateinit var DB : AppDatabase
        lateinit var mWrSearchView : WeakReference<AutoCompleteTextView>
        lateinit var mWrBlurringView2 : WeakReference<BlurringView>
    }

    private val mSearchAdapter : SearchHistoryAdapter by lazy { SearchHistoryAdapter(this, R.layout.search_history_list_item) }
    private val mViewPagerAdapter by lazy { ViewPagerAdapter(supportFragmentManager, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DB =  Room.databaseBuilder(this, AppDatabase::class.java, "db").allowMainThreadQueries().build()
        setContentView(R.layout.activity_main)

        // actionbar setting
        mWrSearchView = WeakReference(actionbar_searchView)
        actionbar_searchView.setAdapter(mSearchAdapter)
        actionbar_searchView.setOnEditorActionListener( TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                DB.searchHistoryDAO().insert(SearchHistoryItem(name = v.text.toString()))
                mSearchAdapter.refreshHistory()
                return@OnEditorActionListener true
            }
            false
        })

        // drawer settng
        activity_main_popup_blur.blurredView(drawer_layout)
        mWrBlurringView2 = WeakReference(activity_main_popup_blur)
        btn_actionbar_info.setOnClickListener {
            drawer_layout.openDrawer(Gravity.RIGHT)
        }
        drawer_btn_ask.setOnClickListener {
            startActivity(Intent(this, InquireActivity::class.java))
            drawer_layout.closeDrawer(Gravity.RIGHT)
        }
        val pInfo = this.packageManager.getPackageInfo(packageName, 0)
        tv_drawer_version.text = pInfo.versionName
        BlurSupport.addTo(drawer_layout)

        // toggle setting
        val arrivalFragment = ArrivalFragment.newInstance(supportFragmentManager, this)
        mViewPagerAdapter.addFragment(DestinationFragment.newInstance(supportFragmentManager, this))
        mViewPagerAdapter.addFragment(arrivalFragment)
        activity_main_viewpager.adapter = mViewPagerAdapter
        activity_main_viewpager.offscreenPageLimit = 2
        activity_main_viewpager.currentItem = 1
        activity_main_viewpager.setScrollDurationFactor(4.0)

        activity_main_toggle.setOnPositionChangedListener {
            activity_main_viewpager.currentItem =
                    when(it){
                        // 도착정보
                        0-> 1
                        // 목적지 정보
                        else -> 0
                    }
        }
    }

    override fun onBackPressed() {
        if(actionbar_searchView.text.toString() != ""){
            actionbar_searchView.text.clear()
        }
        else if(drawer_layout.isDrawerOpen(Gravity.RIGHT)){
            drawer_layout.closeDrawer(Gravity.RIGHT)
        }
        else {
            super.onBackPressed()
        }
    }
}