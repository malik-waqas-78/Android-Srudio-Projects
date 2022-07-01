package com.test.speedmeter;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = DbLocationRect.class, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    abstract DbLocationRectDao dbLocationRectDao();

}
