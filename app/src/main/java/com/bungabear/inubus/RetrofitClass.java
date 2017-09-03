package com.bungabear.inubus;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by minjae on 2017-08-10.
 */
// TODO 인천 버스외에 광역버스도 추가해야한다. 3001, 1301 등
class RetrofitClass{
    private static final String TAG = "Retrofit Debug";
    private static RetrofitClass instance;
    private static Retrofit retrofit = null;
    private static RetrofitService retrofitService = null;

    // 디버깅용으로 주석을 바꾸는게 불편해 플래그로 만듦.
    private static final boolean DEBUG = false;

    private RetrofitClass(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://117.16.231.66:1337/")
                .addConverterFactory(GsonConverterFactory.create());

        if(DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            builder.client(client);
        }
        retrofit = builder.build();
        retrofitService = retrofit.create(RetrofitService.class);
    }

    public static RetrofitService getInstance(){
        if(instance == null){
            instance = new RetrofitClass();
        }
        return instance.retrofitService;
    }
}
