package com.inu.bus.activity

import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.inu.bus.MyService
import com.inu.bus.R
import com.inu.bus.fragment.ArrivalFragment
import com.inu.bus.fragment.DestinationFragment
import com.inu.bus.model.DBSearchHistoryItem
import com.inu.bus.recycler.SearchHistoryAdapter
import com.inu.bus.recycler.ViewPagerAdapter
import com.inu.bus.util.AppDatabase
import com.inu.bus.util.LocalIntent
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
        // ArrivalFragmentTab에서 각 검색결과를 참조하기 위해
        // TODO Observable로 변경
        lateinit var mWrSearchView : WeakReference<AutoCompleteTextView>
        // Drawer BlurView를 공유하면 dim alpha가 적용이 안되서 팝업용 BlurView를 별도로 설정
        lateinit var mWrBlurringView2 : WeakReference<BlurringView>
        // ArrivalViewPager에서 지정단탭일시 액션바를 바꾸기 위해
        lateinit var mWrMainUpperView : WeakReference<LinearLayout>
        // Drawer 여는 이벤트용
        lateinit var mWrBtnInfo : WeakReference<ImageButton>
    }

    private val mSearchAdapter : SearchHistoryAdapter by lazy { SearchHistoryAdapter(this, R.layout.search_history_list_item) }
    private val mViewPagerAdapter by lazy { ViewPagerAdapter(supportFragmentManager, this) }
    private val mBroadcastManager by lazy { LocalBroadcastManager.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Static WeakReference 초기화
        DB =  Room.databaseBuilder(this, AppDatabase::class.java, "db").allowMainThreadQueries().build()
        mWrMainUpperView = WeakReference(ll_main_upper_view_wrapper)
        mWrSearchView = WeakReference(actionbar_searchView)
        mWrBlurringView2 = WeakReference(activity_main_popup_blur)
        setActionBar()
        setDrawer()
        setMainViewPager()
    }

    private fun setActionBar(){
        actionbar_searchView.setAdapter(mSearchAdapter)
        actionbar_searchView.setOnEditorActionListener( TextView.OnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                DB.searchHistoryDAO().insert(DBSearchHistoryItem(name = v.text.toString()))
                mSearchAdapter.refreshHistory()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun setDrawer(){
        activity_main_popup_blur.blurredView(drawer_layout)
        mWrBtnInfo = WeakReference(btn_actionbar_info)
        btn_actionbar_info.setOnClickListener {
            drawer_layout.openDrawer(Gravity.END)
        }
        drawer_btn_ask.setOnClickListener {
            startActivity(Intent(this, InquireActivity::class.java))
            drawer_layout.closeDrawer(Gravity.END)
        }
        val pInfo = this.packageManager.getPackageInfo(packageName, 0)
        tv_drawer_version.text = pInfo.versionName
        BlurSupport.addTo(drawer_layout)
    }

    private fun setMainViewPager(){
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

    override fun onResume() {
        super.onResume()
        startService(Intent(applicationContext, MyService::class.java))
    }

    override fun onPause() {
        super.onPause()
        mBroadcastManager.sendBroadcast(Intent(LocalIntent.SERVICE_EXIT.value))
    }

    override fun onBackPressed() {
        when {
            actionbar_searchView.text.toString() != "" -> actionbar_searchView.text.clear()
            drawer_layout.isDrawerOpen(Gravity.END) -> drawer_layout.closeDrawer(Gravity.END)
            else -> {
                super.onBackPressed()
            }
        }
    }
}