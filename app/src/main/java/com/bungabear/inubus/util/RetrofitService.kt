package com.bungabear.inubus.util

import com.bungabear.inubus.model.ArrivalInfo
import com.bungabear.inubus.model.BusInfoes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Minjae Son on 2018-08-07.
 */

interface RetrofitService{

    @GET("/arrivalInfo")
    fun getArrivalInfo() : Call<ArrivalInfo>

    @GET("/getNodes/")
    fun getNodeRoute() : Call<BusInfoes>

    @GET("/getNodes/{nodenum}")
    fun getNodeRoute(@Query("nodenum") no : String) : Call<BusInfoes>



//    @GET("/arrivalinfoSeoul")
//    fun getSeoulArrivalInfo()
}