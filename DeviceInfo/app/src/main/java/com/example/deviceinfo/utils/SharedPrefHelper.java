package com.example.deviceinfo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefHelper {

    Context context;

    public static String TOTAL_TESTS_KEY="Total tests made till";
    public String SHARED_KEY="MysharedPreferencesindeviceinfo";

    public SharedPrefHelper(Context context) {
        this.context = context;
    }

    public void saveTestMadeinShared(int value){
        SharedPreferences.Editor myEdit = context.getSharedPreferences(SHARED_KEY, MODE_PRIVATE).edit();
        myEdit.putInt(TOTAL_TESTS_KEY,value);
        myEdit.apply();
    }
    public int getTestMadefromShared(){
        int pin=context.getSharedPreferences(SHARED_KEY,MODE_PRIVATE).getInt(TOTAL_TESTS_KEY,0);
        return pin;
    }


}
