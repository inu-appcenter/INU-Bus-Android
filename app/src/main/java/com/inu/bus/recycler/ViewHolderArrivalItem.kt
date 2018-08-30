package com.inu.bus.recycler

import android.content.Intent
import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.os.Message
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.inu.bus.activity.RouteActivity
import com.inu.bus.custom.HandlerArrivalText
import com.inu.bus.databinding.RecyclerArrivalItemBinding
import com.inu.bus.model.BusArrivalInfo
import java.util.*

/**
 * Created by Minjae Son on 2018-08-25.
 */

class ViewHolderArrivalItem(private val mBinding : RecyclerArrivalItemBinding, private val isShowing : ObservableBoolean) : RecyclerView.ViewHolder(mBinding.root) {

    private var needTick = false
    private var mTimer : Timer? = null
    private var currentTask : TimerTask? = null
    private fun newTimerTask() : TimerTask {return object : TimerTask() {
        override fun run() {
//            Log.d(Singleton.LOG_TAG, "$mStrBusStop : ${mBinding.data!!.no} is tick")
            currentTask = this
            sendTime(mBinding.data!!)
            if(needTick){
                mTimer?.cancel()
                mTimer = Timer()
                mTimer!!.schedule(newTimerTask(), 1000)
            }
        }}
    }
    private val mHandler by lazy { HandlerArrivalText(mBinding.recyclerArrival) }

    fun bind(data : BusArrivalInfo){
        mBinding.data = data
        mBinding.listener = this
        sendTime(mBinding.data!!)

        isShowing.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if(sender is ObservableBoolean){
                    if(sender.get()){
                        sendTime(mBinding.data!!)
                        startTick()
                    }
                    else {
                        stopTick()
                    }
                }
            }
        })
    }

    fun sendTime(data: BusArrivalInfo){
        var remain = (data.arrival - System.currentTimeMillis())/1000
        var str = ""

        while(remain < 0){
            remain += data.interval*60
        }
        while(remain > data.interval*60){
            remain -= data.interval*60
        }
        if(remain > 3600){
            str += "${remain/3600}시간 "
        }
        if(remain % 3600 >= 60L){
            str += "${(remain%3600)/60}분 "
        }

        if(str == ""){
            // 1분 미만은 잠시후
            str = "잠시후"
        }

        else if(remain % 60 != 0L)
        {
            str += "${remain%60}초"
        }

        val msg = Message()
        msg.obj = str
        mHandler.sendMessage(msg)
    }

    fun startTick(){
        needTick = true
        mTimer?.cancel()
        currentTask?.cancel()
        mTimer = Timer()
        mTimer!!.schedule(newTimerTask(), 1000)
    }

    fun stopTick(){
        needTick = false
        mTimer?.cancel()
        currentTask?.cancel()
    }

    fun onClick(data : BusArrivalInfo){
        Log.d("test", "OnClick called")
        val context = mBinding.root.context
        val intent = Intent(context, RouteActivity::class.java)
        intent.putExtra("routeNo", data.no)
        context.startActivity(intent)
    }
}