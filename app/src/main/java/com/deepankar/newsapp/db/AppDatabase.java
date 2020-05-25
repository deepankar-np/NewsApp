package com.deepankar.newsapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {NewsArticle.class, NewsCategory.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NewsArticleDao getNewsArticleDao();
    public abstract NewsCategoryDao getNewsCategoryDao();
}
