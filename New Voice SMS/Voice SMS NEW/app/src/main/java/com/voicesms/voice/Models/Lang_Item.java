package com.voicesms.voice.Models;

public class Lang_Item {
    private String localName;
    private int flagId;
    private String countryCode;

    public Lang_Item(String countryName, String countryCode, int flagId) {
        this.localName = countryName;
        this.flagId = flagId;
        this.countryCode = countryCode;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public int getFlagId() {
        return flagId;
    }

    public void setFlagId(int flagId) {
        this.flagId = flagId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
