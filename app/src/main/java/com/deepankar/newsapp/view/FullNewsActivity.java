package com.deepankar.newsapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.deepankar.newsapp.R;
import com.deepankar.newsapp.contract.FullNewsActivityContract;
import com.deepankar.newsapp.presenter.FullNewsActivityPresenter;

public class FullNewsActivity extends AppCompatActivity implements FullNewsActivityContract.View {
    private FullNewsActivityPresenter presenter;
    private WebView fullNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_news);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setPresenter();
        initView();
    }

    @Override
    public void initView() {
        fullNews = findViewById(R.id.full_news_wv);
        fullNews.setWebViewClient(new MyBrowser());
        fullNews.getSettings().setJavaScriptEnabled(true);
        Intent intent = getIntent();
        presenter.openNews(intent);
    }

    @Override
    public void setPresenter() {
        presenter = new FullNewsActivityPresenter(this);
    }

    @Override
    public void loadUrl(String url) {
        fullNews.loadUrl(url);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.full_news_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Uri uri = Uri.parse(presenter.getArticleUrl());
        int id = item.getItemId();
        if (id == R.id.action_open) {

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (id == R.id.action_share) {
//            ShareCompat.IntentBuilder.from(this)
//                    .setType("text/plain")
//                    .setSubject(article.getTitle())
//                    .setChooserTitle("Share URL")
//                    .setText(article.getUrl())
//                    .startChooser();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, presenter.getArticleTitle());
            shareIntent.putExtra(Intent.EXTRA_TEXT, presenter.getArticleUrl());
            startActivity(Intent.createChooser(shareIntent, "Share link using"));
        } else if (id == android.R.id.home) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        return true;
    }
}
