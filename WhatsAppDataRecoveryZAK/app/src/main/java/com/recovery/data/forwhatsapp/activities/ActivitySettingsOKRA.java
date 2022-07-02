package com.recovery.data.forwhatsapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

//import com.google.android.gms.ads.LoadAdError;
import com.recovery.data.forwhatsapp.R;

import java.util.ArrayList;
import java.util.List;

import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

public class ActivitySettingsOKRA extends AppCompatActivity {

    private static final String TAG = "927247";
    Switch sw_storage,sw_notification;
    private static final int PERMISSIONS_REQUSTCODE = 927;
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings_okra);
//        Toolbar toolbar=findViewById(R.id.toolbar);
//        toolbar.setTitle("Settings");
//        setSupportActionBar(toolbar);
//        if(AATAdsManager.isAppInstalledFromPlay(this)) {
////            loadAdmobBanner();
//        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sw_storage=findViewById(R.id.sw_storage);
        sw_notification=findViewById(R.id.sw_notification);
        sw_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sw_storage.isChecked()){
                    SharedPreferences preferences=getSharedPreferences("MyPermissions",0);
                    if(preferences.getBoolean("ASKAGAIN",false)==true){
                        if(!checkPermissions()){
                            checkAndRequestPermissions();
                        }else{
                            sw_storage.setChecked(true);
                        }
                    }else{
                        runtimePermissionsWarning("Permission Required","" +
                                "This permission is required for data recovery. Are you " +
                                "sure you want to continue?");
                    }

                }else{
                    //show dialogus box
                    runtimePermissionsWarning("Warning","" +
                            "If you Turn this permission off you won't be able to recover deleted data. Are you " +
                            "sure you want to continue?");
                }
            }
        });
        sw_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sw_notification.isChecked()){
                    //turn on
                    buildNotificationServiceAlertDialog().show();
                }else{
                    removeservice().show();
                }
            }
        });
    }
    void runtimePermissionsWarning(String title, String msg) {
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialoguebox_layout_okra);
        Button positive = dialog.findViewById(R.id.btn_allow);
        positive.setText("I'm Sure");
        Button negative = dialog.findViewById(R.id.btn_dismiss);
        negative.setText("Dismiss");
        TextView tv_title=dialog.findViewById(R.id.tv_title);
        tv_title.setText(title);
        TextView tv_msg=dialog.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        positive.setOnClickListener(view ->{
            dialog.dismiss();
            ignoreRunTimePermission();
        });
        negative.setOnClickListener (view ->{
            dialog.dismiss();
            sw_storage.setChecked(checkPermissions());
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                sw_storage.setChecked(checkPermissions());
            }
        });

        dialog.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        sw_notification.setChecked(isNotificationServiceEnabled());
        sw_storage.setChecked(checkPermissions());
    }
    private boolean isNotificationServiceEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private boolean checkPermissions() {
        List<String> permissionsNotGranted = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNotGranted.add(permission);
            }
        }
        if (!permissionsNotGranted.isEmpty()) {
            return false;
        }
        SharedPreferences mySharedPreferences = this.getSharedPreferences("MyPermissions", 0);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("ASKAGAIN", true);
        editor.apply();
        return true;

    }

    private boolean checkAndRequestPermissions() {
        List<String> permissionsNotGranted = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNotGranted.add(permission);
            }
        }
        if (!permissionsNotGranted.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNotGranted
                    .toArray(new String[permissionsNotGranted.size()]), PERMISSIONS_REQUSTCODE);
            return false;
        }
        SharedPreferences mySharedPreferences = this.getSharedPreferences("MyPermissions", 0);
        SharedPreferences.Editor editor=mySharedPreferences.edit();
        editor.putBoolean("ASKAGAIN",true);
        editor.apply();
        return true;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUSTCODE) {
            if (grantResults.length > 0 && permissions.length > 0) {
                if (checkPermissions()) {
                        sw_storage.setChecked(true);
                } else {
                    boolean showRational = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!showRational) {
                        //never asked again pressed
                        SharedPreferences mySharedPreferences = this.getSharedPreferences("MyPermissions", 0);
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putBoolean("ASKAGAIN", false);
                        editor.apply();
                        sw_storage.setChecked(false);
                    } else {
                        SharedPreferences mySharedPreferences = this.getSharedPreferences("MyPermissions", 0);
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putBoolean("ASKAGAIN", true);
                        editor.apply();
                        sw_storage.setChecked(false);
                    }

                }
            }
        }
    }
    public void ignoreRunTimePermission () {
        Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + this.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        this.startActivityForResult(i,968);
    }

    private AlertDialog buildNotificationServiceAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       sw_notification.setChecked(isNotificationServiceEnabled());
                    }
                });
        alertDialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Log.d(TAG, "onDismiss: ");
                sw_notification.setChecked(isNotificationServiceEnabled());
            }
        });
        alertDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d(TAG, "onCancel: ");
                sw_notification.setChecked(isNotificationServiceEnabled());
            }
        });
        return (alertDialogBuilder.create());
    }
    private AlertDialog removeservice() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.removeservice);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sw_notification.setChecked(isNotificationServiceEnabled());
                    }
                });
        alertDialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                sw_notification.setChecked(isNotificationServiceEnabled());
            }
        });
        alertDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                sw_notification.setChecked(isNotificationServiceEnabled());
            }
        });
        return (alertDialogBuilder.create());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==968){
            if(checkPermissions()){
                sw_storage.setChecked(true);
            }else{
                sw_storage.setChecked(false);
            }
        }
    }

//    public void loadAdmobBanner() {
//
//        com.google.android.gms.ads.AdView mAdView = new com.google.android.gms.ads.AdView(ActivitySettingsAAT.this);
//        com.google.android.gms.ads.AdSize adSize = AATAdmobFullAdManager.getAdSize(ActivitySettingsAAT.this);
//        mAdView.setAdSize(adSize);
//
//        mAdView.setAdUnitId(getResources().getString(R.string.admob_banner));
//
//        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
//
//        final RelativeLayout adViewLayout = (RelativeLayout) findViewById(R.id.bottom_banner);
//        adViewLayout.addView(mAdView);
//
//        mAdView.loadAd(adRequest);
//
//        mAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//                adViewLayout.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAdClicked() {
//                super.onAdClicked();
//            }
//
//            @Override
//            public void onAdImpression() {
//                super.onAdImpression();
//            }
//
//            @Override
//            public void onAdOpened() {
//                super.onAdOpened();
//            }
//
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                adViewLayout.setVisibility(View.VISIBLE);
//            }
//        });
//
//    }

}