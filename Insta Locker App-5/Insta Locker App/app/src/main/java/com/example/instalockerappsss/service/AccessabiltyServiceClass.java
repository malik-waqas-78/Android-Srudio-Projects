package com.example.instalockerappsss.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.instalockerappsss.activities.LockActivity;
import com.example.instalockerappsss.activities.MainActivity;
import com.example.instalockerappsss.database.RealmHelper;
import com.example.instalockerappsss.modelclass.ModelClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import io.realm.Realm;

public class AccessabiltyServiceClass extends AccessibilityService {
    private static final String TAG = "findname";
    Realm realm;
    RealmHelper realmHelper;
    int counter = 0;
    boolean clicked = false;

    @Override
    protected void onServiceConnected() {
        AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        config.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        config.packageNames = new String[]{"com.instagram.android"};
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        config.flags = AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS;
        this.setServiceInfo(config);
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        Log.d(TAG, "onKeyEvent: ");
        if (event.getKeyCode() == KeyEvent.KEYCODE_HOME || event.getKeyCode() == KeyEvent.KEYCODE_APP_SWITCH) {
            SharedPreferences preferences = getSharedPreferences("Lock", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("resumeAppLock", true);
            if (preferences.getBoolean("setAppPermission", false) == true) {
                editor.putBoolean("appLock", true);
                editor.commit();
                Log.d(TAG, "onKeyEvent: true");
            }else{
                editor.putBoolean("appLock", false);
                editor.commit();
            }
        }
        return false;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getPackageName() == null
                || !accessibilityEvent.getPackageName().equals("com.instagram.android"))
            return;
        String Name = "null";
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            if (accessibilityEvent.getPackageName() != null) {
                Realm.init(this);
                realm = Realm.getDefaultInstance();
                realmHelper = new RealmHelper(realm, this);
                AccessibilityNodeInfo accessibilityNodeInfo = accessibilityEvent.getSource();
                Name = printAllViews(accessibilityNodeInfo);
                clicked = true;
            }
        }
        switch (accessibilityEvent.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                SharedPreferences preferences = getSharedPreferences("Lock", 0);
                if (preferences.getBoolean("setAppPermission", false) == true) {
                    if (preferences.getBoolean("appLock", false) == true) {
                        Intent intent = new Intent(this, LockActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("appLock", true);
                        Log.d(TAG, "onAccessibilityEvent: " + preferences.getBoolean("appLock", false));
                        Log.d(TAG, "onAccessibilityEvent: " + preferences.getBoolean("setAppPermission", false));
                        startActivity(intent);
                        return;
                    }

                } else if (!preferences.getBoolean("setAppPermission", false) == false) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("appLock", false);
                    return;
                } else {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("appLock", false);
                    Log.d(TAG, "onAccessibilityEvent1: " + preferences.getBoolean("appLock", false));
                    Log.d(TAG, "onAccessibilityEvent1: " + preferences.getBoolean("setAppPermission", false));
                }
                if (preferences.getBoolean("resumeAppLock", false) == true) {
                    Intent intent = new Intent(this, LockActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("resumeAppLock", true);
                    startActivity(intent);
                    return;
                }
                if (clicked) {
                    clicked = false;
                } else {
                    return;
                }
                Log.d(TAG, "onAccessibilityEvent: windwo state changed");
                Realm.init(this);
                realm = Realm.getDefaultInstance();
                realmHelper = new RealmHelper(realm, this);
                preferences = getSharedPreferences("Lock", 0);
                AccessibilityNodeInfo accessibilityNodeInfo = accessibilityEvent.getSource();
                onResumeLock(accessibilityNodeInfo, accessibilityEvent, Name);
                break;
        }
    }

