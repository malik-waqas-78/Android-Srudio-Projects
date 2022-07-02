package com.locker.applock.Activities;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.locker.applock.Adapters.AppListAdapter;
import com.locker.applock.Dialogs.GeneralDialog;
import com.locker.applock.Dialogs.LockMethodSelectionDialog;
import com.locker.applock.Dialogs.Secret_Image_Dialog;
import com.locker.applock.Interfaces.AppClickInterface;
import com.locker.applock.Interfaces.GeneralDialogInterface;
import com.locker.applock.Interfaces.LockSelectionInterface;
import com.locker.applock.LockActivities.PatternLockActivity;
import com.locker.applock.LockActivities.PinLockActivity;
import com.locker.applock.Models.AppModel;
import com.locker.applock.R;
import com.locker.applock.Services.AccessibilityHelperClass;
import com.locker.applock.Services.NotificationForegroundService;
import com.locker.applock.Utils.Constants;
import com.locker.applock.Utils.SharedPrefHelper;
import com.locker.applock.databinding.ActivityAppLockListBinding;
import com.screen.mirror.Utils.InterAdHelper_OKRA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.locker.applock.Utils.Constants.ACCESSIBILITY_MODE;
import static com.locker.applock.Utils.Constants.ACCESSIBILITY_SERVICE_PERMISSION;
import static com.locker.applock.Utils.Constants.DRAW_OVER_OTHER_APPS_PERMISSION;
import static com.locker.applock.Utils.Constants.IS_LOCK_SET;
import static com.locker.applock.Utils.Constants.IS_SECRET_QUESTION_SET;
import static com.locker.applock.Utils.Constants.LOCKED;
import static com.locker.applock.Utils.Constants.LOCK_MODE;
import static com.locker.applock.Utils.Constants.LOCK_TYPE;
import static com.locker.applock.Utils.Constants.PATTERN;
import static com.locker.applock.Utils.Constants.PERMISSION_CODE;
import static com.locker.applock.Utils.Constants.PIN;
import static com.locker.applock.Utils.Constants.SETUP_LOCK;
import static com.locker.applock.Utils.Constants.UNLOCKED;
import static com.locker.applock.Utils.Constants.USAGE_ACCESS_PERMISSION;

