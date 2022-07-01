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

public class FullAnimatedActivity extends AppCompatActivity {
    VideoView videoView;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_main_animated_gifs);
        videoView = findViewById(R.id.animated_video_view);
        Intent i = getIntent();
        String path = i.getStringExtra("gifs");
        Uri uri = Uri.parse(path);
        mediaController = new MediaController(this);
        videoView.setVideoURI(uri);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
        videoView.start();
    }
}
