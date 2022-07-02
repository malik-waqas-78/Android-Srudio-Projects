package com.video.trimmer.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;

import com.video.trimmer.modelclasses.FolderModel;

import java.util.ArrayList;

public class FolderFetcherClass {
    private static final String TAG="92727";
    private final String recovery_audios_path ;
    boolean boolean_folder;
    private final int type;
    Context context;
    ArrayList<FolderModel> al_images=new ArrayList<>();
    /*private final static String recovery_audios_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.WhatsAppDataRecovery/WhatsApp Audio/";*/
    ArrayList<FolderModel> folders=new ArrayList<>();

    public FolderFetcherClass(Context context, int type) {
        this.recovery_audios_path = Environment.getExternalStorageDirectory().getPath();
        this.type=type;
        this.context=context;
    }

    public ArrayList<FolderModel> get_Recovery_Audios(){
        if(type==1){
            al_images=getVideoFolders();
        }else if(type==2){
            al_images=getAudioFolders();
        }else if(type==3){
            al_images=getImagesFolders();
        }
        return al_images;
    }
    private class LoadData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            al_images.clear();
        }

        @Override
        protected String doInBackground(String... strings) {

            return "Task Completed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    private ArrayList<FolderModel> getImagesFolders() {
        al_images.clear();
        ArrayList<String> temp_folder_list = new ArrayList<>();
        Uri uri;
        Cursor cursor;

        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            do {
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String dataPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                String folderPath = dataPath.substring(0, dataPath.lastIndexOf(folder + "/"));
                folderPath = folderPath + folder + "/";
                if (!temp_folder_list.contains(folderPath)) {
                    temp_folder_list.add(folderPath);
                    al_images.add(new FolderModel(folder, folderPath));
                }
            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (FolderModel folder : al_images) {
            folder.setVideoPath(QueryInFolderImages(folder.getFolderPath()));
        }

        return al_images;
    }

    private ArrayList<String> QueryInFolderImages(String folderPath) {
        ArrayList<String> subFolderPaths = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DATE_MODIFIED};
        Cursor cursor = context.getContentResolver().query(
                uri
                , projection
                , MediaStore.Images.Media.DATA + " like ? "
                , new String[]{"%" + folderPath + "%"}
                , null);
        try {
            cursor.moveToFirst();
            do {
                subFolderPaths.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

            } while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subFolderPaths;
    }


    public ArrayList<FolderModel> getVideoFolders() {
        al_images.clear();
        ArrayList<String> temp_folder_list = new ArrayList<>();
        //int int_position = 0;
        Uri uri;
        Cursor cursor;
//        int column_index_data, column_index_folder_name;
//
//        String absolutePathOfImage = null;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.BUCKET_ID};

        final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

//        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            do {
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String dataPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                String folderPath = dataPath.substring(0, dataPath.lastIndexOf(folder + "/"));
                folderPath = folderPath + folder + "/";
                if (!temp_folder_list.contains(folderPath)) {
                    temp_folder_list.add(folderPath);
                    al_images.add(new FolderModel(folder, folderPath));
                }
            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (FolderModel folder : al_images) {
            folder.setVideoPath(QueryInFolder(folder.getFolderPath()));
        }

//        for (int i = 0; i < al_images.size(); i++) {
//            Log.e("FOLDER", al_images.get(i).getFolderName());
//            for (int j = 0; j < al_images.get(i).getVideoPath().size(); j++) {
//                Log.e("FILE", al_images.get(i).getVideoPath().get(j));
//            }
//        }

        return al_images;
    }
    public ArrayList<FolderModel> getAudioFolders() {
        al_images.clear();
        ArrayList<String> temp_folder_list = new ArrayList<>();
        //int int_position = 0;
        Uri uri;
        Cursor cursor = null;
//        int column_index_data, column_index_folder_name;
//
//        String absolutePathOfImage = null;
        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            projection = new String[]{MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.BUCKET_DISPLAY_NAME, MediaStore.Audio.Media.BUCKET_ID};
        }else{
            projection = new String[]{MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns._ID};
        }

        String orderBy;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            orderBy = MediaStore.Audio.Media.DATE_TAKEN;
            cursor = context.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        }else{
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
        }


//        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            do {
                String folder;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                    String dataPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    String folderPath = dataPath.substring(0, dataPath.lastIndexOf(folder + "/"));
                    folderPath = folderPath + folder + "/";
                    if (!temp_folder_list.contains(folderPath)) {
                        temp_folder_list.add(folderPath);
                        al_images.add(new FolderModel(folder, folderPath));
                    }
                }else{
                    folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA));
                    String dataPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    String folderPath = dataPath.substring(0, dataPath.lastIndexOf( "/"));
                    folderPath = folderPath + "/";
                    folder=folder.substring(0,folder.lastIndexOf("/"));
                    folder=folder.substring(folder.lastIndexOf("/")+1);
                    if (!temp_folder_list.contains(folderPath)) {
                        temp_folder_list.add(folderPath);
                        al_images.add(new FolderModel(folder, folderPath));
                    }
                }

            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (FolderModel folder : al_images) {
            folder.setVideoPath(QueryInFolderforAudios(folder.getFolderPath()));
        }
        return al_images;
    }

    private ArrayList<String> QueryInFolderforAudios(String folderPath) {
        ArrayList<String> subFolderPaths = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DATE_MODIFIED};
        Cursor cursor = context.getContentResolver().query(
                uri
                , projection
                , MediaStore.Audio.Media.DATA + " like ? "
                , new String[]{"%" + folderPath + "%"}
                , null);
        try {
            cursor.moveToFirst();
            do {
                subFolderPaths.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

            } while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subFolderPaths;
    }

    private ArrayList<String> QueryInFolder(String folderPath) {
    ArrayList<String> subFolderPaths = new ArrayList<>();
    Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DATE_MODIFIED};
    Cursor cursor = context.getContentResolver().query(
            uri
            , projection
            , MediaStore.Video.Media.DATA + " like ? "
            , new String[]{"%" + folderPath + "%"}
            , null);
    try {
        cursor.moveToFirst();
        do {
            subFolderPaths.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

        } while (cursor.moveToNext());
    } catch (Exception e) {
        e.printStackTrace();
    }
    return subFolderPaths;
}

}
