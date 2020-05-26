package com.deepankar.newsapp.db;

import com.deepankar.newsapp.R;

import java.util.ArrayList;
import java.util.List;

public class NewsCategoryData {

    private List<NewsCategory> newsCategoryList = new ArrayList<NewsCategory>() {
        {
            add(new NewsCategory(-1, "", null, true,true, 0,false));
            add(new NewsCategory(R.string.action_india, "in", null, true,true, 1,false));
            add(new NewsCategory(R.string.action_world, null, null, true,true, 2, true));
            add(new NewsCategory(R.string.action_business, "in", "business", false,true, 3, true));
            add(new NewsCategory(R.string.action_sports, "in", "sports",false, true, 4, true));
            add(new NewsCategory(R.string.action_entertainment, "in", "entertainment", false,true, 5, true));
            add(new NewsCategory(R.string.action_technology, "in", "technology", false,true, 6, true));
            add(new NewsCategory(R.string.action_health, "in", "health", false,true, 7, true));
            add(new NewsCategory(R.string.action_science, "in", "science", false,true, 8, true));
            add(new NewsCategory(-2, "", null, true,false, 9, false));
        }
    };

    public List<NewsCategory> getNewsCategoryList() {
        return newsCategoryList;
    }
}
