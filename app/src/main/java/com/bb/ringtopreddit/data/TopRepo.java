package com.bb.ringtopreddit.data;

import android.content.Context;
import android.util.Log;

import com.bb.ringtopreddit.R;
import com.bb.ringtopreddit.data.model.LinkItem;
import com.bb.ringtopreddit.data.model.Listing;
import com.bb.ringtopreddit.data.model.Preview;
import com.bb.ringtopreddit.data.model.RedditLink;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class TopRepo {

    private static final String TAG = "TopRepo";

    private static final String UTC_ID = "UTC";

    private final Context context;
    private final ApiService apiService;

    TopRepo(Context context, ApiService apiService) {
        this.context = context;
        this.apiService = apiService;
    }

    public Single<List<RedditLink>> getPopularFromLastDay(String lastName) {
        Log.d(TAG, "Getting popular reddit links after " + lastName);
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
                .toList();
    }
}
