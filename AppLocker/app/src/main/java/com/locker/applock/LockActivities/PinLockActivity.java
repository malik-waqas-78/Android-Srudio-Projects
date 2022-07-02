package com.locker.applock.LockActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
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
import com.locker.applock.databinding.PinLockActivityBinding;
import com.marcoscg.fingerauth.FingerAuth;
import com.mattprecious.swirl.SwirlView;
import com.screen.mirror.Utils.InterAdHelper_OKRA;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

import static com.locker.applock.Utils.Constants.IS_FINGERPRINT_SET;
import static com.locker.applock.Utils.Constants.IS_LOCK_CHANGE_MODE;
import static com.locker.applock.Utils.Constants.IS_LOCK_SET;
import static com.locker.applock.Utils.Constants.IS_PIN_SET_UP;
import static com.locker.applock.Utils.Constants.LOCK_MODE;
import static com.locker.applock.Utils.Constants.LOCK_TYPE;
import static com.locker.applock.Utils.Constants.MATCH_LOCK;
import static com.locker.applock.Utils.Constants.PATTERN;
import static com.locker.applock.Utils.Constants.PIN;
import static com.locker.applock.Utils.Constants.SAVED_PIN;
import static com.locker.applock.Utils.Constants.SELECTED_THEME;
import static com.locker.applock.Utils.Constants.SELECTED_THEME_DEFAULT_VALUE;
import static com.locker.applock.Utils.Constants.SETUP_DURING_LOCK;
import static com.locker.applock.Utils.Constants.SETUP_LOCK;


public class PinLockActivity extends AppCompatActivity {
    PinLockActivityBinding binding;
    SharedPrefHelper shared_pref_helper;
    Context context;
    int MODE, wrong_count = 0;
    boolean pinChangeMode;
    PinLockView pinLockView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = PinLockActivityBinding.inflate(getLayoutInflater());

        context = PinLockActivity.this;
        shared_pref_helper = new SharedPrefHelper(context);
        int background = shared_pref_helper.Get_Int_AL(SELECTED_THEME, SELECTED_THEME_DEFAULT_VALUE);
        binding.pinLockMainParent.setBackgroundResource(background);
        setContentView(binding.getRoot());
        if(InterAdHelper_OKRA.isAppInstalledFromPlay(PinLockActivity.this)) {
            loadTopBanner();
        }
        setFingerPrint();

//        loadBannerAdd();

        pinLockView = binding.pinLockView;
        pinLockView.attachIndicatorDots(binding.pinLockIndicatorDots);

        MODE = getIntent().getIntExtra(LOCK_MODE, SETUP_LOCK);
        pinChangeMode = getIntent().getBooleanExtra(IS_LOCK_CHANGE_MODE, false);

        if (MODE == SETUP_LOCK) {
            SetPin();
        } else if (MODE == MATCH_LOCK) {
            MatchPin();
        }

