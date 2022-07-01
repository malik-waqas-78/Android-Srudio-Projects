package com.example.profilepicturedownloaderforinstagrame.activities;

public interface FetchListener<T> {
    void onResult(T result);
    default void doBefore() { }
}