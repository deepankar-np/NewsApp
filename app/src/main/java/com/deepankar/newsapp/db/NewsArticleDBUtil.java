package com.deepankar.newsapp.db;

import android.content.Context;

import androidx.room.Room;

import java.util.List;

import io.reactivex.Single;

public class NewsArticleDBUtil {
    private AppDatabase database;

    public NewsArticleDBUtil(Context context){
        this.database = Room.databaseBuilder(context, AppDatabase.class, "db-news-articles")
                //.allowMainThreadQueries()
                .build();
    }

    public Single<List<NewsArticle>> getNewsArticles(String searchKey){
        NewsArticleDao newsArticleDao = database.getNewsArticleDao();
        return newsArticleDao.getNewsArticles(searchKey);
    }

    public void deleteAndSaveNewsArticles(List<NewsArticle> newsArticles, String searchKey){
        NewsArticleDao newsArticleDao = database.getNewsArticleDao();
        newsArticleDao.deleteAllByCountryAndCategory(searchKey);
        newsArticleDao.insert(newsArticles);
    }
}
