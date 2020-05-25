package com.deepankar.newsapp.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "news_category")
public class NewsCategory {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int nameId;
    private String country;
    private String category;
    private boolean isEnabled;
    private boolean isAlwaysEnabled;
    private int sequence;

    public NewsCategory(int nameId, String country, String category, boolean isAlwaysEnabled,  boolean isEnabled, int sequence) {
        this.nameId = nameId;
        this.country = country;
        this.category = category;
        this.isAlwaysEnabled = isAlwaysEnabled;
        this.isEnabled = isEnabled;
        this.sequence = sequence;
    }

    public NewsCategory(NewsCategory newsCategory) {
        this.nameId = newsCategory.nameId;
        this.country = newsCategory.country;
        this.category = newsCategory.category;
        this.isAlwaysEnabled = newsCategory.isAlwaysEnabled;
        this.isEnabled = newsCategory.isEnabled;
        this.sequence = newsCategory.sequence;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getNameId() {
        return nameId;
    }

    public String getCategory() {
        return category;
    }

    public String getCountry() {
        return country;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isAlwaysEnabled() {
        return isAlwaysEnabled;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
