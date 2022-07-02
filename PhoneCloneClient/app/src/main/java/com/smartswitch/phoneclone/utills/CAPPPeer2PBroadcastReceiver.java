package com.smartswitch.phoneclone.utills;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;


import com.smartswitch.phoneclone.interfaces.CAPPDirectCallBack;

import java.util.ArrayList;
import java.util.List;



public class CAPPPeer2PBroadcastReceiver extends BroadcastReceiver {

    public static IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        return intentFilter;
    }

    private static final String TAG = "92727";

    private WifiP2pManager mWifiP2pManager;

    private WifiP2pManager.Channel mChannel;

    private CAPPDirectCallBack mDirectActionListener;

    public CAPPPeer2PBroadcastReceiver(WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel, CAPPDirectCallBack directActionListener) {
        mWifiP2pManager = wifiP2pManager;
        mChannel = channel;
        mDirectActionListener = directActionListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                // Used to indicate Wifi P2P it's usable or not
                case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION: {
                    int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                    Log.d(TAG, "onReceive: state "+state);
                    if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                        mDirectActionListener.wifiP2pEnabled(true);
                    } else {
                        mDirectActionListener.wifiP2pEnabled(false);
                        List<WifiP2pDevice> wifiP2pDeviceList = new ArrayList<>();
                        mDirectActionListener.onPeersAvailable(wifiP2pDeviceList);
                    }
                    break;
                }
                //The list of peer nodes has changed
                case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION: {
                    if(mWifiP2pManager==null){
                        return;
                    }
                    Log.d(TAG, "onReceive: peers changed");
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        Log.d(TAG, "onReceive: no permission");
                        break;
                    }

                    mWifiP2pManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList peers) {
                            mDirectActionListener.onPeersAvailable(peers.getDeviceList());
                            Log.d(TAG, "onPeersAvailable: peers got");
                        }
                    });
                    break;
                }
                // Wifi P2P'S connection status has changed
                case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION: {
                    if(mWifiP2pManager==null){
                        return;
                    }
                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                    if (networkInfo != null && networkInfo.isConnected()) {
                        mWifiP2pManager.requestConnectionInfo(mChannel, info -> {
                            mDirectActionListener.onConnectionInfoAvailable(info);
                            Log.d(TAG, "onConnectionInfoAvailable: connection availabel");
                        });
                        Log.e(TAG, "P2p device connected");
                    } else {
                        mDirectActionListener.onDisconnection();
                        Log.e(TAG, "Disconnected from p2p device");
                    }
                    break;
                }
                //The device information of this device has changed
                case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION: {
                    WifiP2pDevice wifiP2pDevice = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
                    mDirectActionListener.onSelfDeviceAvailable(wifiP2pDevice);
                    break;
                }
            }
        }
    }

}