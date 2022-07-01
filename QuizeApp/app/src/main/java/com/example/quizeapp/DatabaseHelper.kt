package com.example.quizeapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.reflect.Constructor

class DatabaseHelper(
    context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int
): SQLiteOpenHelper(context, name, factory, version) {

    // Database Version
    private val DATABASE_VERSION = 1

    // Database Name
    private val DATABASE_NAME = "EmployeeManager"

    // Employee table name
    private val TABLE_EMPLOYEE = "employee"

    // Employee Table Columns names
    private val KEY_EMP_ID = "emp_id"
    private val KEY_EMP_NAME = "emp_name"
    private val KEY_DEpartment = "emp_department"



    private val DROP_EMPLOYEE_TABLE = "DROP TABLE IF EXISTS $TABLE_EMPLOYEE"


    override fun onCreate(db: SQLiteDatabase?) {
      db?.execSQL("CREATE TABLE Employee ( ID INTEGER PRIMARY KEY, NAME TEXT, DEPARTMENT INTEGER NOT NULL);");
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DROP_EMPLOYEE_TABLE);

        // Create tables again
        onCreate(db);
    }

    fun insertEmployee(id:Int,name:String,dep:Int):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("ID", id)
        contentValues.put("Name", name)
        contentValues.put("Department",dep )
        // Inserting Row
        val success = db.insert("Employee", null, contentValues)

        db.close()

        return success
    }
    fun deleteEmployee():Int{
        val db = this.writableDatabase


        val success = db.delete("Employee","Department=2",null)

        db.close()
        return success
    }
}