package com.recovery.data.forwhatsapp.documentspkg;

import android.os.Environment;
import android.util.Log;

import com.recovery.data.forwhatsapp.MyFilesComparatorOKRA;
import com.recovery.data.forwhatsapp.constants.MyConstantsOKRA;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class DocumentsManageRecoveryDirectoryOKRA {
    private static final String TAG="92727";
    private final static String recovery_documents_path =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/.WhatsApp Data Recover/recycle Bin/"+ MyConstantsOKRA.Companion.getDOCUMENTS_PATH();

    ArrayList<File> recovery_files=new ArrayList<>();

    public ArrayList<File> get_Documents_Images(){
        recovery_files.clear();
        File file= setRecovery_documents_Directory();
        File[] files = file.listFiles();
        Arrays.sort(files,new MyFilesComparatorOKRA());
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
