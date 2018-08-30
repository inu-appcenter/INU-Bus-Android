package com.inu.bus.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inu.bus.R
import com.inu.bus.recycler.RecyclerAdapterDestination
import kotlinx.android.synthetic.main.fragment_swipepull_recycler.*

/**
 * Created by Minjae Son on 2018-08-13.
 */

class DestinationFragment : Fragment() {

    private lateinit var mFm: FragmentManager
    private lateinit var mContext : Context
    private val mBroadcastManager by lazy { LocalBroadcastManager.getInstance(mContext) }
    private lateinit var mAdapter: RecyclerAdapterDestination

    companion object {
        fun newInstance(fm : FragmentManager, context : Context) : DestinationFragment{
            val fragment = DestinationFragment()
            fragment.mFm = fm
            fragment.mContext = context
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        mBroadcastManager.registerReceiver(mBroadcastReceiver, IntentFilter(LocalIntent.FIRST_DATA_RESPONSE.value))
        return inflater.inflate(R.layout.fragment_swipepull_recycler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_node_arrival_swipeRefreshLayout.isEnabled = false
        fragment_node_arrival_swipeRefreshLayout.isRefreshing = false
        rv_fragment_node_arrival_recycler.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        mAdapter = RecyclerAdapterDestination()
        rv_fragment_node_arrival_recycler.adapter = mAdapter
    }
}