package com.phone.clone.activity

import android.content.Intent
import android.net.Uri
import android.net.wifi.p2p.WifiP2pDevice
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView


import com.phone.clone.R
import com.phone.clone.constants.HSMyConstants

import com.phone.clone.interfaces.HSWifiP2PCallBacks
import com.phone.clone.utills.HSMyDialogues
import com.phone.clone.utills.HSMyPermissions
import com.phone.clone.utills.HSWifiP2PConnectionUtils
import com.phone.clone.utills.HSWifiUtilsClass


class HSHSWifiP2PCreateGroupAndReceive : AppCompatActivity(), HSWifiP2PCallBacks {
    var HSWifiP2PConnectioinUtils: HSWifiP2PConnectionUtils? = null

    var isRunning = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

         /*HSIntersitialAdHelper.adCallBack=object : HSIntersitialAdHelper.Companion.AdLoadCallBack{
             override fun adClosed() {

             }

         }*/
        checkForpermissions()
        android.os.Handler().postDelayed({

            if (isRunning) {
                findViewById<TextView>(R.id.tv_retry).visibility = View.VISIBLE
                findViewById<TextView>(R.id.tv_retry).text =
                    "If you are not able to connect to this device please retry. "
                findViewById<Button>(R.id.retry).visibility = View.VISIBLE
                findViewById<Button>(R.id.retry).setOnClickListener {
                    startActivity(Intent(this@HSHSWifiP2PCreateGroupAndReceive, HSHSWifiP2PCreateGroupAndReceive::class.java))
                    finish()
                }
            }
        }, 30000)
       /* HSIntersitialAdHelper.showAdd()

         loadFbBannerAdd()
         admobBanner()*/




