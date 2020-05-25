package com.deepankar.newsapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {NewsArticle.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NewsArticleDao getNewsArticleDao();
}
