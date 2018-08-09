package com.bungabear.inubus.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bungabear.inubus.R
import com.bungabear.inubus.adapter.ArrivalRecyclerAdapter
import com.bungabear.inubus.model.ArrivalInfo
import com.bungabear.inubus.util.LocalIntent
import com.bungabear.inubus.util.Singleton
import kotlinx.android.synthetic.main.fragment_node_arrival.*


/**
 * Created by Minjae Son on 2018-08-07.
 */

class ArrivalFragment : Fragment() {

    private lateinit var mStrBusStop: String
    private lateinit var mContext : Context
    private val mBroadcastManager by lazy { LocalBroadcastManager.getInstance(mContext) }
    private val adapter by lazy { ArrivalRecyclerAdapter(mStrBusStop) }

    companion object {
        fun newInstance(context: Context, stopName: String): ArrivalFragment {
            val fragment = ArrivalFragment()
            fragment.mStrBusStop = stopName
            fragment.mContext = context
            Log.d("test", "$stopName fragment created")
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("test", "$mStrBusStop fragment onCreateView")
        return inflater.inflate(R.layout.fragment_node_arrival, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("test", "$mStrBusStop fragment onViewCreated")
        rv_fragment_node_arrival_recycler.adapter = adapter
        mBroadcastManager.registerReceiver(broadcastReceiver, IntentFilter(LocalIntent.FIRST_DATA_RESPONSE.value))
        mBroadcastManager.registerReceiver(broadcastReceiver, IntentFilter(LocalIntent.ARRIVAL_DATA_REFRESHED.value))
        mBroadcastManager.sendBroadcast(Intent(LocalIntent.NOTIFY_FRAGMENT_READY.value))
        fragment_node_arrival_swipeRefreshLayout.setOnRefreshListener {
            mBroadcastManager.sendBroadcast(Intent(LocalIntent.ARRIVAL_DATA_REFRESH_REQUEST.value))
        }

    }

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("test", "$mStrBusStop received response")
            when(intent.action){
                LocalIntent.FIRST_DATA_RESPONSE.value -> firstDataInsert()
                LocalIntent.ARRIVAL_DATA_REFRESHED.value -> dataRefresh()
            }
        }
    }

    fun firstDataInsert(){
        val checked = Singleton.arrivalInfo!!.filter{ it.name == mStrBusStop }
        if(checked.isEmpty()) return
        val filtered = checked[0].data
        adapter.applyDataSet(filtered)

//        // Type순 정렬
//        filtered.sortWith(Comparator { o1, o2 ->
//            o1.type!!.ordinal - o1.type.ordinal
//        })
//
//        // 번호순 정렬
//        filtered.sortWith(compareBy { it.no })
//
//        val grouped  = filtered.groupBy { it.type }
//
//        grouped.forEach{ group->
//            // 현재 필요한 섹션 헤더만 추가
//            adapter.addSectionHeader(group.key!!)
//            group.value.forEach {
//                adapter.addItem(it)
//            }
//        }
    }

    fun dataRefresh(){
        fragment_node_arrival_swipeRefreshLayout.isRefreshing = false
        val checked = Singleton.arrivalInfo!!.filter{ it.name == mStrBusStop }
        if(checked.isEmpty()) return
        val filtered = checked[0].data
        adapter.applyDataSet(filtered)

//        Singleton.arrivalInfo!!
//                .filter{ it.name == mStrBusStop }
//                .map { it.data }
//                .forEach {
//                    it.forEach { _it -> adapter.applyItem(_it) }
//                }
    }

    fun startRouteView(){

    }

}
