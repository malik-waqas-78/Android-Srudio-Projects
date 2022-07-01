package com.example.whatsappdatarecovery.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.whatsappdatarecovery.R;
import com.github.chrisbanes.photoview.PhotoView;
import androidx.appcompat.app.AppCompatActivity;

public class FullImageActivity extends AppCompatActivity {
    PhotoView fullImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_main_image);
        fullImageView = findViewById(R.id.full_image);
        Intent i=getIntent();
        String path =i.getStringExtra("image");
        Uri uri = Uri.parse(path);
        fullImageView.setImageURI(uri);

    }

}

