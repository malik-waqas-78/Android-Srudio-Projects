package com.screen.mirror.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.screen.mirror.Dialogs.ExitDialog;
import com.screen.mirror.Dialogs.GeneralDialog;
import com.screen.mirror.Interfaces.ExitDialogInterface;
import com.screen.mirror.Interfaces.GeneralDialogInterface;
import com.screen.mirror.R;
import com.screen.mirror.Utils.InterAdHelper_OKRA;
import com.screen.mirror.Utils.NativeAdHelper_OKRA;
import com.screen.mirror.Utils.RateUs_CA;
import com.screen.mirror.Utils.SharedPrefHelperCA;
import com.screen.mirror.databinding.ActivitySplashBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.screen.mirror.Utils.Constants_CA.ENGLISH;
import static com.screen.mirror.Utils.Constants_CA.IS_FIRST_RUN;
import static com.screen.mirror.Utils.Constants_CA.LANGUAGE_KEY;

public class SplashActivity_OKRA extends AppCompatActivity {
    public static final String TAG = "SplashScreenActivity";
    public static final int PERMISSIONS_REQUEST_CODE = 2;
    public static final int PERMISSIONS_Location_REQUEST_CODE = 3;
    ActivitySplashBinding binding;
    SharedPrefHelperCA sharedPrefHelperSM;
    Context context;
    String[] appPermission = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    String[] appLocationPermission = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    ArrayList<String> ListPermissionNeeded = new ArrayList<>();
    private com.google.android.gms.ads.AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());

        ////add loading
        if(InterAdHelper_OKRA.isAppInstalledFromPlay(SplashActivity_OKRA.this)) {
            loadBannerBottom();
            initSdkandLoadTopBannerAd();
            InterAdHelper_OKRA.loadAdmobInterstitial(this);
            NativeAdHelper_OKRA.loadAdmobNativeAd(this);
        }
        ////////////////
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        context = SplashActivity_OKRA.this;
        sharedPrefHelperSM = new SharedPrefHelperCA(context);

        String saved_locale = sharedPrefHelperSM.Get_String_SM(LANGUAGE_KEY, ENGLISH);
        changeLanguage(context, saved_locale);

        boolean is_first_run = sharedPrefHelperSM.Get_Boolean_SM(IS_FIRST_RUN, true);
        if (is_first_run) {
            startActivity(new Intent(context, UserGuideActivity_OKRA.class));
        }

        setContentView(binding.getRoot());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.progressBarSplash.setVisibility(View.INVISIBLE);
                binding.btnStart.setVisibility(View.VISIBLE);
            }
        }, 3000);

        binding.btnStart.setOnClickListener(view -> {
            if (checkLocationPermission()) {
                Intent i = new Intent(context, MainActivity_OKRA.class);
                startActivity(i);
                finish();
            } else {
                showLocationDialog();
            }
        });

        binding.privacyPolicy.setOnClickListener(view -> showPrivacyPolicyDialog());
    }

    private void showLocationDialog() {
        GeneralDialog.CreateGeneralDialog(context
                , getResources().getString(R.string.location_dialog_title)
                , getResources().getString(R.string.location_dialog_desc)
                , getResources().getString(R.string.allow)
                , getResources().getString(R.string.deny)
                , new GeneralDialogInterface() {
                    @Override
                    public void Positive(Dialog dialog) {
                        requestLocationPermission();
                    }

                    @Override
                    public void Negative(Dialog dialog) {

                    }
                }
        );
    }

    private void requestLocationPermission() {
        if (!ListPermissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, ListPermissionNeeded.toArray(new String[ListPermissionNeeded.size()]), PERMISSIONS_Location_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_Location_REQUEST_CODE) {
            HashMap<String, Integer> permissionResults = new HashMap<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResults.put(permissions[i], grantResults[i]);
                } else {
                    Intent intent = new Intent(context, MainActivity_OKRA.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    public boolean checkLocationPermission() {
        ListPermissionNeeded.clear();
        for (String perm : appLocationPermission) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                ListPermissionNeeded.add(perm);
                return false;
            }
        }
        return true;
    }

    private void showPrivacyPolicyDialog() {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_privacy_policy);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        final TextView ok = dialog.findViewById(R.id.btn_okay);
        ok.setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(true);
        dialog.show();
    }

    private void changeLanguage(Context context, String saved_locale) {
        Locale locale = new Locale(saved_locale);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.locale = locale;
        } else {
            config.setLocale(new Locale(saved_locale));
        }
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    @Override
    public void onBackPressed() {
        ExitDialog.CreateExitDialog(context
                , new ExitDialogInterface() {
                    @Override
                    public void ExitFromApp() {
                        finishAffinity();
                    }

                    @Override
                    public void RateUs() {
                        new RateUs_CA(context);
                    }
                });
    }
    public void loadBannerBottom(){
        AdView adView=new AdView(SplashActivity_OKRA.this, getApplicationContext().getResources().getString(R.string.banner_add), AdSize.BANNER_HEIGHT_50);
        AdListener adListener=new AdListener() {


            @Override
            public void onError(Ad ad, AdError adError) {

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


        binding.bottomBanner.addView(adView);
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
    }
    public void initSdkandLoadTopBannerAd(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Map<String, AdapterStatus> map = initializationStatus.getAdapterStatusMap();
                for (Map.Entry<String,AdapterStatus> entry : map.entrySet()) {
                    String className = entry.getKey();
                    AdapterStatus status = entry.getValue();
                    if (status.getInitializationState().equals(AdapterStatus.State.NOT_READY)) {

                        // The adapter initialization did not complete.
                        Log.d("92727586243", "Adapter: " + className + " not ready.");
                    }

                    else if (status.getInitializationState().equals(AdapterStatus.State.READY))  {
                        // The adapter was successfully initialized.
                        Log.d("92727586243", "Adapter: " + className + " is initialized.");
                    }

                }
                loadTopBanner();
            }
        });
    }

    private void loadTopBanner() {
        adView = new com.google.android.gms.ads.AdView(this);
        adView.setAdUnitId(getString(R.string.admob_banner));


        com.google.android.gms.ads.AdSize adSize = getAdSize();
        // Step 4 - Set the adaptive ad size on the ad view.
        adView.setAdSize(adSize);
        // Step 5 - Start loading the ad in the background.

        AdRequest adRequest =
                new AdRequest.Builder()
                        .build();
        adView.loadAd(adRequest);
        adView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });
        binding.topBanner.addView(adView);

    }
        public com.google.android.gms.ads.AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return com.google.android.gms.ads.AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

}