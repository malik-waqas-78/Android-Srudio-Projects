package com.voicesms.voice.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.voicesms.voice.Utils.Constants;
import com.voicesms.voice.databinding.ActivityVoiceSearchSelectorBinding;

public class VoiceSearchSelectorActivity_VS extends AppCompatActivity {
    ActivityVoiceSearchSelectorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVoiceSearchSelectorBinding.inflate(getLayoutInflater());

        setSupportActionBar(binding.toolbarSearchSelectorActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbarSearchSelectorActivity.setNavigationOnClickListener(view -> {
            onBackPressed();
        });

        setContentView(binding.getRoot());
//        loadBannerAdd();

        binding.btnShopping.setOnClickListener(view -> {
            showListActivity(Constants.SEARCH_SHOPPING);
        });
        binding.btnOthers.setOnClickListener(view -> {
            showListActivity(Constants.SEARCH_OTHERS);
        });
        binding.btnSearchEngine.setOnClickListener(view -> {
            showListActivity(Constants.SEARCH_WEB);
        });
        binding.btnSocialApps.setOnClickListener(view -> {
            showListActivity(Constants.SEARCH_SOCIAL);
        });
        binding.btnCommunications.setOnClickListener(view -> {
            showListActivity(Constants.SEARCH_COMMUNICATION);
        });
    }

    private void showListActivity(String MODE) {
        startActivity(new Intent(this, SearchOptionsList_VS.class)
                .putExtra(Constants.SEARCH_MODE, MODE));
    }

//    public void loadBannerAdd() {
//        AdView adView = new AdView(this, this.getResources().getString(R.string.banner_add), AdSize.BANNER_HEIGHT_50);
//
//        AdListener adListener = new AdListener() {
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                Log.d("TAG", "onError: " + adError.getErrorMessage());
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                Log.d("TAG", "Ad loaded");
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//
//            }
//        };
//
//        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
//        RelativeLayout relativeLayout = findViewById(R.id.top_banner);
//        relativeLayout.addView(adView);
//    }
}