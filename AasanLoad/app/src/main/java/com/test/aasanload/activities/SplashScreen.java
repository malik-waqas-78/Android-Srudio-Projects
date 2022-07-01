package com.test.aasanload.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.test.aasanload.R;
import com.test.aasanload.constants.MyConstants;
import com.test.aasanload.dialogues.MyDialogues;
import com.test.aasanload.permissions.MyPermissions;

import java.util.concurrent.ExecutionException;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: SplashScreen.kt */
public final class SplashScreen extends AppCompatActivity {




    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash_screen);
        checkPermissions();
        ((Button) findViewById(R.id.btn_start)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            openChooseNetworkActivity();
            }
        });
    }

    private final void checkPermissions() {

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED) {
           // openChooseNetworkActivity();
        } else {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.CAMERA},
                    2263
            );
        }
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        super.onPause();

    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        super.onDestroy();

    }

    /* Access modifiers changed, original: protected */
    public void onPostResume() {
        super.onPostResume();
    }

    public void onBackPressed() {
        MyDialogues.Companion.showExitDialogue((Activity) this);
    }

    /* Access modifiers changed, original: protected */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==2263){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
               // openChooseNetworkActivity();
            }
        }
    }

    public final void openChooseNetworkActivity() {
        Intent intent = new Intent((Context) this, ChooseNetwork.class);
        startActivity(intent);
        finish();
    }




}
