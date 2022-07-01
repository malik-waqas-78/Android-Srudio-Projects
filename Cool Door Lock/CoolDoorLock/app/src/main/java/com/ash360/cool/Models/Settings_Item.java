package com.ash360.cool.Models;

public class Settings_Item {
    private String Title;
    private int hasSwitch;
    private int Icon;
    private boolean isChecked;

    public Settings_Item(String title, int hasSwitch, int icon, boolean isChecked) {
        Title = title;
        this.hasSwitch = hasSwitch;
        Icon = icon;
        this.isChecked = isChecked;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getHasSwitch() {
        return hasSwitch;
    }

    public void setHasSwitch(int hasSwitch) {
        this.hasSwitch = hasSwitch;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getIcon() {
        return Icon;
    }

    public void setIcon(int icon) {
        Icon = icon;
    }
}
