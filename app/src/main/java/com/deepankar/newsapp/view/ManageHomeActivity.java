package com.deepankar.newsapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.deepankar.newsapp.R;
import com.deepankar.newsapp.adapter.NewsCategoryListAdapter;
import com.deepankar.newsapp.contract.ManageHomeActivityContract;
import com.deepankar.newsapp.presenter.ManageHomePresenter;
import com.deepankar.newsapp.utils.SwipeAndDragHelper;

import java.util.List;

public class ManageHomeActivity extends AppCompatActivity implements ManageHomeActivityContract.View {

    private ManageHomePresenter presenter;
    private NewsCategoryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setPresenter();

        initView();
    }

    @Override
    public void initView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview_news_category_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsCategoryListAdapter(presenter);
        SwipeAndDragHelper swipeAndDragHelper = new SwipeAndDragHelper(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeAndDragHelper);
        adapter.setTouchHelper(touchHelper);
        recyclerView.setAdapter(adapter);
        touchHelper.attachToRecyclerView(recyclerView);

        adapter.loadNewsCategoryList();
    }

    @Override
    public void setPresenter() {
        presenter = new ManageHomePresenter(this, getApplicationContext());
    }
}
