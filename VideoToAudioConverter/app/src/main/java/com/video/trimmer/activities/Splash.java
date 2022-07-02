package com.video.trimmer.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.video.trimmer.R;
import com.video.trimmer.databinding.ActivitySplashBinding;
import com.video.trimmer.utils.LocaleManager_CA;
import com.video.trimmer.utils.SharedPrefClass;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

public class Splash extends AppCompatActivity {
    ActivitySplashBinding binding;


    ActivityResultLauncher<Intent> mGetContent3 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            if (SDK_INT < 23) {
                startNextActivity();
            } else if (Splash.this.checkSelfPermission(WRITE_EXTERNAL_STORAGE) ==PackageManager.PERMISSION_GRANTED) {
                startNextActivity();
            } else {
                Toast.makeText(Splash.this, "Permission not granted Yet", Toast.LENGTH_SHORT).show();
            }
        }
    });
    SharedPrefClass sharedPrefClass;
    boolean themeSwitchState;
    @Override
    protected void attachBaseContext(Context baseContext) {
        super.attachBaseContext(new LocaleManager_CA(baseContext).SetLocale());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        new LocaleManager_CA(this).SetLocale();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       initComponents();

        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity();
            }
        });
        binding.privacyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privacy_Dialog();
            }
        });
    }


    private void startNextActivity() {


        Intent intent=new Intent(Splash.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void privacy_Dialog() {
        final Dialog dialog = new Dialog(Splash.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.privacylayout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        final TextView ok = dialog.findViewById(R.id.btn_okay);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initComponents();
    }

    public void initComponents(){

        sharedPrefClass=new SharedPrefClass(Splash.this);
        themeSwitchState=sharedPrefClass.getThemeStatefromShared();
        if(themeSwitchState){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            new LocaleManager_CA(Splash.this).SetLocale();
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            new LocaleManager_CA(Splash.this).SetLocale();
        }
        new android.os.Handler().postDelayed(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     binding.progressBar.setVisibility(View.INVISIBLE);
                                                     binding.btnStart.setVisibility(View.VISIBLE);
                                                 }
                                             }
                ,3000);
    }
}