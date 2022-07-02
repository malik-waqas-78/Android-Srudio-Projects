package com.video.trimmer.activities;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;


import com.video.trimmer.R;
import com.video.trimmer.utils.Constants;
import com.video.trimmer.videoTrimmer.HgLVideoTrimmer;
import com.video.trimmer.videoTrimmer.interfaces.OnHgLVideoListener;
import com.video.trimmer.videoTrimmer.interfaces.OnTrimVideoListener;

import java.io.File;
import java.util.Random;

public class TrimmerActivity extends AppCompatActivity implements OnTrimVideoListener, OnHgLVideoListener {
    public static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";
    public static final String VIDEO_TOTAL_DURATION = "VIDEO_TOTAL_DURATION";
    public static  int type;
    public static String VIDEOPATH;

    Toolbar toolbar;
    TextView toolbartitle;
    private HgLVideoTrimmer mVideoTrimmer;
    //private ProgressDialog mProgressDialog;
    public static String TRIMMED_VIDEO_FOLDER_PATH;
    public static final String INTENT_DESTINATION_VIDEO_PATH = "trimmeddestiationvideopath";
    Constants constants;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trimmer);
        constants=new Constants(this);
        toolbar=findViewById(R.id.toolbar);
        toolbartitle=findViewById(R.id.toolbarTitle);

        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_icon));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbartitle.setText(VIDEOPATH.substring(VIDEOPATH.lastIndexOf("/")+1));
        toolbartitle.setSelected(true);
        Intent extraIntent = getIntent();
        int maxDuration = 10;

        if (extraIntent != null) {
            maxDuration = extraIntent.getIntExtra(VIDEO_TOTAL_DURATION, 10);

        }

        mVideoTrimmer = ((HgLVideoTrimmer) findViewById(R.id.timeLine));
        if (mVideoTrimmer != null) {


            /**
             * get total duration of video file
             */
            Log.e("tg", "maxDuration = " + maxDuration);
             //mVideoTrimmer.setMaxDuration(maxDuration);
            mVideoTrimmer.setMaxDuration(maxDuration);
            mVideoTrimmer.setOnTrimVideoListener(this);
            mVideoTrimmer.setOnHgLVideoListener(this);
            mVideoTrimmer.setDestinationPath(TRIMMED_VIDEO_FOLDER_PATH);
            mVideoTrimmer.setVideoURI(Uri.parse(VIDEOPATH));
            mVideoTrimmer.setVideoInformationVisibility(true);
        }
    }
    AlertDialog dialog2;
    AlertDialog.Builder alertDialog;
    View dialogView;
    ViewGroup viewGroup;
    ProgressBar pb;
    Button btnok;
    TextView title;
    TextView msg;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onTrimStarted(int check) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(check==2) {
                    alertDialog = new AlertDialog.Builder(TrimmerActivity.this);
                    viewGroup = findViewById(android.R.id.content);
                    dialogView = LayoutInflater.from(TrimmerActivity.this).inflate(R.layout.dialog_video_saving, viewGroup, false);
                    alertDialog.setView(dialogView);
                    dialog2 = alertDialog.create();
                    dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog2.setCancelable(false);
                    dialog2.show();

                    pb = dialogView.findViewById(R.id.progbar);
                    btnok = dialogView.findViewById(R.id.btndelyes);
                    title = dialogView.findViewById(R.id.dialogtitle);
                    msg = dialogView.findViewById(R.id.txtloadinginstr);

                    title.setText(getResources().getString(R.string.videosaving));
                    msg.setText(getResources().getString(R.string.msgsavingtrimvideo));
                }else if(check==1){
                    alertDialog = new AlertDialog.Builder(TrimmerActivity.this);
                    viewGroup = findViewById(android.R.id.content);
                    dialogView = LayoutInflater.from(TrimmerActivity.this).inflate(R.layout.dialog_video_saving, viewGroup, false);
                    alertDialog.setView(dialogView);
                    dialog2 = alertDialog.create();
                    dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog2.setCancelable(false);
                    dialog2.show();

                    pb = dialogView.findViewById(R.id.progbar);
                    btnok = dialogView.findViewById(R.id.btndelyes);
                    title = dialogView.findViewById(R.id.dialogtitle);
                    msg = dialogView.findViewById(R.id.txtloadinginstr);

                    title.setText(getResources().getString(R.string.audiosaving));
                    msg.setText(getResources().getString(R.string.msgsavingconvaudio));
                }else if(check==3){
                    alertDialog = new AlertDialog.Builder(TrimmerActivity.this);
                    viewGroup = findViewById(android.R.id.content);
                    dialogView = LayoutInflater.from(TrimmerActivity.this).inflate(R.layout.dialog_video_saving, viewGroup, false);
                    alertDialog.setView(dialogView);
                    dialog2 = alertDialog.create();
                    dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog2.setCancelable(false);
                    dialog2.show();

                    pb = dialogView.findViewById(R.id.progbar);
                    btnok = dialogView.findViewById(R.id.btndelyes);
                    title = dialogView.findViewById(R.id.dialogtitle);
                    msg = dialogView.findViewById(R.id.txtloadinginstr);

                    title.setText(getResources().getString(R.string.videosaving));
                    msg.setText(getResources().getString(R.string.removingAudio));
                }
            }
        });
    }

    @Override
    public void getResult(final Uri contentUri,int check) {
        dialog2.dismiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (check == 2) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(TrimmerActivity.this);
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(TrimmerActivity.this).inflate(R.layout.dialog_video_saving, viewGroup, false);
                    alertDialog.setView(dialogView);
                    AlertDialog dialog2 = alertDialog.create();
                    dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog2.show();

                    ProgressBar pb = dialogView.findViewById(R.id.progbar);
                    Button btnok = dialogView.findViewById(R.id.btnconfirmOk);
                    TextView title = dialogView.findViewById(R.id.dialogtitle);
                    TextView msg = dialogView.findViewById(R.id.txtloadinginstr);

                    title.setText(getResources().getString(R.string.videosaving));
                    msg.setText(getResources().getString(R.string.trimmedvideosaved));

                    pb.setVisibility(View.INVISIBLE);
                    btnok.setVisibility(View.VISIBLE);

                    btnok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                dialog2.dismiss();
                                setResult(1);
                                finish();

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }else if(check==1){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(TrimmerActivity.this);
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(TrimmerActivity.this).inflate(R.layout.dialog_video_saving, viewGroup, false);
                    alertDialog.setView(dialogView);
                    AlertDialog dialog2 = alertDialog.create();
                    dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog2.show();

                    ProgressBar pb = dialogView.findViewById(R.id.progbar);
                    Button btnok = dialogView.findViewById(R.id.btnconfirmOk);
                    TextView title = dialogView.findViewById(R.id.dialogtitle);
                    TextView msg = dialogView.findViewById(R.id.txtloadinginstr);
                    title.setText(getResources().getString(R.string.audiosaving));
                    msg.setText(getResources().getString(R.string.convertedaudiosaved));
                    pb.setVisibility(View.INVISIBLE);
                    btnok.setVisibility(View.VISIBLE);



                    btnok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                dialog2.dismiss();
                                setResult(1);
                                finish();

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }else if(check==3){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(TrimmerActivity.this);
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(TrimmerActivity.this).inflate(R.layout.dialog_video_saving, viewGroup, false);
                    alertDialog.setView(dialogView);
                    AlertDialog dialog2 = alertDialog.create();
                    dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog2.show();

                    ProgressBar pb = dialogView.findViewById(R.id.progbar);
                    Button btnok = dialogView.findViewById(R.id.btnconfirmOk);
                    TextView title = dialogView.findViewById(R.id.dialogtitle);
                    TextView msg = dialogView.findViewById(R.id.txtloadinginstr);

                    title.setText(getResources().getString(R.string.videosaving));
                    msg.setText(getResources().getString(R.string.removedAudio));

                    pb.setVisibility(View.INVISIBLE);
                    btnok.setVisibility(View.VISIBLE);

                    btnok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                dialog2.dismiss();
                                setResult(1);
                                finish();

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }else if(check==4){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(TrimmerActivity.this);
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(TrimmerActivity.this).inflate(R.layout.dialog_video_saving, viewGroup, false);
                    alertDialog.setView(dialogView);
                    AlertDialog dialog2 = alertDialog.create();
                    dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog2.show();

                    ProgressBar pb = dialogView.findViewById(R.id.progbar);
                    Button btnok = dialogView.findViewById(R.id.btnconfirmOk);
                    TextView title = dialogView.findViewById(R.id.dialogtitle);
                    TextView msg = dialogView.findViewById(R.id.txtloadinginstr);

                    title.setText(getResources().getString(R.string.videosaving));
                    msg.setText(getResources().getString(R.string.noAudio));

                    pb.setVisibility(View.INVISIBLE);
                    btnok.setVisibility(View.VISIBLE);

                    btnok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                dialog2.dismiss();
                                setResult(0);
                                finish();

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }
    public int getRandom(){
        int time=0;
        final int min = 1000;
        final int max = 10000;
        time = new Random().nextInt((max - min) + 1) + min;
        return time;
    }
    public Uri getFileUri(Context context, String fileName) {
        return FileProvider.getUriForFile(context,  "com.example.videotoaudioconverter.fileprovider", new File(fileName));
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(TrimmerActivity.this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


    private void playUriOnVLC(Uri uri) {

        int vlcRequestCode = 42;
        Intent vlcIntent = new Intent(Intent.ACTION_VIEW);
        vlcIntent.setPackage("org.videolan.vlc");
        vlcIntent.setDataAndTypeAndNormalize(uri, "video/*");
        vlcIntent.putExtra("title", "Kung Fury");
        vlcIntent.putExtra("from_start", false);
        vlcIntent.putExtra("position", 90000l);
        startActivityForResult(vlcIntent, vlcRequestCode);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("tg", "resultCode = " + resultCode + " data " + data);
    }

    @Override
    public void cancelAction() {
        //mProgressDialog.cancel();
        dialog2.cancel();
        mVideoTrimmer.destroy();
        finish();
    }

    @Override
    public void onError(final String message) {
       // mProgressDialog.cancel();
        dialog2.cancel();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               // Toast.makeText(TrimmerActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onVideoPrepared() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               // Toast.makeText(TrimmerActivity.this, "onVideoPrepared", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
