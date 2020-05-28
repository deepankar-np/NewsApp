package com.deepankar.newsapp.core.component;

import com.deepankar.newsapp.core.MyApplication;
import com.deepankar.newsapp.core.module.DataModule;
import com.deepankar.newsapp.core.module.MyApplicationModule;
import com.deepankar.newsapp.core.module.NetworkModule;
import com.deepankar.newsapp.view.HeadlinesFragment;
import com.deepankar.newsapp.view.MainActivity;

import dagger.Component;

@Component(modules = {MyApplicationModule.class, DataModule.class, NetworkModule.class})
public interface ApplicationComponent {
    void inject(MyApplication myApplication);

    //MyApplication myApplication();
}
