package com.deepankar.newsapp.presenter;

import com.deepankar.newsapp.R;
import com.deepankar.newsapp.view.HeadlinesFragment;
import com.deepankar.newsapp.contract.MainTabsFragmentContract;

public class MainTabsFragmentPresenter implements MainTabsFragmentContract.Presenter {
    private MainTabsFragmentContract.View view;

    public MainTabsFragmentPresenter(MainTabsFragmentContract.View view) {
        this.view = view;
    }

    @Override
    public void addHeadlinesFragments() {
        view.addHeadlinesFragmentToAdapter(new HeadlinesFragment("in", null), R.string.action_india);
        view.addHeadlinesFragmentToAdapter(new HeadlinesFragment("in", null), R.string.action_india);
        view.addHeadlinesFragmentToAdapter(new HeadlinesFragment(null, null), R.string.action_world);
        view.addHeadlinesFragmentToAdapter(new HeadlinesFragment("in", "business"), R.string.action_business);
        view.addHeadlinesFragmentToAdapter(new HeadlinesFragment("in", "entertainment"), R.string.action_entertainment);
        view.addHeadlinesFragmentToAdapter(new HeadlinesFragment("in", "health"), R.string.action_health);
        view.addHeadlinesFragmentToAdapter(new HeadlinesFragment("in", "science"), R.string.action_science);
        view.addHeadlinesFragmentToAdapter(new HeadlinesFragment("in", "sports"), R.string.action_sports);
        view.addHeadlinesFragmentToAdapter(new HeadlinesFragment("in", "technology"), R.string.action_technology);
    }
}
