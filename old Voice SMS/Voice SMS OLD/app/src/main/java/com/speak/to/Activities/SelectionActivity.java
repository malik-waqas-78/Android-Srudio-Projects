package com.speak.to.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.speak.to.Dialogs.ExitDialog_Voice_SMS;
import com.speak.to.Interfaces.ExitDialogInterface_Voice_SMS;
import com.speak.to.R;
import com.speak.to.Utils.RateUs_Voice_SMS;
import com.speak.to.databinding.ActivitySelectionBinding;

public class SelectionActivity extends AppCompatActivity {
    ActivitySelectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadBannerAdd();
        showCustomAd();

        binding.cardVoiceSms.setOnClickListener(view -> {
            startActivity(new Intent(this, SMSActivity.class));
        });

        binding.cardVoiceRecording.setOnClickListener(view -> {
            startActivity(new Intent(this, RecordingActivity.class));
        });

        binding.cardVoiceSearch.setOnClickListener(view -> {
            startActivity(new Intent(this, VoiceSearchSelectorActivity.class));
        });
    }

    private void showCustomAd() {
        startActivity(new Intent(SelectionActivity.this, AdActivity.class));
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
                new RateUs_Voice_SMS(SelectionActivity.this);
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