package com.example.deviceinfo.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.deviceinfo.R;
import com.example.deviceinfo.adapter.General_info_Adapter;
import com.example.deviceinfo.databinding.ActivitySystemInfoBinding;
import com.example.deviceinfo.models.General_info_Model;
import com.scottyab.rootbeer.RootBeer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

public class System_info extends AppCompatActivity {
    private static final int PERMISSIONS_REQUSTCODE2 = 1001;
    ActivitySystemInfoBinding binding;
    General_info_Adapter general_info_adapter;
    ArrayList<General_info_Model> info_models=new ArrayList<>();

    private Handler updateHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySystemInfoBinding.inflate(getLayoutInflater());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(binding.getRoot());
        updateHandler=new Handler();
        addDatatoList();
    }

    private void addDatatoList() {

        info_models.add(new General_info_Model(getResources().getString(R.string.androidVerision), Build.VERSION.RELEASE));
        info_models.add(new General_info_Model(getResources().getString(R.string.sdkno), SDK_INT+""));
        info_models.add(new General_info_Model(getResources().getString(R.string.buildNo), Build.DISPLAY));
        info_models.add(new General_info_Model(getResources().getString(R.string.baseband), Build.getRadioVersion()));
//        if(hasFingerPrint()){
            info_models.add(new General_info_Model(getResources().getString(R.string.fingerprint), getFingerprint()));
//        }else{
//            info_models.add(new General_info_Model(getResources().getString(R.string.fingerprint), getResources().getString(R.string.noDetected)));
//        }

        info_models.add(new General_info_Model(getResources().getString(R.string.buildtime), getDurationBreakdown(Build.TIME)));
        if(is64Bit()){
            info_models.add(new General_info_Model(getResources().getString(R.string.osbit), "64Bit"));
        }else{
            info_models.add(new General_info_Model(getResources().getString(R.string.osbit), "32Bit"));
        }
       info_models.add(new General_info_Model(getResources().getString(R.string.release),"later"));
        info_models.add(new General_info_Model(getResources().getString(R.string.security)+" "+getResources().getString(R.string.patchlevel), Build.VERSION.SECURITY_PATCH));
        RootBeer rootBeer = new RootBeer(this);
        if (rootBeer.isRooted()) {
            info_models.add(new General_info_Model(getResources().getString(R.string.rootaccess), "Yes"));
        } else {
            info_models.add(new General_info_Model(getResources().getString(R.string.rootaccess), "No"));
        }
        info_models.add(new General_info_Model(getResources().getString(R.string.systemuptime), getuptime(SystemClock.elapsedRealtime())));
        info_models.add(new General_info_Model(getResources().getString(R.string.playservices), "Later"));
        if (SDK_INT >= Build.VERSION_CODES.O) {
            info_models.add(new General_info_Model(getResources().getString(R.string.javavm),System.getProperty("java.vm.version")));
        }
        info_models.add(new General_info_Model(getResources().getString(R.string.kernal), System.getProperty("os.version")));
        info_models.add(new General_info_Model(getResources().getString(R.string.openGl), "Later"));

       setValueinAdapter();
    }
    public void setValueinAdapter(){
        general_info_adapter=new General_info_Adapter(info_models,System_info.this);
        binding.recyclerInfo.setLayoutManager(new GridLayoutManager(System_info.this,1));
        binding.recyclerInfo.setAdapter(general_info_adapter);
        updateUptimes();
    }

    public boolean hasFingerPrint(){
        boolean fingerCheck = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                fingerCheck=false;
            } else {
                fingerCheck=true;
            }
        }else {
            fingerCheck=false;
        }
        return fingerCheck;
    }

    public String getFingerprint() {
        String osBuildNumber = Build.FINGERPRINT;  //"google/shamu/shamu:5.1.1/LMY48Y/2364368:user/release-keys”
        return osBuildNumber.toUpperCase();
    }
    public static String getDurationBreakdown(long millis) {
        Date osInstalledDate = new Date(millis);
        return osInstalledDate .toString();
    }
    public static String getuptime(long millis) {
        if(millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }


        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);

        sb.append(hours);
        sb.append(" : ");
        sb.append(minutes);
        sb.append(" : ");
        sb.append(seconds);
//        sb.append(" Seconds");

        return(sb.toString());
    }
    private void updateUptimes() {

        // Get the whole uptime
        long uptimeMillis = SystemClock.elapsedRealtime();
        String wholeUptime = String.format(Locale.getDefault(),
                "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(uptimeMillis),
                TimeUnit.MILLISECONDS.toMinutes(uptimeMillis)
                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                        .toHours(uptimeMillis)),
                TimeUnit.MILLISECONDS.toSeconds(uptimeMillis)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                        .toMinutes(uptimeMillis)));
//        wholeView.setText(wholeUptime);
//        binding.toolbarTitle.setText(wholeUptime);
       info_models.get(10).setInfo(wholeUptime);
       general_info_adapter.notifyItemChanged(10);
        // Get the uptime without deep sleep


        // Call updateUptimes after one second
        updateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateUptimes();
            }
        }, 1000);
    }

    private static int getVersionFromActivityManager(Context context) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configInfo = activityManager.getDeviceConfigurationInfo();
        if (configInfo.reqGlEsVersion != ConfigurationInfo.GL_ES_VERSION_UNDEFINED) {
            return configInfo.reqGlEsVersion;
        } else {
            return 1 << 16; // Lack of property means OpenGL ES version 1
        }
    }




    private static int getVersionFromPackageManager(Context context) {
        PackageManager packageManager = context.getPackageManager();
        FeatureInfo[] featureInfos = packageManager.getSystemAvailableFeatures();
        if (featureInfos != null && featureInfos.length > 0) {
            for (FeatureInfo featureInfo : featureInfos) {
                // Null feature name means this feature is the open gl es version feature.
                if (featureInfo.name == null) {
                    if (featureInfo.reqGlEsVersion != FeatureInfo.GL_ES_VERSION_UNDEFINED) {
                        return featureInfo.reqGlEsVersion;
                    } else {
                        return 1 << 16; // Lack of property means OpenGL ES version 1
                    }
                }
            }
        }
        return 1;
    }
    public static boolean is64Bit() {
        return (Build.SUPPORTED_64_BIT_ABIS!= null && Build.SUPPORTED_64_BIT_ABIS.length >0);
    }


}