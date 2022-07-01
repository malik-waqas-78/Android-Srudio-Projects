package com.speak.to;

import android.app.Application;

import com.facebook.ads.AudienceNetworkAds;


public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AudienceNetworkAds.initialize(this);
    }
}
