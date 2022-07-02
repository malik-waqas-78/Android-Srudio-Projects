package com.ppt.walkie.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class TempDelClass {
    private static final String TAG="92727";
    private String recovery_documents_path = null;
    Context context;

    public TempDelClass(Context context) {
        this.context = context;
        recovery_documents_path=context.getFilesDir()+"/temp3";
    }

    ArrayList<File> recovery_files=new ArrayList<>();

    public ArrayList<File> get_Documents_Images(){
        recovery_files.clear();
        File file= setRecovery_documents_Directory();
        File[] files = file.listFiles();

        Log.d(TAG, "getFilesList: path :: "+ file.getAbsolutePath());
        for (File f : files) {
            if (!f.isDirectory()) {
                Log.d(TAG, "getFilesList: filePath :: "+f.getPath());
                Log.d(TAG, "getFilesList: fileName :: "+f.getName());
                recovery_files.add(f);
            }
        }
        return recovery_files;
    }
    public File setRecovery_documents_Directory() {
        File recoveryDirectory=new File(recovery_documents_path);
        if(!recoveryDirectory.exists()){
            recoveryDirectory.mkdirs();
        }
        return recoveryDirectory;
    }
}
