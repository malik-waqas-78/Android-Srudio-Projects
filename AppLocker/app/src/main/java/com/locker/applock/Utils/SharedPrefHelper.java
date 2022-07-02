package com.locker.applock.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefHelper {
    private final SharedPreferences sharedPreferences;

    public SharedPrefHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_APP_LOCK, Context.MODE_PRIVATE);
    }

    public void ClearSharedPreferences() {
        sharedPreferences.edit().clear().apply();
    }

    public void Set_Boolean_AL(String KEY, boolean Val) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putBoolean(KEY, Val);
        ed.apply();
    }

    public boolean Get_Boolean_AL(String KEY, boolean defaultValue) {
        return sharedPreferences.getBoolean(KEY, defaultValue);
    }

    public void Set_Int_AL(String KEY, int Val) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt(KEY, Val);
        ed.apply();
    }

    public int Get_Int_AL(String KEY, int defaultValue) {
        return sharedPreferences.getInt(KEY, defaultValue);
    }

    public void Set_String_AL(String KEY, String Val) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(KEY, Val);
        ed.apply();
    }

    public String Get_String_AL(String KEY, String defaultValue) {
        return sharedPreferences.getString(KEY, defaultValue);
    }
}
