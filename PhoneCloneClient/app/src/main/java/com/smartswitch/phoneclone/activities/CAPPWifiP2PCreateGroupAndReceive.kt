package com.smartswitch.phoneclone.activities

import android.content.Intent
import android.net.Uri
import android.net.wifi.p2p.WifiP2pDevice
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.airbnb.lottie.LottieAnimationView


import com.smartswitch.phoneclone.R
//import com.switchphone.transferdata.ads.AATIntersitialAdHelper
//import com.switchphone.transferdata.ads.AATNativeAdHelper
import com.smartswitch.phoneclone.constants.CAPPMConstants

import com.smartswitch.phoneclone.interfaces.CAPPWifiP2PCallBacks
import com.smartswitch.phoneclone.utills.CAPPMDialogues
import com.smartswitch.phoneclone.utills.CAPPMPermissions
import com.smartswitch.phoneclone.utills.CAPPWifiPeer2PConnectionUtils
import com.smartswitch.phoneclone.utills.CAPPWifiUtilsClass


class CAPPWifiP2PCreateGroupAndReceive : AppCompatActivity(), CAPPWifiP2PCallBacks {
    var HSWifiP2PConnectioinUtils: CAPPWifiPeer2PConnectionUtils? = null

    var toolbar: Toolbar?=null
    var isRunning = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group_capp)


        /*checkForpermissions()
        android.os.Handler().postDelayed({

            if (isRunning) {
                findViewById<TextView>(R.id.tv_retry).visibility = View.VISIBLE
                findViewById<TextView>(R.id.tv_retry).text =
                    "If you are not able to connect to this device please retry. "
                findViewById<Button>(R.id.retry).visibility = View.VISIBLE
                findViewById<Button>(R.id.retry).setOnClickListener {
                    startActivity(
                        Intent(
                            this@AATWifiP2PCreateGroupAndReceive,
                            AATWifiP2PCreateGroupAndReceive::class.java
                        )
                    )
                    finish()
                }
            }
        }, 30000)
        AATMConstants.FILES_TO_SHARE.clear()*/

        /* HSIntersitialAdHelper.showAdd()

          loadFbBannerAdd()
          admobBanner()*/
//        AATNativeAdHelper.showAdmobNativeAd(this, findViewById(R.id.ad_frame))

//        init()

//        if (AATIntersitialAdHelper.isAppInstalledFromPlay(this)) {
//            AATIntersitialAdHelper.showAdmobIntersitial_mediated(
//                this@AATWifiP2PCreateGroupAndReceive,
//                object : AATIntersitialAdHelper.Companion.AdLoadCallBack {
//                    override fun adClosed() {
//                        init()
//                    }
//                })
//        } else {
            init()

            toolbar?.setNavigationIcon(R.drawable.ic_group_back_white)
            toolbar?.setNavigationOnClickListener(View.OnClickListener {
                onBackPressed()
            })

