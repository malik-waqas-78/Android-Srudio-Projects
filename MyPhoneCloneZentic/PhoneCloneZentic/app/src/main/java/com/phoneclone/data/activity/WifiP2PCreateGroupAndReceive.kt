package com.phoneclone.data.activity

import android.content.Intent
import android.net.Uri
import android.net.wifi.p2p.WifiP2pDevice
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

import com.facebook.ads.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError


import com.phoneclone.data.R
import com.phoneclone.data.ads.IntersitialAdHelper
import com.phoneclone.data.ads.isAppInstalledFromPlay
import com.phoneclone.data.constants.MyConstants

import com.phoneclone.data.interfaces.WifiP2PCallBacks
import com.phoneclone.data.utills.MyDialogues
import com.phoneclone.data.utills.MyPermissions
import com.phoneclone.data.utills.WifiP2PConnectionUtils
import com.phoneclone.data.utills.WifiUtilsClass


class WifiP2PCreateGroupAndReceive : AppCompatActivity(), WifiP2PCallBacks {
    var wifiP2PConnectioinUtils: WifiP2PConnectionUtils? = null

    var isRunning = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

         IntersitialAdHelper.adCallBack=object : IntersitialAdHelper.Companion.AdLoadCallBack{
             override fun adClosed() {
                 checkForpermissions()
                 android.os.Handler().postDelayed({

                     if (isRunning) {
                         findViewById<TextView>(R.id.tv_retry).visibility = View.VISIBLE
                         findViewById<TextView>(R.id.tv_retry).text =
                             "If you are not able to connect to this device please retry. "
                         findViewById<Button>(R.id.retry).visibility = View.VISIBLE
                         findViewById<Button>(R.id.retry).setOnClickListener {
                             startActivity(Intent(this@WifiP2PCreateGroupAndReceive, WifiP2PCreateGroupAndReceive::class.java))
                             finish()
                         }
                     }
                 }, 30000)
             }

         }



        if(isAppInstalledFromPlay(this)){
            IntersitialAdHelper.showAdmobIntersitial(this)
            loadFbBannerAdd()
            admobBanner()
        }




