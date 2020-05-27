package com.deepankar.newsapp.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.deepankar.newsapp.R;
import com.deepankar.newsapp.adapter.NewsAdapter;
import com.deepankar.newsapp.contract.HeadlinesFragmentContract;
import com.deepankar.newsapp.db.NewsCategory;
import com.deepankar.newsapp.model.service.pojo.Article;
import com.deepankar.newsapp.model.service.pojo.NewsAPIResponse;
import com.deepankar.newsapp.presenter.HeadlinesFragmentPresenter;

public class HeadlinesFragment extends Fragment implements NewsAdapter.NewsAdapterOnClickHandler, HeadlinesFragmentContract.View {
    private static String TAG = HeadlinesFragment.class.getSimpleName();

    private HeadlinesFragmentPresenter presenter;
    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private SwipeRefreshLayout swipeRefreshLayout;

    private NewsAdapter mNewsAdapter;

    private NewsCategory newsCategory;

    public HeadlinesFragment(){
        this.newsCategory = new NewsCategory(R.string.action_india, "in", null, true,true, 1, false);
    }

    public HeadlinesFragment(NewsCategory newsCategory) {
        this.newsCategory = newsCategory;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_headlines, container, false);
        mRecyclerView = view.findViewById(R.id.rv_news);
        mErrorMessageDisplay = view.findViewById(R.id.tv_error_message_display);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        mLoadingIndicator = view.findViewById(R.id.pb_loading_indicator);

        setPresenter();
        initView();

        return view;
    }

    @Override
    public void initView() {

        LinearLayoutManager layoutManager = null;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // landscape
            layoutManager
                    = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        } else {
            // portrait
            layoutManager
                    = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(false);

        mNewsAdapter = new NewsAdapter(this);

        mRecyclerView.setAdapter(mNewsAdapter);

        presenter.loadNewsData(newsCategory.getCountry(), newsCategory.getCategory());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayDataBySelectedMenuItem();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void displayDataBySelectedMenuItem() {
        mRecyclerView.removeAllViews();
        mNewsAdapter.setNewsData(null);
        presenter.loadNewsData(newsCategory.getCountry(), newsCategory.getCategory());
        mNewsAdapter.notifyDataSetChanged();
    }


    @Override
    public void showNewsDataView() {
        hideProgressBar();
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorMessage() {
        hideProgressBar();
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
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void populateNewsData(NewsAPIResponse responseBody) {
        mNewsAdapter.setNewsData(responseBody);
    }

    @Override
    public void onClick(Article article, int position) {
        Intent intent = new Intent(getContext(), ShortNewsActivity.class);
        intent.putExtra("article", article);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void setPresenter() {
        this.presenter = new HeadlinesFragmentPresenter(this, getContext());
    }

    public NewsCategory getNewsCategory() {
        return newsCategory;
    }
}
