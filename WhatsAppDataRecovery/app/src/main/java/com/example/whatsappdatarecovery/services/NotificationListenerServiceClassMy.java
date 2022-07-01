package com.example.whatsappdatarecovery.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Parcelable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.example.whatsappdatarecovery.database.MyHelperClass;
import com.example.whatsappdatarecovery.database.MyRealmHelper;
import com.example.whatsappdatarecovery.modelclass.ModelClass;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.RequiresApi;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static android.os.Environment.getExternalStorageDirectory;

public class NotificationListenerServiceClassMy extends NotificationListenerService {
    MyHelperClass myHelperClass;
    Realm realm;
    FileObserver imagefileobserver, videofileobserver, audiofileobserver, documentsfileobserver, stickersfileobserver, animated_gifs_fileobserver, whatsapp_status_fileObserver, Voice_Notes_fileObserver;
    String imagePath, videoPath, documentPath, audioPath, gifPath, stickerPath, voicenotes, whatsapp_status;
    File _SourceFile;
    static String text,title,formatedate;
    String output_image_path;
    String TAG = "927277";
    ArrayList<File> allVoiceFiles = new ArrayList<>();
    MyRealmHelper helper;
    /*
        private static final int SCHEMA_V_PREV = 1;// previous schema version
    */
    private static final int SCHEMA_V_NOW = 2;// change schema version if any change happened in schema

    public static final class ApplicationPackageNames {
        static final String WHATSAPP_PACK_NAME = "com.whatsapp";
        /*static final String APPLICATION_PACK_NAME = "com.recovery.data.whatsapprecovery.whatsdelete.whatsappdata.services";*/
    }