//        }

    }

    fun init() {
        toolbar=findViewById(R.id.toolbar)


        checkForpermissions()
        android.os.Handler().postDelayed({

            if (isRunning) {
//                findViewById<TextView>(R.id.tv_retry).visibility = View.VISIBLE
//                findViewById<TextView>(R.id.tv_retry).text =
//                    "If you are not able to connect to this device please retry. "
                findViewById<Button>(R.id.retry).visibility = View.VISIBLE
                findViewById<LottieAnimationView>(R.id.animationView).visibility=View.INVISIBLE
                findViewById<Button>(R.id.retry).setOnClickListener {
                    startActivity(
                        Intent(
                            this@CAPPWifiP2PCreateGroupAndReceive,
                            CAPPWifiP2PCreateGroupAndReceive::class.java
                        )
                    )
                    finish()
                }
            }
        }, 30000)
        CAPPMConstants.FILES_TO_SHARE.clear()
    }

    override fun onResume() {
        super.onResume()
        HSWifiP2PConnectioinUtils?.resumed()
        isRunning = true
    }

    private fun checkForpermissions() {
        if (CAPPMPermissions.hasLocationPermission(this@CAPPWifiP2PCreateGroupAndReceive)) {
            if (CAPPMPermissions.hasStoragePermission(this@CAPPWifiP2PCreateGroupAndReceive)) {
                createGroupForDevice()
            } else {
                CAPPMPermissions.showStorageExplanation(this@CAPPWifiP2PCreateGroupAndReceive,
                    object : CAPPMPermissions.ExplanationCallBack {
                        override fun requestPermission() {
                            CAPPMPermissions.requestStoragePermission(this@CAPPWifiP2PCreateGroupAndReceive)
                        }

                        override fun denyPermission() {
                            bothPermissionsDenied()
                        }

                    })
            }
        } else {
            CAPPMPermissions.showLocationExplanation(this@CAPPWifiP2PCreateGroupAndReceive,
                object : CAPPMPermissions.ExplanationCallBack {
                    override fun requestPermission() {
                        CAPPMPermissions.requestLocationPermission(this@CAPPWifiP2PCreateGroupAndReceive)
                    }

                    override fun denyPermission() {
                        if (!CAPPMPermissions.hasStoragePermission(this@CAPPWifiP2PCreateGroupAndReceive)) {
                            CAPPMPermissions.showStorageExplanation(this@CAPPWifiP2PCreateGroupAndReceive,
                                object : CAPPMPermissions.ExplanationCallBack {
                                    override fun requestPermission() {
                                        CAPPMPermissions.requestStoragePermission(this@CAPPWifiP2PCreateGroupAndReceive)
                                    }

                                    override fun denyPermission() {
                                        bothPermissionsDenied()
                                    }

                                })
                        } else {
                            if (CAPPMPermissions.hasLocationPermission(this@CAPPWifiP2PCreateGroupAndReceive)) {
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
        if (CAPPWifiUtilsClass(this@CAPPWifiP2PCreateGroupAndReceive).getWifiState()) {
            if (CAPPMPermissions(
                    applicationContext,
                    this@CAPPWifiP2PCreateGroupAndReceive
                ).CheckIfGPSisOn()
            ) {
                HSWifiP2PConnectioinUtils = CAPPWifiPeer2PConnectionUtils(this)
                //turn wifi on for connection on both sides
                // wifiP2PConnectioinClass?.turnWifiOn(true)
                HSWifiP2PConnectioinUtils?.groupcreator = true
                HSWifiP2PConnectioinUtils?.resumed()
                HSWifiP2PConnectioinUtils?.removeGroup()
                //wifiP2PConnectioinClass?.createGroup()
                HSWifiP2PConnectioinUtils?.discoverPeers()
            } else {
                CAPPMPermissions(
                    applicationContext,
                    this@CAPPWifiP2PCreateGroupAndReceive
                ).turnGpsOn()
            }
        } else {
            if (Build.VERSION.SDK_INT >= 29) {
                CAPPWifiUtilsClass(this@CAPPWifiP2PCreateGroupAndReceive).changeWifiState(true)
            } else {
                CAPPWifiUtilsClass(this@CAPPWifiP2PCreateGroupAndReceive).changeWifiState(true)
                if (CAPPMPermissions(
                        applicationContext,
                        this@CAPPWifiP2PCreateGroupAndReceive
                    ).CheckIfGPSisOn()
                ) {
                    HSWifiP2PConnectioinUtils = CAPPWifiPeer2PConnectionUtils(this)
                    //turn wifi on for connection on both sides
                    // wifiP2PConnectioinClass?.turnWifiOn(true)
                    HSWifiP2PConnectioinUtils?.groupcreator = true
                    HSWifiP2PConnectioinUtils?.removeGroup()
                    //wifiP2PConnectioinClass?.createGroup()
                    HSWifiP2PConnectioinUtils?.discoverPeers()
                } else {
                    CAPPMPermissions(
                        applicationContext,
                        this@CAPPWifiP2PCreateGroupAndReceive
                    ).turnGpsOn()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        isRunning = false
        HSWifiP2PConnectioinUtils?.paused()
        HSWifiP2PConnectioinUtils?.stopPeerDiscovery()
    }


    override fun onDestroy() {
        super.onDestroy()
        CAPPWifiPeer2PConnectionUtils.server?.close()
        CAPPWifiPeer2PConnectionUtils.socket?.close()
        CAPPWifiPeer2PConnectionUtils.clientSocket?.close()
        CAPPWifiPeer2PConnectionUtils.socket = null
        CAPPWifiPeer2PConnectionUtils.server = null
        CAPPWifiPeer2PConnectionUtils.clientSocket = null
        isRunning = false
        HSWifiP2PConnectioinUtils?.removeGroup()
    }

    override fun selfDeviceInfo(wifiP2pDevice: WifiP2pDevice) {

        findViewById<TextView>(R.id.tv_myDeviceName)?.text = wifiP2pDevice?.deviceName
        findViewById<TextView>(R.id.tv_myDeviceAddress)?.text = wifiP2pDevice?.deviceAddress
//        findViewById<TextView>(R.id.tv_myDeviceStatus)?.text =
//            getDeviceStatus(wifiP2pDevice?.status)

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
        if (requestCode == CAPPMConstants.FINISH_ACTIVITY_CODE) {
            CAPPWifiPeer2PConnectionUtils.server?.close()
            CAPPWifiPeer2PConnectionUtils.socket?.close()
            CAPPWifiPeer2PConnectionUtils.clientSocket?.close()
            CAPPWifiPeer2PConnectionUtils.socket = null
            CAPPWifiPeer2PConnectionUtils.server = null
            CAPPWifiPeer2PConnectionUtils.clientSocket = null
            CAPPDataSelectionMainActivity.selectedImagesList.clear()
            CAPPDataSelectionMainActivity.selectedContactsList.clear()
            CAPPDataSelectionMainActivity.selectedVideosList.clear()
            CAPPDataSelectionMainActivity.selectedApksList.clear()
            CAPPDataSelectionMainActivity.selectedCalendarEventsList.clear()
            CAPPDataSelectionMainActivity.imagesList.clear()
            CAPPDataSelectionMainActivity.contactsList.clear()
            CAPPDataSelectionMainActivity.videosList.clear()
            CAPPDataSelectionMainActivity.calendarEventsList.clear()
            CAPPDataSelectionMainActivity.apksList.clear()
            HSWifiP2PConnectioinUtils?.removeGroup()

            this.finish()
        } else if (requestCode == CAPPMConstants.WIFI_TURN_ON) {
            if (CAPPWifiUtilsClass(this@CAPPWifiP2PCreateGroupAndReceive).getWifiState()) {
                if (CAPPMPermissions(
                        applicationContext,
                        this@CAPPWifiP2PCreateGroupAndReceive
                    ).CheckIfGPSisOn()
                ) {
                    HSWifiP2PConnectioinUtils = CAPPWifiPeer2PConnectionUtils(this)
                    //turn wifi on for connection on both sides
                    // wifiP2PConnectioinClass?.turnWifiOn(true)
                    HSWifiP2PConnectioinUtils?.groupcreator = true
                    HSWifiP2PConnectioinUtils?.removeGroup()
                    //wifiP2PConnectioinClass?.createGroup()
                    HSWifiP2PConnectioinUtils?.discoverPeers()
                } else {
                    CAPPMPermissions(
                        applicationContext,
                        this@CAPPWifiP2PCreateGroupAndReceive
                    ).turnGpsOn()
                }
            } else {
                if (!CAPPMPermissions(
                        applicationContext,
                        this@CAPPWifiP2PCreateGroupAndReceive
                    ).CheckIfGPSisOn()
                ) {
                    CAPPMPermissions(
                        applicationContext,
                        this@CAPPWifiP2PCreateGroupAndReceive
                    ).turnGpsOn()
                }
            }
        } else if (requestCode == CAPPMConstants.REQUEST_LOCATION_TURNON) {
            if (CAPPWifiUtilsClass(this@CAPPWifiP2PCreateGroupAndReceive).getWifiState()) {
                if (CAPPMPermissions(
                        applicationContext,
                        this@CAPPWifiP2PCreateGroupAndReceive
                    ).CheckIfGPSisOn()
                ) {
                    HSWifiP2PConnectioinUtils = CAPPWifiPeer2PConnectionUtils(this)
                    //turn wifi on for connection on both sides
                    // wifiP2PConnectioinClass?.turnWifiOn(true)
                    HSWifiP2PConnectioinUtils?.groupcreator = true
                    HSWifiP2PConnectioinUtils?.removeGroup()
                    //wifiP2PConnectioinClass?.createGroup()
                    HSWifiP2PConnectioinUtils?.discoverPeers()
                } else {
                    bothPermissionsDenied()
                }
            } else {
                bothPermissionsDenied()
            }
        } else if (requestCode == CAPPMConstants.LOCATION_PERMISIION_REQUEST_CODE) {
            if (CAPPMPermissions.hasLocationPermission(this@CAPPWifiP2PCreateGroupAndReceive)) {
                if (CAPPMPermissions.hasStoragePermission(this@CAPPWifiP2PCreateGroupAndReceive)) {
                    createGroupForDevice()
                } else {
                    CAPPMPermissions.showStorageExplanation(this@CAPPWifiP2PCreateGroupAndReceive,
                        object : CAPPMPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                CAPPMPermissions.requestStoragePermission(this@CAPPWifiP2PCreateGroupAndReceive)
                            }

                            override fun denyPermission() {
                                bothPermissionsDenied()
                            }

                        })
                }
            } else {
                if (!CAPPMPermissions.hasStoragePermission(this@CAPPWifiP2PCreateGroupAndReceive)) {
                    CAPPMPermissions.showStorageExplanation(this@CAPPWifiP2PCreateGroupAndReceive,
                        object : CAPPMPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                CAPPMPermissions.requestStoragePermission(this@CAPPWifiP2PCreateGroupAndReceive)
                            }

                            override fun denyPermission() {
                                bothPermissionsDenied()
                            }

                        })
                }
            }
        } else if (requestCode == CAPPMConstants.SPRC) {
            if (!CAPPMPermissions.hasStoragePermission(this@CAPPWifiP2PCreateGroupAndReceive)) {
                finish()
            } else {
                if (CAPPMPermissions.hasLocationPermission(this@CAPPWifiP2PCreateGroupAndReceive)) {
                    createGroupForDevice()
                } else {
                    bothPermissionsDenied()
                }
            }
        }

        else if (requestCode == CAPPMConstants.ManageAllFilePermissionRequestCode) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    if (CAPPMPermissions.hasLocationPermission(this@CAPPWifiP2PCreateGroupAndReceive)) {
                        createGroupForDevice()
                    } else {
                        bothPermissionsDenied()
                    }

                } else {
                    finish()
                }
            }

        }
    }

    override fun onBackPressed() {
        CAPPWifiPeer2PConnectionUtils.server?.close()
        CAPPWifiPeer2PConnectionUtils.socket?.close()
        CAPPWifiPeer2PConnectionUtils.clientSocket?.close()
        CAPPWifiPeer2PConnectionUtils.socket = null
        CAPPWifiPeer2PConnectionUtils.server = null
        CAPPWifiPeer2PConnectionUtils.clientSocket = null
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


        if (requestCode == CAPPMConstants.LOCATION_PERMISIION_REQUEST_CODE) {
            if (CAPPMPermissions.hasLocationPermission(this@CAPPWifiP2PCreateGroupAndReceive)) {
                if (CAPPMPermissions.hasStoragePermission(this@CAPPWifiP2PCreateGroupAndReceive)) {
                    createGroupForDevice()
                } else {
                    CAPPMPermissions.showStorageExplanation(this@CAPPWifiP2PCreateGroupAndReceive,
                        object : CAPPMPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                CAPPMPermissions.requestStoragePermission(this@CAPPWifiP2PCreateGroupAndReceive)
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
                        CAPPMPermissions.showLocationRational(this@CAPPWifiP2PCreateGroupAndReceive,
                            object : CAPPMPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        CAPPMConstants.LOCATION_PERMISIION_REQUEST_CODE
                                    )
                                }

                                override fun dismissed() {
                                    if (!CAPPMPermissions.hasStoragePermission(this@CAPPWifiP2PCreateGroupAndReceive)) {
                                        CAPPMPermissions.showStorageExplanation(this@CAPPWifiP2PCreateGroupAndReceive,
                                            object : CAPPMPermissions.ExplanationCallBack {
                                                override fun requestPermission() {
                                                    CAPPMPermissions.requestStoragePermission(this@CAPPWifiP2PCreateGroupAndReceive)
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
                        if (!CAPPMPermissions.hasStoragePermission(this@CAPPWifiP2PCreateGroupAndReceive)) {
                            CAPPMPermissions.showStorageExplanation(this@CAPPWifiP2PCreateGroupAndReceive,
                                object : CAPPMPermissions.ExplanationCallBack {
                                    override fun requestPermission() {
                                        CAPPMPermissions.requestStoragePermission(this@CAPPWifiP2PCreateGroupAndReceive)
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
                    if (!CAPPMPermissions.hasStoragePermission(this@CAPPWifiP2PCreateGroupAndReceive)) {
                        CAPPMPermissions.showStorageExplanation(this@CAPPWifiP2PCreateGroupAndReceive,
                            object : CAPPMPermissions.ExplanationCallBack {
                                override fun requestPermission() {
                                    CAPPMPermissions.requestStoragePermission(this@CAPPWifiP2PCreateGroupAndReceive)
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
        } else if (requestCode == CAPPMConstants.SPRC) {
            if (CAPPMPermissions.hasStoragePermission(this@CAPPWifiP2PCreateGroupAndReceive)) {
                if (CAPPMPermissions.hasLocationPermission(this@CAPPWifiP2PCreateGroupAndReceive)) {
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
                        CAPPMPermissions.showStorageRational(this@CAPPWifiP2PCreateGroupAndReceive,
                            object : CAPPMPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        CAPPMConstants.SPRC
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
        CAPPMDialogues.showPermissionsRequired(this@CAPPWifiP2PCreateGroupAndReceive,
            "This app needs following permissions in order to discover nearby devices and share data.\n1.Location permission\n2.Storage Permission",
            object : CAPPMPermissions.PermissionsDeniedCallBack {
                override fun retryPermissions() {
                    startActivity(
                        Intent(
                            this@CAPPWifiP2PCreateGroupAndReceive,
                            CAPPWifiP2PCreateGroupAndReceive::class.java
                        )
                    )
                    finish()
                }

                override fun exitApp() {
                    finish()
                }

            })
    }
/*

     fun loadFbBannerAdd() {

         val adView = AdView(
             this@HSHSWifiP2PCreateGroupAndReceive,
             this@HSHSWifiP2PCreateGroupAndReceive.resources.getString(R.string.banner_add),
             AdSize.BANNER_HEIGHT_50
         )

         val adListener: AdListener = object : AdListener {

             override fun onError(ad: Ad, adError: AdError) {
                 if (com.facebook.ads.BuildConfig.DEBUG) {
                      Toast.makeText(
                        this@HSHSWifiP2PCreateGroupAndReceive,
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

        val mAdView = com.google.android.gms.ads.AdView(this@HSHSWifiP2PCreateGroupAndReceive)
        val adSize: com.google.android.gms.ads.AdSize = HSIntersitialAdHelper.getAdSize(this@HSHSWifiP2PCreateGroupAndReceive)
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
                Log.d(HSMyConstants.TAG, "onAdOpened: ")
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                adViewLayout.visibility = View.VISIBLE
                Log.d(HSMyConstants.TAG, "onAdLoaded: ")
            }
        }

    }
*/


}