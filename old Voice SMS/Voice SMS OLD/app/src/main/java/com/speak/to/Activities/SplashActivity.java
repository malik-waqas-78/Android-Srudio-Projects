package com.speak.to.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rohitss.uceh.BuildConfig;
import com.rohitss.uceh.UCEHandler;
import com.speak.to.Dialogs.ExitDialog_Voice_SMS;
import com.speak.to.Dialogs.PrivacyDialog_Voice_SMS;
import com.speak.to.Interfaces.ExitDialogInterface_Voice_SMS;
import com.speak.to.R;
import com.speak.to.Utils.NativeAdsVoiceSMS;
import com.speak.to.Utils.RateUs_Voice_SMS;
import com.speak.to.databinding.ActivitySplashBinding;

import static com.speak.to.Utils.Constants.app_values;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadBannerAdd();
        NativeAdsVoiceSMS.prepareAds(SplashActivity.this);
        loadCustomAd();
//        InterstitialAddsVoiceSMS.prepareAdd(SplashActivity.this);

        if (BuildConfig.DEBUG) {
            new UCEHandler.Builder(getApplicationContext()).build();
        }

        binding.btnStart.setOnClickListener(view -> {
            startActivity(new Intent(this, SelectionActivity.class));
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

    private void loadCustomAd() {
        FirebaseApp.initializeApp(SplashActivity.this);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("TAG", "onDataChange: " + snapshot);
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                        app_values.put(snapshot2.getKey(), snapshot2.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void loadBannerAdd() {
        AdView adView = new AdView(this, this.getResources().getString(R.string.banner_add), AdSize.BANNER_HEIGHT_50);

        AdListener adListener = new AdListener() {

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d("TAG", "onError: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d("TAG", "Ad loaded");
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };

        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
        RelativeLayout relativeLayout = findViewById(R.id.top_banner);
        relativeLayout.addView(adView);
    }

    @Override
    public void onBackPressed() {
        ExitDialog_Voice_SMS.CreateExitDialog(this, new ExitDialogInterface_Voice_SMS() {
            @Override
            public void ExitFromApp() {
                finish();
            }

            @Override
            public void RateUs() {
                new RateUs_Voice_SMS(SplashActivity.this);
            }
        });
    }
}