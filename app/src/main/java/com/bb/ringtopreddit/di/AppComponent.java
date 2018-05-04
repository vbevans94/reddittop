package com.bb.ringtopreddit.di;

import android.content.Context;

import com.bb.ringtopreddit.TopApp;
import com.bb.ringtopreddit.utils.Names;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    @Named(Names.APPLICATION)
    Context application();

    void inject(TopApp app);
}
