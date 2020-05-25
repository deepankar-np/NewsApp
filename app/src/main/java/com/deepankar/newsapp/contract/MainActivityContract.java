package com.deepankar.newsapp.contract;

import com.deepankar.newsapp.contract.base.BasePresenter;
import com.deepankar.newsapp.contract.base.BaseView;
import com.deepankar.newsapp.view.HeadlinesFragment;

public interface MainActivityContract {
    interface View extends BaseView {
        void addHeadlinesFragmentToAdapter(HeadlinesFragment fragment, int stringResourceId);
    }

    interface Presenter extends BasePresenter {
        void addHeadlinesFragments();
    }
}
