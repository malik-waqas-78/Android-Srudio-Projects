package com.example.whatsappchatlocker;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import io.realm.Realm;

public class Service extends AccessibilityService {
    String Name;
    Realm realm;
    RealmHelper realmHelper;
    Boolean lock, share;
    AccessibilityServiceInfo config = new AccessibilityServiceInfo();

    @Override
    protected void onServiceConnected() {

        config.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED;
        config.packageNames = new String[]{"com.yowhatsapp"};


        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        if (Build.VERSION.SDK_INT >= 16)
            //Just in case this helps
            config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
            startApp();

        setServiceInfo(config);

    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            if (accessibilityEvent.getPackageName() != null){
                realm = Realm.getDefaultInstance();
                realmHelper = new RealmHelper(realm, this);
                AccessibilityNodeInfo accessibilityNodeInfo = accessibilityEvent.getSource();
                printAllViews(accessibilityNodeInfo);
            }
        }
    }

    @Override
    public void onInterrupt() {}

    private void printAllViews(AccessibilityNodeInfo mNodeInfo) {
        if (mNodeInfo == null)
            return;
        Name = String.valueOf(mNodeInfo.getChild(1).getText());
        SharedPreferences preferences = getSharedPreferences("Lock", 0);
        lock = preferences.getBoolean("whatsapp", false);
        share = preferences.getBoolean("share", false);
        Record object = realm.where(Record.class).equalTo("Name", Name).findFirst();
        if (lock) {
            if (object == null) {
                Record record = new Record();
                record.setName(Name);
                record.setLock(true);
                realmHelper.SaveData(record);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("Name", Name);
                startActivity(intent);

            } else
                if (!share){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Contact Already in Locker List", Toast.LENGTH_SHORT).show();
            }
        }else
            {
                if (object != null) {
                    boolean getLock = object.getLock();
                    if (getLock) {
                        Intent intent = new Intent(this, LockActivity.class);
                        startActivity(intent);
                    }
                }

            }
    }


    private void startApp() {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.whatsappchatlocker").setClass(this, MainActivity.class);
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(launchIntent);
    }
}
