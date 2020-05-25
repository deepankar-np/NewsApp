package com.deepankar.newsapp.contract;

import com.deepankar.newsapp.adapter.NewsCategoryListAdapter;
import com.deepankar.newsapp.contract.base.BasePresenter;
import com.deepankar.newsapp.contract.base.BaseView;
import com.deepankar.newsapp.db.NewsCategory;

import java.util.List;

public interface ManageHomeActivityContract {
    interface View extends BaseView {
    }

    interface Presenter extends BasePresenter {
        void loadNewsCategoryPreferences(NewsCategoryListAdapter adapter);

        void updateNewsCategoryPreferences(List<NewsCategory> newsCategoryList);
    }
}
