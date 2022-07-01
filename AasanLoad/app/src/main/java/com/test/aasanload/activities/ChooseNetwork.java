package com.test.aasanload.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.test.aasanload.R;
import com.test.aasanload.constants.MyConstants;
import com.test.aasanload.dialogues.MyDialogues;

/* compiled from: ChooseNetwork.kt */
public final class ChooseNetwork extends AppCompatActivity {
    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_choose_network);


        ((Button) findViewById(R.id.btn_jazz)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCardScanner("jazz");
            }
        });
        ((Button) findViewById(R.id.btn_zong)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCardScanner("zong");
            }
        });
        ((Button) findViewById(R.id.btn_telenor)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCardScanner("telenor");
            }
        });
        ((Button) findViewById(R.id.btn_ufone)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCardScanner("ufone");
            }
        });

    }




    /* Access modifiers changed, original: protected */

    /* Access modifiers changed, original: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == MyConstants.Companion.getMY_REQUEST_CODE() && i2 != -1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Update flow failed! Result code: ");
            stringBuilder.append(i2);
            Log.e("MY_APP", stringBuilder.toString());
        }
    }

    private final void launchCardScanner(String str) {
        Intent intent = new Intent((Context) this, CameraActivity.class);
        intent.putExtra(MyConstants.Companion.getNETWORK_KEY(), str);
        startActivity(intent);
    }

    public void onBackPressed() {
        MyDialogues.Companion.showExitDialogue((Activity) this);
    }





}
