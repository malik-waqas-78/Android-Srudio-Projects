package com.locker.applock.Services;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.locker.applock.Activities.HomeActivity;
import com.locker.applock.LockActivities.PatternLockActivity;
import com.locker.applock.LockActivities.PinLockActivity;
import com.locker.applock.Receivers.PackageAddedBroadcastReceiver;
import com.locker.applock.Utils.SharedPrefHelper;

import static com.locker.applock.Utils.Constants.ACCESSIBILITY_MODE;
import static com.locker.applock.Utils.Constants.CURRENT_PACKAGE_DEFAULT;
import static com.locker.applock.Utils.Constants.IS_LOCK_ENABLED;
import static com.locker.applock.Utils.Constants.IS_LOCK_SET;
import static com.locker.applock.Utils.Constants.LAUNCHER_PACKAGE;
import static com.locker.applock.Utils.Constants.LOCKED;
import static com.locker.applock.Utils.Constants.LOCK_MODE;
import static com.locker.applock.Utils.Constants.LOCK_TYPE;
import static com.locker.applock.Utils.Constants.MATCH_LOCK;
import static com.locker.applock.Utils.Constants.PATTERN;
import static com.locker.applock.Utils.Constants.STOP_SERVICE_AND_HANDLER;
import static com.locker.applock.Utils.Constants.UNLOCKED;

public class AccessibilityHelperClass extends AccessibilityService {
    Context context;
    SharedPrefHelper sharedPrefHelper;
    String currentPackage;
    Boolean shouldReset;
    //    LockAndBootBroadcastReceiver lockAndBootBroadcastReceiver;
    PackageAddedBroadcastReceiver packageAddedBroadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        context = AccessibilityHelperClass.this;
        sharedPrefHelper = new SharedPrefHelper(context);

        sharedPrefHelper.Set_Boolean_AL(ACCESSIBILITY_MODE, true);
        sharedPrefHelper.Set_Boolean_AL(STOP_SERVICE_AND_HANDLER, true);

        shouldReset = true;

        currentPackage = CURRENT_PACKAGE_DEFAULT;

//        lockAndBootBroadcastReceiver = new LockAndBootBroadcastReceiver();
        packageAddedBroadcastReceiver = new PackageAddedBroadcastReceiver();

        IntentFilter filter_boot_lock = new IntentFilter();
        filter_boot_lock.addAction(Intent.ACTION_SCREEN_OFF);
//        registerReceiver(lockAndBootBroadcastReceiver, filter_boot_lock);

        IntentFilter filter_package_added = new IntentFilter();
        filter_package_added.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter_package_added.addDataScheme("package");
        registerReceiver(packageAddedBroadcastReceiver, filter_package_added);

        startActivity(new Intent(context, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        Log.v("Access_app_lock", "Accessibility Service Started");
    }

    @Override
    public void onDestroy() {
//        if (lockAndBootBroadcastReceiver != null) {
//            unregisterReceiver(lockAndBootBroadcastReceiver);
//            lockAndBootBroadcastReceiver = null;
//        }
        if (packageAddedBroadcastReceiver != null) {
            unregisterReceiver(packageAddedBroadcastReceiver);
            packageAddedBroadcastReceiver = null;
        }
        super.onDestroy();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event != null && sharedPrefHelper.Get_Boolean_AL(ACCESSIBILITY_MODE, false)) {
            switch (event.getEventType()) {
                case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED: {
                    if (event.getPackageName() != null
                            && !event.getPackageName().equals("")) {
                        Log.v("Access_app_lock", "Top Package :: " + event.getPackageName());
                        if (!event.getPackageName().toString().equals(currentPackage)
                                && !event.getPackageName().toString().equals(getPackageName())
                                && !event.getPackageName().toString().equals(sharedPrefHelper.Get_String_AL(LAUNCHER_PACKAGE, ""))
                        ) {
                            currentPackage = event.getPackageName().toString();
                            shouldReset = true;
                            Log.v("Access_app_lock", "Current Package :: " + currentPackage);
                            if (sharedPrefHelper.Get_Int_AL(currentPackage, UNLOCKED) == LOCKED
                                    && sharedPrefHelper.Get_Boolean_AL(IS_LOCK_ENABLED, true)
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
                            } else {
                                if ((!getPackageName().equals(currentPackage))) {
                                    shouldReset = false;
                                    currentPackage = CURRENT_PACKAGE_DEFAULT;
                                    Log.v("Access_app_lock", "Resetting Current Package Inner");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onInterrupt() {

    }
}
