package com.deepankar.newsapp.contract;

import android.content.Intent;

import com.deepankar.newsapp.contract.base.BasePresenter;
import com.deepankar.newsapp.contract.base.BaseView;

public interface FullNewsActivityContract {
    interface View extends BaseView {
        void loadUrl(String url);
    }

    interface Presenter extends BasePresenter {
        void openNews(Intent intent);
        String getArticleUrl();
        String getArticleTitle();
    }
}
