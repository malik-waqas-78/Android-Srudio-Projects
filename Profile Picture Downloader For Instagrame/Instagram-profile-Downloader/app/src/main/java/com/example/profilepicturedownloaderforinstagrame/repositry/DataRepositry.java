package com.example.profilepicturedownloaderforinstagrame.repositry;

import com.example.profilepicturedownloaderforinstagrame.tables.Downloads;
import java.util.List;

import androidx.lifecycle.LiveData;


public interface DataRepositry {



    LiveData<List<Downloads>> getAllDownloads();


    Downloads getSelectedDownload(int id);


    long addDownloadedData(Downloads downloads);

    int deleteDownloadedData(int id);


    int checkIfUserExist(String username);


    void updateUserNameInfo(Integer id, String username, String userProfileImage);
}
