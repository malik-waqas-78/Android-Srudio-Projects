package com.video.trimmer.modelclasses;

import android.net.Uri;

public class VideoModal {
    private Uri uri;
    private String name;
    private long duration;
    private long size;
    String thumbnail;
    String originalPath;
    public VideoModal(Uri uri, String name, long duration, long size,String thumbnail,String originalPath) {
        this.uri = uri;
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.thumbnail=thumbnail;
        this.originalPath=originalPath;
    }

    public VideoModal() {
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
