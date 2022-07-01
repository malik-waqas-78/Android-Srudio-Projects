package com.example.instalockerappsss.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.instalockerappsss.R;
import com.example.instalockerappsss.service.AccessabiltyServiceClass;

import java.util.Iterator;
import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {
    Button btn_splash;

    public String TAG = "SplashScreenActivity";
    TextView privacy_text;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_splash_screen);
        privacy_text = findViewById(R.id.privacy_text);
        privacy_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privacy_Dialog();
            }
        });
        btn_splash = findViewById(R.id.splash_next_btn);
        if (isAccessibilitySettingsOn(SplashScreenActivity.this)){
            editor = getSharedPreferences("Lock", 0).edit();
            editor.putBoolean("setLockPermission", true);
            editor.apply();
        }
        btn_splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAccessibilitySettingsOn(SplashScreenActivity.this)) {
                    startActivityForResult(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), 0);
                } else {
                    Intent i = new Intent(SplashScreenActivity.this, LockeCheckerActivity.class);
                    startActivity(i);
                }
            }
        });
        /*GET THE LIST OF ALL ACTIVITIES OF INSTALLED APPLICATIONS IN DEVICE*/
        List<PackageInfo> installedPackages = getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES);
        Iterator packageIterator = installedPackages.iterator();
        PackageInfo packageInfo = null;
        while (packageIterator.hasNext()) {
            packageInfo = (PackageInfo) packageIterator.next();
            if (packageInfo.activities != null) {
                for (ActivityInfo activity : packageInfo.activities) {
                    Log.d("ACTIVITY", activity.name);
                }
            }
        }
    }

    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = mContext.getPackageName()
                + "/" + AccessabiltyServiceClass.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext
                            .getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v("TAG", "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e("TAG",
                    "Error finding setting, default accessibility to not found: "
                            + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(
                ':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(mContext
                            .getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessabilityService = mStringColonSplitter.next();
                    if (accessabilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //start fingerprint authentication
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void privacy_Dialog() {
        final Dialog dialog = new Dialog(SplashScreenActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_privacy);
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
    }
}