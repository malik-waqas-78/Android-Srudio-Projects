package com.recovery.data.forwhatsapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;


import com.recovery.data.forwhatsapp.chatspkg.ChatsManageRealmOKRA;
import com.recovery.data.forwhatsapp.fileobserverspkg.ManageObserver;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.recovery.data.forwhatsapp.AppOKRA.CHANNEL_ID;


@SuppressLint("OverrideAbstract")
public class MyNotificationListenerOKRA extends NotificationListenerService {
    private static final String TAG = "92727";
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    private ManageObserver observer = null;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public NotificationManager notificationManager;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "Notification service onStartCommandCalled");
        if (intent != null && intent.getAction() != null && intent.getAction().equals("rebind.request")) {
            //  Log.d(TAG, "TRYING REBIND SERVICE at "+HotUtils.formatDate(new Date()));
            tryReconnectService();//switch on/off component and rebind
        }
        //START_STICKY  to order the system to restart your service as soon as possible when it was killed.
        return START_STICKY;
       /* Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.notification_listener_service))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.app_icon)
                .build();
        startForeground(1, notification);
        return START_STICKY;*/

    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        // Log.d(TAG, "onNotificationPosted: \npackage name ::"+sbn.getPackageName());
        if (sbn.getPackageName().equals("com.whatsapp")) {
            //Log.d(TAG, "onNotificationPosted: \npackage name ::"+sbn.getNotification().extras.getString("android.text").length());
            String msgtext = sbn.getNotification().extras.getString("android.text");
            String titletext = sbn.getNotification().extras.getString("android.title");
            Log.d(TAG, "onNotificationPosted: text : " + msgtext);
            Log.d(TAG, "onNotificationPosted: title : " + titletext);
            if (msgtext != null && !msgtext.contains("new messages")) {
                /*&& !titletext.equals("WhatsApp") &&
                    !titletext.equals("Some messages not sent") && !titletext.equals("Missed voice call") && titletext.equals("Deleting messages") &&
                    !titletext.equals("Missed video call") && !titletext.equals("Checking for new messages") && !msgtext.contains("messages from") &&
                    !msgtext.contains("new messages") && !msgtext.contains("new messages") && !msgtext.contains("This message was deleted") &&
                    !msgtext.contains("This message was deleted") && !msgtext.contains("Ringing") && !msgtext.contains("Ringing") &&
                    !msgtext.contains("Sending video") && !msgtext.contains("Calling") && !msgtext.contains("Sending audio") && !msgtext.contains("Sending documents") &&
                    !msgtext.contains("Sending images") && !msgtext.contains("Sending messages") && !msgtext.contains("WhatsApp Web is curreny active") &&
                    !msgtext.contains("WhatsApp Web login") && !msgtext.contains("Ongoing voice call") && !msgtext.contains("Ongoing video call") &&
                    !msgtext.contains("Incoming video call") && !msgtext.contains("Incoming voice call") && !msgtext.contains("Sending file to ") &&
                    !msgtext.contains("Checking for new messages "*/
                Log.d(TAG, "onNotificationPosted: " + sbn.getNotification().extras.getString("android.title"));
                Bitmap bitmap = null;
                if (android.os.Build.VERSION.SDK_INT >= 29) {
                    Icon icon = (Icon) sbn.getNotification().extras.get("android.largeIcon");
                    if (icon != null) {
                        Drawable d = icon.loadDrawable(getApplicationContext());
                        bitmap = ((BitmapDrawable) d).getBitmap();
                    }
                } else {
                    bitmap = (Bitmap) sbn.getNotification().extras.get("android.largeIcon");
                }
                if (bitmap != null) {
                    ChatsManageRealmOKRA manageRealm_class = new ChatsManageRealmOKRA();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh:mm a");
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("d/mm/yy");
//
                    manageRealm_class.setDate(dateFormat.format(Calendar.getInstance().getTime()));
//
                    manageRealm_class.setSender(titletext);
                    manageRealm_class.setTimeinmili(System.currentTimeMillis());
                    manageRealm_class.setText(msgtext);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    manageRealm_class.setBytearray(byteArray);
                    manageRealm_class.manageData();
                }

            }
        }
    }


    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.notification_listener_service))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.app_icon)
                .setOnlyAlertOnce(true)
                .build();
        startForeground(92727, notification);
        Log.d(TAG, "onListenerConnected: ");
        //Toast.makeText(this, "service linked", Toast.LENGTH_SHORT).show();
        //checkObserverManager();
        //showNotification();
        startObserving();
        //manageFileObserverClass.startObserving();
    }

    private void startObserving() {

        observer = ManageObserver.Companion.getInstance();
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.WhatsApp Data Recover";
        ArrayList<String> rootPathToWatch = new ArrayList<>();
        rootPathToWatch.add(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media");
        rootPathToWatch.add(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp/WhatsApp/Media");
        observer.setPath(rootPath + "/backUp Bin", rootPath + "/recycle Bin", rootPathToWatch);
        if (observer.getDirectoriesBeingObserved().isEmpty()) {
            observer.observe();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        requestRebind(new ComponentName(getApplicationContext().getPackageName(), getApplicationContext().getClass().getSimpleName()));
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        //  showNotification();
        //manageFileObserverClass=new ManageFileObserverClass(context);
        //showNotification();
        return super.onBind(intent);
    }


    public void tryReconnectService() {
        toggleNotificationListenerService();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ComponentName componentName =
                    new ComponentName(getApplicationContext(), MyNotificationListenerOKRA.class);

            //It say to Notification Manager RE-BIND your service to listen notifications again inmediatelly!
            requestRebind(componentName);
        }
    }

    /**
     * Try deactivate/activate your component service
     */
    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this, MyNotificationListenerOKRA.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(this, MyNotificationListenerOKRA.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    private void showNotification() {
        // NotificationManagerCompat notificationManagerCompat;
        //notificationManagerCompat = NotificationManagerCompat.from(this);
// notificationId is a unique int for each notification that you must define
        int notificationId = 92727;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.notification_listener_service))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.app_icon)
                .setOnlyAlertOnce(true);
        return builder;

    }

    private boolean checkPermissions() {
        List<String> permissionsNotGranted = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNotGranted.add(permission);
            }
        }
        if (!permissionsNotGranted.isEmpty()) {
            return false;
        }
        try {
            SharedPreferences mySharedPreferences = this.getSharedPreferences("MyPermissions", 0);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putBoolean("ASKAGAIN", true);
            editor.apply();
        } catch (Exception e) {
            //again
        }
        return true;

    }

    private boolean isNotificationServiceEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
