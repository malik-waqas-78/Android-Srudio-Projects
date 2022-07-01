package com.example.whatsappdatarecovery.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.whatsappdatarecovery.R;

import androidx.appcompat.app.AppCompatActivity;

public class FullVideoActivity extends AppCompatActivity {
    VideoView videoView;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_main_video);
        videoView = findViewById(R.id.video_view1);
        Intent i = getIntent();
        String path = i.getStringExtra("video");
        Uri uri = Uri.parse(path);
        mediaController = new MediaController(this);
        videoView.setVideoURI(uri);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
        videoView.start();
    }
}