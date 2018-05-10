package com.bb.ringtopreddit.di;

import com.bb.ringtopreddit.TopApp;
import com.bb.ringtopreddit.picture.PictureFragmentModule;
import com.bb.ringtopreddit.top.TopFragmentModule;
import com.bb.ringtopreddit.top.TopPresenter;

import dagger.Component;
import dagger.Subcomponent;

@ScreenScope
@Subcomponent(modules = TopFragmentModule.class)
public interface TopScreenComponent {

    TopPresenter presenter();

    void inject(TopApp topApp);

    @Subcomponent.Builder
    interface Builder {

        TopScreenComponent build();
    }
}
