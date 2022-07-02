package com.recovery.data.forwhatsapp;

public class MainMenuModalOKRA {
    String text;
    int icon;

    public MainMenuModalOKRA(String text, int icon) {
        this.text = text;
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public int getIcon() {
        return icon;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
