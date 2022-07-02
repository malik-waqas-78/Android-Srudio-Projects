package com.locker.applock.Models;

public class SettingsModel {
    private String Title;
    private String Desc;
    private int Icon;
    private int hasSwitch;
    private int shouldShowExtraText;
    private boolean isChecked;

    public SettingsModel(String title, String desc, int Icon, int hasSwitch, boolean isChecked, int shouldShowExtraText) {
        Title = title;
        Desc = desc;
        this.Icon = Icon;
        this.hasSwitch = hasSwitch;
        this.isChecked = isChecked;
        this.shouldShowExtraText = shouldShowExtraText;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
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

    public int isShouldShowExtraText() {
        return shouldShowExtraText;
    }

    public void setShouldShowExtraText(int shouldShowExtraText) {
        this.shouldShowExtraText = shouldShowExtraText;
    }
}
