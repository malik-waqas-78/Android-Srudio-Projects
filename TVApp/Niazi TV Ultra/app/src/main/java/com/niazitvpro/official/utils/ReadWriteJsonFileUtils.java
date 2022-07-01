package com.niazitvpro.official.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class ReadWriteJsonFileUtils {
    Activity activity;
    Context context;

    public ReadWriteJsonFileUtils(Context context) {
        this.context = context;
    }

    public void createJsonFileData(String filename, String mJsonResponse) {

        if(context!=null){

            try {
                File checkFile = new File(context.getApplicationInfo().dataDir + "/NiaziTVPro/");
                if (!checkFile.exists()) {
                    checkFile.mkdir();
                }
                FileWriter file = new FileWriter(checkFile.getAbsolutePath() + "/" + filename);
                Log.d("path====",checkFile.getAbsolutePath()+ "/" + filename);
                file.write(mJsonResponse);
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String readJsonFileData(String filename) {

        if(context!=null) {
            try {
                File f = new File(context.getApplicationInfo().dataDir + "/NiaziTVPro/" + filename);
                if (!f.exists()) {
                    return null;
                }
                FileInputStream is = new FileInputStream(f);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                return new String(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public void deleteFile() {
        File f = new File(context.getApplicationInfo().dataDir + "/NiaziTVPro/");
        File[] files = f.listFiles();
        for (File fInDir : files) {
            fInDir.delete();
        }
    }

    public void deleteFile(String fileName) {
        if(context!=null) {
            File f = new File(context.getApplicationInfo().dataDir + "/NiaziTVPro/" + fileName);
            if (f.exists()) {
                f.delete();
            }
        }
    }

    public boolean isFileExist(String filename){
        if(context!=null) {
            File f = new File(context.getApplicationInfo().dataDir + "/NiaziTVPro/" + filename);
            if (!f.exists()) {
                return false;
            }
        }
        return true;
    }
}
