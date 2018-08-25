package com.inu.bus.fragment

import android.content.Context
import android.content.Intent
import android.databinding.Observable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inu.bus.R
import com.inu.bus.activity.MainActivity
import com.inu.bus.recycler.RecyclerAdapterBITZonArrival
import com.inu.bus.util.LocalIntent
import com.inu.bus.util.Singleton
import kotlinx.android.synthetic.main.fragment_arrival_tab_bitzon.*

/**
 * Created by Minjae Son on 2018-08-13.
 */

class BITZonFragment : Fragment(){

    private var isShowing = false
    private lateinit var mContext : Context
    private val mBroadcastManager by lazy { LocalBroadcastManager.getInstance(mContext) }
    val mAdapter by lazy { RecyclerAdapterBITZonArrival("BITZon") }

    companion object {
        fun newInstance(context: Context): BITZonFragment {
            val fragment = BITZonFragment()
            fragment.mContext = context
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_arrival_tab_bitzon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_fragment_bitzon_info.setOnClickListener {
            MainActivity.mWrBtnInfo.get()?.performClick()
        }
        rv_fragment_arrival_bitzon.adapter = mAdapter
        mBroadcastManager.sendBroadcast(Intent(LocalIntent.NOTIFY_FRAGMENT_READY.value))
        fragment_arrival_bitzon_swiperefresh.setOnRefreshListener {
            mBroadcastManager.sendBroadcast(Intent(LocalIntent.ARRIVAL_DATA_REFRESH_REQUEST.value))
        }
        Singleton.arrivalToInfo.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                dataRefresh()
            }
        })
        dataRefresh()
    }

    fun dataRefresh(){
        fragment_arrival_bitzon_swiperefresh?.isRefreshing = false
        Singleton.arrivalToInfo.get()?.let{
            mAdapter.applyDataSet(it)
        }
    }
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
//        Log.d(LOG_TAG, "isVisibleToUser $mStrBusStop, $isVisibleToUser")
        mAdapter.isShowing.set(isVisibleToUser)
        isShowing = isVisibleToUser
    }

    override fun onPause() {
        super.onPause()
        mAdapter.isShowing.set(false)
    }

    // 재시작되면서 현재 보이는 탭만 Ticker를 실행하도록.
    override fun onResume() {
        super.onResume()
        mAdapter.isShowing.set(isShowing)
    }
}