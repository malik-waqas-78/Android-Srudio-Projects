package com.speak.to.Models;

public class Model_Class {
    String fileUri;
    boolean isSelected;

    public Model_Class(String fileUri, boolean isSelected) {
        this.fileUri = fileUri;
        this.isSelected = isSelected;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
