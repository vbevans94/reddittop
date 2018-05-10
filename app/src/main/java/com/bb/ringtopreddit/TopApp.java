package com.bb.ringtopreddit;

import android.app.Application;
import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;

import com.bb.ringtopreddit.di.AppComponent;
import com.bb.ringtopreddit.di.AppModule;
import com.bb.ringtopreddit.di.DaggerAppComponent;
import com.bb.ringtopreddit.di.TopScreenComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class TopApp extends Application implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingInjector;
    private AppComponent appComponent;
    private TopScreenComponent topScreenComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        setAppComponent(DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build());
    }

    @VisibleForTesting
    public void setAppComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
        appComponent.inject(this);
    }

    public static void enterTopScreen(Context context) {
        TopApp app = (TopApp) context.getApplicationContext();
        if (app.topScreenComponent == null) {
            app.topScreenComponent = app.appComponent.topScreenComponent()
                    .build();
            app.topScreenComponent.inject(app);
        }
    }

    public static void exitTopScreen(Context context) {
        TopApp app = (TopApp) context.getApplicationContext();
        app.topScreenComponent = null;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingInjector;
    }
}