        binding.btnForgotPin.setOnClickListener(view -> {
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
                                    startActivity(new Intent(context, PinLockActivity.class)
                                            .putExtra(LOCK_MODE, SETUP_LOCK));
                                }
                            }, 200);
                            shared_pref_helper.Set_Boolean_AL(SETUP_DURING_LOCK, true);
                            finish();
                            overridePendingTransition(0, 0);
                        }
                    });
        });
    }

    private void setFingerPrint() {
        boolean isFingerPrintEnabled = shared_pref_helper.Get_Boolean_AL(IS_FINGERPRINT_SET, false);
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
        FingerAuth fingerAuth = new FingerAuth(context).setMaxFailedCount(3);
        fingerAuth.setOnFingerAuthListener(new FingerAuth.OnFingerAuthListener() {
            @Override
            public void onSuccess() {
                setResult(RESULT_OK, new Intent().putExtra(LOCK_TYPE, PIN));
                finish();
            }

            @Override
            public void onFailure() {
                YoYo.with(Techniques.Shake)
                        .duration(200)
                        .repeat(1)
                        .playOn(binding.fingerPrintSwirlView);
                Toasty.warning(context, "FingerPrint Failed", Toasty.LENGTH_SHORT, true).show();
            }

            @Override
            public void onError() {
                binding.fingerPrintSwirlView.setState(SwirlView.State.ERROR);
                Toasty.error(context, "FingerPrint Not Matched\n Enter your Pin", Toasty.LENGTH_SHORT, true).show();
            }
        });
    }

    private void MatchPin() {
        binding.enterPinText.setText(getResources().getString(R.string.enter_your_pin));
        String savedPin = shared_pref_helper.Get_String_AL(SAVED_PIN, "");
        if (savedPin != null && savedPin.length() != 0) {
            pinLockView.setPinLockListener(new PinLockListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onComplete(String pin) {
                    if (savedPin.equals(pin)) {
                        setResult(RESULT_OK, new Intent().putExtra(LOCK_TYPE, PIN));
                        finish();
                    } else if (pinChangeMode) {
                        setResult(RESULT_CANCELED, new Intent().putExtra(LOCK_TYPE, PIN));
                        finish();
                    } else {
                        if (wrong_count > 1) {
                            binding.btnForgotPin.setVisibility(View.VISIBLE);
                        }
                        YoYo.with(Techniques.Shake)
                                .duration(200)
                                .repeat(1)
                                .playOn(pinLockView);
                        wrong_count += 1;
                    }
                    pinLockView.resetPinLockView();
                }

                @Override
                public void onEmpty() {
                }

                @Override
                public void onPinChange(int pinLength, String intermediatePin) {
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

    public void SetPin() {
        if (shared_pref_helper.Get_Boolean_AL(IS_PIN_SET_UP, false)) {
            binding.enterPinText.setText(getResources().getString(R.string.enter_new_pin));
        } else {
            binding.enterPinText.setText(getResources().getString(R.string.set_a_pin));
        }
        pinLockView.setPinLockListener(new PinLockListener() {
            boolean first_time = true;
            String first_attempt = "";

            @Override
            public void onComplete(String pin) {
                if (first_time) {
                    first_attempt = pin;
                    first_time = false;
                    binding.enterPinText.setText(getResources().getString(R.string.re_enter_pin_to_confirm));
                    pinLockView.resetPinLockView();
                } else if (pin.equals(first_attempt)) {
                    shared_pref_helper.Set_String_AL(SAVED_PIN, pin);
                    pinLockView.resetPinLockView();
                    binding.enterPinText.setText(getResources().getString(R.string.enter_your_pin));
                    shared_pref_helper.Set_Boolean_AL(IS_PIN_SET_UP, true);
                    shared_pref_helper.Set_Boolean_AL(IS_LOCK_SET, true);
                    shared_pref_helper.Set_Int_AL(LOCK_TYPE, PIN);
                    Toast.makeText(context, getResources().getString(R.string.pin_set_success), Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, new Intent().putExtra(LOCK_TYPE, PIN));
                    finish();
                } else {
                    binding.enterPinText.setText(getResources().getString(R.string.enter_new_pin));
                    Toast.makeText(context, getResources().getString(R.string.pin_not_matched), Toast.LENGTH_SHORT).show();
                    first_attempt = "";
                    first_time = true;
                    pinLockView.resetPinLockView();
                }
            }

            @Override
            public void onEmpty() {
            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {
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
    @Override
    public void onBackPressed() {
        if (pinChangeMode) {
            setResult(RESULT_CANCELED, new Intent().putExtra(LOCK_TYPE, PATTERN));
            super.onBackPressed();
        } else if (MODE == MATCH_LOCK || (MODE == SETUP_LOCK && shared_pref_helper.Get_Boolean_AL(SETUP_DURING_LOCK, false))) {
            shared_pref_helper.Set_Boolean_AL(SETUP_DURING_LOCK, false);
            LaunchHome();
        } else {
            setResult(RESULT_CANCELED, new Intent().putExtra(LOCK_TYPE, PATTERN));
            super.onBackPressed();
        }
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
