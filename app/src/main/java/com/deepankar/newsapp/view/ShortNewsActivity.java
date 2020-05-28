package com.deepankar.newsapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MotionEventCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deepankar.newsapp.R;
import com.deepankar.newsapp.contract.ShortNewsActivityContract;
import com.deepankar.newsapp.model.service.pojo.Article;
import com.deepankar.newsapp.presenter.ShortNewsActivityPresenter;
import com.deepankar.newsapp.utils.OnSwipeTouchListener;
import com.deepankar.newsapp.utils.StringUtils;
import com.squareup.picasso.Picasso;

public class ShortNewsActivity extends AppCompatActivity implements ShortNewsActivityContract.View {
    private ShortNewsActivityPresenter presenter;

    private ImageView newsImage;
    private TextView newsTitle;
    private TextView newsTime;
    private TextView newsSource;
    private TextView shortNews;
    private Button newsButton;

    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_news);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setPresenter();
        initView();

        relativeLayout = findViewById(R.id.short_news_relative_layout);
        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @Override
            public void onSwipeRight() {

            }

            @Override
            public void onSwipeLeft() {

            }

            @Override
            public void onSwipeTop() {
                presenter.openNextNews(getIntent());
            }

            @Override
            public void onSwipeBottom() {
                presenter.openPreviousNews(getIntent());
            }
        });
    }

    @Override
    public void initView() {
        newsImage = findViewById(R.id.short_news_image);
        newsTitle = findViewById(R.id.short_news_title);
        newsTime = findViewById(R.id.short_news_time);
        newsSource = findViewById(R.id.short_news_source);
        shortNews = findViewById(R.id.short_news_wv);
        newsButton = findViewById(R.id.short_news_btn);

        Intent intent = getIntent();
        presenter.openNews(intent);
    }

    @Override
    public void showFullNews(Article article) {
        Intent intent = new Intent(this, FullNewsActivity.class);
        intent.putExtra("article", article);
        startActivity(intent);
    }

    @Override
    public void hideNewsButton() {
        newsButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showNewsButton(final Article article) {
        newsButton.setVisibility(View.VISIBLE);
        newsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullNews(article);
            }
        });
    }

    @Override
    public void openNextArticle(Article article, int position) {
        Intent intent = new Intent(getApplicationContext(), ShortNewsActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("article", article);
        startActivity(intent);
        finish();
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
        }
        return true;
    }

    @Override
    public void loadNewsImage(String imageUrl) {
        Picasso.get().load(imageUrl).into(newsImage);
    }

    @Override
    public void showShortNews(String content) {
        //shortNews.getSettings().setJavaScriptEnabled(true);
        //shortNews.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        //shortNews.loadData((String) article.getContent(), "text/html", "UTF-8");
        shortNews.setText(content);
    }

    @Override
    public void setNewsTitle(String title) {
        newsTitle.setText(title);
    }

    @Override
    public void setNewsTime(String time) {
        newsTime.setText(time);
    }

    @Override
    public void setNewsSource(String source) {
        newsSource.setText(source);
    }



    @Override
    public void setPresenter() {
        presenter = new ShortNewsActivityPresenter(this, getApplicationContext());
    }
}
