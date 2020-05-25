package com.deepankar.newsapp.model;

import android.content.Context;

import androidx.room.Room;

import com.deepankar.newsapp.db.AppDatabase;
import com.deepankar.newsapp.model.service.pojo.NewsAPIResponse;
import com.deepankar.newsapp.model.service.NewaApiInterface;
import com.deepankar.newsapp.model.service.NewsApiClient;

import retrofit2.Call;

public class HeadlinesModel {
    private NewaApiInterface service;


    public HeadlinesModel(Context context) {
        service = NewsApiClient.getRetrofitInstance(context).create(NewaApiInterface.class);
    }

    public Call<NewsAPIResponse> getTopHeadlines(String country, String category) {
        Call<NewsAPIResponse> call;
        if (country != null && !"".equals(country)) {
            if (category != null && !"".equals(category)) {
                call = service.getTopHeadlinesByCountryAndCategory(country, category);
            } else {
                call = service.getTopHeadlinesByCountry(country);
            }
        } else {
            call = service.getAllTopHeadlines();
        }
        return call;
    }
}
