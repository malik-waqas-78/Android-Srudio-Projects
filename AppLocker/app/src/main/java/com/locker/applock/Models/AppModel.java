package com.locker.applock.Models;

import android.graphics.drawable.Drawable;

public class AppModel {
    // Name of the App and Description
    String name;
    String packageName;
    // Drawable of app icon
    Drawable icon;
    // Visible/Gone status of locked icon on right side
    int isLocked;

    public AppModel(String name, String packageName, Drawable icon, int isLocked) {
        this.name = name;
        this.packageName = packageName;
        this.icon = icon;
        this.isLocked = isLocked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(int isLocked) {
        this.isLocked = isLocked;
    }
}
