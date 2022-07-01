package com.niazitvpro.official.service;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.niazitvpro.official.R;
import com.niazitvpro.official.activity.MainActivity;
import com.niazitvpro.official.crypto.PlaylistDownloader;
import com.niazitvpro.official.download.DownloadInfo;
import com.niazitvpro.official.utils.SharedPrefTVApp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public  class Download_Manager implements PlaylistDownloader.DownloadListener {

    private static final String TAG = "Download_Manager";
    private static BroadcastReceiver onDownloadComplete;
    public static NotificationCompat.Builder notificationBuilder;
    private static NotificationManager mNotificationManager;
    private  static String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private static ProgressDialog progressDialog;
    private static String NOTIFICATION_CHANNEL_ID;
    static int counter = 0;
    private static boolean run = false;
    private static Activity context;
    private String file_Name;
    private static Download_Manager single_instance = null;
    private static int NOTIFICATION_ID=0;
    private static List<DownloadInfo> downloadInfoList = new ArrayList<>();
    private static boolean isDownloadInProgress = false;
    private static boolean isCounter99 = false;
    private SharedPrefTVApp prefTVApp;

    public static Download_Manager getInstance()
    {
        if (single_instance == null)
            single_instance = new Download_Manager();

        return single_instance;
    }

/*
    public static void download(String url,Activity activity) {


        String downloadPath = Environment.getExternalStorageDirectory() + "/" + "DOWNLOAD_AIO_VIDEO/";
        File dir = new File(downloadPath);
        if (!dir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdir();
        }


        String cmd = String.format("-i %s -acodec %s -bsf:a aac_adtstoasc -vcodec %s %s", url, "copy", "copy", dir.toString() + "/"+getFileNameFromURL(url)+"_"+new Random().nextInt() + ".mp4");
        String[] command = cmd.split(" ");
        run = false;
        execFFmpegBinary(activity,command);

    }
*/

    public void startDownload(Activity activity, String liveUrl,String fileName){
        try {
            file_Name =  fileName;
            PlaylistDownloader downloader =
                    new PlaylistDownloader(liveUrl, this);
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/NiaziTv Pro/");
            if(!file.exists()){
                file.mkdir();
            }
            File downloadPath =  new File(file.getAbsolutePath(),fileName);
            downloader.download(downloadPath.getAbsolutePath());
        } catch (java.io.IOException e) {
            Toast.makeText(activity, "Something went wrong!!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

/*
    public static String getFileNameFromURL(String url) {
        if (url == null) {
            return "";
        }
        try {
            URL resource = new URL(url);
            String host = resource.getHost();
            if (host.length() > 0 && url.endsWith(host)) {
                // handle ...example.com
                return "";
            }
        }
        catch(MalformedURLException e) {
            return "";
        }

        int startIndex = url.lastIndexOf('/') + 1;
        int length = url.length();

        // find end index for ?
        int lastQMPos = url.lastIndexOf('?');
        if (lastQMPos == -1) {
            lastQMPos = length;
        }

        // find end index for #
        int lastHashPos = url.lastIndexOf('#');
        if (lastHashPos == -1) {
            lastHashPos = length;
        }

        // calculate the end index
        int endIndex = Math.min(lastQMPos, lastHashPos);
        String fileName = url.substring(startIndex, endIndex);
        fileName = fileName + "_" +new Random().nextInt() + ".mp4";
        return fileName;
    }
*/


/*
    private static void execFFmpegBinary(Activity activity,String[] command) {

        try {

            FFmpeg ffmpeg = FFmpeg.getInstance(activity);

            Thread t = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!run) {
                            Thread.sleep(30000);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (notificationBuilder != null && mNotificationManager != null) {


                                        // Removes the progress bar

                                        notificationBuilder
                                                .setProgress(100, counter++, false);
                                        mNotificationManager.notify(1, notificationBuilder.build());

                                    }
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };

            t.start();

            ffmpeg.execute(command, new FFmpegExecuteResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    Log.i(TAG, "onSuccess: " + message);
//                    progressDialog.dismiss();
                }

                @Override
                public void onProgress(String message) {
                    Log.i(TAG, "onProgress: " + message);

//                    progressDialog.setMessage("Progressing: \n " + message);
                    if (notificationBuilder != null && mNotificationManager != null) {

                        if(message.contains("video:")){

                           run = true;
                           t.stop();
                            counter=0;
                            notificationBuilder.setContentTitle("Download Complete");
                            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText("")).setProgress(0,0,false);
                            Notification notification = notificationBuilder.getNotification();
                            notification.flags = Notification.FLAG_AUTO_CANCEL;
                            mNotificationManager.notify(1, notification);
                        }else {
                            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                                    .setProgress(100, counter, false);
                            mNotificationManager.notify(1, notificationBuilder.build());
                        }

                    }
                }

                @Override
                public void onFailure(String message) {
                    Log.i(TAG, "onFailure: " + message);
                }

                @Override
                public void onStart() {
                    Log.i(TAG, "onStart: ");
                    createNotification(activity,"Downloading...","");
//                    progressDialog.show();
                }

                @Override
                public void onFinish() {
                    if (notificationBuilder != null && mNotificationManager != null) {


                        // Removes the progress bar



                    }
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }
*/


    public static void createNotification(Activity activity, String title, String content) {
         NOTIFICATION_CHANNEL_ID = "Niazi_tv_channel";

        mNotificationManager =
                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Your Notifications",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);

            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        // to diaplay notification in DND Mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = mNotificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);
            channel.canBypassDnd();
        }

        notificationBuilder = new NotificationCompat.Builder(activity, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setColor(ContextCompat.getColor(activity, R.color.colorAccent))
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle(title)
                .setContentText(content)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setChannelId(NOTIFICATION_CHANNEL_ID);

        mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    public void setDownloadPopupDialog(Activity activity, String liveUrl,String fileName,int notiId){

        prefTVApp = new SharedPrefTVApp(activity);
        androidx.appcompat.app.AlertDialog.Builder builder  = new androidx.appcompat.app.AlertDialog.Builder(activity);

        builder.setMessage("Are you sure you want to download this video??")
                .setCancelable(false);
        builder.setPositiveButton("Download now", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
                context = activity;
                downloadInfoList.add(new DownloadInfo(liveUrl,notiId,fileName));
                if(!isDownloadInProgress){
                    if(downloadInfoList.size()!=0){
                        DownloadInfo downloadInfo = downloadInfoList.get(0);
                        NOTIFICATION_ID = downloadInfo.id;
                        startDownload(activity,downloadInfo.liveUrl,downloadInfo.fileName);
                        isDownloadInProgress =true;
                        downloadInfoList.remove(0);
                    }
                }

                //                download(liveUrl,activity);

            }
        });
        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //  Action for 'NO' Button
                dialog.cancel();
            }
        });

        androidx.appcompat.app.AlertDialog alert = builder.create();
        alert.setTitle("Download Video");
        alert.show();
        alert.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#000000"));
        alert.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#000000"));
    }

    public static String networkSpeed(Activity activity) {
        int downSpeed =0;
        int upSpeed =0;
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            NetworkCapabilities nc = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
                }
                downSpeed = nc.getLinkDownstreamBandwidthKbps();
                upSpeed = nc.getLinkUpstreamBandwidthKbps();
            }
        }

        return upSpeed + " kb/s";

    }

    @Override
    public void onProgressUpdate(String progress,int progressCount) {
        counter = progressCount;
        if (notificationBuilder != null && mNotificationManager != null) {

            if(counter==99){

                if(!isCounter99){

                    isCounter99 =true;

                    isDownloadInProgress = false;

                    notificationBuilder.setContentTitle("Download Complete");

                    notificationBuilder.setOnlyAlertOnce(false);
                    notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(file_Name)).setProgress(0,0,false);
                    Notification notification = notificationBuilder.getNotification();
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    mNotificationManager.notify(NOTIFICATION_ID, notification);

                    if(downloadInfoList.size()!=0){
                        DownloadInfo downloadInfo = downloadInfoList.get(0);
                        NOTIFICATION_ID = downloadInfo.id;
                        startDownload(context,downloadInfo.liveUrl,downloadInfo.fileName);
                        isDownloadInProgress =true;
                        downloadInfoList.remove(0);
                    }

                }

            }else {
                if(downloadInfoList.size()!=0){
                    notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(progress + "  FileName:= " + file_Name +  "  Pending Download:=" + downloadInfoList.size()));
                }else {
                    notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(progress + "  FileName:= " + file_Name));
                }
                notificationBuilder.setProgress(100, counter, false);
                notificationBuilder.setOnlyAlertOnce(true);
                mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
                isCounter99 = false;
            }

        }
    }

    @Override
    public void onStartDownload(String url) {
        Toast.makeText(context, "startDownload...", Toast.LENGTH_SHORT).show();
        createNotification(context,"Downloading...","");
    }
}

