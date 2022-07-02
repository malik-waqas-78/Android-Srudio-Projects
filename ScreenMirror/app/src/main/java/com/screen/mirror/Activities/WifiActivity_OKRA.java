package com.screen.mirror.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.screen.mirror.R;
import com.screen.mirror.Utils.InterAdHelper_OKRA;
import com.screen.mirror.Utils.SharedPrefHelperCA;
import com.screen.mirror.databinding.ActivityWifiBinding;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WifiActivity_OKRA extends AppCompatActivity {
    public static final int PERMISSIONS_Location_REQUEST_CODE = 3;
    public static final int REQUEST_WIFI_TURN_ON = 123;
    private static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 214;
    ActivityWifiBinding binding;
    Context context;
    SharedPrefHelperCA sharedPrefHelperSM;
    List<ScanResult> wifiList;
    WifiManager wifiManager;
    ArrayAdapter arrayAdapter;
    WifiStateBroadCastReceiver receiverWifi;
    SettingsClient settingsClient;
    LocationSettingsRequest locationSettingsRequest;
    String[] appLocationPermission = {Manifest.permission.ACCESS_FINE_LOCATION};
    ArrayList<String> ListPermissionNeeded = new ArrayList<>();
    private AdView adViewTop,adviewBottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWifiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //// adloading
        if(InterAdHelper_OKRA.isAppInstalledFromPlay(WifiActivity_OKRA.this)) {
            initSdkandLoadBottomBannerAd();
        }
        context = WifiActivity_OKRA.this;
        sharedPrefHelperSM = new SharedPrefHelperCA(context);

        setSupportActionBar(binding.toolbarWifiActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.wifiWaitingText.setText(getResources().getString(R.string.waiting_text));
        binding.connectWifiTextView.setText(getResources().getString(R.string.connect_wifi_text));

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        locationSettingsRequest = builder.build();
        settingsClient = LocationServices.getSettingsClient(WifiActivity_OKRA.this);

        binding.wifiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                connectToWifi(position);
            }
        });

        binding.connectWifiCardView.setOnClickListener(view -> {
            if (wifiManager.isWifiEnabled()) {
                binding.wifiImageView.setVisibility(View.INVISIBLE);
                binding.connectWifiCardView.setVisibility(View.INVISIBLE);
                binding.wifiListView.setVisibility(View.VISIBLE);
                binding.wifiWaitingText.setVisibility(View.INVISIBLE);
            } else {
                changeWifiState(true);
                binding.wifiImageView.setVisibility(View.VISIBLE);
                binding.connectWifiCardView.setVisibility(View.VISIBLE);
                binding.wifiListView.setVisibility(View.INVISIBLE);
                binding.wifiWaitingText.setVisibility(View.VISIBLE);
            }
        });
    }

    private void changeWifiState(boolean wifiState) {
        if (Build.VERSION.SDK_INT <= 28) {
            wifiManager.setWifiEnabled(true);
        } else {
            //turn wifi ON on android 29
            Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
            ((Activity) context).startActivityForResult(panelIntent, REQUEST_WIFI_TURN_ON);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        receiverWifi = new WifiStateBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(receiverWifi, intentFilter);

        getWifi();

        if (wifiManager.isWifiEnabled()) {
            binding.wifiImageView.setVisibility(View.INVISIBLE);
            binding.connectWifiCardView.setVisibility(View.INVISIBLE);
            binding.wifiListView.setVisibility(View.VISIBLE);
            binding.wifiWaitingText.setVisibility(View.INVISIBLE);
        } else {
            binding.wifiImageView.setVisibility(View.VISIBLE);
            binding.connectWifiCardView.setVisibility(View.VISIBLE);
            binding.wifiListView.setVisibility(View.INVISIBLE);
            binding.wifiWaitingText.setVisibility(View.VISIBLE);
        }
    }

    private void getWifi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context
                    , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(WifiActivity_OKRA.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
            } else {
                wifiManager.startScan();
            }
        } else {
            wifiManager.startScan();
        }
    }

    @Override
    protected void onPause() {
        if (receiverWifi != null) {
            unregisterReceiver(receiverWifi);
            receiverWifi = null;
        }
        super.onPause();
    }

    private void connectToWifi(int position) {
        // TODO: Check if already connected to a network,or password is saved
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.connect_to_wifi_custom_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView networkSSID = dialog.findViewById(R.id.textSSID);
        TextView btnConnect = dialog.findViewById(R.id.btn_connect);
        EditText pass = dialog.findViewById(R.id.et_network_password);
        ScanResult result = wifiList.get(position);
        String ssid = result.SSID;
        networkSSID.setText(ssid);
        // if button is clicked, connect to the network;
        btnConnect.setOnClickListener(v -> {
            String checkPassword = pass.getText().toString();
            finallyConnect(checkPassword, position);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void finallyConnect(String networkPassword, int position) {
        if (position >= 0 && position < wifiList.size()) {
            String networkSSID = wifiList.get(position).SSID;
            WifiConfiguration wifiConfig = new WifiConfiguration();
            wifiConfig.SSID = String.format("\"%s\"", networkSSID);
            wifiConfig.preSharedKey = String.format("\"%s\"", networkPassword);
            int netId = wifiManager.addNetwork(wifiConfig);
            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();
            WifiConfiguration config = new WifiConfiguration();
            config.SSID = "\"\"" + networkSSID + "\"\"";
            config.preSharedKey = "\"" + networkPassword + "\"";
            wifiManager.addNetwork(config);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    private class WifiStateBroadCastReceiver extends BroadcastReceiver {
        StringBuilder sb;

        public WifiStateBroadCastReceiver() {
            wifiList = new ArrayList<>();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
                sb = new StringBuilder();
                wifiList = wifiManager.getScanResults();
                ArrayList<String> deviceList = new ArrayList<>();
                for (ScanResult scanResult : wifiList) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        sb.append("\n").append(scanResult.SSID);
                    }
                    deviceList.add(scanResult.SSID);
                }
                arrayAdapter = new ArrayAdapter(context
                        , android.R.layout.simple_list_item_1
                        , deviceList.toArray());
                binding.wifiListView.setAdapter(arrayAdapter);
            }
        }
    }
    public void initSdkandLoadBottomBannerAd(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Map<String, AdapterStatus> map = initializationStatus.getAdapterStatusMap();
                for (Map.Entry<String,AdapterStatus> entry : map.entrySet()) {
                    String className = entry.getKey();
                    AdapterStatus status = entry.getValue();
                    if (status.getInitializationState().equals(AdapterStatus.State.NOT_READY)) {

                        // The adapter initialization did not complete.
                        Log.d("92727586243", "Adapter: " + className + " not ready.");
                    }

                    else if (status.getInitializationState().equals(AdapterStatus.State.READY))  {
                        // The adapter was successfully initialized.
                        Log.d("92727586243", "Adapter: " + className + " is initialized.");
                    }

                }
                loadTopBanner();

            }
        });
    }


    private void loadTopBanner() {
        adViewTop = new com.google.android.gms.ads.AdView(this);
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