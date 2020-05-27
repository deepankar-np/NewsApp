package com.deepankar.newsapp.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.deepankar.newsapp.R;
import com.deepankar.newsapp.adapter.NewsAdapter;
import com.deepankar.newsapp.contract.SearchResultActivityContract;
import com.deepankar.newsapp.model.service.pojo.Article;
import com.deepankar.newsapp.model.service.pojo.NewsAPIResponse;
import com.deepankar.newsapp.presenter.SearchResultsActivityPresenter;

public class SearchResultsActivity extends AppCompatActivity implements NewsAdapter.NewsAdapterOnClickHandler, SearchResultActivityContract.View {
    private SearchResultsActivityPresenter presenter;

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private SwipeRefreshLayout swipeRefreshLayout;

    private NewsAdapter mNewsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setPresenter();
        initView();
    }

    @Override
    public void initView() {
        mRecyclerView = findViewById(R.id.rv_news);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        LinearLayoutManager layoutManager = null;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // landscape
            layoutManager
                    = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        } else {
            // portrait
            layoutManager
                    = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        }
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(false);

        mNewsAdapter = new NewsAdapter(this);

        mRecyclerView.setAdapter(mNewsAdapter);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayDataBySelectedMenuItem();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        presenter.handleIntent(getIntent());
    }

    private void displayDataBySelectedMenuItem() {
        mRecyclerView.removeAllViews();
        mNewsAdapter.setNewsData(null);
        presenter.loadNewsData();
        mNewsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNewsDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorMessage() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgressBar() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void populateNewsData(NewsAPIResponse responseBody) {
        mNewsAdapter.setNewsData(responseBody);
    }

    @Override
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void showErrorMessage(String message) {
        mErrorMessageDisplay.setText(message);
    }

    @Override
    public void onClick(Article article, int position) {
        Intent intent = new Intent(getApplicationContext(), com.deepankar.newsapp.view.ShortNewsActivity.class);
        intent.putExtra("article", article);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) SearchResultsActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(SearchResultsActivity.this.getComponentName()));
            searchView.setIconifiedByDefault(false);
            final SearchView finalSearchView = searchView;
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // Toast like print
                    if (!finalSearchView.isIconified()) {
                        finalSearchView.setIconified(true);
                    }
                    searchItem.collapseActionView();
                    presenter.updateNewsData(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                    return false;
                }
            });
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up
        return false;
    }

    @Override
    public void setPresenter() {
        presenter = new SearchResultsActivityPresenter(this, getApplicationContext());
    }
}
