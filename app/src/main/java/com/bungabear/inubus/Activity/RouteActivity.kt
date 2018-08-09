package com.bungabear.inubus.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.bungabear.inubus.R
import com.bungabear.inubus.adapter.RouteRecyclerAdapter
import com.bungabear.inubus.util.LOG_TAG
import com.bungabear.inubus.util.Singleton
import kotlinx.android.synthetic.main.activity_route.*

/**
 * Created by Bunga on 2018-02-23.
 */

class RouteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)
        val routeNo = intent.getStringExtra("routeNo")
        val routeInfo = Singleton.busInfo[routeNo]!!
        val routeList = routeInfo.nodeList
        var turnNode = routeInfo.turnNode
        val adapter = RouteRecyclerAdapter()
        if(turnNode == ""){
            // 회차지 없음
            routeList.forEachIndexed { index, s ->
                Log.d(LOG_TAG, s)
                if(index == 1)
                {
                    adapter.addStop(s,1,1)
                }
                else if(index == routeList.lastIndex){
                    adapter.addStop(s,1,2)
                }
                else {
                    adapter.addStop(s,1,3)
                }
            }
        }
        else {
            // 8번 에러
            if(turnNode == "송내역남부")
                turnNode = "송내역"
            val turnNodePosition = routeList.indexOf(turnNode)
            routeList.forEachIndexed { index, s ->
                Log.d(LOG_TAG, s)
                // 시작
                if(index == 0){
                    adapter.addStop(s,1,1)
                }
                // 중간(회차전)
                else if(index < turnNodePosition){
                    adapter.addStop(s,1,3)
                }
                else if(turnNodePosition - index == 0){
                    adapter.addReturn()
                }
                // 끝
                else if(index == routeList.lastIndex){
                    adapter.addStop(s,2,2)
                }
                // 중간(회차후)
                else {
                    adapter.addStop(s,2,3)
                }
            }
        }


//        adapter.addStop("인천대입구", 1, 1)
//        for (i in 0..29) {
//            if (i == 15) {
//                adapter.addReturn()
//            }
//            adapter.addStop("인천대입구", 1 + i / 15, 3)
//        }
//        adapter.addStop("인천대입구", 2, 2)
        rv_route_activity_recycler.adapter = adapter
        //        rv.getRecycledViewPool().setMaxRecycledViews(0, 0);

    }
}
