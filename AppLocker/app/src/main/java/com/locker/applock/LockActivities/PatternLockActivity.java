package com.locker.applock.LockActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.locker.applock.Activities.ClearStorageActivity;
import com.locker.applock.Dialogs.Secret_Image_Dialog;
import com.locker.applock.Interfaces.Secret_Image_Interface;
import com.locker.applock.Models.Secret_Image_Item;
import com.locker.applock.R;
import com.locker.applock.Utils.SharedPrefHelper;
import com.locker.applock.databinding.PatternLockActivityBinding;
import com.marcoscg.fingerauth.FingerAuth;
import com.mattprecious.swirl.SwirlView;
import com.screen.mirror.Utils.InterAdHelper_OKRA;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.locker.applock.Utils.Constants.IS_FINGERPRINT_SET;
import static com.locker.applock.Utils.Constants.IS_LOCK_CHANGE_MODE;
import static com.locker.applock.Utils.Constants.IS_LOCK_SET;
import static com.locker.applock.Utils.Constants.IS_PATTERN_SET;
import static com.locker.applock.Utils.Constants.LOCK_MODE;
import static com.locker.applock.Utils.Constants.LOCK_TYPE;
import static com.locker.applock.Utils.Constants.MATCH_LOCK;
import static com.locker.applock.Utils.Constants.PATTERN;
import static com.locker.applock.Utils.Constants.SAVED_PATTERN;
import static com.locker.applock.Utils.Constants.SELECTED_THEME;
import static com.locker.applock.Utils.Constants.SELECTED_THEME_DEFAULT_VALUE;
import static com.locker.applock.Utils.Constants.SETUP_DURING_LOCK;
import static com.locker.applock.Utils.Constants.SETUP_LOCK;


public class PatternLockActivity extends AppCompatActivity {
    PatternLockActivityBinding binding;
    Context context;
    SharedPrefHelper sharedPrefHelper;
    int MODE, wrong_attempts = 0;
    boolean patternChangeMode;
    PatternLockView patternLockView;
    FingerAuth fingerAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = PatternLockActivityBinding.inflate(getLayoutInflater());

        context = PatternLockActivity.this;
        sharedPrefHelper = new SharedPrefHelper(context);
        int background = sharedPrefHelper.Get_Int_AL(SELECTED_THEME, SELECTED_THEME_DEFAULT_VALUE);
        binding.patternLockMainParent.setBackgroundResource(background);
        setContentView(binding.getRoot());
        if(InterAdHelper_OKRA.isAppInstalledFromPlay(PatternLockActivity.this)) {
            loadTopBanner();
        }
//        loadBannerAdd();

        patternLockView = binding.patternLockView;

        MODE = getIntent().getIntExtra(LOCK_MODE, SETUP_LOCK);
        patternChangeMode = getIntent().getBooleanExtra(IS_LOCK_CHANGE_MODE, false);

        if (MODE == SETUP_LOCK) {
            SetUpLock();
        } else if (MODE == MATCH_LOCK) {
            MatchLock();
        }

