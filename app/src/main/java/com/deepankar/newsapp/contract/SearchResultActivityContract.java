package com.deepankar.newsapp.contract;

import android.content.Intent;

import com.deepankar.newsapp.contract.base.BasePresenter;
import com.deepankar.newsapp.contract.base.BaseView;
import com.deepankar.newsapp.model.service.pojo.NewsAPIResponse;

public interface SearchResultActivityContract {
    interface View extends BaseView {
        void showNewsDataView();
        void showErrorMessage();
        void showProgressBar();
        void hideProgressBar();
        void showToastMessage(String message);
        void populateNewsData(NewsAPIResponse responseBody);
        void setActionBarTitle(String title);
        void showErrorMessage(String message);
    }

    interface Presenter extends BasePresenter {
        void handleIntent(Intent intent);
        void loadNewsData();
        void updateNewsData(String searchTerm);
    }
}
