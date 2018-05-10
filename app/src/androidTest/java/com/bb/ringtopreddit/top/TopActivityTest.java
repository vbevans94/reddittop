package com.bb.ringtopreddit.top;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.UiThread;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.bb.ringtopreddit.DaggerFakeAppComponent;
import com.bb.ringtopreddit.FakeAppComponent;
import com.bb.ringtopreddit.FakeTopRepoModule;
import com.bb.ringtopreddit.R;
import com.bb.ringtopreddit.TopApp;
import com.bb.ringtopreddit.data.TopRepo;
import com.bb.ringtopreddit.data.model.RedditLink;
import com.bb.ringtopreddit.di.AppModule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Single;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TopActivityTest {

    private static final String TAG = "TopTest";

    @Rule
    public ActivityTestRule<TopActivity> activityRule = new ActivityTestRule<>(TopActivity.class, false, false);
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final AtomicInteger linkCounter = new AtomicInteger();
    private Single<List<RedditLink>> linksSingle;

    private Instrumentation instrumentation;
    private TopActivity activity;
    private TopFragment fragment;

    @Before
    public void setUp() {
        instrumentation = InstrumentationRegistry.getInstrumentation();

        // inject test component
        TopApp app = (TopApp) InstrumentationRegistry.getTargetContext().getApplicationContext();
        TopRepo mockRepo = createFakeRepo();
        app.setAppComponent(DaggerFakeAppComponent.builder()
                .fakeTopRepoModule(new FakeTopRepoModule(mockRepo))
                .appModule(new AppModule(app))
                .build());

        // start activity
        activityRule.launchActivity(new Intent(app, TopActivity.class));
        activity = activityRule.getActivity();
        fragment = (TopFragment) activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    private TopRepo createFakeRepo() {
        TopRepo mockRepo = mock(TopRepo.class);

        linksSingle = Single.just(producePageOfLinks());

        when(mockRepo.getPopularFromLastDay(anyString())).then((Answer<Single<List<RedditLink>>>) invocation -> linksSingle);
        when(mockRepo.getPopularFromLastDay(isNull())).then((Answer<Single<List<RedditLink>>>) invocation -> linksSingle);

        return mockRepo;
    }

    private List<RedditLink> producePageOfLinks() {
        Log.d(TAG, "Produce");
        List<RedditLink> redditLinks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            redditLinks.add(new RedditLink("name" + linkCounter.incrementAndGet(), "author",
                    "title", "https://www.google.com/favicon.ico", 10, System.currentTimeMillis() / 1000, null));
        }
        return redditLinks;
    }

    @Test
    @UiThreadTest
    public void loadData_stopStart_notLoadedTwice() {
        assertEquals(10, fragment.getItemCount());

        instrumentation.callActivityOnStop(activity);
        instrumentation.callActivityOnStart(activity);

        handler.post(() -> assertEquals(10, fragment.getItemCount()));
    }

    @Test
    @UiThreadTest
    public void loadData_stopDataCameStart_resultNotLost() {
        fragment.presenter.loadMore();

        instrumentation.callActivityOnStop(activity);

        // postpone start to be done after received
        handler.post(() -> {
            instrumentation.callActivityOnStart(activity);
            assertEquals(20, fragment.getItemCount());
        });
    }
}