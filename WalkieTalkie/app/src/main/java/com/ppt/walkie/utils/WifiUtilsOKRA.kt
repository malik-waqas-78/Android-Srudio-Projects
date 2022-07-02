package com.ppt.walkie.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings

class WifiUtilsOKRA {
    companion object{
        var wifiManager:WifiManager?=null
        fun changeWifiState(context:Context,wifiState: Boolean) {
            if(wifiManager ==null){
                wifiManager =context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            }
                if (Build.VERSION.SDK_INT <= 28) {
                    wifiManager?.isWifiEnabled = wifiState
                } else {
                    //turn wifi ON on android 29
                    val panelIntent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)

                    (context as Activity).startActivityForResult(panelIntent, ConstantsOKRA.REQUEST_WIFI_TURN_ON)

                }
        }

        // get if wifi is enabled
        fun isWifiEnabled(context: Context): Boolean {
            if(wifiManager ==null){
                wifiManager =context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            }
           wifiManager?.apply { return  isWifiEnabled }
            return false
        }
    }
}