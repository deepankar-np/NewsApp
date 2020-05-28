package com.deepankar.newsapp.presenter;

import android.content.Context;

import com.deepankar.newsapp.contract.HeadlinesFragmentContract;
import com.deepankar.newsapp.db.NewsArticle;
import com.deepankar.newsapp.db.NewsArticleDBUtil;
import com.deepankar.newsapp.model.HeadlinesModel;
import com.deepankar.newsapp.model.service.NoConnectivityException;
import com.deepankar.newsapp.model.service.pojo.Article;
import com.deepankar.newsapp.model.service.pojo.NewsAPIResponse;
import com.deepankar.newsapp.model.service.pojo.Source;
import com.deepankar.newsapp.utils.StringUtils;
import com.deepankar.newsapp.view.HeadlinesFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HeadlinesFragmentPresenter implements HeadlinesFragmentContract.Presenter {
    private HeadlinesFragmentContract.View view;
    private HeadlinesModel model;
    private NewsArticleDBUtil dbUtil;

    public HeadlinesFragmentPresenter(HeadlinesFragmentContract.View view, Context context, HeadlinesFragment headlinesFragment) {
        this.view = view;
        this.model = new HeadlinesModel(context, headlinesFragment);
        this.dbUtil = new NewsArticleDBUtil(context);
    }

    @Override
    public void loadNewsData(final String country, final String category) {
        view.showProgressBar();
        //showNewsDataView();
        final Call<NewsAPIResponse> call = model.getTopHeadlines(country, category);
        call.enqueue(new Callback<NewsAPIResponse>() {
            @Override
            public void onResponse(Call<NewsAPIResponse> call, Response<NewsAPIResponse> response) {
                NewsAPIResponse responseBody = response.body();
                if (responseBody == null || responseBody.getArticles().size() == 0) {
                    view.showErrorMessage();
                } else {
                    String searchKey = StringUtils.getNotNullString(country) + "_" + StringUtils.getNotNullString(category);
                    for (Article article : responseBody.getArticles()) {
                        article.setSearchKey(searchKey);
                    }
                    //save data in DB
                    createNewThreadAndSave(responseBody.getArticles());

                    view.populateNewsData(responseBody);
                    view.showNewsDataView();
                }
            }

            @Override
            public void onFailure(Call<NewsAPIResponse> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    // show No Connectivity message to user or do whatever you want.
                    view.showToastMessage(t.getMessage());
                    //fetch data from DB
                    String searchKey = StringUtils.getNotNullString(country) + "_" + StringUtils.getNotNullString(category);
                    fetchNewsArticlesFromDB(searchKey);
                } else {
                    view.showToastMessage("Something went wrong...Please try later!");
                }
            }

            private void createNewThreadAndSave(final List<Article> articles) {
                Single
                        .create(new SingleOnSubscribe<List<Article>>() {
                            @Override
                            public void subscribe(SingleEmitter<List<Article>> emitter) throws Exception {
                                emitter.onSuccess(articles);
                            }
                        })
                        .observeOn(Schedulers.io())
                        .subscribeOn(Schedulers.computation())
                        .subscribe(new SingleObserver<List<Article>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(List<Article> articles) {
                                saveNewsArticles(articles);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
            }

            private void saveNewsArticles(final List<Article> articles) {
                List<NewsArticle> newsArticles = new ArrayList<>();
                for (Article article : articles) {
                    NewsArticle newsArticle = new NewsArticle();
                    Source source = article.getSource();
                    if (source != null) {
                        newsArticle.setSourceId(source.getId());
                        newsArticle.setSourceName(source.getName());
                    }
                    newsArticle.setAuthor(article.getAuthor());
                    newsArticle.setContent((String) article.getContent());
                    newsArticle.setDescription(article.getDescription());
                    newsArticle.setPublishedAt(article.getPublishedAt());
                    newsArticle.setTitle(article.getTitle());
                    newsArticle.setUrl(article.getUrl());
                    newsArticle.setUrlToImage(article.getUrlToImage());
                    newsArticle.setSearchKey(article.getSearchKey());

                    newsArticles.add(newsArticle);
                }
                if (newsArticles.size() > 0) {
                    String searchKey = newsArticles.get(0).getSearchKey();
                    dbUtil.deleteAndSaveNewsArticles(newsArticles, searchKey);
                }
            }

            private void fetchNewsArticlesFromDB(String searchKey) {
                dbUtil.getNewsArticles(searchKey)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<NewsArticle>>() {
                            @Override
                            public void accept(List<NewsArticle> newsArticles) throws Exception {
                                List<Article> articles = new ArrayList<>();
                                for (NewsArticle newsArticle :
                                        newsArticles) {
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

                                    articles.add(article);
                                }
                                NewsAPIResponse responseBody = new NewsAPIResponse();
                                responseBody.setArticles(articles);
                                responseBody.setStatus("Success");
                                responseBody.setTotalResults(articles.size());

                                view.populateNewsData(responseBody);
                                view.showNewsDataView();
                            }
                        });
            }
        });
    }
}
