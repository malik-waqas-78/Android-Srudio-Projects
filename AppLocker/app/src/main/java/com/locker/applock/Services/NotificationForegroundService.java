package com.locker.applock.Services;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.locker.applock.Activities.HomeActivity;
import com.locker.applock.LockActivities.PatternLockActivity;
import com.locker.applock.LockActivities.PinLockActivity;
import com.locker.applock.R;
import com.locker.applock.Receivers.PackageAddedBroadcastReceiver;
import com.locker.applock.Utils.SharedPrefHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.locker.applock.Utils.Constants.ACCESSIBILITY_MODE;
import static com.locker.applock.Utils.Constants.CHANNEL_ID;
import static com.locker.applock.Utils.Constants.CURRENT_PACKAGE;
import static com.locker.applock.Utils.Constants.CURRENT_PACKAGE_DEFAULT;
import static com.locker.applock.Utils.Constants.IS_LOCK_ENABLED;
import static com.locker.applock.Utils.Constants.IS_LOCK_SET;
import static com.locker.applock.Utils.Constants.LOCKED;
import static com.locker.applock.Utils.Constants.LOCK_MODE;
import static com.locker.applock.Utils.Constants.LOCK_TYPE;
import static com.locker.applock.Utils.Constants.MATCH_LOCK;
import static com.locker.applock.Utils.Constants.PATTERN;
import static com.locker.applock.Utils.Constants.STOP_SERVICE_AND_HANDLER;
import static com.locker.applock.Utils.Constants.UNLOCKED;

public class NotificationForegroundService extends Service {
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss:SSS");
    String TAG = "Service_AppLock";
    SharedPrefHelper sharedPrefHelper;

    PackageAddedBroadcastReceiver packageAddedBroadcastReceiver;
    int delay;
    Handler handler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("service_usage_access", "Service On Start Command");
        sharedPrefHelper = new SharedPrefHelper(this);
        Intent pIntent = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, pIntent, 0);

        // Setting Up Notification
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_layout);
        notificationLayout.setTextViewText(R.id.notification_title, getResources().getString(R.string.app_name));
        notificationLayout.setTextViewText(R.id.notification_desc, getResources().getString(R.string.notification_desc));
        notificationLayout.setImageViewResource(R.id.notification_logo, R.drawable.app_icon);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.app_icon)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentIntent(pendingIntent)
                .setCustomContentView(notificationLayout)
                .setOnlyAlertOnce(true)
                .setOngoing(false)
                .build();
        startForeground(1, notification);

        // Register receiver
        packageAddedBroadcastReceiver = new PackageAddedBroadcastReceiver();

        IntentFilter filter_package_added = new IntentFilter();
        filter_package_added.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter_package_added.addDataScheme("package");
        registerReceiver(packageAddedBroadcastReceiver, filter_package_added);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkRunningPackagesInHandler();
            }
        }, 1000);

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Restart Service
        sharedPrefHelper = new SharedPrefHelper(this);
        if (!sharedPrefHelper.Get_Boolean_AL(STOP_SERVICE_AND_HANDLER, false)) {
            Intent intent = new Intent(getApplicationContext(), NotificationForegroundService.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 5000, pendingIntent);
            sharedPrefHelper.Set_Boolean_AL(STOP_SERVICE_AND_HANDLER, false);
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        Log.v("service_usage_access", "Service Destroyed");
        if (packageAddedBroadcastReceiver != null) {
            unregisterReceiver(packageAddedBroadcastReceiver);
            packageAddedBroadcastReceiver = null;
        }
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        sharedPrefHelper = new SharedPrefHelper(this);
        if (!sharedPrefHelper.Get_Boolean_AL(ACCESSIBILITY_MODE, false)) {
            Intent intent = new Intent(getApplicationContext(), NotificationForegroundService.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 5000, pendingIntent);
        }
        Log.v("service_usage_access", "Task Removed");
        super.onTaskRemoved(rootIntent);
    }

    private void checkRunningPackagesInHandler() {
        sharedPrefHelper = new SharedPrefHelper(this);
        delay = 500;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkRunningPackages();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void checkRunningPackages() {
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.MILLISECOND, -1 * delay);
        long startTime = calendar.getTimeInMillis();

        String topPackageName = null;
        UsageStatsManager usage = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> stats = usage.queryUsageStats(UsageStatsManager.INTERVAL_BEST, startTime, endTime);

        if (stats != null) {
            SortedMap<Long, UsageStats> runningTask = new TreeMap<>();
            for (UsageStats usageStats : stats) {
                runningTask.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (!runningTask.isEmpty()) {
                topPackageName = runningTask.get(runningTask.lastKey()).getPackageName();
                Log.v("service_usage_access", "Top Package ::" + topPackageName);
            }
        }
        if (topPackageName != null) {
            if (sharedPrefHelper.Get_Int_AL(topPackageName, UNLOCKED) == LOCKED) {
//                Log.v("packages", "Locked: " + topPackageName);
                if (!sharedPrefHelper.Get_String_AL(CURRENT_PACKAGE, CURRENT_PACKAGE_DEFAULT).equals(topPackageName)) {
                    sharedPrefHelper.Set_String_AL(CURRENT_PACKAGE, topPackageName);
                    if (sharedPrefHelper.Get_Boolean_AL(IS_LOCK_ENABLED, true)
                            && sharedPrefHelper.Get_Boolean_AL(IS_LOCK_SET, false)) {
                        int lock_mode = sharedPrefHelper.Get_Int_AL(LOCK_TYPE, PATTERN);
                        Intent intent;
                        if (lock_mode == PATTERN) {
                            intent = new Intent(this, PatternLockActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        } else {
                            intent = new Intent(this, PinLockActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        }
                        intent.putExtra(LOCK_MODE, MATCH_LOCK);
                        startActivity(intent);
                    }
//                    Log.v("packages", "current: " + sharedPrefHelper.Get_String_AL(CURRENT_PACKAGE, CURRENT_PACKAGE_DEFAULT));
                }
            } else {
                if (!topPackageName.equals(getPackageName())) {
                    sharedPrefHelper.Set_String_AL(CURRENT_PACKAGE, CURRENT_PACKAGE_DEFAULT);
                    Log.v("service_usage_access", "Resetting Current Package :; " + topPackageName);
                }
            }
        }
    }
}
