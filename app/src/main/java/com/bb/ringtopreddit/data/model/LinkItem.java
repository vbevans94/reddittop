package com.bb.ringtopreddit.data.model;

import com.google.gson.annotations.Expose;

public class LinkItem {

    @Expose
    private final RedditLink data;

    public LinkItem(RedditLink data) {
        this.data = data;
    }

    /**
     * Gson requires no param constructor.
     */
    public LinkItem() {
        this(null);
    }

    public RedditLink getData() {
        return data;
    }
}
