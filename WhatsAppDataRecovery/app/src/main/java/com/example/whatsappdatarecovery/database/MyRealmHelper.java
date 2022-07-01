package com.example.whatsappdatarecovery.database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.example.whatsappdatarecovery.modelclass.ModelClass;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmResults;
import static android.content.ContentValues.TAG;
public class MyRealmHelper {
    private Realm realm;
    private Context context;
    public MyRealmHelper(Realm realm, Context context) {
        this.realm = realm;
        this.context = context;
    }
    public void savedata(final ModelClass modelClass) {
        realm.executeTransaction(realm -> {
            realm.copyToRealm(modelClass);
            Log.d(TAG, "execute123: " + modelClass);
        });
    }
    public ArrayList<ModelClass> retrievedata() {
        ArrayList<ModelClass> modelclassdata = new ArrayList<>();
        Log.d(TAG, "retrievedata: "+modelclassdata);
        RealmResults<ModelClass> modelClass = realm.where(ModelClass.class).findAll();
        modelclassdata.addAll(modelClass);
        return modelclassdata;
    }
    public void deletedata(final int id) {
        realm.executeTransactionAsync(realm -> {
            ModelClass result = realm.where(ModelClass.class).equalTo("id", id).findFirst();
            assert result != null;
            result.deleteFromRealm();
        }, () -> Toast.makeText(context, "Data Deleted", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show());
    }
}