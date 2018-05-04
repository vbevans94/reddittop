package com.bb.ringtopreddit.data.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Preview {

    @Expose
    private List<Image> images;

    public Preview(List<Image> images) {
        this.images = images;
    }

    /**
     * Gson required.
     */
    public Preview() {
        this(null);
    }

    public List<Image> getImages() {
        return images;
    }
}
