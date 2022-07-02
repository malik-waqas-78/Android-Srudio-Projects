package com.locker.applock.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.locker.applock.Dialogs.GeneralDialog;
import com.locker.applock.Interfaces.GeneralDialogInterface;
import com.locker.applock.LockActivities.PatternLockActivity;
import com.locker.applock.LockActivities.PinLockActivity;
import com.locker.applock.R;
import com.locker.applock.Utils.SharedPrefHelper;
import com.locker.applock.databinding.ActivityClearStorageActvityBinding;
import com.screen.mirror.Utils.InterAdHelper_OKRA;

import static com.locker.applock.Utils.Constants.IS_LOCK_SET;
import static com.locker.applock.Utils.Constants.LOCK_MODE;
import static com.locker.applock.Utils.Constants.LOCK_TYPE;
import static com.locker.applock.Utils.Constants.MATCH_LOCK;
import static com.locker.applock.Utils.Constants.PATTERN;

public class ClearStorageActivity extends AppCompatActivity {
    ActivityClearStorageActvityBinding binding;
    Context context;
    SharedPrefHelper sharedPrefHelper;
    boolean OnceUnlocked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClearStorageActvityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(InterAdHelper_OKRA.isAppInstalledFromPlay(ClearStorageActivity.this)) {
            loadTopBanner();
        }
        context = ClearStorageActivity.this;
        sharedPrefHelper = new SharedPrefHelper(context);
        OnceUnlocked = false;

        int lock_mode = sharedPrefHelper.Get_Int_AL(LOCK_TYPE, PATTERN);
        Intent intent;
        if (lock_mode == PATTERN) {
            intent = new Intent(this, PatternLockActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        } else {
            intent = new Intent(this, PinLockActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        }
        intent.putExtra(LOCK_MODE, MATCH_LOCK);
        if (sharedPrefHelper.Get_Boolean_AL(IS_LOCK_SET, false)) {
            startActivity(intent);
        } else {
            Toast.makeText(context, "No Lock Set Up", Toast.LENGTH_SHORT).show();
        }

        binding.btnClearStorage.setOnClickListener(view -> {
            GeneralDialog.CreateGeneralDialog(context
                    , getResources().getString(R.string.clear_storage_title)
                    , getResources().getString(R.string.clear_storage_desc)
                    , getResources().getString(R.string.yes)
                    , getResources().getString(R.string.no)
                    , new GeneralDialogInterface() {
                        @Override
                        public void Positive(Dialog dialog) {
                            sharedPrefHelper.ClearSharedPreferences();
                            startActivity(new Intent(context, SplashScreen.class));
                            finish();
                        }

                        @Override
                        public void Negative(Dialog dialog) {
                            finish();
                        }
                    }
            );
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
//    @Override
//    public void onBackPressed() {
//        ExitDialog_AppLock.CreateExitDialog(context, new ExitDialogInterface() {
//            @Override
//            public void ExitFromApp() {
//                finish();
//            }
//
//            @Override
//            public void RateUs() {
//                new RateUs(context);
//            }
//        });
//    }
}