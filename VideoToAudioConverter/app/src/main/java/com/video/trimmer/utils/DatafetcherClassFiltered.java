package com.video.trimmer.utils;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class DatafetcherClassFiltered {
    private static final String TAG="92727";
    private final String recovery_audios_path ;
    String type;



    /*private final static String recovery_audios_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.WhatsAppDataRecovery/WhatsApp Audio/";*/
    ArrayList<File> recovery_files=new ArrayList<>();

    public DatafetcherClassFiltered(String recovery_audios_path,String type) {
        this.type=type;
        this.recovery_audios_path = recovery_audios_path;
    }

    public ArrayList<File> get_Recovery_Audios(){
        File file= setRecovery_Audio_Directory();
        File[] files = file.listFiles();
        Log.d(TAG, "getFilesList: path :: "+ file.getAbsolutePath());
        if(files!=null) {
            for (File f : files) {
                if (!f.isDirectory()) {
                    if(type.equals("a")) {
                        if (f.getName().endsWith(".mp3")||
                                f.getName().endsWith(".ogg")||
                                f.getName().endsWith(".aac")||
                                f.getName().endsWith(".opus")||
                                f.getName().endsWith(".m4a")||
                                f.getName().endsWith(".flac")||
                                f.getName().endsWith(".awm")||
                                f.getName().endsWith(".wav")||
                                f.getName().endsWith(".wma"));
                    }else if(type.equals("v")){
                        if(f.getName().endsWith("mp4")||
                                f.getName().endsWith(".3gp")||
                                f.getName().endsWith(".mkv")||
                                f.getName().endsWith(".mov")||
                                f.getName().endsWith(".m4v")||
                                f.getName().endsWith(".avi")||
                                f.getName().endsWith(".flv")||
                                f.getName().endsWith(".mts")||
                        f.getName().endsWith(".ts"));
                    }
                    Log.d(TAG, "getFilesList: filePath :: " + f.getPath());
                    Log.d(TAG, "getFilesList: fileName :: " + f.getName());
                    recovery_files.add(f);
                }
            }
        }
        return recovery_files;
    }
    //    private class LoadData extends AsyncTask<String, Void, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            recovery_files.clear();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//        }
//    }
    public File setRecovery_Audio_Directory() {
        File recoveryDirectory=new File(recovery_audios_path);
        if(!recoveryDirectory.exists()){
            recoveryDirectory.mkdirs();
        }
        return recoveryDirectory;
    }
}
