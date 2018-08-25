package com.inu.bus.fragment

import android.content.Context
import android.content.Intent
import android.databinding.Observable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inu.bus.R
import com.inu.bus.recycler.RecyclerAdapterArrival
import com.inu.bus.util.LocalIntent
import com.inu.bus.util.Singleton
import kotlinx.android.synthetic.main.fragment_swipepull_recycler.*


/**
 * Created by Minjae Son on 2018-08-07.
 */

class ArrivalFragmentTab : Fragment() {

    private var isShowing = false
    private lateinit var mStrBusStop: String
    private lateinit var mContext : Context
    private val mBroadcastManager by lazy { LocalBroadcastManager.getInstance(mContext) }
    val mAdapter by lazy { RecyclerAdapterArrival(mStrBusStop) }

    companion object {
        fun newInstance(context: Context, stopName: String): ArrivalFragmentTab {
            val fragment = ArrivalFragmentTab()
            fragment.mStrBusStop = stopName
            fragment.mContext = context
            Log.d("test", "$stopName fragment created")
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_swipepull_recycler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("test", "$mStrBusStop fragment onViewCreated")
        rv_fragment_node_arrival_recycler.adapter = mAdapter
        mBroadcastManager.sendBroadcast(Intent(LocalIntent.NOTIFY_FRAGMENT_READY.value))
        fragment_node_arrival_swipeRefreshLayout.setOnRefreshListener {
            mBroadcastManager.sendBroadcast(Intent(LocalIntent.ARRIVAL_DATA_REFRESH_REQUEST.value))
        }
        Singleton.arrivalFromInfo.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                dataRefresh()
            }
        })
        dataRefresh()
    }

    fun dataRefresh(){
        fragment_node_arrival_swipeRefreshLayout?.isRefreshing = false
        Singleton.arrivalFromInfo.get()?.let{
            val checked = it.filter{ subIt->  subIt.name == mStrBusStop }
            if(checked.isEmpty()) return
            val filtered = checked[0].data
            mAdapter.applyDataSet(filtered)
        }
    }

    fun filter(str : String){
        mAdapter.filter(str)
    }

    // TimeTicker 생명주기에 맞춰 활성, 비활성.

    // ViewPager에서 현재 페이지가 보이고 있는지 오는 콜백.
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
//        Log.d(LOG_TAG, "isVisibleToUser $mStrBusStop, $isVisibleToUser")
        mAdapter.isShowing.set(isVisibleToUser)
        isShowing = isVisibleToUser
    }

    // 생명주기에 맞춤
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
