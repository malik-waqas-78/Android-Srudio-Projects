package com.screen.mirror;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import androidx.annotation.NonNull;

import com.screen.mirror.Utils.AppOpenManager_OKRA;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.screen.mirror.Utils.LocaleManager_CA;

import java.util.Map;

public class MyApplication_OKRA extends Application {
    AppOpenManager_OKRA appOpenManagerOKRA;
    @Override
    protected void attachBaseContext(Context baseContext) {
        super.attachBaseContext(new LocaleManager_CA(baseContext).SetLocale());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        new LocaleManager_CA(this).SetLocale();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the Audience Network SDK
        // TODO: Initialize Adsense of Both FB and AdMob
       AudienceNetworkAds.initialize(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull  InitializationStatus initializationStatus) {
                Map<String,AdapterStatus> map = initializationStatus.getAdapterStatusMap();
                for (Map.Entry<String,AdapterStatus> entry : map.entrySet()) {
                    String className = entry.getKey();
                    AdapterStatus status = entry.getValue();
                    if (status.getInitializationState().equals(AdapterStatus.State.NOT_READY)) {

                            // The adapter initialization did not complete.
                            Log.d("92727586243", "Adapter: " + className + " not ready.");
                        }

                        else if (status.getInitializationState().equals(AdapterStatus.State.READY))  {
                            // The adapter was successfully initialized.
                            Log.d("92727586243", "Adapter: " + className + " is initialized.");
                        }

                    }
                appOpenManagerOKRA = new AppOpenManager_OKRA(MyApplication_OKRA.this);
                }

        });

    }
}
