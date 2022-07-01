package com.example.instalockerappsss.database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.example.instalockerappsss.modelclass.ModelClass;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {
    Realm realm;
    Context mcontext;
    ModelClass date1, record;
    public String TAG = "RealmHelper";

    public RealmHelper(Realm realm, Context mcontext) {
        this.realm = realm;
        this.mcontext = mcontext;
    }

    public void SaveData(final ModelClass record) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ModelClass record1 = realm.copyToRealm(record);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
//             Toast.makeText(mcontext, "Data Inserted", Toast.LENGTH_SHORT).show();

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
//             Toast.makeText(mcontext, "Data not  Inserted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveEmail(final ModelClass record) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ModelClass record1 = realm.copyToRealm(record);
            }
        });
    }

    public ArrayList<ModelClass> retriveData() {
        ArrayList<ModelClass> modelRecord = new ArrayList<>();
        RealmResults<ModelClass> recordRealmResults = realm.where(ModelClass.class).findAll();
        for (ModelClass record : recordRealmResults) {
            modelRecord.add(record);
        }
        return modelRecord;
    }

    public ArrayList<ModelClass> retrieveSavedEmail() {
        ArrayList<ModelClass> modelRecord = new ArrayList<>();
        RealmResults<ModelClass> recordRealmResults = realm.where(ModelClass.class).findAll();
        for (ModelClass record : recordRealmResults) {
            modelRecord.add(record);
        }
        return modelRecord;
    }

    public void deleteData(final String name) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                record = realm.where(ModelClass.class).equalTo("Name", name).findFirst();
                Log.e("name is : ", ":" + name);
                record.deleteFromRealm();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(mcontext, "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Update(final String name, final Boolean lock) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
            /*    RealmResults<Record> rows = realm.where(Record.class).equalTo("id", id ).findAll();
                rows.deleteAllFromRealm();*/
                ModelClass record = realm.where(ModelClass.class).equalTo("Name", name).findFirst();
                Log.e("name", ":" + name);
                record.setLock(lock);
            }
        });
    }

    public void update_Visibility(final String name, final Boolean visible) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
            /*    RealmResults<Record> rows = realm.where(Record.class).equalTo("id", id ).findAll();
                rows.deleteAllFromRealm();*/
                ModelClass record = realm.where(ModelClass.class).equalTo("Name", name).findFirst();
                Log.e("name", ":" + name);
                record.setVisible(visible);
            }
        });
    }

    public void update_checked(final String name, final Boolean checked) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
            /*    RealmResults<Record> rows = realm.where(Record.class).equalTo("id", id ).findAll();
                rows.deleteAllFromRealm();*/
                ModelClass record = realm.where(ModelClass.class).equalTo("Name", name).findFirst();
                Log.e("name", ":" + name);
                record.setChecked(checked);
            }
        });
    }

    }
