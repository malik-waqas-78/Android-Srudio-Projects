package com.smartswitch.phoneclone.utills

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.wifi.*
import android.os.Build
import android.provider.Settings
import com.smartswitch.phoneclone.constants.CAPPMConstants


class CAPPWifiUtilsClass(var context: Context) {
    val wifiManager: WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
   // private lateinit var wifiHotspotInterface: WifiHotspotInterface
  /*  var mReservation: LocalOnlyHotspotReservation? = null*/
   /* private var oreoWifiMangerCallbacks: OreoWifiMangerCallBacks? = null*/

   /* constructor(context: Context) : this(context) {
        this.wifiHotspotInterface = wifiHotspotInterface
    }*/

    //turn wifi on
    fun changeWifiState(wifiState: Boolean) {
        if (Build.VERSION.SDK_INT <= 28) {
            wifiManager.isWifiEnabled = wifiState
        } else {
            //turn wifi ON on android 29
                val panelIntent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
            (context as Activity).startActivityForResult(panelIntent,CAPPMConstants.WIFI_TURN_ON)

        }
    }

    // get if wifi is enabled
    fun getWifiState(): Boolean {
        return wifiManager.isWifiEnabled
    }
/*
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    fun changeStateOnOreoAndAbove(): Boolean {

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return false
        }
        wifiManager.startLocalOnlyHotspot(
            @RequiresApi(Build.VERSION_CODES.O)
            object : LocalOnlyHotspotCallback() {
                override fun onStarted(reservation: LocalOnlyHotspotReservation) {
                    super.onStarted(reservation)
                    Log.d(TAG, "Wifi Hotspot is on now")
                    mReservation = reservation
                    // turnOffHotspot()

                }

                override fun onStopped() {
                    super.onStopped()
                    Log.d(TAG, "onStopped: ")
                }

                override fun onFailed(reason: Int) {
                    super.onFailed(reason)
                    Log.d(TAG, "onFailed: $reason")
                }
            }, Handler()
        )
        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun turnOffHotspot() {
        if (mReservation != null) {
            mReservation!!.close()
        }
    }

    fun changeWifiApState(wifiApState: Boolean): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            changeStateOnOreoAndAbove()
            false
        } else {
            changeWifiApStateBelowOreo(wifiApState)
        }
    }

    fun changeWifiApStateBelowOreo(wifiApState: Boolean): Boolean {
        val wifimanager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wificonfiguration: WifiConfiguration? = null
        try {
            val method: Method = wifimanager.javaClass.getMethod(
                "setWifiApEnabled",
                WifiConfiguration::class.java,
                Boolean::class.javaPrimitiveType
            )
            method.invoke(wifimanager, wificonfiguration, wifiApState)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getWifiApState(): Boolean {
        val wifimanager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        try {
            val method: Method = wifimanager.javaClass.getDeclaredMethod("isWifiApEnabled")
            method.setAccessible(true)
            val b = method.invoke(wifimanager) as Boolean
            Log.d(TAG, "getWifiApState: $b")
            return b
        } catch (ignored: Throwable) {
            Log.d(TAG, "getWifiApState: exception")
        }
        Log.d(TAG, "getWifiApState: ")
        return false
    }

    @SuppressLint("MissingPermission")
    fun turnOnHotspot(ssid: String, passWord: String) {
      if(Build.VERSION.SDK_INT<29){
          changeWifiState(false)
      }

         if (getWifiApState()) {
             changeWifiApState(false)
         }

        if (Build.VERSION.SDK_INT >= 26) {
            oreoWifiMangerCallbacks = OreoWifiMangerCallBacks()

            //only run it if location is on
            try {

                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                wifiManager!!.startLocalOnlyHotspot(oreoWifiMangerCallbacks, Handler())
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        } else {
            turnHotSpotOnbelowOreo(ssid, passWord)
            // changeWifiHotspotState(mContext!!,true)
        }
    }

    private fun turnHotSpotOnbelowOreo(ssid: String, passWord: String): Boolean {
        val mMethods: Array<Method> = wifiManager!!.javaClass.declaredMethods
        for (mMethod in mMethods) {
            if (mMethod.getName().equals("setWifiApEnabled")) {

                val netConfig = WifiConfiguration()
                if (passWord === "") {
                    netConfig.SSID = ssid
                    netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN)
                    netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN)
                    netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA)
                    netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
                } else {
                    netConfig.SSID = ssid
                    netConfig.preSharedKey = passWord

                    netConfig.status = WifiConfiguration.Status.ENABLED
                    netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
                    netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
                    netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
                    netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
                    netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
                    netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN)
                    netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA)
                }
                try {
                    mMethod.invoke(wifiManager, netConfig, true)
                 //   wifiHotspotInterface?.getHotspotInfo(ssid!!, passWord)
                    Log.d("92727", "setHotSpot: $ssid $passWord")
                    return true
                } catch (e: Exception) {
                }
            }
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    inner class OreoWifiMangerCallBacks : WifiManager.LocalOnlyHotspotCallback() {

        override fun onStarted(reservation: LocalOnlyHotspotReservation?) {
            super.onStarted(reservation)

            mReservation = reservation
            val configuration = mReservation!!.wifiConfiguration

            if (configuration != null) {
                Log.d(
                    "91717", "THE PASSWORD IS: "
                            + configuration.preSharedKey
                            + " \n SSID is : "
                            + configuration.SSID
                )
            }
          //  wifiHotspotInterface?.getHotspotInfo(configuration!!.SSID, configuration.preSharedKey)
        }

        override fun onStopped() {
            super.onStopped()
            Log.d("92727", "onStopped: ")
        }

        override fun onFailed(reason: Int) {
            super.onFailed(reason)
            Log.d("92727", "onFailed: ")
        }
    }

    fun turnHotSpotOfFromReservation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mReservation?.close()
        } else {
            changeWifiApState(false)
        }
    }


    fun getHotspotsList(): List<ScanResult?>? {
        if (wifiManager.isWifiEnabled) {
            if (wifiManager.startScan()) {
                return wifiManager.scanResults
            }
        } else {
            changeWifiState(true)
            if (wifiManager.startScan()) {
                return wifiManager.scanResults
            }
        }
        return null
    }

    fun getWifiManger(): WifiManager {
        return wifiManager
    }

    fun startScanning() {
        wifiManager.startScan()
    }

     fun connectToHotspotNetwork(SSID: String, PASSWORD: String) {

        if (Build.VERSION.SDK_INT >= 29) {//for android 10 and above
            val networkspecifeir = WifiNetworkSpecifier.Builder()
            networkspecifeir.setSsid(SSID)
            networkspecifeir.setWpa2Passphrase(PASSWORD)
            val networkRequest: NetworkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .setNetworkSpecifier(networkspecifeir.build())
                .build()

          val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

            connectivityManager!!.requestNetwork(networkRequest, NetworkCallBacks())

            *//* wifiManager!!.isWifiEnabled = false
            val networkspecifeir = WifiNetworkSuggestion.Builder()
            networkspecifeir.setSsid(SSID)
            networkspecifeir.setWpa2Passphrase(PASSWORD)
            val sugestions= mutableListOf<WifiNetworkSuggestion>()
            sugestions.add(networkspecifeir.build())
            val status = wifiManager.addNetworkSuggestions(sugestions)

            if (status !== WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
                // Error handling
                Log.d(TAG, "connectToHotspotNetwork: error  $status")
            }

            val intentFilter =
                IntentFilter(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION)

            val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.action == WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION) {
                        launchStartSocketConnection()
                    }else{
                        Log.d(TAG, "connectToHotspotNetwork: error  $status")
                        //error occurred
                    }

                }
            }
            context.applicationContext.registerReceiver(broadcastReceiver, intentFilter)*//*

        } else {//for android 28 and less

            val conf = WifiConfiguration()
            conf.SSID = "\"" + SSID + "\""
            conf.preSharedKey = "\"" + PASSWORD + "\"";
            wifiManager!!.addNetwork(conf)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }

            if (wifiManager!!.configuredNetworks != null) {
                val list: List<WifiConfiguration>? = wifiManager?.getConfiguredNetworks()
                for (i in list!!) {
                    if (i.SSID != null && i.SSID == "\"" + SSID.toString() + "\"") {
                        wifiManager!!.disconnect()
                        wifiManager!!.enableNetwork(i.networkId, true)
                        wifiManager!!.reconnect()
                        launchStartSocketConnection()
//                        val intent = Intent();
//                        intent.putExtra("ssid",SSID)
//                        intent.putExtra("pass",PASSWORD)
//                        codeScanner!!.releaseResources()
//                        setResult(RESULT_OK,intent)
                        Log.d("92772", "onAvailable: 1")

                        break
                    }
                }
            }

        }

    }

    fun getConnectedNetworkInfo():WifiInfo {
        return wifiManager.connectionInfo
    }

    fun disconnect(ssid: String, networkId: Int) {
        wifiManager!!.disconnect()
        wifiManager!!.enableNetwork(networkId, true)
        wifiManager!!.reconnect()
    }
    fun launchStartSocketConnection(){
        Handler().postDelayed(Runnable {
            val intent = Intent(context, LaunchSendData::class.java)
            context.startActivity(intent)
            var activity = context as Activity
            activity.finish()
        }, 15 * 1000)
    }
    inner class NetworkCallBacks : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.d("92727", "onAvailable: connected ${network.toString()}")
            launchStartSocketConnection()
//            runOnUiThread {
//                Toast.makeText(this@ConnectToHotspot, "connected", Toast.LENGTH_SHORT).show()
//                //hide scanner view
//                //scannerView!!.visibility= View.GONE
//                //show the next view
//                val intent = Intent();
//                intent.putExtra("ssid", ssid)
//                intent.putExtra("pass", password)
//                codeScanner!!.releaseResources()
//                setResult(RESULT_OK, intent)
//                Log.d("92772", "onAvailable: 1")
//                finish()
//                Log.d("92727", "onAvailable: 2")
//            }
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
        }

        override fun onUnavailable() {
            super.onUnavailable()
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
        }

        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties)
        }

        override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
            super.onBlockedStatusChanged(network, blocked)
        }
    }*/
}


