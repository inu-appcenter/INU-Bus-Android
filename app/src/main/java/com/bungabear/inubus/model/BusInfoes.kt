package com.bungabear.inubus.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Minjae Son on 2018-08-07.
 */
class BusInfoes : ArrayList<BusInfoes.BusInfo> (

){
    data class BusInfo (
            @SerializedName("no")
            val no : String,
//        val routeId : String,
//        val start : Int,
//        val end : Int,
//        val type : String,
            @SerializedName("nodelist")
            val nodeList : ArrayList<String>,

            @SerializedName("turnnode")
            val turnNode : String
    )
}