package com.ash360.cool.Models;

public class DoorModel {
    private int Icon, isSelected;

    public DoorModel(int icon, int isSelected) {
        Icon = icon;
        this.isSelected = isSelected;
    }

    public int getIcon() {
        return Icon;
    }

    public void setIcon(int icon) {
        Icon = icon;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }
}
