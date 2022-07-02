package com.example.deviceinfo.models;

public class Tools_Model {

    String itemName;
    int itemIcon;

    public Tools_Model() {
    }

    public Tools_Model(String itemName, int itemIcon) {
        this.itemName = itemName;
        this.itemIcon = itemIcon;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(int itemIcon) {
        this.itemIcon = itemIcon;
    }
}
