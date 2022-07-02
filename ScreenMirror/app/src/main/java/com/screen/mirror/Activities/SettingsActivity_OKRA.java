package com.screen.mirror.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.screen.mirror.Adapters.Settings_Adapter;
import com.screen.mirror.Interfaces.Settings_Row_Click_Listener;
import com.screen.mirror.Models.SettingsItem;
import com.screen.mirror.R;
import com.screen.mirror.Utils.InterAdHelper_OKRA;
import com.screen.mirror.Utils.SharedPrefHelperCA;
import com.screen.mirror.databinding.ActivitySettingsBinding;

import java.util.ArrayList;
import java.util.Map;

import static com.screen.mirror.Utils.Constants_CA.LANGUAGE;
import static com.screen.mirror.Utils.Constants_CA.WIFI;

public class SettingsActivity_OKRA extends AppCompatActivity {
    ActivitySettingsBinding binding;
    SharedPrefHelperCA sharedPrefHelperSM;
    Context context;
    ArrayList<SettingsItem> settings_list;
    Settings_Adapter adapter;
    private AdView adViewTop,adviewBottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ////adloading
        if(InterAdHelper_OKRA.isAppInstalledFromPlay(SettingsActivity_OKRA.this)) {
            initSdkandLoadBottomBannerAd();
        }
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = SettingsActivity_OKRA.this;
        sharedPrefHelperSM = new SharedPrefHelperCA(context);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        binding.settingsRecyclerView.setLayoutManager(linearLayoutManager);
        settings_list = getMenuList();
        adapter = new Settings_Adapter(settings_list, context, new Settings_Row_Click_Listener() {
            @Override
            public void OnClick(int adapterPosition) {
                OnItemClick(adapterPosition);
            }
        });
        binding.settingsRecyclerView.setAdapter(adapter);

    }

    private void OnItemClick(int adapterPosition) {
        SettingsItem item = settings_list.get(adapterPosition);
        switch (item.getIcon()) {
            case WIFI: {
                startActivity(new Intent(context, WifiActivity_OKRA.class));
                break;
            }
            case LANGUAGE: {
                startActivity(new Intent(context, LanguageActivity_OKRA.class));
                break;
            }
        }
    }

    private ArrayList<SettingsItem> getMenuList() {
        ArrayList<SettingsItem> settingsList = new ArrayList<>();
        settingsList.add(new SettingsItem(
                R.drawable.ic_wifi
                , getResources().getString(R.string.connect_wifi)));

        settingsList.add(new SettingsItem(
                R.drawable.ic_languages
                , getResources().getString(R.string.language)));

        return settingsList;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void initSdkandLoadBottomBannerAd(){
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
                //loadBottomBanner();
            }
        });
    }


    private void loadTopBanner() {
        adViewTop = new com.google.android.gms.ads.AdView(this);
        adViewTop.setAdUnitId(getString(R.string.admob_banner));


        com.google.android.gms.ads.AdSize adSize = getAdSize();
        // Step 4 - Set the adaptive ad size on the ad view.
        adViewTop.setAdSize(adSize);
        // Step 5 - Start loading the ad in the background.

        AdRequest adRequest =
                new AdRequest.Builder()
                        .build();
        adViewTop.loadAd(adRequest);
        adViewTop.setAdListener(new com.google.android.gms.ads.AdListener() {
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
        binding.topBanner.addView(adViewTop);

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