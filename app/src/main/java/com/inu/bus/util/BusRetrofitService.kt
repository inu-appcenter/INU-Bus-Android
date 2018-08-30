package com.inu.bus.util

import com.inu.bus.model.ArrivalFromNodeInfo
import com.inu.bus.model.ArrivalToNodeInfo
import com.inu.bus.model.BusInformation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Minjae Son on 2018-08-07.
 */

interface BusRetrofitService{

    @GET("/arrivalInfoFrom")
    fun getFromArrivalInfo() : Call<ArrayList<ArrivalFromNodeInfo>>

    @GET("/arrivalInfoTo")
    fun getToArrivalInfo() : Call<ArrayList<ArrivalToNodeInfo>>


    @GET("/getNodes")
    fun getNodeRoute() : Call<ArrayList<BusInformation>>

    @GET("/getNodes/{nodenum}")
    fun getNodeRoute(@Query("nodenum") no : String) : Call<ArrayList<BusInformation>>

//    @GET("/arrivalinfoSeoul")
//    fun getSeoulArrivalInfo()
}