package com.locker.applock;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.locker.applock.Receivers.LockAndBootBroadcastReceiver;

import java.util.Map;

import static com.locker.applock.Utils.Constants.CHANNEL_ID;

public class MyApplication extends Application {
    LockAndBootBroadcastReceiver lockAndBootBroadcastReceiver;



    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("application_Class", "Created");
        lockAndBootBroadcastReceiver = new LockAndBootBroadcastReceiver();
        IntentFilter filter_boot_lock = new IntentFilter();
        filter_boot_lock.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(lockAndBootBroadcastReceiver, filter_boot_lock);

        createNotificationChannel();
        AudienceNetworkAds.initialize(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Map<String, AdapterStatus> map = initializationStatus.getAdapterStatusMap();
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

            }

        });

    }

    @Override
    public void onTerminate() {
        if (lockAndBootBroadcastReceiver != null) {
            unregisterReceiver(lockAndBootBroadcastReceiver);
            lockAndBootBroadcastReceiver = null;
        }
        Log.v("application_Class", "Terminated");
        super.onTerminate();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "App Lock Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
