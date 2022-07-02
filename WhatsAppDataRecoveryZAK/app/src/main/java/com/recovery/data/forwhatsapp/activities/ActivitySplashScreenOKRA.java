package com.recovery.data.forwhatsapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdListener;
//import com.facebook.ads.AdSize;
//import com.facebook.ads.AdView;
//import com.google.android.gms.ads.LoadAdError;
import com.recovery.data.forwhatsapp.BuildConfig;
import com.recovery.data.forwhatsapp.R;
import com.recovery.data.forwhatsapp.fileobserverspkg.CheckObserverOKRA;
//import com.rohitss.uceh.UCEHandler;

import java.util.ArrayList;
import java.util.List;

import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

public class ActivitySplashScreenOKRA extends AppCompatActivity {


    private static final int PERMISSIONS_REQUSTCODE = 927;

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    RelativeLayout splashScreen_Start;


    private AlertDialog enableNotificationListenerAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        /*        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_splash_screen_okra);

        //init componenets and data
        //initialize_Components();

//        if(AATAdsManager.isAppInstalledFromPlay(this)){
////            loadFbBannerAdd();
////            loadAdmobBanner();
//            AATAdsManager.LoadFullAdFacebook(ActivitySplashScreenAAT.this);
//            AATAdsManager.loadNativeAdFacebook(ActivitySplashScreenAAT.this);
//            AATAdmobFullAdManager.loadAdmobInterstitial(ActivitySplashScreenAAT.this);
//        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
           CheckObserverOKRA observer=new CheckObserverOKRA();
           observer.execute();
        }

        if (splashScreen_Start == null) {
            splashScreen_Start = findViewById(R.id.relativestart);
            splashScreen_Start.setOnClickListener(new SplashStart_Clicks());
        } else {
            splashScreen_Start.setOnClickListener(new SplashStart_Clicks());
        }

        TextView textView = findViewById(R.id.privacypolicy);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privacy_Dialog();
            }
        });

        if (BuildConfig.DEBUG) {
//            new UCEHandler.Builder(this).build();

        }

        ProgressBar loadingProgressBar = findViewById(R.id.loadingProgressBar);
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingProgressBar.setVisibility(View.INVISIBLE);
                splashScreen_Start.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }

    private void privacy_Dialog() {
        final Dialog dialog = new Dialog(ActivitySplashScreenOKRA.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.privacypolicy_layout_okra);
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
    protected void onStart() {
        super.onStart();


    }

    public void initialize_Components() {
        if (!isNotificationServiceEnabled()) {
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        } else {
            Intent intent = new Intent(getApplicationContext(), MainActivityOKRA.class);
            startActivity(intent);
        }
    }

    private boolean init_sub() {
        //Toast.makeText(this, "service started", Toast.LENGTH_SHORT).show();
        return requestStoragePermissions();
    }

    private boolean requestStoragePermissions() {
        if (checkAndRequestPermissions()) {
            return true;
        }
        return false;
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

        return true;

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUSTCODE) {
            if (grantResults.length > 0 && permissions.length > 0) {
                if (checkPermissions()) {
                    if (isNotificationServiceEnabled()) {
                        Intent intent = new Intent(this, MainActivityOKRA.class);
                        startActivity(intent);
                    } else {
                        initialize_Components();
                    }
                } else {
                    boolean showRational = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!showRational) {
                        //never asked again pressed
                        SharedPreferences mySharedPreferences = this.getSharedPreferences("MyPermissions", 0);
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putBoolean("ASKAGAIN", false);
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), ActivitySettingsOKRA.class);
                        startActivity(intent);
                    } else {
                        SharedPreferences mySharedPreferences = this.getSharedPreferences("MyPermissions", 0);
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putBoolean("ASKAGAIN", true);
                        editor.apply();
                        initialize_Components();
                    }

                }
            }
        }
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

    private void start_MainApp() {
        Intent intent = new Intent(this, MainActivityOKRA.class);
        startActivity(intent);
    }


    private AlertDialog buildNotificationServiceAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivityForResult(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS), 9876);
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If you choose to not enable the notification listener
                        // the app. will not work as expected
                        Intent intent = new Intent(getApplicationContext(), ActivitySettingsOKRA.class);
                        startActivity(intent);
                    }
                });
        return (alertDialogBuilder.create());
    }

    class SplashStart_Clicks implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (!checkPermissions()) {
                checkAndRequestPermissions();
//                if (!isNotificationServiceEnabled()) {
//                    initialize_Components();
//                } else {
//                    start_MainApp();
//                }
            } else {
                if (!isNotificationServiceEnabled()) {
                    initialize_Components();
                } else {
                    start_MainApp();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9876) {
            if (isNotificationServiceEnabled()) {
                if (checkPermissions()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivityOKRA.class);
                    startActivity(intent);
                } else {
                    SharedPreferences preferences = this.getSharedPreferences("MyPermissions", 0);
                    if (preferences.getBoolean("ASKAGAIN", false) == true) {
                        checkAndRequestPermissions();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), ActivitySettingsOKRA.class);
                        startActivity(intent);
                    }
                }
            }
        } else {

        }
    }


/*    public void loadFbBannerAdd() {
        AdView adView = new AdView(this, getResources().getString(R.string.fbbannerad), AdSize.BANNER_HEIGHT_50);

        AdListener adListener = new AdListener() {

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d("TAG", "onError: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };


        RelativeLayout relativeLayout = findViewById(R.id.top_banner);
        relativeLayout.addView(adView);

        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
    }

    public void loadAdmobBanner() {

        com.google.android.gms.ads.AdView mAdView = new com.google.android.gms.ads.AdView(ActivitySplashScreenAAT.this);
        com.google.android.gms.ads.AdSize adSize = AATAdmobFullAdManager.getAdSize(ActivitySplashScreenAAT.this);
        mAdView.setAdSize(adSize);

        mAdView.setAdUnitId(getResources().getString(R.string.admob_banner));

        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();

        final RelativeLayout adViewLayout = (RelativeLayout) findViewById(R.id.bottom_banner);
        adViewLayout.addView(mAdView);

        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                adViewLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adViewLayout.setVisibility(View.VISIBLE);
            }
        });

    }*/
}
