package com.example.hissabbookapp;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;

public class MyApplication extends Application {
    public static RealmConfiguration config;
    public static RealmConfiguration config2;
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
        config = new RealmConfiguration.Builder()
                        .name("test.db")
                        .schemaVersion(0)
                        .deleteRealmIfMigrationNeeded()
                        .build();
        config2=new RealmConfiguration.Builder()
                .name("subRecords.db")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();

    }
}
