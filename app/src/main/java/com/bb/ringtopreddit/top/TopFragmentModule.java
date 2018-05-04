package com.bb.ringtopreddit.top;

import com.bb.ringtopreddit.di.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class TopFragmentModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract TopFragment contributeTopFragmentInjector();

    @Binds
    @ActivityScope
    abstract TopContract.Presenter topPresenter(TopPresenter presenter);
}
