package com.ash360.cool.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Shared_Pref_DoorLock {
    private final SharedPreferences sharedPreferences;

    public Shared_Pref_DoorLock(Context context) {
        sharedPreferences = context.getSharedPreferences(
                Constants_DoorLock.MY_PREF_NAME, Context.MODE_PRIVATE
        );
    }

    public void SetBool(String KEY, boolean Val) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putBoolean(KEY, Val);
        ed.apply();
    }

    public boolean GetBool(String KEY, boolean defaultValue) {
        return sharedPreferences.getBoolean(KEY, defaultValue);
    }

    public void SetInt(String KEY, int Val) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt(KEY, Val);
        ed.apply();
    }

    public int GetInt(String KEY, int defaultValue) {
        return sharedPreferences.getInt(KEY, defaultValue);
    }

    public void SetStr(String KEY, String Val) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(KEY, Val);
        ed.apply();
    }

    public String GetStr(String KEY, String defaultValue) {
        return sharedPreferences.getString(KEY, defaultValue);
    }
}
