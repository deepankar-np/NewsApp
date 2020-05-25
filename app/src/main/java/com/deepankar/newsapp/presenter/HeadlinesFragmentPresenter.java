package com.deepankar.newsapp.presenter;

import android.content.Context;

import androidx.room.Room;

import com.deepankar.newsapp.contract.HeadlinesFragmentContract;
import com.deepankar.newsapp.db.AppDatabase;
import com.deepankar.newsapp.db.NewsArticleDao;
import com.deepankar.newsapp.db.NewsArticle;
import com.deepankar.newsapp.model.HeadlinesModel;
import com.deepankar.newsapp.model.service.NoConnectivityException;
import com.deepankar.newsapp.model.service.pojo.Article;
import com.deepankar.newsapp.model.service.pojo.NewsAPIResponse;
import com.deepankar.newsapp.model.service.pojo.Source;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
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
    private AppDatabase database;

    public HeadlinesFragmentPresenter(HeadlinesFragmentContract.View view, Context context) {
        this.view = view;
        this.model = new HeadlinesModel(context);
        this.database = Room.databaseBuilder(context, AppDatabase.class, "db-news-articles")
                //.allowMainThreadQueries()
                .build();
    }

    @Override
    public void loadNewsData(String country, String category) {
        view.showProgressBar();
        //showNewsDataView();
        Call<NewsAPIResponse> call = model.getTopHeadlines(country, category);
        call.enqueue(new Callback<NewsAPIResponse>() {
            @Override
            public void onResponse(Call<NewsAPIResponse> call, Response<NewsAPIResponse> response) {
                NewsAPIResponse responseBody = response.body();
                if (responseBody == null || responseBody.getArticles().size() == 0) {
                    view.showErrorMessage();
                } else {
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
//                    List<Article> articles = fetchNewsArticles();
//                    NewsAPIResponse responseBody = new NewsAPIResponse();
//                    responseBody.setArticles(articles);
//                    responseBody.setStatus("Success");
//                    responseBody.setTotalResults(articles.size());
//
//                    view.populateNewsData(responseBody);
//                    view.showNewsDataView();
                    fetchNewsArticlesFromDB();
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
                NewsArticleDao newsArticleDao = database.getNewsArticleDao();
                List<NewsArticle> newsArticles = new ArrayList<>();
                for (Article article :
                        articles) {
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
                    newsArticles.add(newsArticle);
                }
                if (newsArticles.size() > 0) {
                    newsArticleDao.deleteAll();
                    newsArticleDao.insert(newsArticles);
                }
            }

            private void fetchNewsArticlesFromDB() {
                NewsArticleDao newsArticleDao = database.getNewsArticleDao();
                newsArticleDao.getNewsArticles()
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
