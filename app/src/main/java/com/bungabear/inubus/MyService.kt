package com.bungabear.inubus

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.bungabear.inubus.model.ArrivalInfo
import com.bungabear.inubus.model.BusInfoes
import com.bungabear.inubus.util.LocalIntent
import com.bungabear.inubus.util.Singleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Minjae Son on 2018-08-07.
 */

class MyService : Service(){

    private val mBroadcastManager by lazy { LocalBroadcastManager.getInstance(applicationContext) }


    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        mBroadcastManager.registerReceiver(broadcastReceiver, IntentFilter(LocalIntent.FIRST_DATA_REQUEST.value))
        mBroadcastManager.registerReceiver(broadcastReceiver, IntentFilter(LocalIntent.ARRIVAL_DATA_REFRESH_REQUEST.value))
        mBroadcastManager.registerReceiver(broadcastReceiver, IntentFilter(LocalIntent.SERVICE_EXIT.value))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("test", "Service Started")
        if(Singleton.CleanStart){
            requestNodeRoutes()
        }
        requestArrivalInfo()

        return START_STICKY_COMPATIBILITY
    }

    override fun onDestroy() {
        mBroadcastManager.unregisterReceiver(broadcastReceiver)
        Log.d("test", "Service Exit")
        super.onDestroy()
    }

    private fun requestNodeRoutes(){
        Singleton.retrofit.getNodeRoute().enqueue(object : Callback<BusInfoes>{
            override fun onFailure(call: Call<BusInfoes>, t: Throwable) {
                //TODO 에러 표시
                t.printStackTrace()
                stopSelf()
            }
            override fun onResponse(call: Call<BusInfoes>, response: Response<BusInfoes>) {
                response.body()!!.forEach {
                    Singleton.busInfo[it.no] = it
                }
            }
        })
    }

    private fun requestArrivalInfo(callback: (() -> Unit)? = null){
        Singleton.retrofit.getArrivalInfo().enqueue(object : Callback<ArrivalInfo>{
            override fun onFailure(call: Call<ArrivalInfo>, t: Throwable) {
                //TODO 에러 표시
                t.printStackTrace()
                stopSelf()
            }

            override fun onResponse(call: Call<ArrivalInfo>, response: Response<ArrivalInfo>) {
                Singleton.arrivalInfo = response.body()
                if(callback != null)
                    callback()
            }
        })
    }

//    private fun bindBusInfo(){
//        Singleton.arrivalInfo!!.forEach { arrivalNode ->
//            arrivalNode.data.forEach { arrivalInfo ->
//                arrivalInfo.busInfo = Singleton.busInfo[arrivalInfo.no]
//            }
//        }
//    }

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action){
                LocalIntent.FIRST_DATA_REQUEST.value -> {
                    Log.d("test", "Service received firstdatarequest")
                    if(Singleton.arrivalInfo == null){
                        // TODO 에러 표시
                    }
                    else {
//                        bindBusInfo()
                        val newIntent = Intent(LocalIntent.FIRST_DATA_RESPONSE.value)
                        mBroadcastManager.sendBroadcast(newIntent)
                        Log.d("test", "Service sent firstdataresponse")
                    }
                }
                LocalIntent.SERVICE_EXIT.value -> {
                    Singleton.CleanStart = false
                    stopSelf()
                }
                LocalIntent.ARRIVAL_DATA_REFRESH_REQUEST.value -> {
                    Log.d("test", "Service received ARRIVAL_DATA_REFRESH_REQUEST")
                    requestArrivalInfo{ mBroadcastManager.sendBroadcast(Intent(LocalIntent.ARRIVAL_DATA_REFRESHED.value)) }
                }
            }
        }
    }
}


