package com.deepankar.newsapp.presenter;

import android.content.Context;

import androidx.room.Room;

import com.deepankar.newsapp.adapter.NewsCategoryListAdapter;
import com.deepankar.newsapp.contract.ManageHomeActivityContract;
import com.deepankar.newsapp.db.AppDatabase;
import com.deepankar.newsapp.db.NewsCategoryDao;
import com.deepankar.newsapp.db.NewsCategoryData;
import com.deepankar.newsapp.db.NewsCategory;
import com.deepankar.newsapp.model.service.pojo.Article;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ManageHomePresenter implements ManageHomeActivityContract.Presenter {
    private ManageHomeActivityContract.View view;
    private AppDatabase database;

    public ManageHomePresenter(ManageHomeActivityContract.View view, Context context) {
        this.view = view;
        this.database = Room.databaseBuilder(context, AppDatabase.class, "db-news-categories")
                .build();
    }

    @Override
    public void loadNewsCategoryPreferences(final NewsCategoryListAdapter adapter) {
        final NewsCategoryDao dao = database.getNewsCategoryDao();
        dao.getNewsCategories()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<NewsCategory>>() {
                    @Override
                    public void accept(List<NewsCategory> newsCategories) {
                        if (newsCategories.size() == 0) {
                            NewsCategoryData newsCategoryData = new NewsCategoryData();
                            newsCategories = newsCategoryData.getNewsCategoryList();
                        }
                        adapter.setNewsCategoryList(newsCategories);
                    }
                });
    }

    @Override
    public void updateNewsCategoryPreferences(final List<NewsCategory> newsCategories) {
        {
            Single
                    .create(new SingleOnSubscribe<List<NewsCategory>>() {
                        @Override
                        public void subscribe(SingleEmitter<List<NewsCategory>> emitter) throws Exception {
                            emitter.onSuccess(newsCategories);
                        }
                    })
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.computation())
                    .subscribe(new SingleObserver<List<NewsCategory>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(List<NewsCategory> newsCategories) {
                            final NewsCategoryDao dao = database.getNewsCategoryDao();
                            dao.deleteAll();
                            dao.insert(newsCategories);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });


        }
    }
}
