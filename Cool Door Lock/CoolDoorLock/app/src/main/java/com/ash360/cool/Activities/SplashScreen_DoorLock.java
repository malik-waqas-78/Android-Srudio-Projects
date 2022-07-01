package com.ash360.cool.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ash360.cool.AdUtils.NativeAdsCoolDoorLock;
import com.ash360.cool.Dialogs.ExitDialog_DoorLock;
import com.ash360.cool.Dialogs.FeedbackDialog_DoorLock;
import com.ash360.cool.Interfaces.ExitInterface_DoorLock;
import com.ash360.cool.R;
import com.ash360.cool.Utils.RateUs_DoorLock;
import com.ash360.cool.databinding.ActivitySplashScreenDoorLockBinding;
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

import static com.ash360.cool.Utils.Constants_DoorLock.app_values;

public class SplashScreen_DoorLock extends AppCompatActivity {
    ActivitySplashScreenDoorLockBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenDoorLockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseApp.initializeApp(SplashScreen_DoorLock.this);

        context = SplashScreen_DoorLock.this;

        loadBannerAdd();
        NativeAdsCoolDoorLock.prepareAds(SplashScreen_DoorLock.this);
        loadCustomAd();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnStartSplash.setVisibility(View.VISIBLE);
            }
        }, 3000);

        binding.btnStartSplash.setOnClickListener(v -> {
            startActivity(new Intent(this, Settings_DoorLock.class));
            finish();
        });
    }

    private void loadCustomAd() {
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

    @Override
    public void onBackPressed() {
        ExitDialog_DoorLock.CreateExitDialog(SplashScreen_DoorLock.this, new ExitInterface_DoorLock() {
            @Override
            public void ExitFromApp() {
                finish();
            }

            @Override
            public void UpVote() {
                new RateUs_DoorLock(SplashScreen_DoorLock.this);
            }

            @Override
            public void DownVote() {
                FeedbackDialog_DoorLock.CreateFeedbackDialog(context);
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
}