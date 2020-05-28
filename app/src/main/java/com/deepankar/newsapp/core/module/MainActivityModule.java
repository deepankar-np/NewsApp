package com.deepankar.newsapp.core.module;

import android.content.Context;

import com.deepankar.newsapp.core.annotation.ActivityContext;
import com.deepankar.newsapp.core.annotation.ActivityScope;
import com.deepankar.newsapp.view.MainActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {
    private MainActivity mainActivity;

    public Context context;

    public MainActivityModule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        context = mainActivity;
    }

    @Provides
    @ActivityScope
    public MainActivity providesMainActivity() {
        return mainActivity;
    }

    @Provides
    @ActivityScope
    @ActivityContext
    public Context provideContext() {
        return context;
    }
}
