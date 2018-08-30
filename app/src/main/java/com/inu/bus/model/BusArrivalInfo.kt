package com.inu.bus.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Minjae Son on 2018-08-25.
 */

// 각각의 버스 도착정보를 담을 객체
data class BusArrivalInfo(
        @SerializedName("no")
        val no : String = "",

        @SerializedName("arrival")
        var arrival : Long = -1,

        @SerializedName("start")
        var start : Int = -1,

        @SerializedName("end")
        var end : Int = -1,

        @SerializedName("interval")
        val interval : Int = -1,

        var intervalString : String = "",

        @SerializedName("type")
        val type : BusInformation.BusType? = null,

//            var busInfo : BusInformations.BusInformation?,
        var favorite : Boolean = false
        // TODO 버스 API 구분
)