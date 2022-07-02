package com.video.trimmer.utils;

import android.util.Log;


import java.io.File;
import java.util.ArrayList;

public class DataFetcherClass {
    private static final String TAG="92727";
    private final String recovery_audios_path ;
    ArrayList<File> recovery_files=new ArrayList<>();

    public DataFetcherClass(String recovery_audios_path) {
        this.recovery_audios_path = recovery_audios_path;

    }

    public ArrayList<File> get_Recovery_Audios(){
        File file= setRecovery_Audio_Directory();
        File[] files = file.listFiles();
        Log.d(TAG, "getFilesList: path :: "+ file.getAbsolutePath());
        if(files!=null) {
            for (File f : files) {
                if (!f.isDirectory()) {
                    Log.d(TAG, "getFilesList: filePath :: " + f.getPath());
                    Log.d(TAG, "getFilesList: fileName :: " + f.getName());
                    recovery_files.add(f);
                }
            }
        }
        return recovery_files;
    }

    public File setRecovery_Audio_Directory() {
        File recoveryDirectory=new File(recovery_audios_path);
        if(!recoveryDirectory.exists()){
            recoveryDirectory.mkdirs();
        }
        return recoveryDirectory;
    }
}
