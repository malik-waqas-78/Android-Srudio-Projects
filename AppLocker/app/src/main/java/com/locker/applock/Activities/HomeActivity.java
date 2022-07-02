package com.locker.applock.Activities;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.locker.applock.Dialogs.ExitDialog_AppLock;
import com.locker.applock.Dialogs.GeneralDialog;
import com.locker.applock.Dialogs.LockMethodSelectionDialog;
import com.locker.applock.Dialogs.Secret_Image_Dialog;
import com.locker.applock.Interfaces.ExitDialogInterface;
import com.locker.applock.Interfaces.GeneralDialogInterface;
import com.locker.applock.Interfaces.LockSelectionInterface;
import com.locker.applock.LockActivities.PatternLockActivity;
import com.locker.applock.LockActivities.PinLockActivity;
import com.locker.applock.R;
import com.locker.applock.Services.AccessibilityHelperClass;
import com.locker.applock.Services.NotificationForegroundService;
import com.locker.applock.Utils.RateUs;
import com.locker.applock.Utils.SharedPrefHelper;
import com.locker.applock.databinding.ActivityHomeBinding;
import com.screen.mirror.Utils.InterAdHelper_OKRA;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

import static com.locker.applock.Utils.Constants.ACCESSIBILITY_MODE;
import static com.locker.applock.Utils.Constants.ACCESSIBILITY_SERVICE_PERMISSION;
import static com.locker.applock.Utils.Constants.IS_LOCK_SET;
import static com.locker.applock.Utils.Constants.IS_SECRET_QUESTION_SET;
import static com.locker.applock.Utils.Constants.LOCK_MODE;
import static com.locker.applock.Utils.Constants.LOCK_TYPE;
import static com.locker.applock.Utils.Constants.PATTERN;
import static com.locker.applock.Utils.Constants.PERMISSION_CODE;
import static com.locker.applock.Utils.Constants.PIN;
import static com.locker.applock.Utils.Constants.SETUP_LOCK;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    Context context;
    SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(InterAdHelper_OKRA.isAppInstalledFromPlay(HomeActivity.this)) {
            loadTopBanner();
        }
        context = HomeActivity.this;
        sharedPrefHelper = new SharedPrefHelper(context);

        checkLockSetup();

        binding.btnAppLock.setOnClickListener(view -> {
            startActivity(new Intent(context, AppLockListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS));
        });

        binding.btnSettings.setOnClickListener(view -> {
            startActivity(new Intent(context, SettingsScreen.class).addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS));
        });

        binding.btnShare.setOnClickListener(view -> {
            shareIntent(HomeActivity.this);
        });

        binding.btnTheme.setOnClickListener(view -> {
            startActivity(new Intent(context, ThemeSelectionActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS));
        });
    }

    private void shareIntent(HomeActivity mActivity) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String shareText = getResources().getString(R.string.initialText)
                + "\nhttps://play.google.com/store/apps/details?id="
                + Objects.requireNonNull(context).getPackageName();
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(intent, getString(R.string.share_subject)));
    }

    private void checkLockSetup() {
        boolean isLockSetUp = sharedPrefHelper.Get_Boolean_AL(IS_LOCK_SET, false);
        if (!isLockSetUp) {
            setUpLock();
        }
    }

    private void setUpLock() {
        // Give Lock Option to user
        LockMethodSelectionDialog.createListSelectionDialog(context, new LockSelectionInterface() {
            @Override
            public void SelectLock(Dialog dialog, int radioId) {
                int selectedLock = (radioId == PATTERN) ? PATTERN : PIN;
                sharedPrefHelper.Set_Int_AL(LOCK_TYPE, selectedLock);
                Intent intent;
                if (selectedLock == PATTERN) {
                    intent = new Intent(context, PatternLockActivity.class).putExtra(LOCK_MODE, SETUP_LOCK);
                } else {
                    intent = new Intent(context, PinLockActivity.class).putExtra(LOCK_MODE, SETUP_LOCK);
                }
                startActivity(intent);
            }

            @Override
            public void Dismiss(Dialog dialog) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!sharedPrefHelper.Get_Boolean_AL(ACCESSIBILITY_MODE, false)) {
            if (isUsageAccessPermissionGranted()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(context)) {
                    if (!isServiceRunning()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(new Intent(context, NotificationForegroundService.class));
                        } else {
                            startService(new Intent(context, NotificationForegroundService.class));
                        }
                    }
                } else {
                    if (!isServiceRunning()) {
                        startService(new Intent(context, NotificationForegroundService.class));
                    }
                }
            }
        } else {
            if (isServiceRunning()) {
                stopService(new Intent(context, NotificationForegroundService.class));
            }
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            if (!isAccessibilitySettingsOn(context)) {
                GeneralDialog.CreateGeneralDialog(context
                        , getResources().getString(R.string.accessibility_dialog_title)
                        , getResources().getString(R.string.accessibility_dialog_desc)
                        , getResources().getString(R.string.yes)
                        , getResources().getString(R.string.no)
                        , new GeneralDialogInterface() {
                            @Override
                            public void Positive(Dialog dialog) {
                                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                                finish();
                                startActivity(new Intent(context, OverlayActivity.class).putExtra(PERMISSION_CODE, ACCESSIBILITY_SERVICE_PERMISSION));
                            }

                            @Override
                            public void Negative(Dialog dialog) {
                                Toasty.error(context, getResources().getString(R.string.provide_permissions), Toasty.LENGTH_LONG).show();
                            }
                        });
//            } else {
//                startActivity(new Intent(context, AccessibilityHelperClass.class));
            }
        }
        if (sharedPrefHelper.Get_Boolean_AL(IS_LOCK_SET, false)
                && !sharedPrefHelper.Get_Boolean_AL(IS_SECRET_QUESTION_SET, false)) {
            Secret_Image_Dialog.create_secret_image_dialog_box(context
                    , getResources().getString(R.string.secret_image_title));
        }

    }

    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + AccessibilityHelperClass.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            //Log.e(MyConstants.TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationForegroundService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isUsageAccessPermissionGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
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
    @Override
    public void onBackPressed() {


        if(InterAdHelper_OKRA.isAppInstalledFromPlay(HomeActivity.this)) {
            InterAdHelper_OKRA.showAdmobIntersitial(HomeActivity.this);
        }

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
}