package com.deepankar.newsapp.utils;

import android.content.Context;

import com.deepankar.newsapp.model.service.pojo.NewsAPIResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsAPIJsonUtils {

    public static JSONArray getNewsJsonArrayFromJson(Context context, String newsJsonStr) throws JSONException {
        final String STATUS = "status";

        JSONObject newsJson = new JSONObject(newsJsonStr);
        if (newsJson.has(STATUS)) {
            String status = newsJson.getString(STATUS);
            if (!"ok".equals(status)) {
                return null;
            }
            return newsJson.getJSONArray("articles");
        }
        return null;
    }

    public static NewsAPIResponse getNewsApiFromJson(String newsJsonStr) throws JsonSyntaxException {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(newsJsonStr, NewsAPIResponse.class);

    }
}
