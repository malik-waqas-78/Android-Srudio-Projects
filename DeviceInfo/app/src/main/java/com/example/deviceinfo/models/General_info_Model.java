package com.example.deviceinfo.models;

public class General_info_Model {
    String name;
    String info;

    public General_info_Model() {
    }

    public General_info_Model(String name, String info) {
        this.name = name;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
