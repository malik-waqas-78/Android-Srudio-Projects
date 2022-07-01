package com.example.whatsappchatlocker;

import io.realm.RealmObject;

public class Record extends RealmObject {

    String Name;
    Boolean lock;

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
