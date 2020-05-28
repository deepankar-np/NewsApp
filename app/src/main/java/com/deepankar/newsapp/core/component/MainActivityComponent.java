package com.deepankar.newsapp.core.component;

import com.deepankar.newsapp.core.module.HeadlinesFragmentModule;
import com.deepankar.newsapp.core.module.MainActivityModule;
import com.deepankar.newsapp.view.HeadlinesFragment;
import com.deepankar.newsapp.view.MainActivity;

import dagger.Component;

@Component(dependencies = ApplicationComponent.class, modules = {MainActivityModule.class, HeadlinesFragmentModule.class})
public interface MainActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(HeadlinesFragment headlinesFragment);
}
