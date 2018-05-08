package com.bb.ringtopreddit.top;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.bb.ringtopreddit.DaggerFakeAppComponent;
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

import java.util.ArrayList;
import java.util.Collections;
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

    @Rule
    public ActivityTestRule<TopActivity> activityRule = new ActivityTestRule<>(TopActivity.class, false, false);
    private final AtomicInteger counter = new AtomicInteger();
    private TopRepo mockRepo;
    private Context context;
    private Instrumentation instrumentation;
    private TopActivity activity;

    @Before
    public void setUp() {
        instrumentation = InstrumentationRegistry.getInstrumentation();
        context = InstrumentationRegistry.getTargetContext().getApplicationContext();
        mockRepo = createFakeRepo();
        DaggerFakeAppComponent.builder()
                .fakeTopRepoModule(new FakeTopRepoModule(mockRepo))
                .appModule(new AppModule((Application) context))
                .build().inject((TopApp) context);

        activityRule.launchActivity(new Intent(context, TopActivity.class));
        activity = activityRule.getActivity();

        TopFragment fragment = (TopFragment) activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        TopPresenter presenter = (TopPresenter) fragment.presenter;
        presenter.setLoadedData(Collections.emptyList());
    }

    private TopRepo createFakeRepo() {
        TopRepo mockRepo = mock(TopRepo.class);
        List<RedditLink> redditLinks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            redditLinks.add(new RedditLink("name" + counter.incrementAndGet(), "author", "title", "https://www.google.com/favicon.ico", 10, System.currentTimeMillis() / 1000, null));
        }

        Single<List<RedditLink>> single = Single.just(redditLinks)
//                .delay(3, TimeUnit.SECONDS)
                ;
        when(mockRepo.getPopularFromLastDay(anyString())).thenReturn(single);
        when(mockRepo.getPopularFromLastDay(isNull())).thenReturn(single);
        return mockRepo;
    }

    @Test
    @UiThreadTest
    public void loadData_stopStart_notLoadedTwice() {
        instrumentation.callActivityOnStop(activity);
        instrumentation.callActivityOnStart(activity);

        instrumentation.callActivityOnStop(activity);
        instrumentation.callActivityOnStart(activity);

        TopFragment fragment = (TopFragment) activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertEquals(10, fragment.getItemCount());
    }

    @Test
    @UiThreadTest
    public void loadData_restarted_notLoadedTwice() {
        instrumentation.callActivityOnRestart(activity);

        TopFragment fragment = (TopFragment) activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertEquals(10, fragment.getItemCount());
    }
}