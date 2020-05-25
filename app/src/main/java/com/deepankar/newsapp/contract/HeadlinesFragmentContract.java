package com.deepankar.newsapp.contract;

import com.deepankar.newsapp.contract.base.BasePresenter;
import com.deepankar.newsapp.contract.base.BaseView;
import com.deepankar.newsapp.model.service.pojo.NewsAPIResponse;

public interface HeadlinesFragmentContract {
    interface View extends BaseView {
        void showNewsDataView();
        void showErrorMessage();
        void showProgressBar();
        void hideProgressBar();
        void showToastMessage(String message);
        void populateNewsData(NewsAPIResponse responseBody);
    }

    interface Presenter extends BasePresenter {
        void loadNewsData(String country, String category);
    }
}
