package com.example.whatsappchatlocker;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {
    Realm realm;
    Context mcontext;

    public RealmHelper(Realm realm, Context mcontext) {
        this.realm = realm;
        this.mcontext = mcontext;
    }
  public void SaveData(final Record record)
 {
     realm.executeTransactionAsync(new Realm.Transaction() {
         @Override
         public void execute(Realm realm) {
             Record record1 = realm.copyToRealm(record);

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
    public ArrayList<Record> retriveData()
    {
        ArrayList<Record> modelRecord = new ArrayList<>();
        RealmResults<Record> recordRealmResults = realm.where(Record.class).findAll();
        for (Record record : recordRealmResults)
        {
            modelRecord.add(record);
        }
        return modelRecord;
    }
    public void DeleteData(final String name)
    {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
            /*    RealmResults<Record> rows = realm.where(Record.class).equalTo("id", id ).findAll();
                rows.deleteAllFromRealm();*/
                Record record = realm.where(Record.class).equalTo("Name", name).findFirst();
                Log.e("name",":"+name);
                record.deleteFromRealm();

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(mcontext, "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void Update(final String name, final Boolean lock)
    {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
            /*    RealmResults<Record> rows = realm.where(Record.class).equalTo("id", id ).findAll();
                rows.deleteAllFromRealm();*/
                Record record = realm.where(Record.class).equalTo("Name", name).findFirst();
                Log.e("name",":"+name);
                record.setLock(lock);

            }
        });
    }
}
