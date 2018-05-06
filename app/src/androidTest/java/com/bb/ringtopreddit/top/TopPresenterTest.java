package com.bb.ringtopreddit.top;

import com.bb.ringtopreddit.R;
import com.bb.ringtopreddit.data.TopRepo;
import com.bb.ringtopreddit.data.model.RedditLink;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.util.Collections;

import io.reactivex.Single;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class TopPresenterTest {

    private static final RedditLink REDDIT_LINK = new RedditLink();

    private final TopRepo mockRepo = mock(TopRepo.class);
    private final TopContract.View mockView = mock(TopContract.View.class);

    private TopPresenter presenter;

    @Before
    public void setUp() {
        presenter = new TopPresenter(mockRepo);
    }

    @Test
    public void takeView_noItems_shouldStartLoading() {
        when(mockRepo.getPopularFromLastDay(isNull())).thenReturn(Single.fromCallable(() -> Collections.singletonList(REDDIT_LINK)));

        presenter.takeView(mockView);

        verify(mockView).showProgress();
        verify(mockRepo).getPopularFromLastDay(isNull()); // first interaction will have null as last name
    }

    @Test
    public void takeView_hasItems_shouldOnlyShowData() {
        presenter.onSuccess(Collections.singletonList(REDDIT_LINK)); // imitate some results previously loaded

        presenter.takeView(mockView);

        verify(mockView).showData(ArgumentMatchers.anyList());
        verifyNoMoreInteractions(mockRepo, mockView);
    }

    @Test
    public void onSuccess_receivedData_shouldShowData() {
        presenter.setView(mockView);
        presenter.onSuccess(Collections.singletonList(REDDIT_LINK));

        verify(mockView).hideProgress();
        verify(mockView).showData(ArgumentMatchers.anyList());
    }

    @Test
    public void onError_failedToLoad_shouldShowError() {
        presenter.setView(mockView);
        presenter.onError(new Exception());

        verify(mockView).showError(eq(R.string.error_loading_data));
        verify(mockView).hideProgress();
    }

    @Test
    public void loadMore_noView_noInteractions() {
        presenter.loadMore();

        verifyZeroInteractions(mockView, mockRepo);
    }
}