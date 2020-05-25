package com.deepankar.newsapp.model;

import android.content.Context;

import com.deepankar.newsapp.model.service.NewaApiInterface;
import com.deepankar.newsapp.model.service.NewsApiClient;
import com.deepankar.newsapp.model.service.pojo.NewsAPIResponse;

import retrofit2.Call;

public class SearchModel {
    private NewaApiInterface service;

    public SearchModel(Context context) {
        service = NewsApiClient.getRetrofitInstance(context).create(NewaApiInterface.class);
    }

    public Call<NewsAPIResponse> searchNews(String searchText) {
        return service.getEverything(searchText);
    }
}
