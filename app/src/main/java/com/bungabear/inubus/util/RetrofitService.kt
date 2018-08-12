package com.bungabear.inubus.util

import com.bungabear.inubus.model.ArrivalInfoModel
import com.bungabear.inubus.model.BusInfomations
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Minjae Son on 2018-08-07.
 */

interface RetrofitService{

    @GET("/arrivalInfo")
    fun getArrivalInfo() : Call<ArrivalInfoModel>

    @GET("/getNodes/")
    fun getNodeRoute() : Call<BusInfomations>

    @GET("/getNodes/{nodenum}")
    fun getNodeRoute(@Query("nodenum") no : String) : Call<BusInfomations>



//    @GET("/arrivalinfoSeoul")
//    fun getSeoulArrivalInfo()
}