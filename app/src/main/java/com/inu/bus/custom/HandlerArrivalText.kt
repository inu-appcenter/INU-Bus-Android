package com.inu.bus.custom

import android.os.Handler
import android.os.Message
import android.widget.TextView

/**
 * Created by Minjae Son on 2018-08-25.
 */
class HandlerArrivalText(val textView : TextView) : Handler(){
    override fun handleMessage(msg: Message?) {
        super.handleMessage(msg)
        textView.text = "${msg!!.obj}"
    }
}