package com.example.sqliteapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MyDBAdapter(_context : Context) {
    private val DATABASE_NAME : String = "students"
    private var mContext : Context? = null
    private var myDBHelper : MyDBHelper? = null
    private var mSQLiteDatabase : SQLiteDatabase? = null
    private val DATABASE_VERSION = 1

    init {
        this.mContext = _context
        myDBHelper = MyDBHelper(_context, DATABASE_NAME, null, DATABASE_VERSION)
    }
    public fun open(){
        mSQLiteDatabase = myDBHelper?.writableDatabase
    }

    public fun insertStudent(name:String, faculty:String){
        val cv:ContentValues = ContentValues()
        cv.put("name", name)
        cv.put("faculty", faculty)
        mSQLiteDatabase?.insert("students",null, cv)
        Log.i("DBTAG","Record Inserted Successfully.")
    }

    public fun selectAllStudents(): ArrayList<String> {
        var allStudents : ArrayList<String> = ArrayList()
        var cursor : Cursor = mSQLiteDatabase!!.query("students", null,null,null,null,null,null)
        if (cursor.moveToFirst()){
            do {
                allStudents.add(" Name : "+cursor.getString(1)+ ", ID : "+cursor.getString(0)+", Faculty : "+cursor.getString(2))
            }while (cursor.moveToNext())
        }
        return allStudents
    }
    public fun deleteAllEngineers(){
        mSQLiteDatabase?.delete("students", "faculty=Engineering",null)
        Log.i("DBTAG"," All Engineering Faculty deleted......")
    }

    public fun deleteStudents(){
        mSQLiteDatabase?.execSQL("DELETE FROM students")
        Log.i("DBTAG", "All students record deleted successfully...")
    }
    inner class MyDBHelper(context: Context?, name:String?, factory: SQLiteDatabase.CursorFactory?, version:Int) :
        SQLiteOpenHelper(context, name, factory, version) {

        override fun onCreate(db: SQLiteDatabase?) {
            val query = "CREATE TABLE students(id integer primary key autoincrement, name text, faculty integer);"
            db?.execSQL(query)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            val query = "DROP TABLE IF EXISTS students;"

            db?.execSQL(query)
            onCreate(db)
        }

    }
}