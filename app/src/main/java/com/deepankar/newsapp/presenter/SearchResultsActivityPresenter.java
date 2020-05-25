package com.deepankar.newsapp.presenter;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;

import com.deepankar.newsapp.contract.SearchResultActivityContract;
import com.deepankar.newsapp.model.SearchModel;
import com.deepankar.newsapp.model.service.pojo.NewsAPIResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultsActivityPresenter implements SearchResultActivityContract.Presenter {
    private SearchResultActivityContract.View view;
    private String searchTerm;
    private SearchModel model;

    public SearchResultsActivityPresenter(SearchResultActivityContract.View view, Context context) {
        this.view = view;
        this.model = new SearchModel(context);
    }

    @Override
    public void handleIntent(Intent intent) {
        String query = intent.getStringExtra(SearchManager.QUERY);
        searchTerm = query;
        //use the query to search your data somehow
        loadNewsData();
    }

    @Override
    public void updateNewsData(String searchTerm) {
        this.searchTerm = searchTerm;
        //use the query to search your data somehow
        loadNewsData();
    }

    @Override
    public void loadNewsData() {
        view.showProgressBar();
        view.setActionBarTitle(searchTerm);
        //showNewsDataView();
        Call<NewsAPIResponse> call = model.searchNews(searchTerm);
        call.enqueue(new Callback<NewsAPIResponse>() {
            @Override
            public void onResponse(Call<NewsAPIResponse> call, Response<NewsAPIResponse> response) {
                NewsAPIResponse responseBody = response.body();
                if(responseBody == null || responseBody.getArticles().size() == 0){
                    view.showErrorMessage("No news found with keyword : " + searchTerm);
                    view.showErrorMessage();
                }else{
                    view.populateNewsData(responseBody);
                    view.showNewsDataView();
                }
            }

            @Override
            public void onFailure(Call<NewsAPIResponse> call, Throwable t) {
                view.showToastMessage("Something went wrong...Please try later!");
            }
        });
    }
}
