package com.recovery.data.forwhatsapp.audiopkg;

import android.os.Environment;
import android.util.Log;

import com.recovery.data.forwhatsapp.constants.MyConstantsOKRA;

import java.io.File;
import java.util.ArrayList;

public class AudiosManageRecoveryDirectoryOKRA {
    private static final String TAG="92727";
    private final static String recovery_audios_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.WhatsApp Data Recover/recycle Bin/"+ MyConstantsOKRA.Companion.getAUDIO_PATH();

    /*private final static String recovery_audios_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.WhatsAppDataRecovery/WhatsApp Audio/";*/
    ArrayList<File> recovery_files=new ArrayList<>();

    public ArrayList<File> get_Recovery_Audios(){
        recovery_files.clear();
        File file= setRecovery_Audio_Directory();
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
    public File setRecovery_Audio_Directory() {
        File recoveryDirectory=new File(recovery_audios_path);
        if(!recoveryDirectory.exists()){
            recoveryDirectory.mkdirs();
        }
        return recoveryDirectory;
    }
}
