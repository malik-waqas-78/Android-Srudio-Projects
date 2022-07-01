package com.example.whatsappdatarecovery.database;

import com.example.whatsappdatarecovery.modelclass.ModelClass;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyHelperClass {
    Realm realm;
    RealmResults<ModelClass> modelClass;

    public MyHelperClass(Realm realm) {
        this.realm = realm;
    }
    public void SelectFromDB()
    {
        modelClass=realm.where(ModelClass.class).findAll();
    }
    public ArrayList<ModelClass> justRefresh()
    {
        ArrayList<ModelClass> modelClassArrayList=new ArrayList<>();
        for (ModelClass md:modelClass)
        {
            modelClassArrayList.add(md);
        }
        return modelClassArrayList;
    }

}
