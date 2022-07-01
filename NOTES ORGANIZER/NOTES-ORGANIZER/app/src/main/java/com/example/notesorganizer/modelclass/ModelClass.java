package com.example.notesorganizer.modelclass;

import android.util.Log;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ModelClass extends RealmObject {

    @PrimaryKey
    private int id;
    String title;
    String details;
    String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ModelClass() { }

       public ModelClass(String details) {
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
        Log.d("TAG", "details1: "+details);
    }
}
