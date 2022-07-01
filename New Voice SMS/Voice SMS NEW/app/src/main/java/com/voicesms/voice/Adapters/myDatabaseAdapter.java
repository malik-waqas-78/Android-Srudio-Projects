package com.voicesms.voice.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.voicesms.voice.Models.Message_Item;

import java.util.ArrayList;

public class myDatabaseAdapter {
    myDatabaseHelper helper;

    public myDatabaseAdapter(Context context) {
        helper = new myDatabaseHelper(context);
    }

    public long insertData(String message) {
        SQLiteDatabase dbb = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDatabaseHelper.MESSAGE, message);
        long id = dbb.insert(myDatabaseHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    public ArrayList<Message_Item> getData() {
        ArrayList<Message_Item> dataset = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {myDatabaseHelper.UID, myDatabaseHelper.MESSAGE};
        Cursor cursor = db.query(myDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int cid = cursor.getInt(cursor.getColumnIndex(myDatabaseHelper.UID));
            String msg = cursor.getString(cursor.getColumnIndex(myDatabaseHelper.MESSAGE));
            dataset.add(new Message_Item(cid, msg));
        }
        return dataset;
    }

    public boolean searchInDb(String message) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {myDatabaseHelper.UID, myDatabaseHelper.MESSAGE};
        Cursor cursor = db.query(myDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String msg = cursor.getString(cursor.getColumnIndex(myDatabaseHelper.MESSAGE));
            if (msg.equals(message)) {
                return true;
            }
        }
        return false;
    }

    public int delete(String uname) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {uname};
        int count = db.delete(myDatabaseHelper.TABLE_NAME, myDatabaseHelper.MESSAGE + " = ?", whereArgs);
        return count;
    }

    public void deleteAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {myDatabaseHelper.UID, myDatabaseHelper.MESSAGE};
        Cursor cursor = db.query(myDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String msg = cursor.getString(cursor.getColumnIndex(myDatabaseHelper.MESSAGE));
            delete(msg);
        }
    }

    static class myDatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "voiceSMSDatavase";    // Database Name
        private static final String TABLE_NAME = "myTable";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String UID = "_id";     // Column I (Primary Key)
        private static final String MESSAGE = "Message";    //Column II
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MESSAGE + " VARCHAR(255));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private final Context context;

        public myDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context = context;
        }

        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
