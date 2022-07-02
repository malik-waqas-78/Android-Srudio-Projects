package com.zak.clone.zakactivity

import android.content.Intent
import android.net.Uri
import android.net.wifi.p2p.WifiP2pDevice
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView


import com.zak.clone.R
import com.zak.clone.zakconstants.ZakMyConstants

import com.zak.clone.zakinterfaces.ZakWifiP2PCallBacks
import com.zak.clone.zakutills.ZakMyDialogues
import com.zak.clone.zakutills.ZakMyPermissions
import com.zak.clone.zakutills.ZakWifiP2PConnectionUtils
import com.zak.clone.zakutills.ZakWifiUtilsClass


class ZakWifiP2PCreateGroupAndReceive : AppCompatActivity(), ZakWifiP2PCallBacks {
    var HSWifiP2PConnectioinUtils: ZakWifiP2PConnectionUtils? = null

    var isRunning = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zak_activity_create_group)

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
                    startActivity(Intent(this@ZakWifiP2PCreateGroupAndReceive, ZakWifiP2PCreateGroupAndReceive::class.java))
                    finish()
                }
            }
        }, 30000)
       /* HSIntersitialAdHelper.showAdd()

         loadFbBannerAdd()
         admobBanner()*/

        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }


        ZakMyConstants.FILES_TO_SHARE.clear()

    }

    override fun onResume() {
        super.onResume()
        HSWifiP2PConnectioinUtils?.resumed()
    }

    private fun checkForpermissions() {
        if (ZakMyPermissions.hasLocationPermission(this@ZakWifiP2PCreateGroupAndReceive)) {
            if (ZakMyPermissions.hasStoragePermission(this@ZakWifiP2PCreateGroupAndReceive)) {
                createGroupForDevice()
            } else {
                ZakMyPermissions.showStorageExplanation(this@ZakWifiP2PCreateGroupAndReceive,
                    object : ZakMyPermissions.ExplanationCallBack {
                        override fun requestPermission() {
                            ZakMyPermissions.requestStoragePermission(this@ZakWifiP2PCreateGroupAndReceive)
                        }

                        override fun denyPermission() {
                            bothPermissionsDenied()
                        }

                    })
            }
        } else {
            ZakMyPermissions.showLocationExplanation(this@ZakWifiP2PCreateGroupAndReceive,
                object : ZakMyPermissions.ExplanationCallBack {
                    override fun requestPermission() {
                        ZakMyPermissions.requestLocationPermission(this@ZakWifiP2PCreateGroupAndReceive)
                    }

                    override fun denyPermission() {
                        if (!ZakMyPermissions.hasStoragePermission(this@ZakWifiP2PCreateGroupAndReceive)) {
                            ZakMyPermissions.showStorageExplanation(this@ZakWifiP2PCreateGroupAndReceive,
                                object : ZakMyPermissions.ExplanationCallBack {
                                    override fun requestPermission() {
                                        ZakMyPermissions.requestStoragePermission(this@ZakWifiP2PCreateGroupAndReceive)
                                    }

                                    override fun denyPermission() {
                                        bothPermissionsDenied()
                                    }

                                })
                        } else {
                            if (ZakMyPermissions.hasLocationPermission(this@ZakWifiP2PCreateGroupAndReceive)) {
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
        if (ZakWifiUtilsClass(this@ZakWifiP2PCreateGroupAndReceive).getWifiState()) {
            if (ZakMyPermissions(
                    applicationContext,
                    this@ZakWifiP2PCreateGroupAndReceive
                ).CheckIfGPSisOn()
            ) {
                HSWifiP2PConnectioinUtils = ZakWifiP2PConnectionUtils(this)
                //turn wifi on for connection on both sides
                // wifiP2PConnectioinClass?.turnWifiOn(true)
                HSWifiP2PConnectioinUtils?.groupcreator = true
                HSWifiP2PConnectioinUtils?.resumed()
                HSWifiP2PConnectioinUtils?.removeGroup()
                //wifiP2PConnectioinClass?.createGroup()
                HSWifiP2PConnectioinUtils?.discoverPeers()
            } else {
                ZakMyPermissions(applicationContext, this@ZakWifiP2PCreateGroupAndReceive).turnGpsOn()
            }
        } else {
            if (Build.VERSION.SDK_INT >= 29) {
                ZakWifiUtilsClass(this@ZakWifiP2PCreateGroupAndReceive).changeWifiState(true)
            } else {
                ZakWifiUtilsClass(this@ZakWifiP2PCreateGroupAndReceive).changeWifiState(true)
                if (ZakMyPermissions(
                        applicationContext,
                        this@ZakWifiP2PCreateGroupAndReceive
                    ).CheckIfGPSisOn()
                ) {
                    HSWifiP2PConnectioinUtils = ZakWifiP2PConnectionUtils(this)
                    //turn wifi on for connection on both sides
                    // wifiP2PConnectioinClass?.turnWifiOn(true)
                    HSWifiP2PConnectioinUtils?.groupcreator = true
                    HSWifiP2PConnectioinUtils?.removeGroup()
                    //wifiP2PConnectioinClass?.createGroup()
                    HSWifiP2PConnectioinUtils?.discoverPeers()
                } else {
                    ZakMyPermissions(applicationContext, this@ZakWifiP2PCreateGroupAndReceive).turnGpsOn()
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
        ZakWifiP2PConnectionUtils.server?.close()
        ZakWifiP2PConnectionUtils.socket?.close()
        ZakWifiP2PConnectionUtils.clientSocket?.close()
        ZakWifiP2PConnectionUtils.socket = null
        ZakWifiP2PConnectionUtils.server = null
        ZakWifiP2PConnectionUtils.clientSocket = null
        isRunning=false
        HSWifiP2PConnectioinUtils?.removeGroup()
    }

    override fun selfDeviceInfo(wifiP2pDevice: WifiP2pDevice) {

        findViewById<TextView>(R.id.tv_myDeviceName)?.text = wifiP2pDevice?.deviceName
        /*findViewById<TextView>(R.id.tv_myDeviceAddress)?.text = wifiP2pDevice?.deviceAddress
        findViewById<TextView>(R.id.tv_myDeviceStatus)?.text =
            getDeviceStatus(wifiP2pDevice?.status)*/

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
        if (requestCode == ZakMyConstants.FINISH_ACTIVITY_CODE) {
            ZakWifiP2PConnectionUtils.server?.close()
            ZakWifiP2PConnectionUtils.socket?.close()
            ZakWifiP2PConnectionUtils.clientSocket?.close()
            ZakWifiP2PConnectionUtils.socket = null
            ZakWifiP2PConnectionUtils.server = null
            ZakWifiP2PConnectionUtils.clientSocket = null
            ZakDataSelectionActivity.selectedImagesList.clear()
            ZakDataSelectionActivity.selectedContactsList.clear()
            ZakDataSelectionActivity.selectedVideosList.clear()
            ZakDataSelectionActivity.selectedApksList.clear()
            ZakDataSelectionActivity.selectedCalendarEventsList.clear()
            ZakDataSelectionActivity.imagesList.clear()
            ZakDataSelectionActivity.contactsList.clear()
            ZakDataSelectionActivity.videosList.clear()
            ZakDataSelectionActivity.calendarEventsList.clear()
            ZakDataSelectionActivity.apksList.clear()
            HSWifiP2PConnectioinUtils?.removeGroup()

            this.finish()
        } else if (requestCode == ZakMyConstants.WIFI_TURN_ON) {
            if (ZakWifiUtilsClass(this@ZakWifiP2PCreateGroupAndReceive).getWifiState()) {
                if (ZakMyPermissions(
                        applicationContext,
                        this@ZakWifiP2PCreateGroupAndReceive
                    ).CheckIfGPSisOn()
                ) {
                    HSWifiP2PConnectioinUtils = ZakWifiP2PConnectionUtils(this)
                    //turn wifi on for connection on both sides
                    // wifiP2PConnectioinClass?.turnWifiOn(true)
                    HSWifiP2PConnectioinUtils?.groupcreator = true
                    HSWifiP2PConnectioinUtils?.removeGroup()
                    //wifiP2PConnectioinClass?.createGroup()
                    HSWifiP2PConnectioinUtils?.discoverPeers()
                } else {
                    ZakMyPermissions(applicationContext, this@ZakWifiP2PCreateGroupAndReceive).turnGpsOn()
                }
            } else {
                if (!ZakMyPermissions(
                        applicationContext,
                        this@ZakWifiP2PCreateGroupAndReceive
                    ).CheckIfGPSisOn()
                ) {
                    ZakMyPermissions(applicationContext, this@ZakWifiP2PCreateGroupAndReceive).turnGpsOn()
                }
            }
        } else if (requestCode == ZakMyConstants.REQUEST_LOCATION_TURNON) {
            if (ZakWifiUtilsClass(this@ZakWifiP2PCreateGroupAndReceive).getWifiState()) {
                if (ZakMyPermissions(
                        applicationContext,
                        this@ZakWifiP2PCreateGroupAndReceive
                    ).CheckIfGPSisOn()
                ) {
                    HSWifiP2PConnectioinUtils = ZakWifiP2PConnectionUtils(this)
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
        } else if (requestCode == ZakMyConstants.LOCATION_PERMISIION_REQUEST_CODE) {
            if (ZakMyPermissions.hasLocationPermission(this@ZakWifiP2PCreateGroupAndReceive)) {
                if (ZakMyPermissions.hasStoragePermission(this@ZakWifiP2PCreateGroupAndReceive)) {
                    createGroupForDevice()
                } else {
                    ZakMyPermissions.showStorageExplanation(this@ZakWifiP2PCreateGroupAndReceive,
                        object : ZakMyPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                ZakMyPermissions.requestStoragePermission(this@ZakWifiP2PCreateGroupAndReceive)
                            }

                            override fun denyPermission() {
                                bothPermissionsDenied()
                            }

                        })
                }
            } else {
                if (!ZakMyPermissions.hasStoragePermission(this@ZakWifiP2PCreateGroupAndReceive)) {
                    ZakMyPermissions.showStorageExplanation(this@ZakWifiP2PCreateGroupAndReceive,
                        object : ZakMyPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                ZakMyPermissions.requestStoragePermission(this@ZakWifiP2PCreateGroupAndReceive)
                            }

                            override fun denyPermission() {
                                bothPermissionsDenied()
                            }

                        })
                }
            }
        } else if (requestCode == ZakMyConstants.SPRC) {
            if (!ZakMyPermissions.hasStoragePermission(this@ZakWifiP2PCreateGroupAndReceive)) {
                finish()
            } else {
                if (ZakMyPermissions.hasLocationPermission(this@ZakWifiP2PCreateGroupAndReceive)) {
                    createGroupForDevice()
                } else {
                    bothPermissionsDenied()
                }
            }
        }
    }

    override fun onBackPressed() {
        ZakWifiP2PConnectionUtils.server?.close()
        ZakWifiP2PConnectionUtils.socket?.close()
        ZakWifiP2PConnectionUtils.clientSocket?.close()
        ZakWifiP2PConnectionUtils.socket = null
        ZakWifiP2PConnectionUtils.server = null
        ZakWifiP2PConnectionUtils.clientSocket = null
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


        if (requestCode == ZakMyConstants.LOCATION_PERMISIION_REQUEST_CODE) {
            if (ZakMyPermissions.hasLocationPermission(this@ZakWifiP2PCreateGroupAndReceive)) {
                if (ZakMyPermissions.hasStoragePermission(this@ZakWifiP2PCreateGroupAndReceive)) {
                    createGroupForDevice()
                } else {
                    ZakMyPermissions.showStorageExplanation(this@ZakWifiP2PCreateGroupAndReceive,
                        object : ZakMyPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                ZakMyPermissions.requestStoragePermission(this@ZakWifiP2PCreateGroupAndReceive)
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
                        ZakMyPermissions.showLocationRational(this@ZakWifiP2PCreateGroupAndReceive,
                            object : ZakMyPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        ZakMyConstants.LOCATION_PERMISIION_REQUEST_CODE
                                    )
                                }

                                override fun dismissed() {
                                    if (!ZakMyPermissions.hasStoragePermission(this@ZakWifiP2PCreateGroupAndReceive)) {
                                        ZakMyPermissions.showStorageExplanation(this@ZakWifiP2PCreateGroupAndReceive,
                                            object : ZakMyPermissions.ExplanationCallBack {
                                                override fun requestPermission() {
                                                    ZakMyPermissions.requestStoragePermission(this@ZakWifiP2PCreateGroupAndReceive)
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
                        if (!ZakMyPermissions.hasStoragePermission(this@ZakWifiP2PCreateGroupAndReceive)) {
                            ZakMyPermissions.showStorageExplanation(this@ZakWifiP2PCreateGroupAndReceive,
                                object : ZakMyPermissions.ExplanationCallBack {
                                    override fun requestPermission() {
                                        ZakMyPermissions.requestStoragePermission(this@ZakWifiP2PCreateGroupAndReceive)
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
                    if (!ZakMyPermissions.hasStoragePermission(this@ZakWifiP2PCreateGroupAndReceive)) {
                        ZakMyPermissions.showStorageExplanation(this@ZakWifiP2PCreateGroupAndReceive,
                            object : ZakMyPermissions.ExplanationCallBack {
                                override fun requestPermission() {
                                    ZakMyPermissions.requestStoragePermission(this@ZakWifiP2PCreateGroupAndReceive)
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
        } else if (requestCode == ZakMyConstants.SPRC) {
            if (ZakMyPermissions.hasStoragePermission(this@ZakWifiP2PCreateGroupAndReceive)) {
                if (ZakMyPermissions.hasLocationPermission(this@ZakWifiP2PCreateGroupAndReceive)) {
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
                        ZakMyPermissions.showStorageRational(this@ZakWifiP2PCreateGroupAndReceive,
                            object : ZakMyPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        ZakMyConstants.SPRC
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
        ZakMyDialogues.showPermissionsRequired(this@ZakWifiP2PCreateGroupAndReceive,
            "This app needs following permissions in order to discover nearby devices and share data.\n1.Location permission\n2.Storage Permission",
            object : ZakMyPermissions.PermissionsDeniedCallBack {
                override fun retryPermissions() {
                    startActivity(
                        Intent(
                            this@ZakWifiP2PCreateGroupAndReceive,
                            ZakWifiP2PCreateGroupAndReceive::class.java
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