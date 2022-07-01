package com.voicesms.voice.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.rohitss.uceh.BuildConfig;
import com.rohitss.uceh.UCEHandler;
import com.voicesms.voice.Dialogs.ExitDialog_Voice_SMS;
import com.voicesms.voice.Dialogs.PrivacyDialog_Voice_SMS;
import com.voicesms.voice.Interfaces.ExitDialogInterface_Voice_SMS;
import com.voicesms.voice.Utils.RateUs_Voice_SMS;
import com.voicesms.voice.databinding.ActivitySplashBinding;

public class SplashActivity_VS extends AppCompatActivity {
    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        loadBannerAdd();
//        NativeAdsVoiceSMS.prepareAds(SplashActivity_VS.this);
//        InterstitialAddsVoiceSMS.prepareAdd(SplashActivity_VS.this);

        if (BuildConfig.DEBUG) {
            new UCEHandler.Builder(getApplicationContext()).build();
        }

        binding.btnStart.setOnClickListener(view -> {
            startActivity(new Intent(this, SelectionActivity_VS.class));
            finish();
        });

        binding.privacyPolicy.setOnClickListener(view -> {
            PrivacyDialog_Voice_SMS.CreatePrivacyDialog(this);
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.progressBarSplash.setVisibility(View.INVISIBLE);
                binding.btnStart.setVisibility(View.VISIBLE);
            }
        }, 3000);
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

    @Override
    public void onBackPressed() {
        ExitDialog_Voice_SMS.CreateExitDialog(this, new ExitDialogInterface_Voice_SMS() {
            @Override
            public void ExitFromApp() {
                finish();
            }

            @Override
            public void RateUs() {
                new RateUs_Voice_SMS(SplashActivity_VS.this);
            }
        });
    }
}