        HSMyConstants.FILES_TO_SHARE.clear()

    }

    override fun onResume() {
        super.onResume()
        HSWifiP2PConnectioinUtils?.resumed()
    }

    private fun checkForpermissions() {
        if (HSMyPermissions.hasLocationPermission(this@HSHSWifiP2PCreateGroupAndReceive)) {
            if (HSMyPermissions.hasStoragePermission(this@HSHSWifiP2PCreateGroupAndReceive)) {
                createGroupForDevice()
            } else {
                HSMyPermissions.showStorageExplanation(this@HSHSWifiP2PCreateGroupAndReceive,
                    object : HSMyPermissions.ExplanationCallBack {
                        override fun requestPermission() {
                            HSMyPermissions.requestStoragePermission(this@HSHSWifiP2PCreateGroupAndReceive)
                        }

                        override fun denyPermission() {
                            bothPermissionsDenied()
                        }

                    })
            }
        } else {
            HSMyPermissions.showLocationExplanation(this@HSHSWifiP2PCreateGroupAndReceive,
                object : HSMyPermissions.ExplanationCallBack {
                    override fun requestPermission() {
                        HSMyPermissions.requestLocationPermission(this@HSHSWifiP2PCreateGroupAndReceive)
                    }

                    override fun denyPermission() {
                        if (!HSMyPermissions.hasStoragePermission(this@HSHSWifiP2PCreateGroupAndReceive)) {
                            HSMyPermissions.showStorageExplanation(this@HSHSWifiP2PCreateGroupAndReceive,
                                object : HSMyPermissions.ExplanationCallBack {
                                    override fun requestPermission() {
                                        HSMyPermissions.requestStoragePermission(this@HSHSWifiP2PCreateGroupAndReceive)
                                    }

                                    override fun denyPermission() {
                                        bothPermissionsDenied()
                                    }

                                })
                        } else {
                            if (HSMyPermissions.hasLocationPermission(this@HSHSWifiP2PCreateGroupAndReceive)) {
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
        if (HSWifiUtilsClass(this@HSHSWifiP2PCreateGroupAndReceive).getWifiState()) {
            if (HSMyPermissions(
                    applicationContext,
                    this@HSHSWifiP2PCreateGroupAndReceive
                ).CheckIfGPSisOn()
            ) {
                HSWifiP2PConnectioinUtils = HSWifiP2PConnectionUtils(this)
                //turn wifi on for connection on both sides
                // wifiP2PConnectioinClass?.turnWifiOn(true)
                HSWifiP2PConnectioinUtils?.groupcreator = true
                HSWifiP2PConnectioinUtils?.resumed()
                HSWifiP2PConnectioinUtils?.removeGroup()
                //wifiP2PConnectioinClass?.createGroup()
                HSWifiP2PConnectioinUtils?.discoverPeers()
            } else {
                HSMyPermissions(applicationContext, this@HSHSWifiP2PCreateGroupAndReceive).turnGpsOn()
            }
        } else {
            if (Build.VERSION.SDK_INT >= 29) {
                HSWifiUtilsClass(this@HSHSWifiP2PCreateGroupAndReceive).changeWifiState(true)
            } else {
                HSWifiUtilsClass(this@HSHSWifiP2PCreateGroupAndReceive).changeWifiState(true)
                if (HSMyPermissions(
                        applicationContext,
                        this@HSHSWifiP2PCreateGroupAndReceive
                    ).CheckIfGPSisOn()
                ) {
                    HSWifiP2PConnectioinUtils = HSWifiP2PConnectionUtils(this)
                    //turn wifi on for connection on both sides
                    // wifiP2PConnectioinClass?.turnWifiOn(true)
                    HSWifiP2PConnectioinUtils?.groupcreator = true
                    HSWifiP2PConnectioinUtils?.removeGroup()
                    //wifiP2PConnectioinClass?.createGroup()
                    HSWifiP2PConnectioinUtils?.discoverPeers()
                } else {
                    HSMyPermissions(applicationContext, this@HSHSWifiP2PCreateGroupAndReceive).turnGpsOn()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        isRunning=false
        HSWifiP2PConnectioinUtils?.paused()
        HSWifiP2PConnectioinUtils?.stopPeerDiscovery()
    }


    override fun onDestroy() {
        super.onDestroy()
        HSWifiP2PConnectionUtils.server?.close()
        HSWifiP2PConnectionUtils.socket?.close()
        HSWifiP2PConnectionUtils.clientSocket?.close()
        HSWifiP2PConnectionUtils.socket = null
        HSWifiP2PConnectionUtils.server = null
        HSWifiP2PConnectionUtils.clientSocket = null
        isRunning=false
        HSWifiP2PConnectioinUtils?.removeGroup()
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
        if (requestCode == HSMyConstants.FINISH_ACTIVITY_CODE) {
            HSWifiP2PConnectionUtils.server?.close()
            HSWifiP2PConnectionUtils.socket?.close()
            HSWifiP2PConnectionUtils.clientSocket?.close()
            HSWifiP2PConnectionUtils.socket = null
            HSWifiP2PConnectionUtils.server = null
            HSWifiP2PConnectionUtils.clientSocket = null
            HSDataSelectionActivity.selectedImagesList.clear()
            HSDataSelectionActivity.selectedContactsList.clear()
            HSDataSelectionActivity.selectedVideosList.clear()
            HSDataSelectionActivity.selectedApksList.clear()
            HSDataSelectionActivity.selectedCalendarEventsList.clear()
            HSDataSelectionActivity.imagesList.clear()
            HSDataSelectionActivity.contactsList.clear()
            HSDataSelectionActivity.videosList.clear()
            HSDataSelectionActivity.calendarEventsList.clear()
            HSDataSelectionActivity.apksList.clear()
            HSWifiP2PConnectioinUtils?.removeGroup()

            this.finish()
        } else if (requestCode == HSMyConstants.WIFI_TURN_ON) {
            if (HSWifiUtilsClass(this@HSHSWifiP2PCreateGroupAndReceive).getWifiState()) {
                if (HSMyPermissions(
                        applicationContext,
                        this@HSHSWifiP2PCreateGroupAndReceive
                    ).CheckIfGPSisOn()
                ) {
                    HSWifiP2PConnectioinUtils = HSWifiP2PConnectionUtils(this)
                    //turn wifi on for connection on both sides
                    // wifiP2PConnectioinClass?.turnWifiOn(true)
                    HSWifiP2PConnectioinUtils?.groupcreator = true
                    HSWifiP2PConnectioinUtils?.removeGroup()
                    //wifiP2PConnectioinClass?.createGroup()
                    HSWifiP2PConnectioinUtils?.discoverPeers()
                } else {
                    HSMyPermissions(applicationContext, this@HSHSWifiP2PCreateGroupAndReceive).turnGpsOn()
                }
            } else {
                if (!HSMyPermissions(
                        applicationContext,
                        this@HSHSWifiP2PCreateGroupAndReceive
                    ).CheckIfGPSisOn()
                ) {
                    HSMyPermissions(applicationContext, this@HSHSWifiP2PCreateGroupAndReceive).turnGpsOn()
                }
            }
        } else if (requestCode == HSMyConstants.REQUEST_LOCATION_TURNON) {
            if (HSWifiUtilsClass(this@HSHSWifiP2PCreateGroupAndReceive).getWifiState()) {
                if (HSMyPermissions(
                        applicationContext,
                        this@HSHSWifiP2PCreateGroupAndReceive
                    ).CheckIfGPSisOn()
                ) {
                    HSWifiP2PConnectioinUtils = HSWifiP2PConnectionUtils(this)
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
        } else if (requestCode == HSMyConstants.LOCATION_PERMISIION_REQUEST_CODE) {
            if (HSMyPermissions.hasLocationPermission(this@HSHSWifiP2PCreateGroupAndReceive)) {
                if (HSMyPermissions.hasStoragePermission(this@HSHSWifiP2PCreateGroupAndReceive)) {
                    createGroupForDevice()
                } else {
                    HSMyPermissions.showStorageExplanation(this@HSHSWifiP2PCreateGroupAndReceive,
                        object : HSMyPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                HSMyPermissions.requestStoragePermission(this@HSHSWifiP2PCreateGroupAndReceive)
                            }

                            override fun denyPermission() {
                                bothPermissionsDenied()
                            }

                        })
                }
            } else {
                if (!HSMyPermissions.hasStoragePermission(this@HSHSWifiP2PCreateGroupAndReceive)) {
                    HSMyPermissions.showStorageExplanation(this@HSHSWifiP2PCreateGroupAndReceive,
                        object : HSMyPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                HSMyPermissions.requestStoragePermission(this@HSHSWifiP2PCreateGroupAndReceive)
                            }

                            override fun denyPermission() {
                                bothPermissionsDenied()
                            }

                        })
                }
            }
        } else if (requestCode == HSMyConstants.SPRC) {
            if (!HSMyPermissions.hasStoragePermission(this@HSHSWifiP2PCreateGroupAndReceive)) {
                finish()
            } else {
                if (HSMyPermissions.hasLocationPermission(this@HSHSWifiP2PCreateGroupAndReceive)) {
                    createGroupForDevice()
                } else {
                    bothPermissionsDenied()
                }
            }
        }
    }

    override fun onBackPressed() {
        HSWifiP2PConnectionUtils.server?.close()
        HSWifiP2PConnectionUtils.socket?.close()
        HSWifiP2PConnectionUtils.clientSocket?.close()
        HSWifiP2PConnectionUtils.socket = null
        HSWifiP2PConnectionUtils.server = null
        HSWifiP2PConnectionUtils.clientSocket = null
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


        if (requestCode == HSMyConstants.LOCATION_PERMISIION_REQUEST_CODE) {
            if (HSMyPermissions.hasLocationPermission(this@HSHSWifiP2PCreateGroupAndReceive)) {
                if (HSMyPermissions.hasStoragePermission(this@HSHSWifiP2PCreateGroupAndReceive)) {
                    createGroupForDevice()
                } else {
                    HSMyPermissions.showStorageExplanation(this@HSHSWifiP2PCreateGroupAndReceive,
                        object : HSMyPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                HSMyPermissions.requestStoragePermission(this@HSHSWifiP2PCreateGroupAndReceive)
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
                        HSMyPermissions.showLocationRational(this@HSHSWifiP2PCreateGroupAndReceive,
                            object : HSMyPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        HSMyConstants.LOCATION_PERMISIION_REQUEST_CODE
                                    )
                                }

                                override fun dismissed() {
                                    if (!HSMyPermissions.hasStoragePermission(this@HSHSWifiP2PCreateGroupAndReceive)) {
                                        HSMyPermissions.showStorageExplanation(this@HSHSWifiP2PCreateGroupAndReceive,
                                            object : HSMyPermissions.ExplanationCallBack {
                                                override fun requestPermission() {
                                                    HSMyPermissions.requestStoragePermission(this@HSHSWifiP2PCreateGroupAndReceive)
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
                        if (!HSMyPermissions.hasStoragePermission(this@HSHSWifiP2PCreateGroupAndReceive)) {
                            HSMyPermissions.showStorageExplanation(this@HSHSWifiP2PCreateGroupAndReceive,
                                object : HSMyPermissions.ExplanationCallBack {
                                    override fun requestPermission() {
                                        HSMyPermissions.requestStoragePermission(this@HSHSWifiP2PCreateGroupAndReceive)
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
                    if (!HSMyPermissions.hasStoragePermission(this@HSHSWifiP2PCreateGroupAndReceive)) {
                        HSMyPermissions.showStorageExplanation(this@HSHSWifiP2PCreateGroupAndReceive,
                            object : HSMyPermissions.ExplanationCallBack {
                                override fun requestPermission() {
                                    HSMyPermissions.requestStoragePermission(this@HSHSWifiP2PCreateGroupAndReceive)
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
        } else if (requestCode == HSMyConstants.SPRC) {
            if (HSMyPermissions.hasStoragePermission(this@HSHSWifiP2PCreateGroupAndReceive)) {
                if (HSMyPermissions.hasLocationPermission(this@HSHSWifiP2PCreateGroupAndReceive)) {
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
                        HSMyPermissions.showStorageRational(this@HSHSWifiP2PCreateGroupAndReceive,
                            object : HSMyPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        HSMyConstants.SPRC
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
        HSMyDialogues.showPermissionsRequired(this@HSHSWifiP2PCreateGroupAndReceive,
            "This app needs following permissions in order to discover nearby devices and share data.\n1.Location permission\n2.Storage Permission",
            object : HSMyPermissions.PermissionsDeniedCallBack {
                override fun retryPermissions() {
                    startActivity(
                        Intent(
                            this@HSHSWifiP2PCreateGroupAndReceive,
                            HSHSWifiP2PCreateGroupAndReceive::class.java
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