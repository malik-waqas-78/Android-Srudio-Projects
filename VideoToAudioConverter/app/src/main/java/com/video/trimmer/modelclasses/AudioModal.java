package com.video.trimmer.modelclasses;

import android.net.Uri;

public class AudioModal {

    private Uri uri;
    private String name;
    private long duration;
    private long size;
    String originalPath;
    public AudioModal(Uri uri, String name, long duration, long size,String originalPath) {
        this.uri = uri;
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.originalPath=originalPath;
    }

    public AudioModal() {
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
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
