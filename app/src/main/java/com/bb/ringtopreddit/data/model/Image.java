package com.bb.ringtopreddit.data.model;

import com.google.gson.annotations.Expose;

public class Image {

    @Expose
    private final Picture source;

    public Image(Picture source) {
        this.source = source;
    }

    /**
     * Gson required.
     */
    public Image() {
        this(null);
    }

    public Picture getSource() {
        return source;
    }
}
