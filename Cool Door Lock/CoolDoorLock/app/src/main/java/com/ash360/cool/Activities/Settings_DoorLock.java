package com.ash360.cool.Activities;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ash360.cool.Adapters.Adapter_Settings;
import com.ash360.cool.Dialogs.ExitDialog_DoorLock;
import com.ash360.cool.Dialogs.FeedbackDialog_DoorLock;
import com.ash360.cool.Dialogs.FullScreenDialog_DoorLock;
import com.ash360.cool.Dialogs.GeneralDialog_DoorLock;
import com.ash360.cool.Interfaces.ExitInterface_DoorLock;
import com.ash360.cool.Interfaces.GeneralDialogInterface;
import com.ash360.cool.Interfaces.Settings_Row_interface;
import com.ash360.cool.Locks.DotLock;
import com.ash360.cool.Models.Settings_Item;
import com.ash360.cool.R;
import com.ash360.cool.Services.LockListenerService_DoorLock;
import com.ash360.cool.Utils.RateUs_DoorLock;
import com.ash360.cool.Utils.Shared_Pref_DoorLock;
import com.ash360.cool.databinding.ActivitySettingsDoorLockBinding;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import java.util.ArrayList;
import java.util.Objects;

import static com.ash360.cool.Utils.Constants_DoorLock.DOT_LOCK_MODE;
import static com.ash360.cool.Utils.Constants_DoorLock.ENABLE_LOCK;
import static com.ash360.cool.Utils.Constants_DoorLock.ENABLE_LOCK_DEFAULT_VAL;
import static com.ash360.cool.Utils.Constants_DoorLock.ENABLE_SOUND;
import static com.ash360.cool.Utils.Constants_DoorLock.ENABLE_SOUND_DEFAULT_VAL;
import static com.ash360.cool.Utils.Constants_DoorLock.HOW_TO_USE;
import static com.ash360.cool.Utils.Constants_DoorLock.IS_FIRST_RUN;
import static com.ash360.cool.Utils.Constants_DoorLock.IS_FIRST_RUN_DEFAULT_VALUE;
import static com.ash360.cool.Utils.Constants_DoorLock.IS_LOCK_SET;
import static com.ash360.cool.Utils.Constants_DoorLock.IS_LOCK_SET_DEFAULT_VALUE;
import static com.ash360.cool.Utils.Constants_DoorLock.LOCK_MATCH;
import static com.ash360.cool.Utils.Constants_DoorLock.LOCK_SETUP;
import static com.ash360.cool.Utils.Constants_DoorLock.PERMISSION_READ_PHONE;
import static com.ash360.cool.Utils.Constants_DoorLock.RATE_US;
import static com.ash360.cool.Utils.Constants_DoorLock.RESET_PASSWORD;
import static com.ash360.cool.Utils.Constants_DoorLock.SELECT_THEME;
import static com.ash360.cool.Utils.Constants_DoorLock.SETTINGS_ICON_DRAWABLES;
import static com.ash360.cool.Utils.Constants_DoorLock.SETTINGS_SW_VISIBILITY;
import static com.ash360.cool.Utils.Constants_DoorLock.SHOULD_SHOW_SECURE_LOCK_DIALOG;
import static com.ash360.cool.Utils.Constants_DoorLock.TELL_A_FRIEND;
import static com.ash360.cool.Utils.Constants_DoorLock.TIME_STYLE;
import static com.ash360.cool.Utils.Constants_DoorLock.TIME_STYLE_DEFAULT_VAL;

