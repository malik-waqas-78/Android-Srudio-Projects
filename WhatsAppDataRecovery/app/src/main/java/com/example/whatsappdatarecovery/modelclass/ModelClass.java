package com.example.whatsappdatarecovery.modelclass;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ModelClass extends RealmObject {
    @PrimaryKey
    private int id;
    private String name;
    private String details;
    private String date;
    public ModelClass() {
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
