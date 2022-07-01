package com.voicesms.voice.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Shared_Pref_Helper_Voice_SMS {
    private final SharedPreferences sharedPreferences;

    public Shared_Pref_Helper_Voice_SMS(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_VOICE_SMS_NAME, Context.MODE_PRIVATE);
    }

    public void Set_Boolean_VSMS(String KEY, boolean Val) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putBoolean(KEY, Val);
        ed.apply();
    }

    public boolean Get_Boolean_VSMS(String KEY, boolean defaultValue) {
        return sharedPreferences.getBoolean(KEY, defaultValue);
    }

    public void Set_Int_VSMS(String KEY, int Val) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt(KEY, Val);
        ed.apply();
    }

    public int Get_Int_VSMS(String KEY, int defaultValue) {
        return sharedPreferences.getInt(KEY, defaultValue);
    }

    public void Set_String_VSMS(String KEY, String Val) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(KEY, Val);
        ed.apply();
    }

    public String Get_String_VSMS(String KEY, String defaultValue) {
        return sharedPreferences.getString(KEY, defaultValue);
    }
}
