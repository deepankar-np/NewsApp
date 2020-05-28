package com.deepankar.newsapp.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.deepankar.newsapp.R;
import com.deepankar.newsapp.adapter.TabAdapter;
import com.deepankar.newsapp.contract.MainActivityContract;
import com.deepankar.newsapp.core.MyApplication;
import com.deepankar.newsapp.core.NetworkApi;
import com.deepankar.newsapp.core.component.DaggerMainActivityComponent;
import com.deepankar.newsapp.core.module.MainActivityModule;
import com.deepankar.newsapp.presenter.MainActivityPresenter;
import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {
    private static String TAG = MainActivity.class.getSimpleName();

    private MainActivityPresenter presenter;

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Inject
    NetworkApi networkApi;

    //@Inject
    //SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkThemeAndApply();

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DaggerMainActivityComponent.builder()
                .applicationComponent(((MyApplication) this.getApplicationContext()).getAppComponent()).mainActivityModule(new MainActivityModule(this)).build();
        setPresenter();

        initView();
    }

    private void checkThemeAndApply() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sharedPreferences.getString("theme", "MODE_NIGHT_FOLLOW_SYSTEM");
        int mode;
        switch (theme) {
            case "MODE_NIGHT_NO":
                mode = AppCompatDelegate.MODE_NIGHT_NO;
                break;
            case "MODE_NIGHT_YES":
                mode = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            default:
                mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                break;
        }
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    @Override
    public void initView() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        presenter.addHeadlinesFragments();
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        //addFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);

        manageSearch(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_manage_home) {
            startActivityForResult(new Intent(this, SettingsActivity.class), 100);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            initView();
        }
    }

    private void manageSearch(Menu menu) {
        final MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
            final SearchView finalSearchView = searchView;
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (!finalSearchView.isIconified()) {
                        finalSearchView.setIconified(true);
                    }
                    searchItem.collapseActionView();
                    Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
                    intent.putExtra(SearchManager.QUERY, query);
                    startActivity(intent);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                    return false;
                }
            });
        }
    }

    @Override
    public void setPresenter() {
        presenter = new MainActivityPresenter(this, getApplicationContext());
    }

//    private void addFragment() {
//        MainTabsFragment headlinesFragment = (MainTabsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
//        if (headlinesFragment == null) {
//            headlinesFragment = MainTabsFragment.getInstance();
//            ActivityUtils.addFragmentToActivity(
//                    getSupportFragmentManager(), headlinesFragment, R.id.contentFrame);
//        }
//    }

    @Override
    public void addHeadlinesFragmentToAdapter(HeadlinesFragment fragment, int stringResourceId) {
        adapter.addFragment(fragment, getString(stringResourceId));
    }
}
