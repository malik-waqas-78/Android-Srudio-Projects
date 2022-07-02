package com.locker.applock.Activities;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.locker.applock.Adapters.SettingsAdapter;
import com.locker.applock.Dialogs.GeneralDialog;
import com.locker.applock.Dialogs.LockMethodSelectionDialog;
import com.locker.applock.Dialogs.Secret_Image_Dialog;
import com.locker.applock.Interfaces.GeneralDialogInterface;
import com.locker.applock.Interfaces.LockSelectionInterface;
import com.locker.applock.Interfaces.SettingsRowInterface;
import com.locker.applock.LockActivities.PatternLockActivity;
import com.locker.applock.LockActivities.PinLockActivity;
import com.locker.applock.Models.SettingsModel;
import com.locker.applock.R;
import com.locker.applock.Services.AccessibilityHelperClass;
import com.locker.applock.Services.NotificationForegroundService;
import com.locker.applock.Utils.SharedPrefHelper;
import com.locker.applock.databinding.ActivitySettingsScreenBinding;
import com.screen.mirror.Utils.InterAdHelper_OKRA;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.locker.applock.Utils.Constants.ACCESSIBILITY_MODE;
import static com.locker.applock.Utils.Constants.ACCESSIBILITY_SERVICE_PERMISSION;
import static com.locker.applock.Utils.Constants.BATTERY_OPTIMIZATION_MODE;
import static com.locker.applock.Utils.Constants.CHANGE_LOCK_TYPE;
import static com.locker.applock.Utils.Constants.CHANGE_PASSWORD;
import static com.locker.applock.Utils.Constants.CHANGE_SECURITY_QUESTION;
import static com.locker.applock.Utils.Constants.DRAW_OVER_OTHER_APPS_PERMISSION;
import static com.locker.applock.Utils.Constants.ENABLE_APP_LOCK;
import static com.locker.applock.Utils.Constants.FINGERPRINT;
import static com.locker.applock.Utils.Constants.IS_FINGERPRINT_SET;
import static com.locker.applock.Utils.Constants.IS_LOCK_CHANGE_MODE;
import static com.locker.applock.Utils.Constants.IS_LOCK_ENABLED;
import static com.locker.applock.Utils.Constants.IS_LOCK_NEW_APPS_ENABLED;
import static com.locker.applock.Utils.Constants.IS_LOCK_SET;
import static com.locker.applock.Utils.Constants.IS_PATTERN_SET;
import static com.locker.applock.Utils.Constants.IS_PIN_SET_UP;
import static com.locker.applock.Utils.Constants.IS_SECRET_QUESTION_SET;
import static com.locker.applock.Utils.Constants.LOCK_MODE;
import static com.locker.applock.Utils.Constants.LOCK_NEW_APP;
import static com.locker.applock.Utils.Constants.LOCK_TYPE;
import static com.locker.applock.Utils.Constants.MATCH_LOCK;
import static com.locker.applock.Utils.Constants.PATTERN;
import static com.locker.applock.Utils.Constants.PERMISSION_CODE;
import static com.locker.applock.Utils.Constants.PIN;
import static com.locker.applock.Utils.Constants.SETUP_LOCK;
import static com.locker.applock.Utils.Constants.USAGE_ACCESS_PERMISSION;

