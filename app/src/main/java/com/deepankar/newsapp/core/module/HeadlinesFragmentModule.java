package com.deepankar.newsapp.core.module;

import android.content.Context;

import com.deepankar.newsapp.core.annotation.ActivityContext;
import com.deepankar.newsapp.core.annotation.ActivityScope;
import com.deepankar.newsapp.view.HeadlinesFragment;
import com.deepankar.newsapp.view.MainActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class HeadlinesFragmentModule {
    private HeadlinesFragment headlinesFragment;

    public Context context;

    public HeadlinesFragmentModule(HeadlinesFragment headlinesFragment) {
        this.headlinesFragment = headlinesFragment;
        context = headlinesFragment.getContext();
    }

    @Provides
    @ActivityScope
    public HeadlinesFragment providesHeadlinesFragment() {
        return headlinesFragment;
    }

    @Provides
    @ActivityScope
    @ActivityContext
    public Context provideContext() {
        return context;
    }
}
