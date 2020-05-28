package com.deepankar.newsapp.model.service;

import com.deepankar.newsapp.model.service.pojo.NewsAPIResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {
    String TOP_HEADLINES =
            "top-headlines";

    String apiKey = "e8372d5f7e014630aacc7905bb0a845c";

    @GET(TOP_HEADLINES + "?language=en&apiKey=" + apiKey)
    Call<NewsAPIResponse> getAllTopHeadlines();

    @GET(TOP_HEADLINES + "?apiKey=" + apiKey)
    Call<NewsAPIResponse> getTopHeadlinesByCountry(@Query("country") String country);

    @GET(TOP_HEADLINES + "?apiKey=" + apiKey)
    Call<NewsAPIResponse> getTopHeadlinesByCountryAndCategory(@Query("country") String country, @Query("category") String category);

    @GET(TOP_HEADLINES + "?apiKey=" + apiKey)
    Call<NewsAPIResponse> getEverything(@Query("q") String searchTerm);
}
