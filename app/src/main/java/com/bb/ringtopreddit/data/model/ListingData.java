package com.bb.ringtopreddit.data.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ListingData {

    @Expose
    private final List<LinkItem> children;

    public ListingData(List<LinkItem> children) {
        this.children = children;
    }

    /**
     * Gson requires no param constructor.
     */
    public ListingData() {
        this(null);
    }

    public List<LinkItem> getChildren() {
        return children;
    }
}
