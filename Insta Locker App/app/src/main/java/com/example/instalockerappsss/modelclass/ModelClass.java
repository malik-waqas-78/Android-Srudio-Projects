package com.example.instalockerappsss.modelclass;

import android.util.Log;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ModelClass extends RealmObject {
    String email;
    @PrimaryKey
    String Name;
    String userName;
    String date;
    Boolean lock;
    String subString;
    String name_firstLetter;
    public boolean visible=false,checked=false;
public String TAG="ModelClass";
    public void setEmail(String email) {
        this.email = email;
        Log.d(TAG, "setEmail: "+email);
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getEmail() {
        return email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
        Log.d("TAG", "setName: " + name);
    }

    public String getSubString() {
        return subString;
    }

    public void setSubString(String subString) {
        this.subString = subString;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName_firstLetter() {
        return name_firstLetter;
    }

    public void setName_firstLetter(String name_firstLetter) {
        this.name_firstLetter = name_firstLetter;
    }
}
