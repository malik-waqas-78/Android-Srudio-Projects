package com.example.whatsappdatarecovery.services;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.os.Build;
import android.service.notification.NotificationListenerService;

import static android.service.notification.NotificationListenerService.requestRebind;

public class App extends Application {
    public static final String CHANNEL_ID = "exampleServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            ComponentName componentName =
                    new ComponentName(getApplicationContext(), NotificationListenerService.class);

            //It say to Notification Manager RE-BIND your service to listen notifications again inmediatelly!
            requestRebind(componentName);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}