package com.deepankar.newsapp.presenter;

import android.content.Intent;

import com.deepankar.newsapp.contract.ShortNewsActivityContract;
import com.deepankar.newsapp.model.service.pojo.Article;
import com.deepankar.newsapp.utils.StringUtils;

public class ShortNewsActivityPresenter implements ShortNewsActivityContract.Presenter {
    private ShortNewsActivityContract.View view;
    private Article article;

    public ShortNewsActivityPresenter(ShortNewsActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void openNews(Intent intent) {
        article = (Article) intent.getSerializableExtra("article");
        view.setNewsTitle(StringUtils.getNotNullString(article.getTitle()));
        String publishedAt = article.getPublishedAt();
        if (publishedAt != null) {
            view.setNewsTime(StringUtils.getDateTimeString(publishedAt));
        }
        view.setNewsSource(StringUtils.getNotNullString(article.getSource().getName()));
        String imageUrl = article.getUrlToImage();
        if (!StringUtils.isEmpty(imageUrl)) {
            view.loadNewsImage(imageUrl);
        }

        String content = (String) article.getContent();
        if (content != null && !content.isEmpty()) {
            view.showShortNews(content);
        } else {
            view.showFullNews(article);
        }

        if (article.getUrl() == null) {
            view.hideNewsButton();
        } else {
            view.showNewsButton(article);
        }
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
