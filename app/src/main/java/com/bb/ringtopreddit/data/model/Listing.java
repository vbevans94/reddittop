package com.bb.ringtopreddit.data.model;

import com.google.gson.annotations.Expose;

public class Listing {

    @Expose
    private final ListingData data;

    public Listing(ListingData data) {
        this.data = data;
    }

    /**
     * Gson requires no param constructor.
     */
    public Listing() {
        this(null);
    }

    public ListingData getData() {
        return data;
    }
}
