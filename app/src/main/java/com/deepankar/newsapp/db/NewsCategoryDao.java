package com.deepankar.newsapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface NewsCategoryDao {
    @Insert
    public void insert(List<NewsCategory> newsCategories);

    @Update
    public void update(NewsCategory... newsCategories);

    @Delete
    public void delete(NewsCategory... newsCategories);

    @Query("DELETE FROM news_category")
    public void deleteAll();

    @Query("SELECT * FROM news_category order by sequence")
    public Single<List<NewsCategory>> getNewsCategories();

    @Query(value = "SELECT * FROM news_category where isEnabled = 1 and nameId != -1")
    public List<NewsCategory> getEnabledNewsCategories();
}
