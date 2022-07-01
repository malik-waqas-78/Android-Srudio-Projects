package com.test.recycleright;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;

public class MySharedPreferences {

    private static MySharedPreferences mySharedPreferences=null;
    SharedPreferences sharedPreferences;
    String key="scores";
    public static MySharedPreferences getInstance(Context context){
        if(mySharedPreferences==null){
            mySharedPreferences=new MySharedPreferences(context);
        }
        return mySharedPreferences;
    }
    public MySharedPreferences(Context context){
        sharedPreferences=context.getSharedPreferences("My Gifts",Context.MODE_PRIVATE);
    }

    public void incrementScores(){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        int scores=getScores()+5;
        editor.putInt(key,scores);
        editor.apply();
    }
    public int getScores(){
        return sharedPreferences.getInt(key,0);
    }

}