        binding.btnForgotPattern.setOnClickListener(view -> {
            Secret_Image_Dialog.create_forgot_image_dialog_box(context
                    , getApplicationContext().getString(R.string.secret_image_title)
                    , new Secret_Image_Interface() {
                        @Override
                        public void OnClick(ArrayList<Secret_Image_Item> mDataSet, int adapterPosition) {
                            // Do Nothing here
                        }

                        @Override
                        public void initiateResetLockActivity() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(context, PatternLockActivity.class)
                                            .putExtra(LOCK_MODE, SETUP_LOCK));
                                    sharedPrefHelper.Set_Boolean_AL(SETUP_DURING_LOCK, true);
                                }
                            }, 200);
                            finish();
                            overridePendingTransition(0, 0);
                        }
                    });
        });
    }

    private void setFingerPrint() {
        boolean isFingerPrintEnabled = sharedPrefHelper.Get_Boolean_AL(IS_FINGERPRINT_SET, false);
        if (isFingerPrintEnabled) {
            binding.fingerPrintSwirlView.setVisibility(View.VISIBLE);
            binding.fingerPrintSwirlView.setState(SwirlView.State.ON);
            if (MODE != SETUP_LOCK) {
                InitFingerprint();
            }
        } else {
            binding.fingerPrintSwirlView.setVisibility(View.INVISIBLE);
        }
    }

    private void InitFingerprint() {
        fingerAuth = new FingerAuth(context).setMaxFailedCount(3);
        fingerAuth.setOnFingerAuthListener(new FingerAuth.OnFingerAuthListener() {
            @Override
            public void onSuccess() {
                setResult(RESULT_OK, new Intent().putExtra(LOCK_TYPE, PATTERN));
                finish();
            }

            @Override
            public void onFailure() {
                YoYo.with(Techniques.Shake)
                        .duration(100)
                        .repeat(1)
                        .playOn(binding.fingerPrintSwirlView);
                Toasty.warning(context, "FingerPrint Failed", Toasty.LENGTH_SHORT, true).show();
            }

            @Override
            public void onError() {
                binding.fingerPrintSwirlView.setState(SwirlView.State.ERROR);
                Toasty.error(context, "FingerPrint Not Matched\n Draw your pattern", Toasty.LENGTH_SHORT, true).show();
            }
        });
    }


    private void MatchLock() {
        binding.drawPatternText.setText(getResources().getString(R.string.draw_your_pattern));
        String savedPattern = sharedPrefHelper.Get_String_AL(SAVED_PATTERN, "");
        if (savedPattern != null && savedPattern.length() > 0) {
            patternLockView.addPatternLockListener(new PatternLockViewListener() {
                @Override
                public void onStarted() {
                }

                @Override
                public void onProgress(List<PatternLockView.Dot> progressPattern) {
                }

                @Override
                public void onComplete(List<PatternLockView.Dot> pattern) {
                    String InputPattern = PatternLockUtils.patternToString(patternLockView, pattern);
                    if (InputPattern.equals(savedPattern)) {
                        setResult(RESULT_OK, new Intent().putExtra(LOCK_TYPE, PATTERN));
                        finish();
                    } else if (patternChangeMode) {
                        setResult(RESULT_CANCELED, new Intent().putExtra(LOCK_TYPE, PATTERN));
                        finish();
                    } else {
                        if (wrong_attempts > 1) {
                            binding.btnForgotPattern.setVisibility(View.VISIBLE);
                        }
                        YoYo.with(Techniques.Shake)
                                .duration(200)
                                .repeat(1)
                                .playOn(patternLockView);
                        wrong_attempts += 1;
                    }
                    patternLockView.clearPattern();
                }

                @Override
                public void onCleared() {

                }
            });
        }

    }

    private void LaunchHome() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
    }

    private void SetUpLock() {
        if (sharedPrefHelper.Get_Boolean_AL(IS_PATTERN_SET, false)) {
            binding.drawPatternText.setText(getResources().getString(R.string.draw_new_pattern));
        } else {
            binding.drawPatternText.setText(getResources().getString(R.string.draw_your_pattern));
        }
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            boolean first_time = true;
            String first_attempt = "";

            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                if (first_time) {
                    first_attempt = PatternLockUtils.patternToString(patternLockView, pattern);
                    if (first_attempt.length() < 4) {
                        Toast.makeText(context, getResources().getString(R.string.min_dots_error), Toast.LENGTH_SHORT).show();
                        first_attempt = "";
                        first_time = true;
                    } else {
                        first_time = false;
                        binding.drawPatternText.setText(getResources().getString(R.string.re_draw_to_confirm));
                    }
                    patternLockView.clearPattern();
                } else if (first_attempt.equals(PatternLockUtils
                        .patternToString(patternLockView, pattern))) {
                    sharedPrefHelper.Set_String_AL(SAVED_PATTERN, PatternLockUtils.patternToString(patternLockView, pattern));
                    patternLockView.clearPattern();
                    binding.drawPatternText.setText(getResources().getString(R.string.draw_your_pattern));
                    sharedPrefHelper.Set_Boolean_AL(IS_PATTERN_SET, true);
                    sharedPrefHelper.Set_Boolean_AL(IS_LOCK_SET, true);
                    sharedPrefHelper.Set_Int_AL(LOCK_TYPE, PATTERN);
                    Toast.makeText(context, getResources().getString(R.string.pattern_set), Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, new Intent().putExtra(LOCK_TYPE, PATTERN));
                    finish();
                } else {
                    Toast.makeText(context, getResources().getString(R.string.pattern_not_matched), Toast.LENGTH_SHORT).show();
                    binding.drawPatternText.setText(getResources().getString(R.string.draw_new_pattern));
                    first_attempt = "";
                    first_time = true;
                    patternLockView.clearPattern();
                }
            }

            @Override
            public void onCleared() {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setFingerPrint();
    }

    @Override
    protected void onPause() {
        if (fingerAuth != null) {
            fingerAuth.cancelSignal();
            fingerAuth = null;
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (patternChangeMode) {
            setResult(RESULT_CANCELED, new Intent().putExtra(LOCK_TYPE, PATTERN));
            super.onBackPressed();
        } else if (MODE == MATCH_LOCK || (MODE == SETUP_LOCK && sharedPrefHelper.Get_Boolean_AL(SETUP_DURING_LOCK, false))) {
            sharedPrefHelper.Set_Boolean_AL(SETUP_DURING_LOCK, false);
            LaunchHome();
        } else {
            setResult(RESULT_CANCELED, new Intent().putExtra(LOCK_TYPE, PATTERN));
            super.onBackPressed();
        }
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