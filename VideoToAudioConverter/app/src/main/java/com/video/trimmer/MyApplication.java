package com.video.trimmer;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.video.trimmer.utils.LocaleManager_CA;

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context baseContext) {
        super.attachBaseContext(new LocaleManager_CA(baseContext).SetLocale());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        new LocaleManager_CA(this).SetLocale();
    }
}
