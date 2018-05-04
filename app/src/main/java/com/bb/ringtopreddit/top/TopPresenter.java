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

    @Inject
    TopPresenter(TopRepo topRepo) {
        this.topRepo = topRepo;
    }

    @Override
    public void takeView(TopContract.View view) {
        this.view = view;

        if (loadedData.isEmpty()) {
            loadMore();
        } else {
            view.showData(loadedData);
        }
    }

    @Override
    public void loadMore() {
        if (view == null) {
            return;
        }

        if (loadedData.size() < MAX_ITEMS_COUNT) {
            view.showProgress();
            disposable.add(topRepo.getLinks(lastName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSuccess, this::onError));
        }
    }

    @VisibleForTesting
    void onSuccess(List<RedditLink> data) {
        Log.d(TAG, "Received links: " + data.size());
        loadedData.addAll(data);

        if (view == null) {
            return;
        }

        view.hideProgress();
        view.showData(data);

        lastName = data.get(data.size() - 1).getName();
    }

    @VisibleForTesting
    void onError(Throwable error) {
        Log.e(TAG, "Failed to load items", error);

        if (view == null) {
            return;
        }

        view.showError(R.string.error_loading_data);
        view.hideProgress();
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
