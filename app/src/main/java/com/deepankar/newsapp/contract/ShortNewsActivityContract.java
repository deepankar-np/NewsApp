package com.deepankar.newsapp.contract;

import android.content.Intent;

import com.deepankar.newsapp.contract.base.BasePresenter;
import com.deepankar.newsapp.contract.base.BaseView;
import com.deepankar.newsapp.model.service.pojo.Article;

public interface ShortNewsActivityContract {
    interface View extends BaseView {
        void setNewsTitle(String title);
        void setNewsTime(String time);
        void setNewsSource(String source);
        void loadNewsImage(String imageUrl);
        void showShortNews(String content);
        void showFullNews(Article article);
        void hideNewsButton();
        void showNewsButton(Article article);
    }

    interface Presenter extends BasePresenter {
        void openNews(Intent intent);
        String getArticleUrl();
        String getArticleTitle();


    }
}
