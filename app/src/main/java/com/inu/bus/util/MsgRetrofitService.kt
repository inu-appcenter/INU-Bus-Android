package com.inu.bus.util

import com.inu.bus.model.InquireModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Minjae Son on 2018-08-25.
 */

interface MsgRetrofitService{
    @POST("/errormsg")
    fun postErrorMsg(@Body message : InquireModel) : Call<ResponseBody>
}