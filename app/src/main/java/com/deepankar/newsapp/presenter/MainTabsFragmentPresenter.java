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
//        view.addHeadlinesFragmentToAdapter(new HeadlinesFragment("in", null), R.string.action_india);
//        view.addHeadlinesFragmentToAdapter(new HeadlinesFragment(null, null), R.string.action_world);
//        view.addHeadlinesFragmentToAdapter(new HeadlinesFragment("in", "business"), R.string.action_business);
//        view.addHeadlinesFragmentToAdapter(new HeadlinesFragment("in", "entertainment"), R.string.action_entertainment);
//        view.addHeadlinesFragmentToAdapter(new HeadlinesFragment("in", "health"), R.string.action_health);
//        view.addHeadlinesFragmentToAdapter(new HeadlinesFragment("in", "science"), R.string.action_science);
//        view.addHeadlinesFragmentToAdapter(new HeadlinesFragment("in", "sports"), R.string.action_sports);
//        view.addHeadlinesFragmentToAdapter(new HeadlinesFragment("in", "technology"), R.string.action_technology);
        final NewsCategoryDao dao = database.getNewsCategoryDao();
        List<NewsCategory> newsCategories = dao.getEnabledNewsCategories();
        for (NewsCategory newsCategory : newsCategories) {
            if (newsCategory.getNameId() != -1) {
                view.addHeadlinesFragmentToAdapter(new HeadlinesFragment(newsCategory.getCountry(), newsCategory.getCategory(), newsCategory.getSequence()), newsCategory.getNameId());
            }
        }
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Consumer<List<NewsCategory>>() {
//                    @Override
//                    public void accept(List<NewsCategory> newsCategories) {
//                        if (newsCategories.size() > 0) {
//                            for (NewsCategory newsCategory :
//                                    newsCategories) {
//                                if (newsCategory.getNameId() != -1) {
//                                    view.addHeadlinesFragmentToAdapter(new HeadlinesFragment(newsCategory.getCountry(), newsCategory.getCategory()), newsCategory.getNameId());
//                                }
//                            }
//                        }
//                    }
//                });
    }
}
