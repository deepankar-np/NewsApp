package com.deepankar.newsapp.core;

import android.app.Application;

import com.deepankar.newsapp.core.component.ApplicationComponent;
import com.deepankar.newsapp.core.component.DaggerApplicationComponent;
import com.deepankar.newsapp.core.module.DataModule;
import com.deepankar.newsapp.core.module.MyApplicationModule;
import com.deepankar.newsapp.core.module.NetworkModule;

public class MyApplication extends Application {
    private ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerApplicationComponent.builder()
                .myApplicationModule(new MyApplicationModule(this))
                .dataModule(new DataModule())
                .networkModule(new NetworkModule())
                .build();
    }

    public ApplicationComponent getAppComponent() {
        return appComponent;
    }
}
