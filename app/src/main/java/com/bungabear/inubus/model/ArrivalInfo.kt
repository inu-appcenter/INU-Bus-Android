package com.bungabear.inubus.model

import android.graphics.Color
import com.google.gson.annotations.SerializedName

/**
 * Created by Minjae Son on 2018-08-07.
 */

// 전체 익명 배열을 받을 객체. empty constructor 위해 class로 선언
class ArrivalInfo() : ArrayList<ArrivalInfo.ArrivalNodeInfo>()
{

    // 정류장 구분 enum
    enum class BusStop(val hangul : String){
        @SerializedName("frontgate")
        FrontGate("정문"),

        @SerializedName("science")
        Science("자과대"),

        @SerializedName("engineer")
        Engineer("공대"),

        @SerializedName("biz")
        BITZon("지정단")
    }


    enum class BusType(val value: String, val color: Int){
        @SerializedName("간선")
        BLUE("간선", Color.parseColor("#2f60ce")),

        @SerializedName("간선급행")
        BRT("간선급행", Color.parseColor("#8d14bf")),

        @SerializedName("광역")
        RED("광역", Color.parseColor("#de2222")),

        @SerializedName("광역급행")
        RED_EXPRESS("광역급행", Color.parseColor("#33b5e5")),

        @SerializedName("순환")
        YELLOW("순환", Color.parseColor("#22c244"));

        companion object {
            fun fromOrdinal(ordinal : Int) : BusType =  BusType.values()[ordinal]
            fun eachToEnd( type : BusType , callback : (BusType)-> Unit){
                for(ordinal in type.ordinal until values().size){
                    callback(fromOrdinal(ordinal))
                }
            }
        }
    }

    // 각 화면에 보일 버스 도착 정보들을 가진 정류장 객체
    data class ArrivalNodeInfo(
            @SerializedName("name")
            val name : String,

            @SerializedName("data")
            val data : ArrayList<BusArrivalInfo>
    )

    // 각 화면에 보일 버스 도착 정보 객체
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

            @SerializedName("type")
            val type : BusType? = null,

//            var busInfo : BusInfoes.BusInfo?,
            var favorite : Boolean = false
            // TODO 버스 API 구분
    )
}