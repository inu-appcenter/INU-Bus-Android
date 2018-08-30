package com.inu.bus

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.inu.bus.model.ArrivalFromNodeInfo
import com.inu.bus.model.ArrivalToNodeInfo
import com.inu.bus.model.BusInformation
import com.inu.bus.util.LocalIntent
import com.inu.bus.util.Singleton
import com.inu.bus.util.Singleton.LOG_TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by Minjae Son on 2018-08-07.
 */

class MyService : Service(){

    private val mBroadcastManager by lazy { LocalBroadcastManager.getInstance(applicationContext) }
    private var mTimer : Timer? = null
    private var currentTimerTask : TimerTask? = null

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        mBroadcastManager.registerReceiver(mBroadcastReceiver, IntentFilter(LocalIntent.FIRST_DATA_REQUEST.value))
        mBroadcastManager.registerReceiver(mBroadcastReceiver, IntentFilter(LocalIntent.ARRIVAL_DATA_REFRESH_REQUEST.value))
        mBroadcastManager.registerReceiver(mBroadcastReceiver, IntentFilter(LocalIntent.SERVICE_EXIT.value))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("test", "Service Started")
        requestNodeRoutes()
        requestArrivalInfo()
        startAutoRefresh()
        return START_STICKY_COMPATIBILITY
    }

    private fun startAutoRefresh(){
        currentTimerTask = newTimerTask()
        mTimer = Timer()
        mTimer!!.schedule(currentTimerTask,5000)
    }

    private fun newTimerTask() : TimerTask {return object : TimerTask() {
        override fun run() {
            requestArrivalInfo()
            currentTimerTask?.cancel()
            mTimer?.cancel()
            currentTimerTask = this
            mTimer = Timer()
            mTimer!!.schedule(newTimerTask(), 30000)
        }}
    }

    override fun onDestroy() {
        mBroadcastManager.unregisterReceiver(mBroadcastReceiver)
        Log.d("test", "Service Exit")
        super.onDestroy()
    }

    private fun requestNodeRoutes(){
        Singleton.retrofit.getNodeRoute().enqueue(object : Callback<ArrayList<BusInformation>>{
            override fun onFailure(call: Call<ArrayList<BusInformation>>, t: Throwable) {
                //TODO 에러 표시
                Log.e(LOG_TAG, "requestNodeRoutes", t)
//                t.printStackTrace()
            }
            override fun onResponse(call: Call<ArrayList<BusInformation>>, response: Response<ArrayList<BusInformation>>) {
                val newMap = mutableMapOf<String, BusInformation>()
                response.body()!!.forEach {
                    newMap[it.no] = it
                }
                Singleton.busInfo.set(newMap)
            }
        })
    }

    private fun requestArrivalInfo(callback: (() -> Unit)? = null){
        // TODO 콜백, 응답 단일처리
        Singleton.retrofit.getFromArrivalInfo().enqueue(object : Callback<ArrayList<ArrivalFromNodeInfo>>{
            override fun onFailure(call: Call<ArrayList<ArrivalFromNodeInfo>>, t: Throwable) {
                //TODO 에러 표시
                Log.e(LOG_TAG, "requestArrivalInfo getFrom", t)
//                stopSelf()
            }

            override fun onResponse(call: Call<ArrayList<ArrivalFromNodeInfo>>, response: Response<ArrayList<ArrivalFromNodeInfo>>) {
                Singleton.arrivalFromInfo.set(response.body())
                if(callback != null)
                    callback()
            }
        })

        Singleton.retrofit.getToArrivalInfo().enqueue(object : Callback<ArrayList<ArrivalToNodeInfo>>{
            override fun onFailure(call: Call<ArrayList<ArrivalToNodeInfo>>, t: Throwable) {
                //TODO 에러 표시
                Log.e(LOG_TAG, "requestArrivalInfo getTo", t)
//                stopSelf()
            }

            override fun onResponse(call: Call<ArrayList<ArrivalToNodeInfo>>, response: Response<ArrayList<ArrivalToNodeInfo>>) {
                Singleton.arrivalToInfo.set(response.body())
//                if(callback != null)
//                    callback()
            }
        })
    }

//    private fun bindBusInfo(){
//        Singleton.arrivalFromInfo!!.forEach { arrivalNode ->
//            arrivalNode.data.forEach { arrivalFromInfo ->
//                arrivalFromInfo.busInfo = Singleton.busInfo[arrivalFromInfo.no]
//            }
//        }
//    }

    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action){
                LocalIntent.FIRST_DATA_REQUEST.value -> {
                    Log.d("test", "Service received first data request")
                    if(Singleton.arrivalFromInfo.get() == null){
                        // TODO 에러 표시
                    }
                    else {
//                        bindBusInfo()
                        // FIXME 타이밍이 빨라 Fragment들이 수신 못하는 문제가 간혹 있음.
                        // Timer사용해서 재전송?
                        val newIntent = Intent(LocalIntent.FIRST_DATA_RESPONSE.value)
                        mBroadcastManager.sendBroadcast(newIntent)
                        Log.d("test", "Service sent first data response")
                    }
                }
                LocalIntent.SERVICE_EXIT.value -> {
                    mTimer?.cancel()
                    currentTimerTask?.cancel()
                    stopSelf()
                }
                LocalIntent.ARRIVAL_DATA_REFRESH_REQUEST.value -> {
                    Log.d("test", "Service received ARRIVAL_DATA_REFRESH_REQUEST")
//                    requestArrivalInfo{ mBroadcastManager.sendBroadcast(Intent(LocalIntent.ARRIVAL_DATA_REFRESHED.value)) }
                    requestArrivalInfo()
                }
            }
        }
    }
}


