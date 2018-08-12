package com.bungabear.inubus.util

import com.bungabear.inubus.Config
import com.bungabear.inubus.model.ArrivalInfoModel
import com.bungabear.inubus.model.BusInfomations
import com.bungabear.inubus.util.Singleton.myPackageName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by Minjae Son on 2018-08-07.
 */

object Singleton{
    val retrofit = Retrofit.Builder().baseUrl(Config.serverAddr).addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitService::class.java)
    var CleanStart = true
    val myPackageName = "com.bungabear.inubus"
    val busInfo  = mutableMapOf<String, BusInfomations.BusInformation>()
    var arrivalInfo : ArrivalInfoModel? = null
    const val LOG_TAG = "INU Bus"
    const val DB_VERSTION = 1
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