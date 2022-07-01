package com.ash360.cool.Services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ash360.cool.Activities.Settings_DoorLock;
import com.ash360.cool.R;

import static com.ash360.cool.Utils.Constants_DoorLock.CHANNEL_ID;

public class LockListenerService_DoorLock extends Service {
    private final int time_out_restart = 3000; //3000ms
    private BroadCastReceiver_DoorLock mReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        // Restart Service
        Intent intent = new Intent(getApplicationContext(), LockListenerService_DoorLock.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + time_out_restart, pendingIntent);
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent = new Intent(getApplicationContext(), LockListenerService_DoorLock.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + time_out_restart, pendingIntent);
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent pIntent = new Intent(this, Settings_DoorLock.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, pIntent, 0);

        // Setting Up Notification
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Cool Door Lock")
                .setContentText("Click to Open the App")
                .setSmallIcon(R.drawable.app_icon)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .build();
        startForeground(1, notification);

        // Register receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        mReceiver = new BroadCastReceiver_DoorLock();
        registerReceiver(mReceiver, filter);

        return Service.START_STICKY;
    }
}
