package com.deepankar.newsapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface NewsArticleDao {
    @Insert
    public void insert(List<NewsArticle> newsArticles);

    @Update
    public void update(NewsArticle... newsArticles);

    @Delete
    public void delete(NewsArticle... newsArticles);

    @Query("DELETE FROM news_article")
    public void deleteAll();

    @Query("SELECT * FROM news_article")
    public Single<List<NewsArticle>> getNewsArticles();
}
