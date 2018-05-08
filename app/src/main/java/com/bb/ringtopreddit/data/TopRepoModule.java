package com.bb.ringtopreddit.data;

import android.content.Context;

import com.bb.ringtopreddit.utils.Names;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public class TopRepoModule {

    @Provides
    @Singleton
    public TopRepo providesTopRepo(@Named(Names.APPLICATION) Context context, ApiService apiService) {
        return new TopRepo(context, apiService);
    }
}
