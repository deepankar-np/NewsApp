package com.deepankar.newsapp.presenter;

import android.content.Context;

import androidx.room.Room;

import com.deepankar.newsapp.R;
import com.deepankar.newsapp.contract.MainActivityContract;
import com.deepankar.newsapp.db.AppDatabase;
import com.deepankar.newsapp.db.NewsCategory;
import com.deepankar.newsapp.db.NewsCategoryDao;
import com.deepankar.newsapp.db.NewsCategoryData;
import com.deepankar.newsapp.view.HeadlinesFragment;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivityPresenter implements MainActivityContract.Presenter {
    private MainActivityContract.View view;
    private AppDatabase database;

    public MainActivityPresenter(MainActivityContract.View view, Context context) {
        this.view = view;
        this.database = Room.databaseBuilder(context, AppDatabase.class, "db-news-categories")
                .allowMainThreadQueries()
                .build();
    }

    @Override
    public void addHeadlinesFragments() {
        final NewsCategoryDao dao = database.getNewsCategoryDao();
        List<NewsCategory> newsCategories = dao.getEnabledNewsCategories();
        if (newsCategories.size() == 0) {
            NewsCategoryData newsCategoryData = new NewsCategoryData();
            newsCategories = newsCategoryData.getNewsCategoryList();
        }
        for (NewsCategory newsCategory : newsCategories) {
            if (newsCategory.getNameId() != -1 && newsCategory.isEnabled()) {
                view.addHeadlinesFragmentToAdapter(new HeadlinesFragment(newsCategory.getCountry(), newsCategory.getCategory(), newsCategory.getSequence()), newsCategory.getNameId());
            }
        }
    }
}
