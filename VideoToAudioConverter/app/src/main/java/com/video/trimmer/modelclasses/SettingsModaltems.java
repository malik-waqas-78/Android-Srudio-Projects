package com.video.trimmer.modelclasses;

public class SettingsModaltems {
    int imageId;
    String name;
    boolean switchEnable,switchState;

    public SettingsModaltems(int imageId, String name, boolean switchEnable, boolean switchState) {
        this.imageId = imageId;
        this.name = name;
        this.switchEnable = switchEnable;
        this.switchState = switchState;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSwitchEnable() {
        return switchEnable;
    }

    public void setSwitchEnable(boolean switchEnable) {
        this.switchEnable = switchEnable;
    }

    public boolean isSwitchState() {
        return switchState;
    }

    public void setSwitchState(boolean switchState) {
        this.switchState = switchState;
    }
}
