package com.inu.bus.model

import android.graphics.Color
import com.google.gson.annotations.SerializedName

/**
 * Created by Minjae Son on 2018-08-25.
 */

// 버스 노선과 기본 정보를 가지는 객체
// /getNodes용
data class BusInformation (
        @SerializedName("no")
        val no : String,

        @SerializedName("routeid")
        val routeId : String,

        @SerializedName("start")
        val start : Int,

        @SerializedName("end")
        val end : Int,

        @SerializedName("type")
        val type : BusType,

        @SerializedName("nodelist")
        val nodeList : ArrayList<String>,

        @SerializedName("turnnode")
        val turnNode : String
){
    enum class BusType(val value: String, val color: Int) {
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
    }
}