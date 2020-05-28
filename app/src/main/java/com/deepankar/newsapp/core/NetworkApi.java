package com.deepankar.newsapp.core;

import android.content.Context;

import com.deepankar.newsapp.model.service.NetworkConnectionInterceptor;

import javax.inject.Inject;

import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkApi {
    private Retrofit retrofit;
    private static final String BASE_URL = "https://newsapi.org/v2/";

    @Inject
    public NetworkApi(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                //.addInterceptor(new NetworkConnectionInterceptor(context))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
