package com.example.ratatouille23client;

import io.reactivex.internal.schedulers.RxThreadFactory;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.Url;

public class RetrofitInstance {

    //NEW
    public static String baseURL="http://";

    private static Retrofit retrofitInstance;

    public static Retrofit getRetrofitInstance(){

        if(retrofitInstance==null){
            retrofitInstance = new Retrofit.Builder()
                   // .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        return retrofitInstance;
    }
}
