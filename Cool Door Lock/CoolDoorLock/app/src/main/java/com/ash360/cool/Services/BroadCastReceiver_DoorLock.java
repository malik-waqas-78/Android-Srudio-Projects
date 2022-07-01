package com.ash360.cool.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.ash360.cool.Locks.FullScreenLock;
import com.ash360.cool.Utils.Constants_DoorLock;
import com.ash360.cool.Utils.Shared_Pref_DoorLock;

public class BroadCastReceiver_DoorLock extends BroadcastReceiver {
    private final String ONCE_RANG = "ONCE_RANG";
    private boolean screenOff, isLockEnabled;
    private Shared_Pref_DoorLock shared_pref_doorLock;

    @Override
    public void onReceive(Context context, Intent intent) {
        shared_pref_doorLock = new Shared_Pref_DoorLock(context);
        screenOff = intent.getAction().equals(Intent.ACTION_SCREEN_OFF);
        isLockEnabled = shared_pref_doorLock.GetBool(Constants_DoorLock.ENABLE_LOCK
                , Constants_DoorLock.ENABLE_LOCK_DEFAULT_VAL);
        if (screenOff && isLockEnabled) {
            context.startActivity(new Intent(context, FullScreenLock.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            context.startActivity(new Intent(context, FullScreenLock.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        if (intent.hasExtra(TelephonyManager.EXTRA_STATE)) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state != null && state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                context.sendBroadcast(new Intent().setAction("RINGING_DURING_LOCK"));
                shared_pref_doorLock.SetBool(ONCE_RANG, true);
            }
            if (state != null && state.equals(TelephonyManager.EXTRA_STATE_IDLE)
                    && shared_pref_doorLock.GetBool(ONCE_RANG, false)) {
                context.startActivity(new Intent(context, FullScreenLock.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                shared_pref_doorLock.SetBool(ONCE_RANG, false);
            }
        }
    }
}