        MyConstants.FILES_TO_SHARE.clear()

    }

    override fun onResume() {
        super.onResume()
        wifiP2PConnectioinUtils?.resumed()
        isRunning=true
    }

    private fun checkForpermissions() {
        if (MyPermissions.hasLocationPermission(this@WifiP2PCreateGroupAndReceive)) {
            if (MyPermissions.hasStoragePermission(this@WifiP2PCreateGroupAndReceive)) {
                createGroupForDevice()
            } else {
                MyPermissions.showStorageExplanation(this@WifiP2PCreateGroupAndReceive,
                    object : MyPermissions.ExplanationCallBack {
                        override fun requestPermission() {
                            MyPermissions.requestStoragePermission(this@WifiP2PCreateGroupAndReceive)
                        }

                        override fun denyPermission() {
                            bothPermissionsDenied()
                        }

                    })
            }
        } else {
            MyPermissions.showLocationExplanation(this@WifiP2PCreateGroupAndReceive,
                object : MyPermissions.ExplanationCallBack {
                    override fun requestPermission() {
                        MyPermissions.requestLocationPermission(this@WifiP2PCreateGroupAndReceive)
                    }

                    override fun denyPermission() {
                        if (!MyPermissions.hasStoragePermission(this@WifiP2PCreateGroupAndReceive)) {
                            MyPermissions.showStorageExplanation(this@WifiP2PCreateGroupAndReceive,
                                object : MyPermissions.ExplanationCallBack {
                                    override fun requestPermission() {
                                        MyPermissions.requestStoragePermission(this@WifiP2PCreateGroupAndReceive)
                                    }

                                    override fun denyPermission() {
                                        bothPermissionsDenied()
                                    }

                                })
                        } else {
                            if (MyPermissions.hasLocationPermission(this@WifiP2PCreateGroupAndReceive)) {
                                createGroupForDevice()
                            } else {
                                bothPermissionsDenied()
                            }
                        }
                    }

                })
        }
    }

    fun createGroupForDevice() {
        if (WifiUtilsClass(this@WifiP2PCreateGroupAndReceive).getWifiState()) {
            if (MyPermissions(
                    applicationContext,
                    this@WifiP2PCreateGroupAndReceive
                ).CheckIfGPSisOn()
            ) {
                wifiP2PConnectioinUtils = WifiP2PConnectionUtils(this)
                //turn wifi on for connection on both sides
                // wifiP2PConnectioinClass?.turnWifiOn(true)
                wifiP2PConnectioinUtils?.groupcreator = true
                wifiP2PConnectioinUtils?.resumed()
                wifiP2PConnectioinUtils?.removeGroup()
                //wifiP2PConnectioinClass?.createGroup()
                wifiP2PConnectioinUtils?.discoverPeers()
            } else {
                MyPermissions(applicationContext, this@WifiP2PCreateGroupAndReceive).turnGpsOn()
            }
        } else {
            if (Build.VERSION.SDK_INT >= 29) {
                WifiUtilsClass(this@WifiP2PCreateGroupAndReceive).changeWifiState(true)
            } else {
                WifiUtilsClass(this@WifiP2PCreateGroupAndReceive).changeWifiState(true)
                if (MyPermissions(
                        applicationContext,
                        this@WifiP2PCreateGroupAndReceive
                    ).CheckIfGPSisOn()
                ) {
                    wifiP2PConnectioinUtils = WifiP2PConnectionUtils(this)
                    //turn wifi on for connection on both sides
                    // wifiP2PConnectioinClass?.turnWifiOn(true)
                    wifiP2PConnectioinUtils?.groupcreator = true
                    wifiP2PConnectioinUtils?.removeGroup()
                    //wifiP2PConnectioinClass?.createGroup()
                    wifiP2PConnectioinUtils?.discoverPeers()
                } else {
                    MyPermissions(applicationContext, this@WifiP2PCreateGroupAndReceive).turnGpsOn()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        isRunning=false
        wifiP2PConnectioinUtils?.paused()
        wifiP2PConnectioinUtils?.stopPeerDiscovery()
    }


    override fun onDestroy() {
        super.onDestroy()
        WifiP2PConnectionUtils.server?.close()
        WifiP2PConnectionUtils.socket?.close()
        WifiP2PConnectionUtils.clientSocket?.close()
        WifiP2PConnectionUtils.socket = null
        WifiP2PConnectionUtils.server = null
        WifiP2PConnectionUtils.clientSocket = null
        isRunning=false
        wifiP2PConnectioinUtils?.removeGroup()
    }

    override fun selfDeviceInfo(wifiP2pDevice: WifiP2pDevice) {

        findViewById<TextView>(R.id.tv_myDeviceName)?.text = wifiP2pDevice?.deviceName
        findViewById<TextView>(R.id.tv_myDeviceAddress)?.text = wifiP2pDevice?.deviceAddress
        findViewById<TextView>(R.id.tv_myDeviceStatus)?.text =
            getDeviceStatus(wifiP2pDevice?.status)

        /*    if(wifiP2pDevice?.deviceName.isNotEmpty()&&getDeviceStatus(wifiP2pDevice?.status).equals("Connected")){
               *//* val intent= Intent(this@WifiP2PReceiver!!,LaunchReceiveSocketConnection::class.java)
            startActivity(intent)*//*
        }*/
    }

    fun getDeviceStatus(deviceStatus: Int): String? {
        return when (deviceStatus) {
            WifiP2pDevice.AVAILABLE -> "Usable"
            WifiP2pDevice.INVITED -> "Inviting"
            WifiP2pDevice.CONNECTED -> "Connected"
            WifiP2pDevice.FAILED -> "Lose"
            WifiP2pDevice.UNAVAILABLE -> "Unavailable"
            else -> "Unknown"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MyConstants.FINISH_ACTIVITY_CODE) {
            WifiP2PConnectionUtils.server?.close()
            WifiP2PConnectionUtils.socket?.close()
            WifiP2PConnectionUtils.clientSocket?.close()
            WifiP2PConnectionUtils.socket = null
            WifiP2PConnectionUtils.server = null
            WifiP2PConnectionUtils.clientSocket = null
            SelectionActivity.selectedImagesList.clear()
            SelectionActivity.selectedContactsList.clear()
            SelectionActivity.selectedVideosList.clear()
            SelectionActivity.selectedApksList.clear()
            SelectionActivity.selectedCalendarEventsList.clear()
            SelectionActivity.imagesList.clear()
            SelectionActivity.contactsList.clear()
            SelectionActivity.videosList.clear()
            SelectionActivity.calendarEventsList.clear()
            SelectionActivity.apksList.clear()
            wifiP2PConnectioinUtils?.removeGroup()

            this.finish()
        } else if (requestCode == MyConstants.WIFI_TURN_ON) {
            if (WifiUtilsClass(this@WifiP2PCreateGroupAndReceive).getWifiState()) {
                if (MyPermissions(
                        applicationContext,
                        this@WifiP2PCreateGroupAndReceive
                    ).CheckIfGPSisOn()
                ) {
                    wifiP2PConnectioinUtils = WifiP2PConnectionUtils(this)
                    //turn wifi on for connection on both sides
                    // wifiP2PConnectioinClass?.turnWifiOn(true)
                    wifiP2PConnectioinUtils?.groupcreator = true
                    wifiP2PConnectioinUtils?.removeGroup()
                    //wifiP2PConnectioinClass?.createGroup()
                    wifiP2PConnectioinUtils?.discoverPeers()
                } else {
                    MyPermissions(applicationContext, this@WifiP2PCreateGroupAndReceive).turnGpsOn()
                }
            } else {
                if (!MyPermissions(
                        applicationContext,
                        this@WifiP2PCreateGroupAndReceive
                    ).CheckIfGPSisOn()
                ) {
                    MyPermissions(applicationContext, this@WifiP2PCreateGroupAndReceive).turnGpsOn()
                }
            }
        } else if (requestCode == MyConstants.REQUEST_LOCATION_TURNON) {
            if (WifiUtilsClass(this@WifiP2PCreateGroupAndReceive).getWifiState()) {
                if (MyPermissions(
                        applicationContext,
                        this@WifiP2PCreateGroupAndReceive
                    ).CheckIfGPSisOn()
                ) {
                    wifiP2PConnectioinUtils = WifiP2PConnectionUtils(this)
                    //turn wifi on for connection on both sides
                    // wifiP2PConnectioinClass?.turnWifiOn(true)
                    wifiP2PConnectioinUtils?.groupcreator = true
                    wifiP2PConnectioinUtils?.removeGroup()
                    //wifiP2PConnectioinClass?.createGroup()
                    wifiP2PConnectioinUtils?.discoverPeers()
                } else {
                    bothPermissionsDenied()
                }
            } else {
                bothPermissionsDenied()
            }
        } else if (requestCode == MyConstants.LOCATION_PERMISIION_REQUEST_CODE) {
            if (MyPermissions.hasLocationPermission(this@WifiP2PCreateGroupAndReceive)) {
                if (MyPermissions.hasStoragePermission(this@WifiP2PCreateGroupAndReceive)) {
                    createGroupForDevice()
                } else {
                    MyPermissions.showStorageExplanation(this@WifiP2PCreateGroupAndReceive,
                        object : MyPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                MyPermissions.requestStoragePermission(this@WifiP2PCreateGroupAndReceive)
                            }

                            override fun denyPermission() {
                                bothPermissionsDenied()
                            }

                        })
                }
            } else {
                if (!MyPermissions.hasStoragePermission(this@WifiP2PCreateGroupAndReceive)) {
                    MyPermissions.showStorageExplanation(this@WifiP2PCreateGroupAndReceive,
                        object : MyPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                MyPermissions.requestStoragePermission(this@WifiP2PCreateGroupAndReceive)
                            }

                            override fun denyPermission() {
                                bothPermissionsDenied()
                            }

                        })
                }
            }
        } else if (requestCode == MyConstants.SPRC) {
            if (!MyPermissions.hasStoragePermission(this@WifiP2PCreateGroupAndReceive)) {
                finish()
            } else {
                if (MyPermissions.hasLocationPermission(this@WifiP2PCreateGroupAndReceive)) {
                    createGroupForDevice()
                } else {
                    bothPermissionsDenied()
                }
            }
        }
    }

    override fun onBackPressed() {
        WifiP2PConnectionUtils.server?.close()
        WifiP2PConnectionUtils.socket?.close()
        WifiP2PConnectionUtils.clientSocket?.close()
        WifiP2PConnectionUtils.socket = null
        WifiP2PConnectionUtils.server = null
        WifiP2PConnectionUtils.clientSocket = null
        super.onBackPressed()

    }

    override fun peersAvailable(wifiP2pDeviceList: Collection<WifiP2pDevice>) {

    }

    override fun deviceInfo(name: String, address: String, status: Int) {

    }

    override fun deviceClicked(position: Int) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (permissions.isEmpty() || grantResults.isEmpty()) {
            return
        }


        if (requestCode == MyConstants.LOCATION_PERMISIION_REQUEST_CODE) {
            if (MyPermissions.hasLocationPermission(this@WifiP2PCreateGroupAndReceive)) {
                if (MyPermissions.hasStoragePermission(this@WifiP2PCreateGroupAndReceive)) {
                    createGroupForDevice()
                } else {
                    MyPermissions.showStorageExplanation(this@WifiP2PCreateGroupAndReceive,
                        object : MyPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                MyPermissions.requestStoragePermission(this@WifiP2PCreateGroupAndReceive)
                            }

                            override fun denyPermission() {
                                bothPermissionsDenied()
                            }

                        })
                }
            } else {

                if (Build.VERSION.SDK_INT >= 23) {
                    var showRational = shouldShowRequestPermissionRationale(permissions[0])
                    if (!showRational) {
                        MyPermissions.showLocationRational(this@WifiP2PCreateGroupAndReceive,
                            object : MyPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        MyConstants.LOCATION_PERMISIION_REQUEST_CODE
                                    )
                                }

                                override fun dismissed() {
                                    if (!MyPermissions.hasStoragePermission(this@WifiP2PCreateGroupAndReceive)) {
                                        MyPermissions.showStorageExplanation(this@WifiP2PCreateGroupAndReceive,
                                            object : MyPermissions.ExplanationCallBack {
                                                override fun requestPermission() {
                                                    MyPermissions.requestStoragePermission(this@WifiP2PCreateGroupAndReceive)
                                                }

                                                override fun denyPermission() {
                                                    bothPermissionsDenied()
                                                }

                                            })
                                    } else {
                                        bothPermissionsDenied()
                                    }
                                }

                            })
                    } else {
                        if (!MyPermissions.hasStoragePermission(this@WifiP2PCreateGroupAndReceive)) {
                            MyPermissions.showStorageExplanation(this@WifiP2PCreateGroupAndReceive,
                                object : MyPermissions.ExplanationCallBack {
                                    override fun requestPermission() {
                                        MyPermissions.requestStoragePermission(this@WifiP2PCreateGroupAndReceive)
                                    }

                                    override fun denyPermission() {
                                        bothPermissionsDenied()
                                    }

                                })
                        } else {
                            bothPermissionsDenied()
                        }
                    }
                } else {
                    if (!MyPermissions.hasStoragePermission(this@WifiP2PCreateGroupAndReceive)) {
                        MyPermissions.showStorageExplanation(this@WifiP2PCreateGroupAndReceive,
                            object : MyPermissions.ExplanationCallBack {
                                override fun requestPermission() {
                                    MyPermissions.requestStoragePermission(this@WifiP2PCreateGroupAndReceive)
                                }

                                override fun denyPermission() {
                                    bothPermissionsDenied()
                                }

                            })
                    } else {
                        bothPermissionsDenied()
                    }
                }
            }
        } else if (requestCode == MyConstants.SPRC) {
            if (MyPermissions.hasStoragePermission(this@WifiP2PCreateGroupAndReceive)) {
                if (MyPermissions.hasLocationPermission(this@WifiP2PCreateGroupAndReceive)) {
                    createGroupForDevice()
                } else {
                    bothPermissionsDenied()
                }
            } else {

                if (Build.VERSION.SDK_INT >= 23) {
                    //show rational
                    var showRational = shouldShowRequestPermissionRationale(permissions[0])
                    //user deind permission permannently
                    if (!showRational) {
                        MyPermissions.showStorageRational(this@WifiP2PCreateGroupAndReceive,
                            object : MyPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        MyConstants.SPRC
                                    )
                                }

                                override fun dismissed() {
                                    bothPermissionsDenied()
                                }

                            })
                    } else {
                        bothPermissionsDenied()
                    }
                } else {
                    //user deind permission
                    bothPermissionsDenied()
                }
            }
        }
    }

    fun bothPermissionsDenied() {
        MyDialogues.showPermissionsRequired(this@WifiP2PCreateGroupAndReceive,
            "This app needs following permissions in order to discover nearby devices and share data.\n1.Location permission\n2.Storage Permission",
            object : MyPermissions.PermissionsDeniedCallBack {
                override fun retryPermissions() {
                    startActivity(
                        Intent(
                            this@WifiP2PCreateGroupAndReceive,
                            WifiP2PCreateGroupAndReceive::class.java
                        )
                    )
                    finish()
                }

                override fun exitApp() {
                    finish()
                }

            })
    }

     fun loadFbBannerAdd() {

         val adView = AdView(
             this@WifiP2PCreateGroupAndReceive,
             this@WifiP2PCreateGroupAndReceive.resources.getString(R.string.banner_add),
             AdSize.BANNER_HEIGHT_50
         )

         val adListener: AdListener = object : AdListener {

             override fun onError(ad: Ad, adError: AdError) {
                 if (com.facebook.ads.BuildConfig.DEBUG) {
                      Toast.makeText(
                        this@WifiP2PCreateGroupAndReceive,
                        "Error: " + adError.errorMessage,
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }

            override fun onAdLoaded(ad: Ad) {
                // Ad loaded callback
            }

            override fun onAdClicked(ad: Ad) {
                // Ad clicked callback

            }

            override fun onLoggingImpression(ad: Ad) {
                // Ad impression logged callback
            }
        }


        adView?.loadAd(adView?.buildLoadAdConfig()?.withAdListener(adListener)?.build())
        findViewById<RelativeLayout>(R.id.bottom_banner).addView(adView)
    }

    fun admobBanner() {

        val mAdView = com.google.android.gms.ads.AdView(this@WifiP2PCreateGroupAndReceive)
        val adSize: com.google.android.gms.ads.AdSize = IntersitialAdHelper.getAdSize(this@WifiP2PCreateGroupAndReceive)
        mAdView.adSize = adSize

        mAdView.adUnitId = resources.getString(R.string.admob_banner)

        val adRequest = AdRequest.Builder().build()

        val adViewLayout = findViewById<View>(R.id.top_banner) as RelativeLayout
        adViewLayout.addView(mAdView)

        mAdView.loadAd(adRequest)

        mAdView.adListener = object : com.google.android.gms.ads.AdListener() {
            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
                adViewLayout.visibility = View.INVISIBLE
            }

            override fun onAdOpened() {
                super.onAdOpened()
                Log.d(MyConstants.TAG, "onAdOpened: ")
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                adViewLayout.visibility = View.VISIBLE
                Log.d(MyConstants.TAG, "onAdLoaded: ")
            }
        }

    }


}