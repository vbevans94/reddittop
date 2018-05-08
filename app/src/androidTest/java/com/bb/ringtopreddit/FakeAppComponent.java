package com.bb.ringtopreddit;

import com.bb.ringtopreddit.di.AppComponent;
import com.bb.ringtopreddit.di.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, FakeTopRepoModule.class})
public interface FakeAppComponent extends AppComponent {
}
