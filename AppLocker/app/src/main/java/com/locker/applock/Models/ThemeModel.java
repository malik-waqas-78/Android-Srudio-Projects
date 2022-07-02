package com.locker.applock.Models;

public class ThemeModel {
    private int Icon, IsSelected;

    public ThemeModel(int icon, int isSelected) {
        Icon = icon;
        IsSelected = isSelected;
    }

    public int getIcon() {
        return Icon;
    }

    public void setIcon(int icon) {
        Icon = icon;
    }

    public int getIsSelected() {
        return IsSelected;
    }

    public void setIsSelected(int isSelected) {
        IsSelected = isSelected;
    }
}
