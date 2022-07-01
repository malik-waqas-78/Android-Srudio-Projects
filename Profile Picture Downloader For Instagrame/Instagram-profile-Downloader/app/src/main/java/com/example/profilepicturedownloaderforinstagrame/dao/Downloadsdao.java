package com.example.profilepicturedownloaderforinstagrame.dao;

import com.example.profilepicturedownloaderforinstagrame.tables.Downloads;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
@Dao
public interface Downloadsdao {

    @Query("Select * From downloads_table")
    LiveData<List<Downloads>> getAllDownloads();

    @Query("Select * From downloads_table Where id = :id")
    Downloads getSelectedDownload(int id);

    @Insert
    long insert(Downloads downloads);

    @Query("Delete From downloads_table Where id = :id")
    int delete(int id);

}
