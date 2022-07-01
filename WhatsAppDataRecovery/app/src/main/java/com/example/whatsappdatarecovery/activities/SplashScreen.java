package com.example.whatsappdatarecovery.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whatsappdatarecovery.R;
import com.example.whatsappdatarecovery.services.NotificationListenerServiceClassMy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SplashScreen extends AppCompatActivity {
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final int PERMISSIONS_REQUEST_CODE = 2;
    AlertDialog enableNotificationListenerAlertDialog;
    ImageView btn_start;
    TextView textView;
    /*PERMISSIONS START*/
    String[] apppermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        btn_start = findViewById(R.id.image_splash_btn);
        textView = findViewById(R.id.text_privacy_policy);
        textView.setOnClickListener(view -> privacy_Dialog());
        if (!isNotificationServiceEnabled()) {
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        }
        else if (Build.VERSION.SDK_INT > 29) {
            Intent serviceintent = new Intent(getApplicationContext(), NotificationListenerServiceClassMy.class);
            startForegroundService(serviceintent);
        }
        else {
            Intent serviceintent = new Intent(getApplicationContext(), NotificationListenerServiceClassMy.class);
            startService(serviceintent);
        }
        btn_start.setOnClickListener(view -> {
            if (CheckAndRequestPermission()) {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void privacy_Dialog() {
        final Dialog dialog = new Dialog(SplashScreen.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_privacy);
        dialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(dialog.getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        final TextView ok = dialog.findViewById(R.id.btn_okay);
        ok.setOnClickListener(view -> dialog.dismiss());
    }

    private boolean isNotificationServiceEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Build Notification Listener Alert Dialog.
     */
    private AlertDialog buildNotificationServiceAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Notification Listener Service");
        alertDialogBuilder.setMessage("For the the app. to work you need to enable the Notification Listener Service. Enable it now?");
        alertDialogBuilder.setPositiveButton("Yes",
                (dialog, id) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        alertDialogBuilder.setNegativeButton("No",
                (dialog, id) -> {
                });
        return (alertDialogBuilder.create());
    }

    public boolean CheckAndRequestPermission() {
        ArrayList<String> ListPermissionNeeded = new ArrayList<>();
        for (String perm : apppermission) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                ListPermissionNeeded.add(perm);
            }
        }
        if (!ListPermissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, ListPermissionNeeded.toArray(new String[0]), PERMISSIONS_REQUEST_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            HashMap<String, Integer> permissionResults = new HashMap<>();
            int deniedCount = 0;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResults.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }
            if (deniedCount == 0) {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
            }
            else {
                for (Map.Entry<String, Integer> entry : permissionResults.entrySet()) {
                    String permName = entry.getKey();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)) {
                        showDialog("", "This app needs Storage and Internet Permissions .", "Yes, grant Permissions",
                                (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                    CheckAndRequestPermission();
                                },
                                "No,Exit app", (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                    finish();
                                }, false);
                    }
                    else {
                        showDialog("", "You have denied some permissions.Allow all permissions at [Setting] > [permissions]", "Go to Settings",
                                (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                },
                                "No , Exit App", (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                    finish();
                                }, false);
                        break;
                    }
                }
            }
        }
    }

    public void showDialog(String title, String msg, String positivelabel, DialogInterface.OnClickListener positiveOnClick,
                           String negativelabel, DialogInterface.OnClickListener negativeOnClick, boolean isCancelable) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(isCancelable);
        builder.setMessage(msg);
        builder.setPositiveButton(positivelabel, positiveOnClick);
        builder.setNegativeButton(negativelabel, negativeOnClick);
        androidx.appcompat.app.AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
