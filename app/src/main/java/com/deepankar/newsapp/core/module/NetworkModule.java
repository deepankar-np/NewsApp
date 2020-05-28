package com.deepankar.newsapp.core.module;

import android.content.Context;

import com.deepankar.newsapp.core.MyApplication;
import com.deepankar.newsapp.model.service.NetworkConnectionInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    //private Retrofit retrofit;
    private static final String BASE_URL = "https://newsapi.org/v2/";

    @Provides @Singleton
    Retrofit getRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                //.addInterceptor(new NetworkConnectionInterceptor(context))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

}
