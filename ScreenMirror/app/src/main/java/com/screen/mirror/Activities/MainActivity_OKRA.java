package com.screen.mirror.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;


import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.screen.mirror.Activities.Images.ImagesActivity_OKRA;
import com.screen.mirror.Activities.Videos.VideosActivity_OKRA;
import com.screen.mirror.Adapters.MainMenuAdapter;
import com.screen.mirror.Dialogs.ExitDialog;
import com.screen.mirror.Interfaces.ExitDialogInterface;
import com.screen.mirror.Interfaces.MainMenuInterface;
import com.screen.mirror.Models.Main_Menu_Model;
import com.screen.mirror.R;
import com.screen.mirror.Utils.InterAdHelper_OKRA;
import com.screen.mirror.Utils.RateUs_CA;
import com.screen.mirror.Utils.SharedPrefHelperCA;
import com.screen.mirror.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.screen.mirror.Utils.Constants_CA.CASTING;
import static com.screen.mirror.Utils.Constants_CA.HELP;
import static com.screen.mirror.Utils.Constants_CA.IMAGES;
import static com.screen.mirror.Utils.Constants_CA.SETTINGS;
import static com.screen.mirror.Utils.Constants_CA.VIDEOS;
import static com.screen.mirror.Utils.Constants_CA.XIAOMI;
import static com.screen.mirror.Utils.Constants_CA.YOUTUBE;

public class MainActivity_OKRA extends AppCompatActivity implements InterAdHelper_OKRA.Companion.AdLoadCallBack {
    private static final int REQUEST_CHECK_SETTINGS = 214;
    public static String TAG = "MainActivity_OKRA";
    ActivityMainBinding binding;
    SharedPrefHelperCA sharedPrefHelperSM;
    Context context;
    ArrayList<Main_Menu_Model> main_menu_list;
    MainMenuAdapter adapter;
    SettingsClient settingsClient;
    LocationSettingsRequest locationSettingsRequest;

    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ///adloading
        if(InterAdHelper_OKRA.isAppInstalledFromPlay(MainActivity_OKRA.this)) {
            loadBannerBottom();
            loadTopBanner();
        }
        /////////////
        context = MainActivity_OKRA.this;
        sharedPrefHelperSM = new SharedPrefHelperCA(context);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        locationSettingsRequest = builder.build();
        settingsClient = LocationServices.getSettingsClient(context);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        binding.mainMenuRecyclerview.setLayoutManager(gridLayoutManager);
        main_menu_list = getMenuList();
        adapter = new MainMenuAdapter(main_menu_list, context, new MainMenuInterface() {
            @Override
            public void OnClick(int position) {
                OnItemClick(position);
            }
        });
        binding.mainMenuRecyclerview.setAdapter(adapter);
    }

    private void OnItemClick(int position) {
        Main_Menu_Model current_item = main_menu_list.get(position);
        switch (current_item.getIcon()) {
            case CASTING: {
                setCasting();
                break;
            }
            case SETTINGS: {
                startActivity(new Intent(context, SettingsActivity_OKRA.class));
                break;
            }
            case IMAGES: {
                startActivity(new Intent(context, ImagesActivity_OKRA.class));
                break;
            }
            case VIDEOS: {
                startActivity(new Intent(context, VideosActivity_OKRA.class));
                break;
            }
            case HELP: {
                startActivity(new Intent(context, UserGuideActivity_OKRA.class));
                break;
            }
            case YOUTUBE: {
                startActivity(new Intent(context, WebViewActivity.class));
                break;
            }
        }
    }

    public void setCasting() {
        settingsClient
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(locationSettingsResponse -> {
                    try {
                        Intent panelIntent;
                        if (Build.MANUFACTURER.equals(XIAOMI)) {
                            panelIntent = new Intent("android.settings.WIRELESS_SETTINGS");
                        } else {
                            panelIntent = new Intent("android.settings.WIFI_DISPLAY_SETTINGS");
                        }
                        startActivity(panelIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            startActivity(new Intent("com.samsung.wfd.LAUNCH_WFD_PICKER_DLG"));
                        } catch (Exception ex) {
                            try {
                                startActivity(new Intent("android.settings.CAST_SETTINGS"));
                            } catch (Exception exception) {
                                Toasty.error(MainActivity_OKRA.this, getResources().getString(R.string.device_not_supported), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult(MainActivity_OKRA.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sie) {
                                Log.e(TAG, "Unable to execute request.");
                            }
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Log.e(TAG, "Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
                    }
                })
                .addOnCanceledListener(() -> Log.e("GPS", "checkLocationSettings -> onCanceled"));
    }

    private ArrayList<Main_Menu_Model> getMenuList() {
        ArrayList<Main_Menu_Model> menuList = new ArrayList<>();
        menuList.add(new Main_Menu_Model(
                R.drawable.ic_casting
                , getResources().getString(R.string.casting)));

        menuList.add(new Main_Menu_Model(
                R.drawable.ic_settings
                , getResources().getString(R.string.settings)));

        menuList.add(new Main_Menu_Model(
                R.drawable.ic_images
                , getResources().getString(R.string.images)));

        menuList.add(new Main_Menu_Model(
                R.drawable.ic_videos
                , getResources().getString(R.string.videos)));

        menuList.add(new Main_Menu_Model(
                R.drawable.ic_how_to_use
                , getResources().getString(R.string.help)));

        menuList.add(new Main_Menu_Model(
                R.drawable.ic_youtube
                , getResources().getString(R.string.youtube)));
        return menuList;
    }

    @Override
    public void onBackPressed() {

            if(InterAdHelper_OKRA.isAppInstalledFromPlay(MainActivity_OKRA.this)) {
                InterAdHelper_OKRA.showAdmobIntersitial(MainActivity_OKRA.this);
            }

            ExitDialog.CreateExitDialog(context, new ExitDialogInterface() {
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


    public void loadBannerBottom() {
        AdView adView = new AdView(MainActivity_OKRA.this, getApplicationContext().getResources().getString(R.string.banner_add), AdSize.BANNER_HEIGHT_50);
        AdListener adListener = new AdListener() {


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

    public void initSdkandLoadTopBannerAd() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Map<String, AdapterStatus> map = initializationStatus.getAdapterStatusMap();
                for (Map.Entry<String, AdapterStatus> entry : map.entrySet()) {
                    String className = entry.getKey();
                    AdapterStatus status = entry.getValue();
                    if (status.getInitializationState().equals(AdapterStatus.State.NOT_READY)) {

                        // The adapter initialization did not complete.
                        Log.d("92727586243", "Adapter: " + className + " not ready.");
                    } else if (status.getInitializationState().equals(AdapterStatus.State.READY)) {
                        // The adapter was successfully initialized.
                        Log.d("92727586243", "Adapter: " + className + " is initialized.");
                    }

                }
                loadTopBanner();
            }
        });
    }

    private void loadTopBanner() {
         com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(this);
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

    public void loadInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, getString(R.string.admob_interstitial_ad), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd1) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        interstitialAd = interstitialAd1;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        interstitialAd = null;
                    }
                });

    }

    @Override
    public void adClosed() {
        if(InterAdHelper_OKRA.isAppInstalledFromPlay(MainActivity_OKRA.this)){
            InterAdHelper_OKRA.loadAdmobInterstitial(MainActivity_OKRA.this);
        }

        ExitDialog.CreateExitDialog(context, new ExitDialogInterface() {
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