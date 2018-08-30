package com.inu.bus.util

import android.app.Activity
import android.databinding.ObservableField
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.inu.bus.Config
import com.inu.bus.model.ArrivalFromNodeInfo
import com.inu.bus.model.ArrivalToNodeInfo
import com.inu.bus.model.BusInformation
import com.inu.bus.util.Singleton.myPackageName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by Minjae Son on 2018-08-07.
 */

object Singleton{
    val retrofit = Retrofit.Builder().baseUrl(Config.serverAddr).addConverterFactory(GsonConverterFactory.create()).build().create(BusRetrofitService::class.java)
    val msgRetrofit = Retrofit.Builder().baseUrl(Config.msgServerAddr).addConverterFactory(GsonConverterFactory.create()).build().create(MsgRetrofitService::class.java)
    val busInfo  = ObservableField(mutableMapOf<String, BusInformation>())
    var arrivalFromInfo = ObservableField<ArrayList<ArrivalFromNodeInfo>>()
    var arrivalToInfo = ObservableField<ArrayList<ArrivalToNodeInfo>>()
    const val myPackageName = "com.bungabear.inubus"
    const val LOG_TAG = "INU Bus"
    const val DB_VERSION = 1

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

enum class LocalIntent(val value : String) : CharSequence by value {
    FIRST_DATA_REQUEST("$myPackageName.FIRST_DATA_REQUEST"),
    FIRST_DATA_RESPONSE("$myPackageName.FIRST_DATA_RESPONSE"),
    ARRIVAL_DATA_REFRESH_REQUEST("$myPackageName.ARRIVAL_DATA_REFRESH_REQUEST"),
    ARRIVAL_DATA_REFRESHED("$myPackageName.ARRIVAL_DATA_REFRESHED"),
    SERVICE_EXIT("$myPackageName.SERVICE_EXIT"),
    NOTIFY_FRAGMENT_READY("$myPackageName.NOTIFY_FRAGMENT_READY");

    override fun toString() = value
}