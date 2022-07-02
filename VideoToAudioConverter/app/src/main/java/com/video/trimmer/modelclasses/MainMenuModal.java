package com.video.trimmer.modelclasses;

public class MainMenuModal {
    int menuItemIcon,menuItemColor;
    String menuItemName;

    public MainMenuModal(int menuItemIcon, int menuItemColor, String menuItemName) {
        this.menuItemIcon = menuItemIcon;
        this.menuItemColor = menuItemColor;
        this.menuItemName = menuItemName;
    }

    public MainMenuModal() {
    }

    public int getMenuItemIcon() {
        return menuItemIcon;
    }

    public void setMenuItemIcon(int menuItemIcon) {
        this.menuItemIcon = menuItemIcon;
    }

    public int getMenuItemColor() {
        return menuItemColor;
    }

    public void setMenuItemColor(int menuItemColor) {
        this.menuItemColor = menuItemColor;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }
}
