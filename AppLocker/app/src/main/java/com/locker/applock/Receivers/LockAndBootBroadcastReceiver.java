package com.locker.applock.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.locker.applock.Services.NotificationForegroundService;
import com.locker.applock.Utils.SharedPrefHelper;

import static com.locker.applock.Utils.Constants.ACCESSIBILITY_MODE;
import static com.locker.applock.Utils.Constants.CURRENT_PACKAGE;
import static com.locker.applock.Utils.Constants.CURRENT_PACKAGE_DEFAULT;
import static com.locker.applock.Utils.Constants.IS_LOCK_ENABLED;

public class LockAndBootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            Log.v("receivers", "Rainbow six going Dark");
            new SharedPrefHelper(context).Set_String_AL(CURRENT_PACKAGE, CURRENT_PACKAGE_DEFAULT);
        }
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            if (new SharedPrefHelper(context).Get_Boolean_AL(IS_LOCK_ENABLED, true)
            && !(new SharedPrefHelper(context).Get_Boolean_AL(ACCESSIBILITY_MODE,false))
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(new Intent(context.getApplicationContext(), NotificationForegroundService.class));
                } else {
                    context.startService(new Intent(context.getApplicationContext(), NotificationForegroundService.class));
                }
            }
        }
    }
}
