package com.locker.applock.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.locker.applock.Activities.TransparentDialogActivity;
import com.locker.applock.Utils.Constants;
import com.locker.applock.Utils.SharedPrefHelper;

import static com.locker.applock.Utils.Constants.IS_LOCK_NEW_APPS_ENABLED;

public class PackageAddedBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (new SharedPrefHelper(context).Get_Boolean_AL(IS_LOCK_NEW_APPS_ENABLED, true)
                && Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            String packageName = intent.getDataString().split(":")[1];
            context.startActivity(new Intent(context, TransparentDialogActivity.class)
                    .putExtra(Constants.INSTALLED_PACKAGE_NAME, packageName)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
