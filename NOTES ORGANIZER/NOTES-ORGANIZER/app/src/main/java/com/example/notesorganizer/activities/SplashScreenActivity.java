package com.example.notesorganizer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notesorganizer.R;

import java.util.ArrayList;
public class SplashScreenActivity extends AppCompatActivity {
    ImageView btn_splash;
    TextView privacy_policy;
    String[] apppermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int PERMISSIONS_REQUEST_CODE = 0x11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        privacy_policy=findViewById(R.id.privacy_policy_textview);
        btn_splash=findViewById(R.id.splash_btn);
        btn_splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckAndRequestPermission())
                {
                    Intent i=new Intent(getApplicationContext(), ShowFirstTimeActivity.class);
                    startActivity(i);
                }
            }
        });
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                privacy_Dialog();
            }
        });
    }
    public boolean CheckAndRequestPermission() {
        ArrayList<String> ListPermissionNeeded = new ArrayList<>();
        for (String perm : apppermission) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                ListPermissionNeeded.add(perm);
                Intent i=new Intent(SplashScreenActivity.this,ShowFirstTimeActivity.class);
                startActivity(i);
            }
        }
        if (!ListPermissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, ListPermissionNeeded.toArray(new String[ListPermissionNeeded.size()]), PERMISSIONS_REQUEST_CODE);
            return false;
        }
        return true;
    }
    private void privacy_Dialog() {
        final Dialog dialog = new Dialog(SplashScreenActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_privacy);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        final TextView ok = dialog.findViewById(R.id.btn_okay);
        ok.setOnClickListener(v -> dialog.dismiss());
    }
}