package com.screen.mirror.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefHelperCA {
    private final SharedPreferences sharedPreferences;

    public SharedPrefHelperCA(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants_CA.SHARED_PREFS_SCREEN_MIRRORING, Context.MODE_PRIVATE);
    }

    public void ClearSharedPreferences() {
        sharedPreferences.edit().clear().apply();
    }

    public void Set_Boolean_SM(String KEY, boolean Val) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putBoolean(KEY, Val);
        ed.apply();
    }

    public boolean Get_Boolean_SM(String KEY, boolean defaultValue) {
        return sharedPreferences.getBoolean(KEY, defaultValue);
    }

    public void Set_Int_SM(String KEY, int Val) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt(KEY, Val);
        ed.apply();
    }

    public int Get_Int_SM(String KEY, int defaultValue) {
        return sharedPreferences.getInt(KEY, defaultValue);
    }

    public void Set_String_SM(String KEY, String Val) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(KEY, Val);
        ed.apply();
    }

    public String Get_String_SM(String KEY, String defaultValue) {
        return sharedPreferences.getString(KEY, defaultValue);
    }

}
