package com.example.instalockerappsss.modelclass;

public class SelectedModelClass {
    String userName;
    boolean visible;

    public SelectedModelClass(String userName, boolean visible) {
        this.userName = userName;
        this.visible = visible;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
