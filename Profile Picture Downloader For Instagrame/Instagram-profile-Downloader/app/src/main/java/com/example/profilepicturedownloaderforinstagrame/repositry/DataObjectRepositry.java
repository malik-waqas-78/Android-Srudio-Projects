package com.example.profilepicturedownloaderforinstagrame.repositry;

import android.app.Application;
import android.util.Log;

import com.example.profilepicturedownloaderforinstagrame.dao.Downloadsdao;
import com.example.profilepicturedownloaderforinstagrame.tables.Downloads;

import java.util.List;

import androidx.lifecycle.LiveData;

import static android.content.ContentValues.TAG;

public class DataObjectRepositry implements DataRepositry {
    Downloadsdao downloadsdao;

    public static DataObjectRepositry dataObjectRepositry = null;

    public DataObjectRepositry(Application application) {

    }

    public static void init(Application application) {
        String PREF_NAME = application.getPackageName();
        if (dataObjectRepositry == null) {
            dataObjectRepositry = new DataObjectRepositry(application);
        } else {
            throw new RuntimeException("DataObjectRepository not initialized");
        }
    }

    @Override
    public LiveData<List<Downloads>> getAllDownloads() {
        return downloadsdao.getAllDownloads();
    }

    @Override
    public Downloads getSelectedDownload(int id) {
        return downloadsdao.getSelectedDownload(id);
    }

    @Override
    public long addDownloadedData(Downloads downloads) {
        Log.d("TAG", "addDownloadedData: "+downloads);
        return downloadsdao.insert(downloads);

    }

    @Override
    public int deleteDownloadedData(int id) {
        return downloadsdao.delete(id);
    }

    @Override
    public int checkIfUserExist(String username) {
        return 0;
    }

    @Override
    public void updateUserNameInfo(Integer id, String username, String userProfileImage) {

    }


}
