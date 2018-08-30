package com.inu.bus.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import com.inu.bus.R
import com.inu.bus.databinding.ActivityRouteBinding
import com.inu.bus.recycler.RecyclerAdapterRoute
import com.inu.bus.recycler.RecyclerAdapterRoute.Direction
import com.inu.bus.recycler.RecyclerAdapterRoute.RouteType
import com.inu.bus.util.Singleton

/**
 * Created by Bunga on 2018-02-23.
 */

class RouteActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRouteBinding
    private lateinit var mRvRoute : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_route)
        mBinding.listener = this
        mRvRoute = mBinding.rvRouteActivityRecycler

        // 상단 정보 설정
        val routeNo = intent.getStringExtra("routeNo")
        val routeInfo = Singleton.busInfo.get()!![routeNo]!!
        mBinding.no = routeNo
        mBinding.startTime = String.format("%02d:%02d", routeInfo.start/100, routeInfo.start%100)
        mBinding.endTime = String.format("%02d:%02d", routeInfo.end/100, routeInfo.end%100)
        var fee : Int? = null

        Singleton.busCost.forEach { no, cost ->
            if(routeNo == no){
                fee = cost
            }
        }
        fee = fee?: Singleton.busCost["else"]
        mBinding.fee = "${fee}원"

        // 노선 정보 설정
        val routeList = routeInfo.nodeList
        val turnNode = routeInfo.turnNode
        val adapter = RecyclerAdapterRoute()

        // 회차지가 없는경우
        if(turnNode == ""){
            routeList.forEachIndexed { index, s ->
                when {
                    index != routeList.lastIndex -> {
                        adapter.addStop(s, Direction.RIGHT,RouteType.STOP)
                        adapter.addLine()
                    }
                    else ->   adapter.addStop(s, Direction.RIGHT,RouteType.STOP)
                }
            }
        }
        // 회차지가 있으면 동일한 String을 찾아가며 추가
        else {
            // TODO 시작 끝 구분 없어짐
            val turnNodePosition = routeList.indexOf(turnNode)
            routeList.forEachIndexed { index, s ->
                // 시작
                when {
                    index < turnNodePosition -> {
                        adapter.addStop(s, Direction.RIGHT,RouteType.STOP)
                        adapter.addLine()
                    }
                    // 회차지
                    turnNodePosition - index == 0 -> {
                        adapter.addStop(s, Direction.RIGHT,RouteType.STOP)
                        adapter.addReturn()
                        adapter.addStop(s, Direction.LEFT,RouteType.STOP)
                        adapter.addLine()
                    }
                    // 끝
                    else -> adapter.addStop(s, Direction.LEFT,RouteType.STOP)
                }
            }
        }

        mRvRoute.adapter = adapter
    }

    fun btnCloseClicked(){
        finish()
    }
}
