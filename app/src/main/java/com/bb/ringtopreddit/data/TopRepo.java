package com.bb.ringtopreddit.data;

import android.content.Context;

import com.bb.ringtopreddit.R;
import com.bb.ringtopreddit.data.ApiService;
import com.bb.ringtopreddit.data.model.LinkItem;
import com.bb.ringtopreddit.data.model.Listing;
import com.bb.ringtopreddit.data.model.Preview;
import com.bb.ringtopreddit.data.model.RedditLink;
import com.bb.ringtopreddit.utils.Names;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class TopRepo {

    private static final String UTC_ID = "UTC";

    private final Context context;
    private final ApiService apiService;

    @Inject
    TopRepo(@Named(Names.APPLICATION) Context context, ApiService apiService) {
        this.context = context;
        this.apiService = apiService;
    }

    public Single<List<RedditLink>> getLinks(String lastName) {
        return apiService.topPopular("day", lastName, 10)
                .flatMap((Function<Listing, ObservableSource<LinkItem>>) listing -> Observable.fromIterable(listing.getData().getChildren()))
                .map(LinkItem::getData)
                .doOnNext(link -> {
                    long createdAtMillisUtc = TimeUnit.SECONDS.toMillis(link.getCreatedAtUtcSeconds());

                    Calendar now = Calendar.getInstance();
                    now.setTimeZone(TimeZone.getTimeZone(UTC_ID));

                    int hours = (int) TimeUnit.MILLISECONDS.toHours(now.getTimeInMillis() - createdAtMillisUtc);

                    link.setHoursAgo(context.getResources().getQuantityString(R.plurals.hour_ago, hours, hours));

                    Preview preview = link.getPreview();
                    if (preview != null && preview.getImages() != null && !preview.getImages().isEmpty()) {
                        preview.getImages().get(0).getSource().setTitle(link.getTitle());
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