public class Settings_DoorLock extends AppCompatActivity {
    private final ActivityResultLauncher<Intent> lockSettings = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                }
            }
    );
    private Adapter_Settings adapter_settings;
    private ArrayList<Settings_Item> dataset;
    private ActivitySettingsDoorLockBinding binding;
    private Shared_Pref_DoorLock shared_pref_doorLock;
    private Context context;
    private final ActivityResultLauncher<Intent> setUpLock = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        shared_pref_doorLock.SetBool(IS_LOCK_SET, true);
                        shared_pref_doorLock.SetBool(ENABLE_LOCK, true);
                        checKSecureLock();
                    } else {
                        dataset.get(0).setChecked(false);
                        adapter_settings.notifyDataSetChanged();
                        shared_pref_doorLock.SetBool(ENABLE_LOCK, false);
                        shared_pref_doorLock.SetBool(IS_LOCK_SET, false);
                    }
                }
            });
    private final ActivityResultLauncher<Intent> matchLock = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        setUpLock.launch(new Intent(context, DotLock.class)
                                .putExtra(DOT_LOCK_MODE, LOCK_SETUP)
                                .putExtra(SHOULD_SHOW_SECURE_LOCK_DIALOG, false)
                        );
                    }
                }
            }
    );
    private final ActivityResultLauncher<Intent> openSettings = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult()
            , result -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.canDrawOverlays(context)) {
                        checkPhoneStateReadPermission();
                    } else {
                        checkDrawOverAppsPermission();
                    }
                } else {
                    shared_pref_doorLock.SetBool(ENABLE_LOCK, false);
                    dataset.get(0).setChecked(false);
                    adapter_settings.notifyDataSetChanged();
                }
            });
    private final ActivityResultLauncher<Intent> phoneStatePermResults = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (hasPermission(Manifest.permission.READ_PHONE_STATE)) {
                            CheckOrSetLock();
                        } else {
                            dataset.get(0).setChecked(false);
                            adapter_settings.notifyDataSetChanged();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsDoorLockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadBannerAdd();
        showCustomAd();

        context = Settings_DoorLock.this;
        shared_pref_doorLock = new Shared_Pref_DoorLock(context);

        // Add First Run Check
        boolean isFirstRun = shared_pref_doorLock.GetBool(IS_FIRST_RUN, IS_FIRST_RUN_DEFAULT_VALUE);
        if (isFirstRun) {
            startActivity(new Intent(context, IntroSliderActivity_LockScreen.class));
        }
        setRecyclerView();
    }

    private void showCustomAd() {
        startActivity(new Intent(Settings_DoorLock.this, AdActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean val = Settings.canDrawOverlays(context) && hasPermission(Manifest.permission.READ_PHONE_STATE);
            shared_pref_doorLock.SetBool(ENABLE_LOCK, val);
            dataset.get(0).setChecked(val);
            adapter_settings.notifyDataSetChanged();
        }
        if (shared_pref_doorLock.GetBool(ENABLE_LOCK, ENABLE_LOCK_DEFAULT_VAL)) {
            startNotification(getApplicationContext());
        } else {
            if (isServiceRunning()) {
                stopService(new Intent(context, LockListenerService_DoorLock.class));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode
            , @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            switch (requestCode) {
                case PERMISSION_READ_PHONE: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            CheckOrSetLock();
                        }
                    } else {
                        dataset.get(0).setChecked(false);
                        adapter_settings.notifyDataSetChanged();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                                showMessageOKCancel("You need to allow access to \"phone state read permission\" to proper working of this app",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                phoneStatePermResults.launch(intent);
                                            }
                                        }
                                        , new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                dataset.get(0).setChecked(false);
                                                adapter_settings.notifyDataSetChanged();
                                            }
                                        });
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        ExitDialog_DoorLock.CreateExitDialog(Settings_DoorLock.this, new ExitInterface_DoorLock() {
            @Override
            public void ExitFromApp() {
                finish();
            }

            @Override
            public void UpVote() {
                new RateUs_DoorLock(Settings_DoorLock.this);
            }

            @Override
            public void DownVote() {
                FeedbackDialog_DoorLock.CreateFeedbackDialog(context);
            }
        });
    }

    private void CheckOrSetLock() {
        boolean isLockSet = shared_pref_doorLock.GetBool(IS_LOCK_SET, IS_LOCK_SET_DEFAULT_VALUE);
        if (!isLockSet) {
            setUpLock.launch(new Intent(context, DotLock.class)
                    .putExtra(DOT_LOCK_MODE, LOCK_SETUP)
                    .putExtra(SHOULD_SHOW_SECURE_LOCK_DIALOG, true)
            );
        } else {
            checKSecureLock();
        }
    }

    private void startNotification(Context context) {
        if (!isServiceRunning()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(context, LockListenerService_DoorLock.class));
            } else {
                startService(new Intent(context, LockListenerService_DoorLock.class));
            }
        }
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (LockListenerService_DoorLock.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void setRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerViewSettings.setLayoutManager(linearLayoutManager);
        dataset = getList();
        adapter_settings = new Adapter_Settings(dataset, context, new Settings_Row_interface() {
            @Override
            public void OnItemSelection(int position, Settings_Item item) {
                RowClickHandler(position, item);
            }
        });
        binding.recyclerViewSettings.setAdapter(adapter_settings);
    }

    private boolean isLockSecure() {
        KeyguardManager km = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        return km.isKeyguardSecure();
    }

    private void RowClickHandler(int position, Settings_Item item) {
        switch (item.getTitle()) {
            case ENABLE_LOCK: {
                if (item.isChecked()) {
                    checkDrawOverAppsPermission();
                    boolean isLockSet = shared_pref_doorLock.GetBool(IS_LOCK_SET, IS_LOCK_SET_DEFAULT_VALUE);
                    if (isLockSet) {
                        shared_pref_doorLock.SetBool(ENABLE_LOCK, true);
                        startNotification(context);
                    }
                } else {
                    shared_pref_doorLock.SetBool(ENABLE_LOCK, false);
                    if (isServiceRunning()) {
                        stopService(new Intent(context, LockListenerService_DoorLock.class));
                    }
                }
                break;
            }
            case RESET_PASSWORD: {
                ResetPattern();
                break;
            }
            case SELECT_THEME: {
                startActivity(new Intent(context, DoorSelectionActivity.class));
                break;
            }
            case ENABLE_SOUND: {
                shared_pref_doorLock.SetBool(ENABLE_SOUND, item.isChecked());
                break;
            }
            case TIME_STYLE: {
                shared_pref_doorLock.SetBool(TIME_STYLE, item.isChecked());
                break;
            }
            case HOW_TO_USE: {
                startActivity(new Intent(context, IntroSliderActivity_LockScreen.class));
                break;
            }
            case RATE_US: {
                new RateUs_DoorLock(context);
                break;
            }
            case TELL_A_FRIEND: {
                startSharing();
                break;
            }
        }
    }

    private void startSharing() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String shareText = getResources().getString(R.string.initialText)
                + "\nhttps://play.google.com/store/apps/details?id="
                + Objects.requireNonNull(context).getPackageName();
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(intent, getString(R.string.share_subject)));
    }

    private void checKSecureLock() {
        if (isLockSecure()) {
            FullScreenDialog_DoorLock.CreateGeneralDialog(
                    context
                    , getApplicationContext().getResources().getString(R.string.full_screen_title)
                    , getApplicationContext().getResources().getString(R.string.full_screen_desc)
                    , getApplicationContext().getResources().getString(R.string.ok_disable_it)
                    , getApplicationContext().getResources().getString(R.string.no_keep_it)
                    , new GeneralDialogInterface() {
                        @Override
                        public void OnPositiveClick(Dialog dialog) {
                            Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                            lockSettings.launch(intent);
                        }

                        @Override
                        public void OnNegativeClick(Dialog dialog) {
                        }
                    }
            );
        }
    }

    private void ResetPattern() {
        if (shared_pref_doorLock.GetBool(IS_LOCK_SET, IS_LOCK_SET_DEFAULT_VALUE)) {
            matchLock.launch(new Intent(context, DotLock.class)
                    .putExtra(DOT_LOCK_MODE, LOCK_MATCH)
                    .putExtra(SHOULD_SHOW_SECURE_LOCK_DIALOG, false));
        } else {
            Toast.makeText(context, "Set a Lock First", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPhoneStateReadPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermission(Manifest.permission.READ_PHONE_STATE)) {
                GeneralDialog_DoorLock.CreateGeneralDialog(
                        context
                        , "Grant Read Phone State Permission"
                        , "This permission is required to handle lock behaviour on Incoming phone calls"
                        , "Allow"
                        , "Deny"
                        , new GeneralDialogInterface() {
                            @Override
                            public void OnPositiveClick(Dialog dialog) {
                                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_READ_PHONE);
                            }

                            @Override
                            public void OnNegativeClick(Dialog dialog) {
                                dataset.get(0).setChecked(false);
                                adapter_settings.notifyDataSetChanged();
                            }
                        }
                );
            } else {
                CheckOrSetLock();
            }
        }
    }

    private boolean hasPermission(String perm) {
        return ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED;
    }

    private void checkDrawOverAppsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                GeneralDialog_DoorLock.CreateGeneralDialog(
                        context
                        , "Grant Draw Over Apps Permission"
                        , "This permission will keep our app on top to mimic screen lock behaviour"
                        , "Allow"
                        , "Deny"
                        , new GeneralDialogInterface() {
                            @Override
                            public void OnPositiveClick(Dialog dialog) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                                openSettings.launch(intent);
                            }

                            @Override
                            public void OnNegativeClick(Dialog dialog) {
                                dataset.get(0).setChecked(false);
                                adapter_settings.notifyDataSetChanged();
                            }
                        }
                );
            } else {
                checkPhoneStateReadPermission();
            }
        }
    }

    private void showMessageOKCancel(String message
            , DialogInterface.OnClickListener okListener
            , DialogInterface.OnCancelListener cancelListener) {
        new AlertDialog.Builder(Settings_DoorLock.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private ArrayList<Settings_Item> getList() {
        ArrayList<Settings_Item> list = new ArrayList<>();

        list.add(new Settings_Item(ENABLE_LOCK
                , SETTINGS_SW_VISIBILITY[0]
                , SETTINGS_ICON_DRAWABLES[0]
                , shared_pref_doorLock.GetBool(ENABLE_LOCK, ENABLE_LOCK_DEFAULT_VAL)));
        list.add(new Settings_Item(RESET_PASSWORD
                , SETTINGS_SW_VISIBILITY[1]
                , SETTINGS_ICON_DRAWABLES[1]
                , false));
        list.add(new Settings_Item(SELECT_THEME
                , SETTINGS_SW_VISIBILITY[2]
                , SETTINGS_ICON_DRAWABLES[2]
                , false));
        list.add(new Settings_Item(ENABLE_SOUND
                , SETTINGS_SW_VISIBILITY[3]
                , SETTINGS_ICON_DRAWABLES[3]
                , shared_pref_doorLock.GetBool(ENABLE_SOUND, ENABLE_SOUND_DEFAULT_VAL)));
        list.add(new Settings_Item(TIME_STYLE
                , SETTINGS_SW_VISIBILITY[4]
                , SETTINGS_ICON_DRAWABLES[4]
                , shared_pref_doorLock.GetBool(TIME_STYLE, TIME_STYLE_DEFAULT_VAL)));
        list.add(new Settings_Item(HOW_TO_USE
                , SETTINGS_SW_VISIBILITY[5]
                , SETTINGS_ICON_DRAWABLES[5]
                , false));
        list.add(new Settings_Item(RATE_US
                , SETTINGS_SW_VISIBILITY[6]
                , SETTINGS_ICON_DRAWABLES[6]
                , false));
        list.add(new Settings_Item(TELL_A_FRIEND
                , SETTINGS_SW_VISIBILITY[7]
                , SETTINGS_ICON_DRAWABLES[7]
                , false));

        return list;
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