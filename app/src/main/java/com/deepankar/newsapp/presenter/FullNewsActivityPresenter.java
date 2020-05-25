package com.deepankar.newsapp.presenter;

import android.content.Intent;

import com.deepankar.newsapp.contract.FullNewsActivityContract;
import com.deepankar.newsapp.model.service.pojo.Article;

public class FullNewsActivityPresenter implements FullNewsActivityContract.Presenter {
    private FullNewsActivityContract.View view;
    private Article article;

    public FullNewsActivityPresenter(FullNewsActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void openNews(Intent intent) {
        article = (Article) intent.getSerializableExtra("article");
        view.loadUrl(getArticleUrl());
    }

    @Override
    public String getArticleUrl() {
        return article.getUrl();
    }

    @Override
    public String getArticleTitle() {
        return article.getTitle();
    }
}
