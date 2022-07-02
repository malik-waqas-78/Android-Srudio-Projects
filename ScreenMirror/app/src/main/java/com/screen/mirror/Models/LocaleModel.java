package com.screen.mirror.Models;

public class LocaleModel {
    private int icon;
    private String localeName;
    private String localeCode;

    public LocaleModel(String localeName, String localeCode, int icon) {
        this.icon = icon;
        this.localeName = localeName;
        this.localeCode = localeCode;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getLocaleName() {
        return localeName;
    }

    public void setLocaleName(String localeName) {
        this.localeName = localeName;
    }

    public String getLocaleCode() {
        return localeCode;
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }
}
