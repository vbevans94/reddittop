package com.bb.ringtopreddit.top;

import com.bb.ringtopreddit.R;
import com.bb.ringtopreddit.data.TopRepo;
import com.bb.ringtopreddit.data.model.RedditLink;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class TopPresenterTest {

    private final RedditLink link = new RedditLink();
    private final TopRepo mockRepo = mock(TopRepo.class);
    private final TopContract.View mockView = mock(TopContract.View.class);

    private TopPresenter presenter;

    @Before
    public void setUp() {
        presenter = new TopPresenter(mockRepo);
    }

    @Test
    public void takeView_failedToLoad_shouldShowError() {
        when(mockRepo.getLinks(isNull())).thenReturn(Single.error(new Exception()));

        presenter.takeView(mockView);

        verify(mockView).showProgress();
        verify(mockRepo).getLinks(isNull()); // first interaction will have null as last name
        verify(mockView).showError(eq(R.string.error_loading_data));
        verify(mockView).hideProgress();
    }

    @Test
    public void takeView_success_shouldShowData() {
        when(mockRepo.getLinks(isNull())).thenReturn(Single.fromCallable(() -> Collections.singletonList(link)));

        presenter.takeView(mockView);

        verify(mockView).showProgress();
        verify(mockRepo).getLinks(isNull()); // first interaction will have null as last name
        verify(mockView).hideProgress();
        verify(mockView).showData(ArgumentMatchers.anyList());
    }

    @Test
    public void takeView_hasItems_shouldOnlyShowData() {
        presenter.onSuccess(Collections.singletonList(link)); // imitate some results previously loaded

        presenter.takeView(mockView);

        verify(mockView).showData(ArgumentMatchers.anyList());
        verifyNoMoreInteractions(mockRepo, mockView);
    }

    @Test
    public void loadMore_noView_noInteractions() {
        presenter.loadMore();

        verifyZeroInteractions(mockView, mockRepo);
    }
}