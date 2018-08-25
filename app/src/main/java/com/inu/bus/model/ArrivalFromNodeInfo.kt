package com.inu.bus.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Minjae Son on 2018-08-07.
 */

// 하교용 정류장 name을 가진 객체. ArrayList로 응답 수신
data class ArrivalFromNodeInfo(
        @SerializedName("name")
        val name: String,

        @SerializedName("data")
        val data: ArrayList<BusArrivalInfo>
)
