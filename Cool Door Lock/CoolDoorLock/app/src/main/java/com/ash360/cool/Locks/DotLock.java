package com.ash360.cool.Locks;

import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.ash360.cool.Dialogs.FullScreenDialog_DoorLock;
import com.ash360.cool.Dialogs.GeneralDialog_DoorLock;
import com.ash360.cool.Interfaces.GeneralDialogInterface;
import com.ash360.cool.R;
import com.ash360.cool.Utils.Shared_Pref_DoorLock;
import com.ash360.cool.databinding.ActivityDotLockBinding;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import static com.ash360.cool.Utils.Constants_DoorLock.DOT_LOCK_MODE;
import static com.ash360.cool.Utils.Constants_DoorLock.DOT_LOCK_PATTERN;
import static com.ash360.cool.Utils.Constants_DoorLock.IS_LOCK_SET;
import static com.ash360.cool.Utils.Constants_DoorLock.LOCK_MATCH;
import static com.ash360.cool.Utils.Constants_DoorLock.LOCK_SETUP;
import static com.ash360.cool.Utils.Constants_DoorLock.SHOULD_SHOW_SECURE_LOCK_DIALOG;

public class DotLock extends AppCompatActivity {
    private final ActivityResultLauncher<Intent> lockSettings = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    finish();
                }
            }
    );
    private ActivityDotLockBinding binding;
    private Context context;
    private Shared_Pref_DoorLock shared_pref_doorLock;
    private SeekBar[] seekbars;
    private int LOCK_MODE, SETUP_COUNT, FIRST_ATTEMPT = 9999;
    private boolean shouldShowSecureLockDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDotLockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadBannerAdd();

        context = DotLock.this;
        shared_pref_doorLock = new Shared_Pref_DoorLock(context);
        seekbars = new SeekBar[]{binding.seekBar1, binding.seekBar2, binding.seekBar3, binding.seekBar4};

        if (getIntent().hasExtra(DOT_LOCK_MODE)) {
            LOCK_MODE = getIntent().getIntExtra(DOT_LOCK_MODE, LOCK_SETUP);
            SETUP_COUNT = 0;
        }
        if (LOCK_MODE == LOCK_SETUP) {
            binding.dotLockTitle.setText("Please Enter Your New Pin");

        } else if (LOCK_MODE == LOCK_MATCH) {
            binding.dotLockTitle.setText("Please Enter Your Pin");
        }
        shouldShowSecureLockDialog = getIntent().getBooleanExtra(SHOULD_SHOW_SECURE_LOCK_DIALOG, false);
        initListeners();
    }
    private void initListeners() {

        binding.btnOk.setOnClickListener(view -> {
            int savedPattern = shared_pref_doorLock.GetInt(DOT_LOCK_PATTERN, 9999);
            if (LOCK_MODE == LOCK_SETUP) {
                if (SETUP_COUNT < 1) {
                    FIRST_ATTEMPT = getDotPin();
                    SETUP_COUNT += 1;
                    binding.dotLockTitle.setText("Please Confirm your pattern");
                } else {
                    if (FIRST_ATTEMPT == getDotPin()) {
                        shared_pref_doorLock.SetInt(DOT_LOCK_PATTERN, FIRST_ATTEMPT);
                        shared_pref_doorLock.GetBool(IS_LOCK_SET, true);
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        FIRST_ATTEMPT = 0;
                        SETUP_COUNT = 0;
                        binding.dotLockTitle.setText("Please Enter your pattern");
                        Toast.makeText(context, "Not Matched, Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (LOCK_MODE == LOCK_MATCH) {
                if (savedPattern == getDotPin()) {
                    setResult(RESULT_OK);
                } else {
                    setResult(RESULT_CANCELED);
                    Toast.makeText(context, "Not Matched, Try Again", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }

    private int getDotPin() {
        String lockValue = "";
        for (SeekBar seekBar : seekbars) {
            lockValue += String.valueOf(seekBar.getProgress() + 1);
            seekBar.setProgress(0);
        }
        return Integer.parseInt(lockValue);
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