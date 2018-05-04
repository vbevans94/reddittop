package com.bb.ringtopreddit.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RedditLink {

    @Expose
    private final String name;

    @Expose
    private final String author;

    @Expose
    private final String title;

    @Expose
    private final String thumbnail;

    @Expose
    @SerializedName("num_comments")
    private final int commentsAmount;

    @Expose
    @SerializedName("created_utc")
    private long createdAtUtcSeconds;

    private String hoursAgo;

    @Expose
    private final Preview preview;

    public RedditLink(String name, String author, String title, String thumbnail, int commentsAmount, long createdAtUtcSeconds, Preview preview) {
        this.name = name;
        this.author = author;
        this.title = title;
        this.thumbnail = thumbnail;
        this.commentsAmount = commentsAmount;
        this.createdAtUtcSeconds = createdAtUtcSeconds;
        this.preview = preview;
    }

    // Gson
    public RedditLink() {
        this(null, null, null, null, 0, 0, null);
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public int getCommentsAmount() {
        return commentsAmount;
    }

    public long getCreatedAtUtcSeconds() {
        return createdAtUtcSeconds;
    }

    public void setHoursAgo(String hoursAgo) {
        this.hoursAgo = hoursAgo;
    }

    public String getHoursAgo() {
        return hoursAgo;
    }

    public Preview getPreview() {
        return preview;
    }

    @Override
    public String toString() {
        return "RedditLink{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
