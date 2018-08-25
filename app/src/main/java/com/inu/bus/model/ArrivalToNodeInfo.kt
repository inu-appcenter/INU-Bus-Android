package com.inu.bus.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Minjae Son on 2018-08-07.
 */

// 하교용 정류장 name을 가진 객체. ArrayList로 응답 수신
data class ArrivalToNodeInfo(
        @SerializedName("id")
        val id: ID,

        @SerializedName("data")
        val data: ArrayList<BusArrivalInfo>
)
{
//    ICB164000373 : 지식정보단지 2번출구 정류장
//    ICB164000403 :'지식정보단지 3번출구 정류장
//    ICB164000380 : 지식정보단지 4번출구 정류장
//    ICB164000395 : 인천대입구 2번출구 정류장
    enum class ID{
        ICB164000373, ICB164000403, ICB164000380, ICB164000395
    }
}