package com.smartswitch.phoneclone.interfaces

import android.net.wifi.p2p.WifiP2pDevice

interface CAPPWifiP2PCallBacks {

    fun selfDeviceInfo(wifiP2pDevice: WifiP2pDevice)
    fun peersAvailable(wifiP2pDeviceList: Collection<WifiP2pDevice>)
    fun deviceInfo(name:String,address:String,status:Int)
    fun deviceClicked(position:Int)
}