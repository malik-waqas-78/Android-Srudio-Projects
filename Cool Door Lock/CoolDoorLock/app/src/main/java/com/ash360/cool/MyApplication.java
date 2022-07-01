package com.ash360.cool;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.facebook.ads.AudienceNetworkAds;

import static com.ash360.cool.Utils.Constants_DoorLock.CHANNEL_ID;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AudienceNetworkAds.initialize(this);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Cool Door Lock",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
