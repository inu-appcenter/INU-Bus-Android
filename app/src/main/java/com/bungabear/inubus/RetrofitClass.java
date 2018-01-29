package com.bungabear.inubus;

import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClass
{
    private static final boolean DEBUG = false;
    private static final String TAG = "Retrofit Debug";
    private static RetrofitClass instance;
    private static Retrofit retrofit = null;
    private static RetrofitService retrofitService = null;

    private RetrofitClass()
    {
        retrofit = new Retrofit
                .Builder()
                .baseUrl(KeyClass.serverAddr)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = (RetrofitService)retrofit.create(RetrofitService.class);
    }

    public static RetrofitService getInstance()
    {
        if (instance == null) {
            instance = new RetrofitClass();
        }
        RetrofitClass localRetrofitClass = instance;
        return retrofitService;
    }
}
