package com.bb.ringtopreddit.di;

import android.app.Application;
import android.content.Context;

import com.bb.ringtopreddit.data.DataModule;
import com.bb.ringtopreddit.utils.Names;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {AndroidModules.class, DataModule.class})
public class AppModule {

    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    @Named(Names.APPLICATION)
    public Context provideApplicationContext() {
        return application;
    }
}
