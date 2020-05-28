package com.deepankar.newsapp.model;

import android.content.Context;

import com.deepankar.newsapp.core.MyApplication;
import com.deepankar.newsapp.core.NetworkApi;
import com.deepankar.newsapp.core.component.DaggerMainActivityComponent;
import com.deepankar.newsapp.core.component.MainActivityComponent;
import com.deepankar.newsapp.core.module.HeadlinesFragmentModule;
import com.deepankar.newsapp.core.module.MainActivityModule;
import com.deepankar.newsapp.model.service.NewsApiClient;
import com.deepankar.newsapp.model.service.pojo.NewsAPIResponse;
import com.deepankar.newsapp.model.service.APIInterface;
import com.deepankar.newsapp.view.HeadlinesFragment;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Retrofit;

public class HeadlinesModel {
    private APIInterface service;

    @Inject
    Retrofit retrofit;

    public HeadlinesModel(Context context, HeadlinesFragment headlinesFragment) {
        //DaggerMainActivityComponent.builder().headlinesFragmentModule(new HeadlinesFragmentModule(headlinesFragment)).build();

        // ((MyApplication)headlinesFragment.getContext()).getAppComponent().injectHeadlinesFragment(headlinesFragment);

        //service = retrofit.create(APIInterface.class);
        service = NewsApiClient.getRetrofitInstance(context).create(APIInterface.class);
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
