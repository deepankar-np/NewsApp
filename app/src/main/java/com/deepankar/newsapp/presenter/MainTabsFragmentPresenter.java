package com.deepankar.newsapp.presenter;

import android.content.Context;

import androidx.room.Room;

import com.deepankar.newsapp.R;
import com.deepankar.newsapp.db.AppDatabase;
import com.deepankar.newsapp.db.NewsCategory;
import com.deepankar.newsapp.db.NewsCategoryDao;
import com.deepankar.newsapp.db.NewsCategoryData;
import com.deepankar.newsapp.view.HeadlinesFragment;
import com.deepankar.newsapp.contract.MainTabsFragmentContract;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainTabsFragmentPresenter implements MainTabsFragmentContract.Presenter {
    private MainTabsFragmentContract.View view;
    private AppDatabase database;

    public MainTabsFragmentPresenter(MainTabsFragmentContract.View view, Context context) {
        this.view = view;
        this.database = Room.databaseBuilder(context, AppDatabase.class, "db-news-categories")
                .allowMainThreadQueries()
                .build();
    }

    @Override
    public void addHeadlinesFragments() {
        final NewsCategoryDao dao = database.getNewsCategoryDao();
        List<NewsCategory> newsCategories = dao.getEnabledNewsCategories();
        for (NewsCategory newsCategory : newsCategories) {
            if (newsCategory.getNameId() != -1) {
                view.addHeadlinesFragmentToAdapter(new HeadlinesFragment(newsCategory.getCountry(), newsCategory.getCategory(), newsCategory.getSequence()), newsCategory.getNameId());
            }
        }
    }
}
