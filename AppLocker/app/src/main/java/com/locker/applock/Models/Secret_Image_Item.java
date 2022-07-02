package com.locker.applock.Models;

public class Secret_Image_Item {
    int secretImage;
    int selected;
    String name;

    public Secret_Image_Item(int secretImage, int selected, String name) {
        this.secretImage = secretImage;
        this.selected = selected;
        this.name = name;
    }

    public int getSecretImage() {
        return secretImage;
    }

    public void setSecretImage(int secretImage) {
        this.secretImage = secretImage;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
