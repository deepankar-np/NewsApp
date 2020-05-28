package com.deepankar.newsapp.core.module;

import com.deepankar.newsapp.core.MyApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MyApplicationModule {
    private final MyApplication app;

    public MyApplicationModule(MyApplication app){
        this.app = app;
    }

    @Provides @Singleton
    MyApplication provideMyApplication(){
        return app;
    }
}
