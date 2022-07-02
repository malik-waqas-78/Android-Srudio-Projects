package com.recovery.data.forwhatsapp.voicenotespkg;

import android.os.Environment;
import android.util.Log;

import com.recovery.data.forwhatsapp.MyFilesComparatorOKRA;
import com.recovery.data.forwhatsapp.constants.MyConstantsOKRA;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class VoiceNotesManageRecoveryDirectoryOKRA {
    private static final String TAG="92727";
    private final static String recovery_voicenotes_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.WhatsApp Data Recover/recycle Bin/"+ MyConstantsOKRA.Companion.getVOICE_NOTES();

    ArrayList<File> recovery_files=new ArrayList<>();

    public ArrayList<File> get_Recovery_VoiceNotes(){
        recovery_files.clear();
        File file= setRecovery_voicenotes_Directory();
        get_Recovery_NotesFiles(file);
        return recovery_files;
    }

    private void get_Recovery_NotesFiles(File file) {
        File[] files = file.listFiles();
        Arrays.sort(files,new MyFilesComparatorOKRA());
        Log.d(TAG, "getFilesList: path :: "+ file.getAbsolutePath());
        for(File f:files){
                recovery_files.add(f);
        }
    }

    public File setRecovery_voicenotes_Directory() {
        File recoveryDirectory=new File(recovery_voicenotes_path);
        if(!recoveryDirectory.exists()){
            recoveryDirectory.mkdirs();
        }
        return recoveryDirectory;
    }
}
