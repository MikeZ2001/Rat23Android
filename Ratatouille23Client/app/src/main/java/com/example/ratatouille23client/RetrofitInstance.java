package com.example.ratatouille23client;

import io.reactivex.internal.schedulers.RxThreadFactory;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.Url;

public class RetrofitInstance {

    //public static String baseURL="http://192.168.1.16:8080/";

    //public static String baseURL="http://192.168.1.165:8080/";

    //OLD
   // public static String baseURL="http://ec2-100-25-193-144.compute-1.amazonaws.com:8080/";

    //NEW
    public static String baseURL="http://mikedns.eastus.cloudapp.azure.com:8080/";

    private static Retrofit retrofitInstance;

    public static Retrofit getRetrofitInstance(){

        if(retrofitInstance==null){
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        return retrofitInstance;
    }
}
