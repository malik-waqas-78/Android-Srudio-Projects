package com.example.whatsappdatarecovery.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.whatsappdatarecovery.R;

import androidx.appcompat.app.AppCompatActivity;

public class FullStickersActivity extends AppCompatActivity {
    ImageView fullImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_main_image);
        fullImageView = findViewById(R.id.full_image);
        Intent i = getIntent();
        Bitmap bitmap = (Bitmap) i.getParcelableExtra("Bitmap");
        fullImageView.setImageBitmap(bitmap);
    }
}