package com.recovery.data.forwhatsapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;

//import com.facebook.ads.AudienceNetworkAds;
//import com.google.android.gms.ads.MobileAds;

import io.realm.Realm;

public class AppOKRA extends Application {
    public static final String CHANNEL_ID = "Whatsapp_Recovery_App";
    public static final String FILE_SAVED_CHANNEL_ID="FILE_SAVED";
    @Override
    public void onCreate() {
        super.onCreate();

//        AudienceNetworkAds.initialize(this);
//        MobileAds.initialize(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        Realm.init(this);
        createNotificationChannel(CHANNEL_ID);
        createNotificationChannel(FILE_SAVED_CHANNEL_ID);
    }

    private void createNotificationChannel(String channel_id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    channel_id,
                    getString(R.string.notification_listener_service),
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
//
        }
    }

}
