package com.voicesms.voice.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.voicesms.voice.Dialogs.ExitDialog_Voice_SMS;
import com.voicesms.voice.Interfaces.ExitDialogInterface_Voice_SMS;
import com.voicesms.voice.Utils.RateUs_Voice_SMS;
import com.voicesms.voice.databinding.ActivitySelectionBinding;

public class SelectionActivity_VS extends AppCompatActivity {
    ActivitySelectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        loadBannerAdd();

        binding.cardVoiceSms.setOnClickListener(view -> {
            startActivity(new Intent(this, SMSActivity_VS.class));
        });

        binding.cardVoiceRecording.setOnClickListener(view -> {
            startActivity(new Intent(this, RecordingActivity_VS.class));
        });

        binding.cardVoiceSearch.setOnClickListener(view -> {
            startActivity(new Intent(this, VoiceSearchSelectorActivity_VS.class));
        });
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
                new RateUs_Voice_SMS(SelectionActivity_VS.this);
            }
        });
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