package com.bb.ringtopreddit;


import com.bb.ringtopreddit.data.TopRepo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public class FakeTopRepoModule {

    private final TopRepo providesFakeTopRepo;

    public FakeTopRepoModule(TopRepo fakeTopRepo) {
        providesFakeTopRepo = fakeTopRepo;
    }

    @Provides
    @Singleton
    public TopRepo providesFakeTopRepo() {
        return providesFakeTopRepo;
    }
}
