package com.example.deviceinfo.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Dialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;


import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.deviceinfo.R;
import com.example.deviceinfo.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashBinding.inflate(getLayoutInflater());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(binding.getRoot());
        initComponents();

        binding.ivStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity();
            }
        });
        binding.tvPrivacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privacy_Dialog();
            }
        });

    }


    private void startNextActivity() {


        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void privacy_Dialog() {
        final Dialog dialog = new Dialog(SplashActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_privacy_capp);
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
        new android.os.Handler().postDelayed(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     binding.pbarLoadAdds.setVisibility(View.INVISIBLE);
                                                     binding.ivStart.setVisibility(View.VISIBLE);
                                                 }
                                             }
                ,3000);
    }

}