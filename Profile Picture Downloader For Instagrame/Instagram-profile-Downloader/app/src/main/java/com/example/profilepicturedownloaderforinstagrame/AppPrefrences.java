package com.example.profilepicturedownloaderforinstagrame;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
public class AppPrefrences {
    public static final String APP_PREFERENCES_FILE_NAME = "userData";
    public static final String USER_ID = "userid";
    public static final String TOKEN = "token";
    public static final String PROFILE_PIC = "profile_pic";
    public static final String USER_NAME = "username";
    private SharedPreferences sharedPreferences;
    public AppPrefrences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(APP_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }
    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();

    }
    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}