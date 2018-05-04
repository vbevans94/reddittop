package com.bb.ringtopreddit.picture;

import com.bb.ringtopreddit.di.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class PictureFragmentModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract PictureFragment contributePictureFragmentInjector();

    @Binds
    @ActivityScope
    abstract PictureContract.Presenter picturePresenter(PicturePresenter presenter);
}
