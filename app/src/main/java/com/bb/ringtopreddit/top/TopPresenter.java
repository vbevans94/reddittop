package com.bb.ringtopreddit.top;

import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.bb.ringtopreddit.R;
import com.bb.ringtopreddit.data.model.Picture;
import com.bb.ringtopreddit.data.model.RedditLink;
import com.bb.ringtopreddit.data.TopRepo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class TopPresenter implements TopContract.Presenter {

    private static final String TAG = "TopPresenter";
    private static final int MAX_ITEMS_COUNT = 50;

    private final TopRepo topRepo;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final List<RedditLink> loadedData = new ArrayList<>();
    private TopContract.View view;
    private String lastName = null;
    private boolean loading;

    @Inject
    TopPresenter(TopRepo topRepo) {
        this.topRepo = topRepo;
    }

    @Override
    public void takeView(TopContract.View view) {
        this.view = view;

        if (loadedData.isEmpty()) {
            loadMore();
        } else if (view.getItemCount() == 0) {
            view.hideProgress();
            view.showData(loadedData);
        }
    }

    @VisibleForTesting
    void setView(TopContract.View view) {
        this.view = view;
    }

    @VisibleForTesting
    void setLoadedData(List<RedditLink> loadedData) {
        this.loadedData.clear();
        this.loadedData.addAll(loadedData);
    }

    @Override
    public boolean isLastPage() {
        return loadedData.size() >= MAX_ITEMS_COUNT;
    }

    @Override
    public void loadMore() {
        if (view == null) {
            return;
        }

        if (!loading && !isLastPage()) {
            loading = true;
            view.showProgress();
            disposable.add(topRepo.getPopularFromLastDay(lastName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSuccess, this::onError));
        }
    }

    @VisibleForTesting
    void onSuccess(List<RedditLink> data) {
        Log.d(TAG, "Received links: " + data.size());
        loadedData.addAll(data);
        lastName = data.get(data.size() - 1).getName();
        loading = false;

        if (view == null) {
            return;
        }

        view.hideProgress();
        view.appendData(data);
    }

    @VisibleForTesting
    void onError(Throwable error) {
        Log.e(TAG, "Failed to load items", error);
        loading = false;

        if (view == null) {
            return;
        }

        view.hideProgress();
        view.showError(R.string.error_loading_data);
    }

    @Override
    public void onSelectPicture(Picture picture) {
        if (view != null) {
            view.openPictureActivity(picture);
        }
    }

    @Override
    public void onNoPicture() {
        if (view != null) {
            view.showError(R.string.error_no_picture);
        }
    }

    @Override
    public void dropView() {
        view = null;
    }
}
