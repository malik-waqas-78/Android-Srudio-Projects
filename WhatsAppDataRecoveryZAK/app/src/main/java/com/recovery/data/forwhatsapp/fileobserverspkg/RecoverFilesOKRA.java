package com.recovery.data.forwhatsapp.fileobserverspkg;

import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.recovery.data.forwhatsapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.recovery.data.forwhatsapp.AppOKRA.FILE_SAVED_CHANNEL_ID;


public class RecoverFilesOKRA {
    Context context;
    public RecoverFilesOKRA(Context context) {
        this.context=context;
    }
    boolean saved=false;
    String TAG="92727";
    public boolean storeFileToRecoveryFolder(String url) {
        String saved_path= Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Recovered Files/";
        try {
            storeFiles(url,saved_path,new File(url).getName());
        } catch (IOException e) {
            Log.d(TAG, "storeFileToRecoveryFolder: exception recovery");
            e.printStackTrace();
        }
        return saved;
    }
    private void storeFiles(String src_FilePath,String des_FilePath,String filesname) throws IOException {
        boolean success=false;
        File fileIn = new File(src_FilePath);
        File file=new File(des_FilePath);
        if(!file.exists()){
            file.mkdirs();
        }
        File fileOut = new File(des_FilePath, filesname);
        if (fileOut.exists()) {
            Log.d(TAG, "storeFilesToInternalStorage: file already exists");
        } else {
            fileOut.createNewFile();
            FileChannel source = null;
            FileChannel destination = null;
            FileInputStream in;
            FileOutputStream out;
            try {
                in = new FileInputStream(fileIn);
                out = new FileOutputStream(fileOut);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    FileUtils.copy(in, out);

                } else {
                    source = in.getChannel();
                    destination = out.getChannel();
                    destination.transferFrom(source, 0, source.size());
                }
                Log.d(TAG, "storeFilesToInternalStorage: file saved :: " + fileOut.getName());
                success=true;
                saved=true;
                MediaScannerConnection.scanFile(context, new String[]{des_FilePath},new String[]{"video/*","image/*"},null);
            } catch (FileNotFoundException e) {
                Log.d(TAG, "storeFilesToInternalStorage: exception 1"+e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.d(TAG, "storeFilesToInternalStorage: exception 2"+e.getMessage());
                e.printStackTrace();
            } finally {
                if (source != null) {
                    source.close();
                } else {
                    Log.d(TAG, "storeFilesToInternalStorage: no source");
                }
                if (destination != null) {
                    destination.close();
                } else {
                    Log.d(TAG, "storeFilesToInternalStorage: no destination");
                }
            }
            if(success){
                fileIn.delete();
                System.gc();
                showNotification();
            }


        }
    }
    private void showNotification() {
        // NotificationManagerCompat notificationManagerCompat;
        //notificationManagerCompat = NotificationManagerCompat.from(this);
// notificationId is a unique int for each notification that you must define
        int notificationId = 92778;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            StatusBarNotification[] notifications = notificationManager.getActiveNotifications();
            for (StatusBarNotification notification : notifications) {
                if (notification.getId() == notificationId) {
                    return;
                }
            }
        }
        notificationManager.notify(notificationId, notificationBuilder().build());
    }

    private NotificationCompat.Builder notificationBuilder() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, FILE_SAVED_CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(context.getString(R.string.filesaved))
                .setContentText(context.getString(R.string.file_deleted_text))
                .setPriority(NotificationCompat.PRIORITY_LOW);
        // Set the intent that will fire when the user taps the notification
        //.setContentIntent(pendingIntent)
        return builder;
    }

}
