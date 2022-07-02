package com.locker.applock.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.locker.applock.Dialogs.ExitDialog_AppLock;
import com.locker.applock.Interfaces.ExitDialogInterface;
import com.locker.applock.R;
import com.locker.applock.Utils.RateUs;
import com.locker.applock.Utils.SharedPrefHelper;
import com.locker.applock.databinding.ActivitySplashScreenBinding;
import com.screen.mirror.Utils.InterAdHelper_OKRA;
import com.screen.mirror.Utils.NativeAdHelper_OKRA;

import java.util.Map;

import static com.locker.applock.Utils.Constants.LAUNCHER_PACKAGE;

public class SplashScreen extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    Context context;
    SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        InterAdHelper_OKRA.loadAdmobInterstitial(this);
        NativeAdHelper_OKRA.loadAdmobNativeAd(this);

        loadTopBanner();
        context = SplashScreen.this;
        sharedPrefHelper = new SharedPrefHelper(context);

        getLauncherPackage();

        binding.textPrivacyPolicy.setOnClickListener(view -> {
            showPrivacyDialog();
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.loaderProgressBar.setVisibility(View.INVISIBLE);
                binding.btnStart.setVisibility(View.VISIBLE);
            }
        }, 3000);

        binding.btnStart.setOnClickListener(view -> {
            startActivity(new Intent(context, HomeActivity.class));
            finish();
        });
    }

    private void getLauncherPackage() {
        PackageManager localPackageManager = getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        String launcher = localPackageManager.resolveActivity(intent
                , PackageManager.MATCH_DEFAULT_ONLY).activityInfo.packageName;
        sharedPrefHelper.Set_String_AL(LAUNCHER_PACKAGE, launcher);
    }

    private void showPrivacyDialog() {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.privacy_policy_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        final TextView ok = dialog.findViewById(R.id.btn_okay);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        ExitDialog_AppLock.CreateExitDialog(context
                , new ExitDialogInterface() {
                    @Override
                    public void ExitFromApp() {
                        finish();
                    }

                    @Override
                    public void RateUs() {
                        new RateUs(context);
                    }
                });
    }

    private void loadTopBanner() {
        AdView adViewTop = new com.google.android.gms.ads.AdView(this);
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

                Log.d("addloadfailederror",loadAdError.toString());

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