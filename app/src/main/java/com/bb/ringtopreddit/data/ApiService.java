package com.bb.ringtopreddit.data;

import com.bb.ringtopreddit.data.model.Listing;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("r/popular/top.json")
    Observable<Listing> topPopular(@Query("t") String period, @Query("after") String after, @Query("limit") int limit);
}