    private String printTree(AccessibilityNodeInfo source) {
        if (source != null) {
            for (int i = 0; i < source.getChildCount(); i++) {
                if (source.getChild(i) != null) {
                    if (source.getChild(i).getText() != null) {
                        Log.d(TAG, "printTree: " + source.getChild(i).getText());
                        String username = source.getChild(i).getText().toString();
                        if (source.getChild(i).getViewIdResourceName() != null)
                            Log.d(TAG, "printTree: " + source.getChild(i).getViewIdResourceName());
                        return username;
                    } else {
                        String str = printTree(source.getChild(i));
                        if (!str.equals("null")) {
                            return str;
                        }
                    }
                }
            }
            return "null";
        }
        return "null";
    }

    @Override
    public void onInterrupt() {
    }

    private void onResumeLock(AccessibilityNodeInfo mNodeInfo, AccessibilityEvent accessibilityEvent, String Name) {
        if (mNodeInfo == null)
            return;
        String username = printTree(mNodeInfo);
        if (username.equals("null") || username == null || (accessibilityEvent.getEventType() != AccessibilityEvent.TYPE_VIEW_CLICKED && Name == null))
            return;
        if (username.contains("Search") || username.contains("Your Story") || username.contains("Your story") || username.contains("Welcome to Instagram"))
            return;
        SharedPreferences preferences = getSharedPreferences("Lock", 0);
        boolean lock = preferences.getBoolean("lock", false);
        DateFormat df = new SimpleDateFormat("d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        ModelClass object = realm.where(ModelClass.class).equalTo("Name", username).findFirst();
        if (object == null) {
            object = realm.where(ModelClass.class).equalTo("userName", username).findFirst();
        }
        if (lock) {
            if (object == null) {
                ModelClass record = new ModelClass();
                record.setUserName(Name);
                record.setName(username);
                record.setDate(date);
                record.setLock(true);
                record.setName_firstLetter(username.substring(0, 1));
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("lock", false);
                editor.apply();
                realmHelper.SaveData(record);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("Name", username);
                intent.putExtra("Date", date);
                intent.putExtra("resume", true);
                Log.d(TAG, "printAllViews: " + "Name IS : " + username);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else {
            if (object != null) {
                boolean getLock = object.getLock();
                if (getLock) {
                    Intent intent = new Intent(this, LockActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return;
                }
            }
        }
    }

    private String printAllViews(AccessibilityNodeInfo mNodeInfo) {
        if (mNodeInfo == null)
            return null;
        String Name = getchildsinfo(mNodeInfo);
        return Name;
    }

    public String getchildsinfo(AccessibilityNodeInfo mNodeInfo) {
        Log.d(TAG, "getchildsinfo: " + mNodeInfo.getClassName());
        if (mNodeInfo.getClassName().equals("android.widget.LinearLayout")) {
            Log.d(TAG, "getchildsinfo: Linear Layout ");
            if (mNodeInfo.getChild(0) != null && mNodeInfo.getChild(0).getClassName().equals("android.widget.FrameLayout")) {
                Log.d(TAG, "getchildsinfo: F L ");
                mNodeInfo = mNodeInfo.getChild(0);
                if (mNodeInfo.getChild(0) != null && mNodeInfo.getChild(0).getClassName().equals("android.widget.ImageView")) {
                    Log.d(TAG, "getchildsinfo: Image view");
                    mNodeInfo = mNodeInfo.getParent().getChild(1);
                    if (mNodeInfo != null && mNodeInfo.getClassName().equals("android.view.ViewGroup")) {
                        Log.d(TAG, "getchildsinfo: View Group ");
                        if (mNodeInfo.getChild(0) != null && mNodeInfo.getChild(0).getClassName().equals("android.widget.TextView")) {
                            Log.d(TAG, "getchildsinfo: TextView ");
                            if (mNodeInfo.getChild(0).getText() != null) {
                                Log.d(TAG, "getchildsinfo: Linear Layout ");
                                return mNodeInfo.getChild(0).getText().toString();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public void listAllActivities(Context context) {
        PackageManager pManager = context.getPackageManager();
        String packageName = context.getApplicationContext().getPackageName();
        try {
            ActivityInfo[] list = pManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).activities;
            for (ActivityInfo activityInfo : list) {
                Log.d(TAG, "ActivityInfo = " + activityInfo.name);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}