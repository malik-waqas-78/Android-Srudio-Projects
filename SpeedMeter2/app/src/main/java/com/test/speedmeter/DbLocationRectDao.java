package com.test.speedmeter;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
abstract class DbLocationRectDao {
    @Query("SELECT * FROM dbLocationRect")
    public abstract List<DbLocationRect> getAll();


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insert(DbLocationRect dbLocationRect);

    @Delete
    public abstract void delete(DbLocationRect dbLocationRect);


}