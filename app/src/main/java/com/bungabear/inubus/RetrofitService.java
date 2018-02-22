package com.bungabear.inubus;

import com.google.gson.JsonArray;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("{path}")
    Call<JsonArray> getData(@Path("path") String path);

    @FormUrlEncoded
    @POST("errormsg")
    Call<ResponseBody> sendErrMsg(@Field("title") String title, @Field("contact") String contact, @Field("msg") String msg, @Field("device") String device);
}