public class AppLockListActivity extends AppCompatActivity {
    private final String TAG = "AppLockListActivity";
    private ActivityAppLockListBinding binding;
    private Context context;
    private SharedPrefHelper sharedPrefHelper;
    private ArrayList<AppModel> mApps;
    private AppListAdapter adapter;
    private int currentPosition;
    private AppModel currentApp;
    private final ActivityResultLauncher<Intent> setUpLockLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    if (resultCode == RESULT_CANCELED) {
                        uncheckAndNotify();
                    } else {
                        LockAppAndStartService();
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
                        checkLockSetup();
                    } else {
                        uncheckAndNotify();
                    }
                }
            }
    );
    private final ActivityResultLauncher<Intent> getUsageAccessPermission = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (isUsageAccessPermissionGranted()) {
                        checkDrawOverAppsPermission();
                    } else {
                        uncheckAndNotify();
                    }
                }
            });

    private void LockAppAndStartService() {
        if (!sharedPrefHelper.Get_Boolean_AL(IS_SECRET_QUESTION_SET, false)) {
            Secret_Image_Dialog.create_secret_image_dialog_box(context,
                    getResources().getString(R.string.secret_image_title));
        }
        sharedPrefHelper.Set_Int_AL(currentApp.getPackageName(), LOCKED);
        mApps.get(currentPosition).setIsLocked(LOCKED);
        adapter.notifyDataSetChanged();

        if (sharedPrefHelper.Get_Boolean_AL(ACCESSIBILITY_MODE, false)) {
            if (isServiceRunning()) {
                stopService(new Intent(context, NotificationForegroundService.class));
            }
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            Log.v("TAG", "Accessibility Mode");
        } else {
            if (!isServiceRunning()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(new Intent(context, NotificationForegroundService.class));
                } else {
                    startService(new Intent(context, NotificationForegroundService.class));
                }
            }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppLockListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(InterAdHelper_OKRA.isAppInstalledFromPlay(AppLockListActivity.this)) {
            loadTopBanner();
        }

        context = AppLockListActivity.this;
        sharedPrefHelper = new SharedPrefHelper(context);

        setSupportActionBar(binding.toolbarAppLockList);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbarAppLockList.setNavigationOnClickListener(view -> {
            onBackPressed();
        });

        binding.searchAppEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterString(s.toString());

            }
        });

        new AsyncAppListLoader().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void filterString(String text) {
        ArrayList<AppModel> filteredList = new ArrayList<>();
        if (mApps != null) {
            for (AppModel app : mApps) {
                if (app.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(app);
                }
            }
        }
        if (!filteredList.isEmpty()) {
            adapter.filterList(filteredList);
        }
    }

    private ArrayList<AppModel> getModelList(PackageManager packageManager, List<PackageInfo> packages) {
        ArrayList<AppModel> apps = new ArrayList<>();
        for (PackageInfo pkg : packages) {
            if (!isSystemPackage(pkg) && !pkg.equals(getPackageName())) {
                apps.add(new AppModel(
                        (String) pkg.applicationInfo.loadLabel(packageManager)
                        , pkg.applicationInfo.packageName
                        , pkg.applicationInfo.loadIcon(packageManager)
                        , sharedPrefHelper.Get_Int_AL(pkg.packageName, Constants.UNLOCKED)
                ));
            }
        }
        return apps;
    }

    private boolean isExcludedPackage(PackageInfo pkg) {
        String packageName = pkg.packageName;
        String[] excludedPackages = new String[]{"calendar", "calculator", "camera", "gallery"
                , "chrome", "contacts", "drive", "maps", "gm", "mail", "files", "messag"
                , "photos", "youtube", "documents", "videos", "music"

        };
        boolean shouldExclude = false;
        for (String ex_pkg : excludedPackages) {
            shouldExclude |= packageName.contains(ex_pkg);
        }
        return shouldExclude;
    }

    private boolean isSystemPackage(PackageInfo pkg) {
        return ((pkg.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) && !isExcludedPackage(pkg);
    }

    private void setRecyclerView() {
        if (mApps != null && mApps.size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            binding.recyclerViewAppList.setLayoutManager(linearLayoutManager);
            adapter = new AppListAdapter(mApps, context, new AppClickInterface() {
                @Override
                public void OnClick(AppModel app, int adapterPosition) {
                    currentPosition = adapterPosition;
                    currentApp = app;
                    if (app.getIsLocked() == LOCKED) {
                        if (sharedPrefHelper.Get_Boolean_AL(ACCESSIBILITY_MODE, false)) {
                            checkAccessibilityAndLock();
                        } else {
                            lockApp();
                        }
                    } else {
                        sharedPrefHelper.Set_Int_AL(currentApp.getPackageName(), UNLOCKED);
                    }
                }
            });
            binding.recyclerViewAppList.setAdapter(adapter);
        }
    }

    private void checkAccessibilityAndLock() {
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
                            startActivity(new Intent(context, OverlayActivity.class).putExtra(PERMISSION_CODE, ACCESSIBILITY_SERVICE_PERMISSION));
                        }

                        @Override
                        public void Negative(Dialog dialog) {
                            uncheckAndNotify();
                        }
                    }
            );
        } else {
            if (sharedPrefHelper.Get_Boolean_AL(IS_LOCK_SET, false)) {
                sharedPrefHelper.Set_Int_AL(currentApp.getPackageName(), LOCKED);
            } else {
                setUpLock();
            }
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

    private void lockApp() {
        // Check Usage Access Permission
        if (isUsageAccessPermissionGranted()) {
            checkDrawOverAppsPermission();
        } else {
            // Show Dialog to show Usage Access Permission
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
                            uncheckAndNotify();
                        }
                    }
            );
        }
    }

    private void checkDrawOverAppsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
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
                                uncheckAndNotify();
                            }
                        });
            } else {
                checkLockSetup();
            }
        } else {
            checkLockSetup();
        }
    }

    private void checkLockSetup() {
        boolean isLockSetUp = sharedPrefHelper.Get_Boolean_AL(IS_LOCK_SET, false);
        if (isLockSetUp) {
            LockAppAndStartService();
        } else {
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
                setUpLockLauncher.launch(intent);
            }

            @Override
            public void Dismiss(Dialog dialog) {
                uncheckAndNotify();
            }
        });
    }

    private void uncheckAndNotify() {
        mApps.get(currentPosition).setIsLocked(UNLOCKED);
        adapter.notifyDataSetChanged();
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

    private ArrayList<AppModel> sortAppsByName(ArrayList<AppModel> appsList) {
        Collections.sort(appsList, new Comparator<AppModel>() {
            @Override
            public int compare(AppModel lhs, AppModel rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        return appsList;
    }

    private class AsyncAppListLoader extends AsyncTask<Void, Integer, ArrayList<AppModel>> {
        @Override
        protected ArrayList<AppModel> doInBackground(Void... voids) {
            final PackageManager packageManager = context.getPackageManager();
            List<PackageInfo> packages = packageManager.getInstalledPackages(
                    PackageManager.GET_META_DATA);
            return getModelList(packageManager, packages);
        }

        @Override
        protected void onPreExecute() {
            binding.recyclerViewAppList.setVisibility(View.INVISIBLE);
            binding.appsLoadingProgress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            binding.appsLoadingProgress.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<AppModel> appModels) {
            super.onPostExecute(appModels);
            binding.recyclerViewAppList.setVisibility(View.VISIBLE);
            binding.appsLoadingProgress.setVisibility(View.INVISIBLE);
            mApps = sortAppsByName(appModels);
            setRecyclerView();
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
}