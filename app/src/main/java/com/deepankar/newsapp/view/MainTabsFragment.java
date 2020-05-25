package com.deepankar.newsapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.deepankar.newsapp.R;
import com.deepankar.newsapp.adapter.TabAdapter;
import com.deepankar.newsapp.contract.MainTabsFragmentContract;
import com.deepankar.newsapp.presenter.MainTabsFragmentPresenter;
import com.google.android.material.tabs.TabLayout;

public class MainTabsFragment extends Fragment implements MainTabsFragmentContract.View {
    private MainTabsFragmentPresenter presenter;

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static MainTabsFragment getInstance() {
        return new MainTabsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_tabs, container, false);

        viewPager = root.findViewById(R.id.viewPager);
        tabLayout = root.findViewById(R.id.tabLayout);

        setPresenter();
        initView();

        return root;
    }

    @Override
    public void initView() {
        adapter = new TabAdapter(getChildFragmentManager());
        presenter.addHeadlinesFragments();
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void setPresenter() {
        presenter = new MainTabsFragmentPresenter(this, getContext());
    }

    @Override
    public void addHeadlinesFragmentToAdapter(HeadlinesFragment fragment, int stringResourceId) {
        adapter.addFragment(fragment, getString(stringResourceId));
    }
}
