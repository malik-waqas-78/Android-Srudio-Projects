package com.su.employeesrecord;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "EmployeeManagement";

    // Employee table name
    private static final String TABLE_EMPLOYEE = "employee";

    // Employee Table Columns names
    private static final String KEY_EMP_ID = "emp_id";
    private static final String KEY_EMP_NAME = "emp_name";
    private static final String KEY_EMP_PH_NO = "emp_phone_number";

    private String CREATE_EMPLOYEE_TABLE = "CREATE TABLE " + TABLE_EMPLOYEE + "("
            + KEY_EMP_ID + " INTEGER PRIMARY KEY," + KEY_EMP_NAME + " TEXT,"
            + KEY_EMP_PH_NO + " TEXT" + ")";

    private String DROP_EMPLOYEE_TABLE = "DROP TABLE IF EXISTS " + TABLE_EMPLOYEE;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EMPLOYEE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop Employee Table if exist
        db.execSQL(DROP_EMPLOYEE_TABLE);

        // Create tables again
        onCreate(db);

    }
    //Create employee record
    public void addEmployee(Employee employee){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMP_NAME, employee.getEmpName());
        values.put(KEY_EMP_PH_NO, employee.getEmpPhoneNo());

        // Inserting Row
        db.insert(TABLE_EMPLOYEE, null, values);
        db.close();
    }

    public ArrayList<Employee> getAllEmployee() {
        ArrayList<Employee> employeeList = new ArrayList<Employee>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EMPLOYEE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Employee employee = new Employee();
                employee.setEmpId(Integer.parseInt(cursor.getString(0)));
                employee.setEmpName(cursor.getString(1));
                employee.setEmpPhoneNo(cursor.getString(2));
                // Adding employee record to list
                employeeList.add(employee);
            } while (cursor.moveToNext());
        }

        // return employee list
        return employeeList;
    }

    public int updateEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMP_NAME, employee.getEmpName());
        values.put(KEY_EMP_PH_NO, employee.getEmpPhoneNo());

        // updating row
        return db.update(TABLE_EMPLOYEE, values, KEY_EMP_ID + " = ?",
                new String[] { String.valueOf(employee.getEmpId()) });
    }
    public void deleteEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EMPLOYEE, KEY_EMP_ID + " = ?",
                new String[]{String.valueOf(employee.getEmpId())});
        db.close();
    }

}