public class SettingsScreen extends AppCompatActivity {
    private ActivitySettingsScreenBinding binding;
    private Context context;
    private final ActivityResultLauncher<Intent> changeLockLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult()
                    , new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            int resultCode = result.getResultCode();
                            if (resultCode == RESULT_CANCELED) {
                                Toast.makeText(context, "Not Matched, Try Again", Toast.LENGTH_SHORT).show();
                            } else if (resultCode == RESULT_OK) {
                                int lock_type = result.getData().getIntExtra(LOCK_TYPE, PATTERN);
                                Intent intent;
                                if (lock_type == PATTERN) {
                                    intent = new Intent(context, PatternLockActivity.class).putExtra(LOCK_MODE, SETUP_LOCK);
                                } else {
                                    intent = new Intent(context, PinLockActivity.class).putExtra(LOCK_MODE, SETUP_LOCK);
                                }
                                startActivity(intent);
                            }
                        }
                    }
            );
    private SharedPrefHelper sharedPrefHelper;
    private SettingsAdapter adapter;
    private ArrayList<SettingsModel> mSettingsList;
    private int adapterPosition;
    private final ActivityResultLauncher<Intent> getUsageAccessPermission = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (isUsageAccessPermissionGranted()) {
                        startServiceAndNotification();
                    } else {
                        CheckAndNotify();
                    }
                }
            });
    private final ActivityResultLauncher<Intent> getDrawOverAppsPermission = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (Settings.canDrawOverlays(context)) {
                        if (!isUsageAccessPermissionGranted()) {
                            showUsageDialogAndGetPermission();
                        } else {
                            startServiceAndNotification();
                        }
                    } else {
                        CheckAndNotify();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(InterAdHelper_OKRA.isAppInstalledFromPlay(SettingsScreen.this)) {
            loadTopBanner();
        }


        context = SettingsScreen.this;
        sharedPrefHelper = new SharedPrefHelper(context);

        setRecyclerView();

        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void setRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerViewSettings.setLayoutManager(linearLayoutManager);
        mSettingsList = getList();
        adapter = new SettingsAdapter(mSettingsList, context, new SettingsRowInterface() {
            @Override
            public void OnClick(int position) {
                adapterPosition = position;
                OnClickInSettingsRow();
            }
        });
        binding.recyclerViewSettings.setAdapter(adapter);
    }

    private void OnClickInSettingsRow() {
        SettingsModel currentItem = mSettingsList.get(adapterPosition);
        switch (currentItem.getTitle()) {
            case ENABLE_APP_LOCK: {
                if (!currentItem.isChecked()) {
                    if (isServiceRunning()) {
                        stopService(new Intent(context, NotificationForegroundService.class));
                    }
                } else {
                    startServiceAndNotification();
                }
                sharedPrefHelper.Set_Boolean_AL(IS_LOCK_ENABLED, currentItem.isChecked());
                break;
            }
            case CHANGE_PASSWORD: {
                if (sharedPrefHelper.Get_Boolean_AL(IS_LOCK_SET, false)) {
                    int lock_type = (sharedPrefHelper.Get_Int_AL(LOCK_TYPE, PATTERN) == PATTERN) ? PATTERN : PIN;
                    Intent intent;
                    if (lock_type == PATTERN) {
                        intent = new Intent(context, PatternLockActivity.class)
                                .putExtra(LOCK_MODE, MATCH_LOCK)
                                .putExtra(IS_LOCK_CHANGE_MODE, true);
                    } else {
                        intent = new Intent(context, PinLockActivity.class)
                                .putExtra(LOCK_MODE, MATCH_LOCK)
                                .putExtra(IS_LOCK_CHANGE_MODE, true);
                    }
                    changeLockLauncher.launch(intent);
                } else {
                    Toast.makeText(context, "Please Set Up A Password First", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case FINGERPRINT: {
                if (mSettingsList.get(adapterPosition).isChecked()) {
                    if (hasFingerprintSupport()) {
                        sharedPrefHelper.Set_Boolean_AL(IS_FINGERPRINT_SET, currentItem.isChecked());
                    } else {
                        mSettingsList.get(adapterPosition).setChecked(false);
                        Toasty.error(context
                                , getResources().getString(R.string.fingerprint_not_available)
                                , Toasty.LENGTH_SHORT).show();
                    }
                } else {
                    sharedPrefHelper.Set_Boolean_AL(IS_FINGERPRINT_SET, currentItem.isChecked());
                }
                break;
            }
            case LOCK_NEW_APP: {
                sharedPrefHelper.Set_Boolean_AL(IS_LOCK_NEW_APPS_ENABLED, currentItem.isChecked());
                break;
            }
            case CHANGE_LOCK_TYPE: {
                LockMethodSelectionDialog.createListSelectionDialog(context, new LockSelectionInterface() {
                    @Override
                    public void SelectLock(Dialog dialog, int radioId) {
                        int selectedLock = (radioId == PATTERN) ? PATTERN : PIN;
                        if (selectedLock == PATTERN) {
                            if (!sharedPrefHelper.Get_Boolean_AL(IS_PATTERN_SET, false)) {
                                startActivity(new Intent(context, PatternLockActivity.class)
                                        .putExtra(LOCK_MODE, SETUP_LOCK));
                            } else {
                                sharedPrefHelper.Set_Int_AL(LOCK_TYPE, PATTERN);
                                mSettingsList.get(adapterPosition).setDesc("Pattern");
                            }
                        } else {
                            if (!sharedPrefHelper.Get_Boolean_AL(IS_PIN_SET_UP, false)) {
                                startActivity(new Intent(context, PinLockActivity.class)
                                        .putExtra(LOCK_MODE, SETUP_LOCK));
                            } else {
                                sharedPrefHelper.Set_Int_AL(LOCK_TYPE, PIN);
                                mSettingsList.get(adapterPosition).setDesc("Pin");
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void Dismiss(Dialog dialog) {

                    }
                });
                break;
            }
            case CHANGE_SECURITY_QUESTION: {
                if (sharedPrefHelper.Get_Boolean_AL(IS_SECRET_QUESTION_SET, false)) {
                    Secret_Image_Dialog.create_change_secret_image_dialog_box(context
                            , context.getResources().getString(R.string.secret_image_title_for_change_new));
                } else {
                    Toast.makeText(context
                            , context.getResources().getString(R.string.no_secret_image_set)
                            , Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case BATTERY_OPTIMIZATION_MODE:
                if (currentItem.isChecked()) // Accessibility Mode
                {
                    CheckAndGetAccessibilityPermission();
                    // Ask for accessibility permission and do all other things in there
                } else { // Normal Mode
                    if (!isServiceRunning()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (Settings.canDrawOverlays(context)) {
                                if (isUsageAccessPermissionGranted()) {
                                    startServiceAndNotification();
                                } else {
                                    showUsageDialogAndGetPermission();
                                }
                            } else {
                                showDrawOverAppsDialogAndGetPermission();
                            }
                        } else {
                            if (isUsageAccessPermissionGranted()) {
                                startServiceAndNotification();
                            } else {
                                showUsageDialogAndGetPermission();
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showDrawOverAppsDialogAndGetPermission() {
        GeneralDialog.CreateGeneralDialog(context
                , getResources().getString(R.string.draw_over_apps_dialog_title)
                , getResources().getString(R.string.draw_over_apps_dialog_desc)
                , getResources().getString(R.string.yes)
                , getResources().getString(R.string.no)
                , new GeneralDialogInterface() {
                    @Override
                    public void Positive(Dialog dialog) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                        getDrawOverAppsPermission.launch(intent);
                        startActivity(new Intent(context, OverlayActivity.class).putExtra(PERMISSION_CODE, DRAW_OVER_OTHER_APPS_PERMISSION));
                    }

                    @Override
                    public void Negative(Dialog dialog) {
                        UncheckAndNotify();
                    }
                });
    }

    private void showUsageDialogAndGetPermission() {
        GeneralDialog.CreateGeneralDialog(context
                , getResources().getString(R.string.usage_access_dialog_title)
                , getResources().getString(R.string.usage_access_dialog_desc)
                , getResources().getString(R.string.yes)
                , getResources().getString(R.string.no)
                , new GeneralDialogInterface() {
                    @Override
                    public void Positive(Dialog dialog) {
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        getUsageAccessPermission.launch(intent);
                        startActivity(new Intent(context, OverlayActivity.class).putExtra(PERMISSION_CODE, USAGE_ACCESS_PERMISSION));
                    }

                    @Override
                    public void Negative(Dialog dialog) {
                        CheckAndNotify();
                    }
                }
        );
    }

    private void CheckAndNotify() {
        mSettingsList.get(adapterPosition).setChecked(true);
        adapter.notifyDataSetChanged();
        sharedPrefHelper.Set_Boolean_AL(ACCESSIBILITY_MODE, true);
    }

    private void UncheckAndNotify() {
        mSettingsList.get(adapterPosition).setChecked(false);
        adapter.notifyDataSetChanged();
        sharedPrefHelper.Set_Boolean_AL(ACCESSIBILITY_MODE, false);
    }

    private void startServiceAndNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(context, NotificationForegroundService.class));
        } else {
            startService(new Intent(context, NotificationForegroundService.class));
        }
        sharedPrefHelper.Set_Boolean_AL(ACCESSIBILITY_MODE, false);
    }

    private void CheckAndGetAccessibilityPermission() {
        if (!isAccessibilitySettingsOn(context)) { // First time if permission not granted
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
                            startActivity(new Intent(context, OverlayActivity.class)
                                    .putExtra(PERMISSION_CODE, ACCESSIBILITY_SERVICE_PERMISSION));
                        }

                        @Override
                        public void Negative(Dialog dialog) {
                            UncheckAndNotify();
                        }
                    }
            );
        } else { // Otherwise Just Stop Service, and Change Mode
            if (isServiceRunning()) {
                stopService(new Intent(context, NotificationForegroundService.class));
            }
            CheckAndNotify();
        }
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

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationForegroundService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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

    private boolean hasFingerprintSupport() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);
            // Check Device hardware
            if (fingerprintManager != null && fingerprintManager.isHardwareDetected()) {
                // Check USE_FINGERPRINT permission
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED) {
                    // Check if user has registered
                    if (fingerprintManager.hasEnrolledFingerprints()) {
                        // Check if keyguard secure
                        if (keyguardManager != null) {
                            return keyguardManager.isKeyguardSecure();
                        }
                    }
                }
            }
        }
        return false;
    }

    private ArrayList<SettingsModel> getList() {
        ArrayList<SettingsModel> settingsList = new ArrayList<>();
        settingsList.add(new SettingsModel(
                getApplicationContext().getResources().getString(R.string.settings_enable_app_lock)
                , getApplicationContext().getResources().getString(R.string.settings_enable_app_lock_desc)
                , R.drawable.ic_settings_enable_lock
                , VISIBLE
                , sharedPrefHelper.Get_Boolean_AL(IS_LOCK_ENABLED, true)
                , INVISIBLE
        ));

        settingsList.add(new SettingsModel(
                getApplicationContext().getResources().getString(R.string.settings_battery_optimize)
                , getApplicationContext().getResources().getString(R.string.settings_battery_optimize_desc)
                , R.drawable.ic_battery_optimization
                , VISIBLE
                , sharedPrefHelper.Get_Boolean_AL(ACCESSIBILITY_MODE, false)
                , INVISIBLE
        ));

        settingsList.add(new SettingsModel(
                getApplicationContext().getResources().getString(R.string.settings_change_password)
                , getApplicationContext().getResources().getString(R.string.settings_change_password_desc)
                , R.drawable.ic_settings_change_password
                , GONE
                , false
                , INVISIBLE
        ));

        settingsList.add(new SettingsModel(
                getApplicationContext().getResources().getString(R.string.settings_figerprint)
                , getApplicationContext().getResources().getString(R.string.settings_figerprint_desc)
                , R.drawable.ic_fingerprint
                , VISIBLE
                , sharedPrefHelper.Get_Boolean_AL(IS_FINGERPRINT_SET, false)
                , INVISIBLE
        ));

        settingsList.add(new SettingsModel(
                getApplicationContext().getResources().getString(R.string.settings_lock_the_new_app)
                , getApplicationContext().getResources().getString(R.string.settings_lock_the_new_app_desc)
                , R.drawable.ic_lock_new_app
                , VISIBLE
                , sharedPrefHelper.Get_Boolean_AL(IS_LOCK_NEW_APPS_ENABLED, true)
                , INVISIBLE
        ));

        int lock_type = sharedPrefHelper.Get_Int_AL(LOCK_TYPE, PATTERN);
        settingsList.add(new SettingsModel(
                getApplicationContext().getResources().getString(R.string.settings_change_lock_type)
                , ((lock_type == PATTERN) ? "Pattern" : "Pin")
                , R.drawable.ic_change_lock_type
                , GONE
                , false
                , VISIBLE
        ));

//        settingsList.add(new SettingsModel(
//                getApplicationContext().getResources().getString(R.string.settings_get_help)
//                , getApplicationContext().getResources().getString(R.string.settings_get_help_desc)
//                , R.drawable.ic_lock_new_app
//                , 0
//                , false
//        ));

        settingsList.add(new SettingsModel(
                getApplicationContext().getResources().getString(R.string.settings_change_security_question)
                , getApplicationContext().getResources().getString(R.string.settings_change_security_question_dec)
                , R.drawable.ic_change_security_question
                , GONE
                , false
                , INVISIBLE
        ));

        return settingsList;
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
}