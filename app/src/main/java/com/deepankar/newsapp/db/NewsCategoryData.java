package com.deepankar.newsapp.db;

import com.deepankar.newsapp.R;

import java.util.ArrayList;
import java.util.List;

public class NewsCategoryData {

    private List<NewsCategory> newsCategoryList = new ArrayList<NewsCategory>() {
        {
            add(new NewsCategory(-1, "", null, true,true, 0));
            add(new NewsCategory(R.string.action_india, "in", null, true,true, 1));
            add(new NewsCategory(R.string.action_world, null, null, false,true, 2));
            add(new NewsCategory(R.string.action_business, "in", "business", false,true, 3));
            add(new NewsCategory(R.string.action_sports, "in", "sports",false, true, 4));
            add(new NewsCategory(-1, "", null, true,false, 5));
            add(new NewsCategory(R.string.action_entertainment, "in", "entertainment", false,true, 6));
            add(new NewsCategory(R.string.action_technology, "in", "technology", false,false, 7));
            add(new NewsCategory(R.string.action_health, "in", "health", false,true, 8));
            add(new NewsCategory(R.string.action_science, "in", "science", false,true, 9));
        }
    };

    public List<NewsCategory> getNewsCategoryList() {
        return newsCategoryList;
    }
}
