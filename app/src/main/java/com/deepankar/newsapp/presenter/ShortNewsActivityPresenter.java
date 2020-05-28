package com.deepankar.newsapp.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.deepankar.newsapp.contract.ShortNewsActivityContract;
import com.deepankar.newsapp.db.NewsArticle;
import com.deepankar.newsapp.db.NewsArticleDBUtil;
import com.deepankar.newsapp.model.service.pojo.Article;
import com.deepankar.newsapp.model.service.pojo.NewsAPIResponse;
import com.deepankar.newsapp.model.service.pojo.Source;
import com.deepankar.newsapp.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ShortNewsActivityPresenter implements ShortNewsActivityContract.Presenter {
    private ShortNewsActivityContract.View view;
    private Article article;

    private NewsArticleDBUtil dbUtil;
    private Context context;

    public ShortNewsActivityPresenter(ShortNewsActivityContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.dbUtil = new NewsArticleDBUtil(context);
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

    @Override
    public void openNextNews(Intent intent) {
        int position = intent.getIntExtra("position", -1);
        fetchNewsArticlesFromDB(position);
    }

    private void fetchNewsArticlesFromDB(final int position) {
        dbUtil.getNewsArticles(article.getSearchKey())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<NewsArticle>>() {
                    @Override
                    public void accept(List<NewsArticle> newsArticles) throws Exception {
                        int newPosition = position+1;
                        if(newsArticles.size()>newPosition){
                            NewsArticle newsArticle = newsArticles.get(newPosition);
                            Article article = new Article();
                            article.setAuthor(newsArticle.getAuthor());
                            article.setContent(newsArticle.getContent());
                            article.setDescription(newsArticle.getDescription());
                            article.setPublishedAt(newsArticle.getPublishedAt());
                            article.setTitle(newsArticle.getTitle());
                            article.setUrl(newsArticle.getUrl());
                            article.setUrlToImage(newsArticle.getUrlToImage());
                            Source source = new Source();
                            source.setId(newsArticle.getSourceId());
                            source.setName(newsArticle.getSourceName());
                            article.setSource(source);
                            article.setSearchKey(newsArticle.getSearchKey());
                            view.openNextArticle(article, newPosition);
                        }else{
                            Toast.makeText(context, "That's All!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
