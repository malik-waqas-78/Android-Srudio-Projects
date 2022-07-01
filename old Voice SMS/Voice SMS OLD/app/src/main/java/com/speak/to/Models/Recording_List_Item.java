package com.speak.to.Models;

public class Recording_List_Item {
    String absolutePath;
    String Name;

    public Recording_List_Item(String absolutePath, String isSelected) {
        this.absolutePath = absolutePath;
        this.Name = isSelected;
    }

    public String get_absolute_path() {
        return absolutePath;
    }

    public void set_absolute_path(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String get_name() {
        return Name;
    }

    public void set_name(String name) {
        Name = name;
    }
}