    public static final class InterceptedNotificationCode {
        static final int WHATSAPP_CODE = 1;
        /*static final int APPLICATION_CODE = 2;*/
        static final int OTHER_NOTIFICATIONS_CODE = 3; // We ignore all notification with code == 3
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        myHelperClass = new MyHelperClass(realm);
        final String statusPath;
        if (checkEXTERNALStorage()) {
            statusPath = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsApp/Media/.Statuses";
        } else {
            statusPath = getFilesDir() + "/WhatsApp/Media/.Statuses";
        }
        whatsapp_status_fileObserver = new FileObserver(statusPath) {
            @Override
            public void onEvent(final int event, final String path) {
                String copied_image = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Status/Images";
                String copied_video = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Status/Videos";
                allVoiceFiles.clear();
                load_Directory_files(new File(statusPath));
                ArrayList<File> allmainVoicenote = new ArrayList<>(allVoiceFiles);
                allVoiceFiles.clear();
                load_Directory_files(new File(copied_image));
                load_Directory_files(new File(copied_video));
                ArrayList<File> allcopiedVoicenote = new ArrayList<>(allVoiceFiles);
                for (File file : allmainVoicenote) {
                    boolean found = false;
                    for (File targ : allcopiedVoicenote) {
                        if (file.getName().equals(targ.getName())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        if (file.getName().contains(".jpg") || file.getName().contains(".jpeg") || file.getName().contains(".png")) {
                            copy_voice_notes(file.getAbsolutePath(), copied_image, file.getName());
                        } else {
                            copy_voice_notes(file.getAbsolutePath(), copied_video, file.getName());
                        }
                    }
                }
            }
        };
        whatsapp_status_fileObserver.startWatching();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.d("abcdef", "onNotificationPosted: " + sbn.getPackageName());
        int notificationCode = matchNotificationCode(sbn);
        Bundle extras = sbn.getNotification().extras;
        final String pack = sbn.getPackageName();
        title = sbn.getNotification().extras.getString("android.title");
        text = sbn.getNotification().extras.getString("android.text");
        Log.d(TAG, "onNotificationPosted111: "+title);
        int iconId = extras.getInt(Notification.EXTRA_SMALL_ICON);
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy/MM/dd");
         formatedate = simpleDateFormat.format(calendar.getTime());
        Log.d(TAG, "title_text: " + title);
        Log.d(TAG, "title_text: " + text);
        Log.d(TAG, "title_text: " + formatedate);
        Log.d(TAG, "title_text: " + iconId);
        try {
            text = Objects.requireNonNull(extras.getCharSequence("android.text")).toString();
        } catch (NullPointerException e) {
            Log.d(TAG, "onNotificationPosted: " + e.getMessage());
        }
        if (title == null) {
            return;
        }
        FileOutputStream fos;
        try {
            fos = openFileOutput("WhatsApp Data Recovery.txt", Context.MODE_PRIVATE);
            fos.write((title + "  " + text).getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String subtext = "";
        if (notificationCode != NotificationListenerServiceClassMy.InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE) {
            Parcelable[] b = new Parcelable[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                b = (Parcelable[]) extras.get(Notification.EXTRA_MESSAGES);
            }
            if (b != null) {
                for (Parcelable tmp : b) {
                    Bundle msgBundle = (Bundle) tmp;
                    subtext = msgBundle.getString("text");
                }
                assert subtext != null;
            }
            if (title.contains("title") || title.contains("WhatsApp") || title.contains("Your data isn't backed up") || title.contains("Some messages not sent") || title.contains("Missed voice call") || title.contains("Deleting messages") || title.contains("Missed video call") || title.contains("Checking for new messages")) {
                if (text.contains("messages from")
                        || text.contains("new messages") || subtext.contains("new messages")
                        || text.contains("This message was deleted") || subtext.contains("This message was deleted")
                        || text.contains("Ringing") || subtext.contains("Ringing")
                        || text.contains("Add a backup account now") || subtext.contains("Add a backup account now")
                        || text.contains("Sending video") || subtext.contains("Sending video")
                        || text.contains("Calling") || subtext.contains("Calling")
                        || text.contains("Sending audio") || subtext.contains("Sending audio")
                        || text.contains("Sending documents") || subtext.contains("Sending documents")
                        || text.contains("Sending images") || subtext.contains("Sending images")
                        || text.contains("Sending messages") || subtext.contains("Sending messages")
                        || text.contains("WhatsApp Web is currently active") || subtext.contains("WhatsApp Web is currently active")
                        || text.contains("WhatsApp Web login") || subtext.contains("WhatsApp Web login")
                        || text.contains("Ongoing voice call") || subtext.contains("Ongoing voice call")
                        || text.contains("Ongoing video call") || subtext.contains("Ongoing video call")
                        || text.contains("Incoming video call") || subtext.contains("Incoming video call")
                        || text.contains("Incoming voice call") || subtext.contains("Incoming voice call")
                        || text.contains("Sending file to ") || subtext.contains("Sending file to ")
                        || text.contains("Checking for new messages ") || subtext.contains("Checking for new messages ")) {
                    return;
                }
            }
            Intent intent = new Intent("com.example.whatsappdatarecovery.services");
            intent.putExtra("Notification Code", notificationCode);
            intent.putExtra("package", pack);
            intent.putExtra("title", title);
            intent.putExtra("text", subtext);
            intent.putExtra("id", sbn.getId());
            sendBroadcast(intent);
            /*SEND DATA TO MODELCLASS */
            /*ModelClass md = new ModelClass();
            md.setName(title);
            md.setDetails(text);
            md.setDate(formatedate);
            helper.savedata(md);*/
            save_data();
            if (Build.VERSION.SDK_INT < 29) {
                voicenotes = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsApp/Media/WhatsApp Voice Notes";
                stickerPath = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsApp/Media/WhatsApp Stickers";
                gifPath = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsApp/Media/WhatsApp Animated Gifs";
                whatsapp_status = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsApp/Media/WhatsApp Animated Gifs";
                imagePath = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsApp/Media/WhatsApp Images";
                videoPath = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsApp/Media/WhatsApp Video";
                audioPath = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsApp/Media/WhatsApp Audio";
                documentPath = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsApp/Media/WhatsApp Documents";
            } else {
                voicenotes = getApplicationContext().getExternalFilesDir(null) + "/WhatsApp/Media/WhatsApp Voice Notes/Private";
                stickerPath = getApplicationContext().getExternalFilesDir(null) + "/WhatsApp/Media/WhatsApp Stickers/Private";
                gifPath = getApplicationContext().getExternalFilesDir(null) + "/WhatsApp/Media/WhatsApp Animated Gifs/Private";
                whatsapp_status = getApplicationContext().getExternalFilesDir(null) + "/WhatsApp/Media/WhatsApp Animated Gifs/Private";
                imagePath = getApplicationContext().getExternalFilesDir(null) + "/WhatsApp/Media/WhatsApp Images/Private";
                videoPath = getApplicationContext().getExternalFilesDir(null) + "/WhatsApp/Media/WhatsApp Video/Private";
                audioPath = getApplicationContext().getExternalFilesDir(null) + "/WhatsApp/Media/WhatsApp Audio/Private";
                documentPath = getApplicationContext().getExternalFilesDir(null) + "/WhatsApp/Media/WhatsApp Documents/Private";
            }
            imagefileobserver = new FileObserver(imagePath) {
                @Override
                public void onEvent(final int event, final String path) {
                    file_extension(path);
                    if (event == FileObserver.ACCESS) {
                        _SourceFile = new File(file_extension(path) + "/" + path);
                        Log.d("TAG", "images_find: " + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Images", _SourceFile.getName());
                    }
                    if (event == FileObserver.OPEN) {
                        Log.d("TAG", "images_find: " + path);
                        _SourceFile = new File(file_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Images", _SourceFile.getName());
                    }
                    if (event == FileObserver.ALL_EVENTS) {
                        Log.d("TAG", "images_find: " + path);
                        _SourceFile = new File(file_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Images", _SourceFile.getName());
                    }
                    if (event == FileObserver.ATTRIB) {
                        Log.d("TAG", "images_find: " + path);
                        _SourceFile = new File(file_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Images", _SourceFile.getName());
                    }
                    if (event == FileObserver.DELETE) {
                        Log.d("TAG", "images_find: " + path);
                        _SourceFile = new File(file_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Images", _SourceFile.getName());
                    }
                    if (event == FileObserver.MOVED_FROM) {
                        Log.d("TAG", "images_find: " + path);
                        _SourceFile = new File(file_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Images", _SourceFile.getName());
                    }
                    if (event == FileObserver.MOVE_SELF) {
                        Log.d("TAG", "images_find: " + path);
                        _SourceFile = new File(file_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Images", _SourceFile.getName());
                    }
                    if (event == FileObserver.MOVED_TO) {
                        Log.d("TAG", "images_find: " + path);
                        _SourceFile = new File(file_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Images", _SourceFile.getName());
                    }
                    if (event == FileObserver.CREATE) {
                        Log.d("TAG", "images_find: " + path);
                        _SourceFile = new File(file_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Images", _SourceFile.getName());
                    }
                }
            };
            videofileobserver = new FileObserver(videoPath) {
                @Override
                public void onEvent(final int event, final String path) {
                    video_extension(path);
                    if (event == FileObserver.ACCESS) {
                        _SourceFile = new File(video_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Videos", _SourceFile.getName());
                    }
                    if (event == FileObserver.OPEN) {
                        _SourceFile = new File(video_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Videos", _SourceFile.getName());
                    }
                    if (event == FileObserver.ALL_EVENTS) {
                        _SourceFile = new File(video_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Videos", _SourceFile.getName());
                    }
                    if (event == FileObserver.ATTRIB) {
                        _SourceFile = new File(video_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Videos", _SourceFile.getName());
                    }
                    if (event == FileObserver.DELETE) {
                        _SourceFile = new File(video_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Videos", _SourceFile.getName());
                    }
                    if (event == FileObserver.MOVED_FROM) {
                        _SourceFile = new File(video_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Videos", _SourceFile.getName());
                    }
                    if (event == FileObserver.MOVE_SELF) {
                        _SourceFile = new File(video_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Videos", _SourceFile.getName());
                    }
                    if (event == FileObserver.MOVED_TO) {
                        _SourceFile = new File(video_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Videos", _SourceFile.getName());
                    }
                    if (event == FileObserver.CREATE) {
                        _SourceFile = new File(video_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Videos", _SourceFile.getName());
                    }
                }
            };
            audiofileobserver = new FileObserver(audioPath) {
                @Override
                public void onEvent(final int event, final String path) {
                    audio_extension(path);
                    if (event == FileObserver.ACCESS) {
                        _SourceFile = new File(audio_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Audios", _SourceFile.getName());
                    }
                    if (event == FileObserver.OPEN) {
                        _SourceFile = new File(audio_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Audios", _SourceFile.getName());
                    }
                    if (event == FileObserver.ALL_EVENTS) {
                        _SourceFile = new File(audio_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Audios", _SourceFile.getName());
                    }
                    if (event == FileObserver.ATTRIB) {
                        _SourceFile = new File(audio_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Audios", _SourceFile.getName());
                    }
                    if (event == FileObserver.DELETE) {
                        _SourceFile = new File(audio_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Audios", _SourceFile.getName());
                    }
                    if (event == FileObserver.MOVED_FROM) {
                        _SourceFile = new File(audio_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Audios", _SourceFile.getName());
                    }
                    if (event == FileObserver.MOVE_SELF) {
                        _SourceFile = new File(audio_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Audios", _SourceFile.getName());
                    }
                    if (event == FileObserver.MOVED_TO) {
                        _SourceFile = new File(audio_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Audios", _SourceFile.getName());
                    }
                    _SourceFile = new File(audio_extension(path) + "/" + path);
                    copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Audios", _SourceFile.getName());
                }

            };
            documentsfileobserver = new FileObserver(documentPath) {
                @Override
                public void onEvent(final int event, final String path) {
                    docs_extension(path);
                    if (event == FileObserver.ACCESS) {
                        _SourceFile = new File(docs_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Documents", _SourceFile.getName());
                    }
                    if (event == FileObserver.OPEN) {
                        _SourceFile = new File(docs_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Documents", _SourceFile.getName());
                    }
                    if (event == FileObserver.ALL_EVENTS) {
                        _SourceFile = new File(docs_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Documents", _SourceFile.getName());
                    }
                    if (event == FileObserver.ATTRIB) {
                        _SourceFile = new File(docs_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Documents", _SourceFile.getName());
                    }
                    if (event == FileObserver.DELETE) {
                        _SourceFile = new File(docs_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Documents", _SourceFile.getName());
                    }
                    if (event == FileObserver.MOVED_FROM) {
                        _SourceFile = new File(docs_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Documents", _SourceFile.getName());
                    }
                    if (event == FileObserver.MOVE_SELF) {
                        _SourceFile = new File(docs_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Documents", _SourceFile.getName());
                    }
                    if (event == FileObserver.MOVED_TO) {
                        _SourceFile = new File(docs_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Documents", _SourceFile.getName());
                    }
                    if (event == FileObserver.CREATE) {
                        _SourceFile = new File(docs_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Documents", _SourceFile.getName());
                    }
                }
            };
            stickersfileobserver = new FileObserver(stickerPath) {
                @Override
                public void onEvent(final int event, final String path) {
                    stickers_extension(path);
                    if (event == FileObserver.ACCESS) {
                        _SourceFile = new File(stickers_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Stickers", _SourceFile.getName());
                    }
                    if (event == FileObserver.OPEN) {
                        _SourceFile = new File(stickers_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Stickers", _SourceFile.getName());
                    }
                    if (event == FileObserver.ALL_EVENTS) {
                        _SourceFile = new File(stickers_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Stickers", _SourceFile.getName());
                    }
                    if (event == FileObserver.ATTRIB) {
                        _SourceFile = new File(stickers_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Stickers", _SourceFile.getName());
                    }
                    if (event == FileObserver.DELETE) {
                        _SourceFile = new File(stickers_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Stickers", _SourceFile.getName());
                    }
                    if (event == FileObserver.MOVED_FROM) {
                        _SourceFile = new File(stickers_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Stickers", _SourceFile.getName());
                    }
                    if (event == FileObserver.MOVE_SELF) {
                        _SourceFile = new File(stickers_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Stickers", _SourceFile.getName());
                    }
                    if (event == FileObserver.MOVED_TO) {
                        _SourceFile = new File(stickers_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Stickers", _SourceFile.getName());
                    }
                    if (event == FileObserver.CREATE) {
                        _SourceFile = new File(stickers_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Stickers", _SourceFile.getName());
                    }
                }
            };
            animated_gifs_fileobserver = new FileObserver(gifPath) {
                @Override
                public void onEvent(final int event, final String path) {
                    animate_gifs_extension(path);
                    if (event == FileObserver.ACCESS) {
                        _SourceFile = new File(animate_gifs_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Gifs", _SourceFile.getName());
                    }
                    if (event == FileObserver.OPEN) {
                        _SourceFile = new File(animate_gifs_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Gifs", _SourceFile.getName());
                    }
                    if (event == FileObserver.ALL_EVENTS) {
                        _SourceFile = new File(animate_gifs_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Gifs", _SourceFile.getName());
                    }
                    if (event == FileObserver.ATTRIB) {
                        _SourceFile = new File(animate_gifs_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Gifs", _SourceFile.getName());
                    }
                    if (event == FileObserver.MOVED_FROM) {
                        _SourceFile = new File(animate_gifs_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Gifs", _SourceFile.getName());
                    }
                    if (event == FileObserver.MOVE_SELF) {
                        _SourceFile = new File(animate_gifs_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Gifs", _SourceFile.getName());
                    }
                    if (event == FileObserver.MOVED_TO) {
                        _SourceFile = new File(animate_gifs_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Gifs", _SourceFile.getName());
                    }
                    if (event == FileObserver.CREATE) {
                        _SourceFile = new File(animate_gifs_extension(path) + "/" + path);
                        copy_image_file(_SourceFile.getAbsolutePath(), getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Gifs", _SourceFile.getName());
                    }
                }
            };
            Voice_Notes_fileObserver = new FileObserver(voicenotes) {
                @Override
                public void onEvent(final int event, final String path) {
                    String copiedfilespath = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsAppRecovery/Voice Notes";
                    allVoiceFiles.clear();
                    load_Directory_files(new File(voicenotes));
                    ArrayList<File> allmainVoicenote = new ArrayList<>(allVoiceFiles);
                    allVoiceFiles.clear();
                    load_Directory_files(new File(copiedfilespath));
                    ArrayList<File> allcopiedVoicenote = new ArrayList<>(allVoiceFiles);
                    for (File file : allmainVoicenote) {
                        boolean found = false;
                        for (File targ : allcopiedVoicenote) {
                            if (file.getName().equals(targ.getName())) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            copy_voice_notes(file.getAbsolutePath(), copiedfilespath, file.getName());
                        }
                    }
                }
            };
            imagefileobserver.startWatching();
            audiofileobserver.startWatching();
            videofileobserver.startWatching();
            documentsfileobserver.startWatching();
            stickersfileobserver.startWatching();
            animated_gifs_fileobserver.startWatching();
            Voice_Notes_fileObserver.startWatching();

        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d("1234", "onNotificationRemoved: " + sbn.getPackageName());
        int notificationCode = matchNotificationCode(sbn);
        if (notificationCode != NotificationListenerServiceClassMy.InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE) {
            Intent intent = new Intent("com.github.chagall.notificationlistenerexample");
            intent.putExtra("Notification Code", notificationCode);
            sendBroadcast(intent);
        }
    }

    private int matchNotificationCode(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();
        /*case ApplicationPackageNames.APPLICATION_PACK_NAME:
                return (InterceptedNotificationCode.APPLICATION_CODE);*/
        if (ApplicationPackageNames.WHATSAPP_PACK_NAME.equals(packageName)) {
            return (InterceptedNotificationCode.WHATSAPP_CODE);
        }
        return (InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE);
    }


        private void copy_image_file(String inputPath, String outputPath, String f) {
            InputStream in;
            OutputStream out;
            try {
                File dir = new File(outputPath);
                File file = new File(dir.getAbsolutePath() + "/" + f);
                in = new FileInputStream(inputPath);
                out = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                // write the output file (You have now copied the file)
                out.flush();
                out.close();
                Log.d(TAG, "copy_image_file12345: "+out);
            } catch (Exception e) {
                Log.e("*E1*", Objects.requireNonNull(e.getMessage()));
                e.printStackTrace();
            }
        }
/*
    private void copy_image_file(String inputPath, String outputPath, String f) {
        OutputStream out = null;

        File dir = new File(outputPath);
        File file = new File(dir.getAbsolutePath() + "/" + f);
        dir.mkdir();
        File file1 = new File(file, f);
        try {
            out = new FileOutputStream(file1);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        // write the output file (You have now copied the file)
        try {
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    private void copy_voice_notes(String inputPath, String outputPath, String f) {
        InputStream in;
        OutputStream out;
        try {
            File dir = new File(outputPath);
            File[] files = dir.listFiles();
            assert files != null;
            File file = new File(dir.getAbsolutePath() + "/" + f);
            in = new FileInputStream(inputPath);
            out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                Log.e("**", "Read : " + read);
                out.write(buffer, 0, read);
            }
            in.close();
            // write the output file (You have now copied the file)
            out.flush();
            out.close();

        } catch (Exception e) {
            Log.e("**", Objects.requireNonNull(e.getMessage()));
            e.printStackTrace();
        }
    }

    public String file_extension(String path1) {
        if (path1 != null) {
            if (path1.endsWith(".jpg") || path1.endsWith(".png") || path1.endsWith(".jpeg")) {
                output_image_path = getExternalStorageDirectory().getAbsolutePath() + "/WhatsAppRecovery/Images/";
                return getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsApp/Media/WhatsApp Images";
            }
        }
        return null;
    }

    public String video_extension(String path1) {
        if (path1 != null) {
            if (path1.endsWith(".mp4") || path1.endsWith(".3gp") || path1.endsWith(".mkv")) {
                output_image_path = getExternalStorageDirectory().getAbsolutePath() + "/WhatsAppRecovery/Videos/";
                Log.d(TAG, "file_extensionvideos: " + output_image_path);
                return getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsApp/Media/WhatsApp Video";
            }
        }
        return null;
    }

    public String audio_extension(String path1) {
        if (path1 != null) {
            if (path1.endsWith(".mp3") || path1.endsWith(".m4a") || path1.endsWith(".wav") || path1.endsWith(".opus")) {
                output_image_path = getExternalStorageDirectory().getAbsolutePath() + "/WhatsAppRecovery/Audios/";
                Log.d(TAG, "file_extensionaudios: " + output_image_path);
                return getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsApp/Media/WhatsApp Audio";
            }
        }
        return null;
    }

    public String docs_extension(String path1) {
        if (path1 != null) {
            if (path1.endsWith(".zip") || path1.endsWith(".apk") || path1.endsWith(".pdf") || path1.endsWith(".docs")) {
                output_image_path = getExternalStorageDirectory().getAbsolutePath() + "/WhatsAppRecovery/Documents/";
                Log.d(TAG, "file_extensionaudios: " + output_image_path);
                return getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsApp/Media/WhatsApp Documents";
            }
        }
        return null;
    }

    public String stickers_extension(String path1) {
        if (path1 != null) {
            if (path1.endsWith(".webp")) {
                output_image_path = getExternalStorageDirectory().getAbsolutePath() + "/WhatsAppRecovery/Stickers/";
                Log.d(TAG, "file_extension_sticker: " + output_image_path);
                return getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsApp/Media/WhatsApp Stickers";
            }
        }
        return null;
    }

    public String animate_gifs_extension(String path1) {
        if (path1 != null) {
            if (path1.endsWith(".mp4")) {
                output_image_path = getExternalStorageDirectory().getAbsolutePath() + "/WhatsAppRecovery/Animated Gifs/";
                Log.d(TAG, "file_extensionaudios: " + output_image_path);
                return getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/WhatsApp/Media/WhatsApp Animated Gifs";
            }
        }
        return null;
    }

    public boolean checkEXTERNALStorage() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED) || state.equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

    public void load_Directory_files(File file) {
        File[] filelist = file.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (File value : filelist) {
                if (value.isDirectory()) {
                    load_Directory_files(value);
                } else {
                    allVoiceFiles.add(value);
                }
            }
        }
    }

    public static RealmConfiguration getDefaultConfig() {
        return new RealmConfiguration.Builder()
                .schemaVersion(SCHEMA_V_NOW)
                .deleteRealmIfMigrationNeeded()
                .build();
    }
    public void save_data() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number maxid = realm.where(ModelClass.class).max("id");
                int newKey = (maxid == null) ? 1 : maxid.intValue() + 1;
                ModelClass modelClass = realm.createObject(ModelClass.class, newKey);
                modelClass.setName(title);
                modelClass.setDetails(text);
                modelClass.setDate(formatedate);
                realm.copyToRealm(modelClass);
            }
        });
    }
}