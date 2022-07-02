package com.zak.clone.zakactivity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.net.wifi.p2p.WifiP2pDevice
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zak.clone.R
import com.zak.clone.zakadapters.ZakAdapterDevicesList
import com.zak.clone.zakconstants.ZakMyConstants
import com.zak.clone.zakinterfaces.ZakWifiP2PCallBacks
import java.util.*
import kotlin.collections.ArrayList
import android.os.Handler
import android.provider.Settings
import android.widget.Button
import android.widget.ImageView

import com.zak.clone.zakutills.ZakMyDialogues
import com.zak.clone.zakutills.ZakMyPermissions
import com.zak.clone.zakutills.ZakWifiP2PConnectionUtils
import com.zak.clone.zakutills.ZakWifiUtilsClass


class ZakWifiP2PJoinGroupAndSend : AppCompatActivity(), ZakWifiP2PCallBacks {
    var HSWifiP2PConnectioinUtils: ZakWifiP2PConnectionUtils? = null
    var rc_deviceList: RecyclerView? = null
    var HSAdapterDevice: ZakAdapterDevicesList? = null
    var avalableP2pDevices: List<WifiP2pDevice> = ArrayList()
    var isRunning = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zak_activity_join_group)

       /* HSIntersitialAdHelper.adCallBack=object : HSIntersitialAdHelper.Companion.AdLoadCallBack{
            override fun adClosed() {



            }


        }*/
        android.os.Handler().postDelayed({
            if (isRunning) {
                findViewById<TextView>(R.id.tv_retry).visibility = View.VISIBLE
                findViewById<TextView>(R.id.tv_retry).text =
                    "If you are not able to find your device please retry."
                findViewById<Button>(R.id.retry).visibility = View.VISIBLE
                findViewById<Button>(R.id.retry).setOnClickListener {
                    val intent = Intent()
                    setResult(ZakMyConstants.RESTART_JOIN, intent)
                    finish()
                }
            }
        }, 30000)
        HSWifiP2PConnectioinUtils = ZakWifiP2PConnectionUtils(this@ZakWifiP2PJoinGroupAndSend)
        HSWifiP2PConnectioinUtils?.resumed()
        checkForpermissions()
/*
         HSIntersitialAdHelper.showAdd()
         admobBanner()
         loadFbBannerAdd()*/

        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finishThisActivity()
        }
        initView()



    }

    override fun onResume() {
        super.onResume()
        decodingDialogue = null
        HSWifiP2PConnectioinUtils?.resumed()
    }
    fun joinGroup(){
        if (ZakWifiUtilsClass(this@ZakWifiP2PJoinGroupAndSend).getWifiState()) {
            if (ZakMyPermissions(
                    applicationContext,
                    this@ZakWifiP2PJoinGroupAndSend
                ).CheckIfGPSisOn()
            ) {
                HSWifiP2PConnectioinUtils?.removeGroup()
                HSWifiP2PConnectioinUtils?.discoverPeers()
            } else {
                ZakMyPermissions(applicationContext, this@ZakWifiP2PJoinGroupAndSend).turnGpsOn()
            }
        } else {
            if (Build.VERSION.SDK_INT >= 29) {
                ZakWifiUtilsClass(this@ZakWifiP2PJoinGroupAndSend).changeWifiState(true)
            } else {
                ZakWifiUtilsClass(this@ZakWifiP2PJoinGroupAndSend).changeWifiState(true)
                if (ZakMyPermissions(
                        applicationContext,
                        this@ZakWifiP2PJoinGroupAndSend
                    ).CheckIfGPSisOn()
                ) {
                    HSWifiP2PConnectioinUtils?.removeGroup()
                    HSWifiP2PConnectioinUtils?.discoverPeers()
                } else {
                    ZakMyPermissions(applicationContext, this@ZakWifiP2PJoinGroupAndSend).turnGpsOn()
                }
            }
        }


    }

    private fun checkForpermissions() {
        if (ZakMyPermissions.hasLocationPermission(this@ZakWifiP2PJoinGroupAndSend)) {
            if (ZakMyPermissions.hasStoragePermission(this@ZakWifiP2PJoinGroupAndSend)) {
               joinGroup()
            } else {
                ZakMyPermissions.showStorageExplanation(this@ZakWifiP2PJoinGroupAndSend,
                    object : ZakMyPermissions.ExplanationCallBack {
                        override fun requestPermission() {
                            ZakMyPermissions.requestStoragePermission(this@ZakWifiP2PJoinGroupAndSend)
                        }

                        override fun denyPermission() {
                            bothPermissionsDenied()
                        }

                    })
            }
        } else {
            ZakMyPermissions.showLocationExplanation(this@ZakWifiP2PJoinGroupAndSend,
                object : ZakMyPermissions.ExplanationCallBack {
                    override fun requestPermission() {
                        ZakMyPermissions.requestLocationPermission(this@ZakWifiP2PJoinGroupAndSend)
                    }

                    override fun denyPermission() {
                        if (!ZakMyPermissions.hasStoragePermission(this@ZakWifiP2PJoinGroupAndSend)) {
                            ZakMyPermissions.showStorageExplanation(this@ZakWifiP2PJoinGroupAndSend,
                                object : ZakMyPermissions.ExplanationCallBack {
                                    override fun requestPermission() {
                                        ZakMyPermissions.requestStoragePermission(this@ZakWifiP2PJoinGroupAndSend)
                                    }

                                    override fun denyPermission() {
                                        bothPermissionsDenied()
                                    }

                                })
                        }else{
                            if(ZakMyPermissions.hasLocationPermission(this@ZakWifiP2PJoinGroupAndSend)){
                               joinGroup()
                            }else{
                                bothPermissionsDenied()
                            }
                        }
                    }

                })
        }
    }

    fun bothPermissionsDenied(){
        ZakMyDialogues.showPermissionsRequired(this@ZakWifiP2PJoinGroupAndSend,
            "This app needs following permissions in order to discover nearby devices and share data.\n1.Location permission\n2.Storage Permission"
            ,object:ZakMyPermissions.PermissionsDeniedCallBack{
                override fun retryPermissions() {
                    startActivity(Intent(this@ZakWifiP2PJoinGroupAndSend,ZakWifiP2PCreateGroupAndReceive::class.java))
                    finish()
                }

                override fun exitApp() {
                    finish()
                }

            })
    }

    private fun initView() {
        rc_deviceList = findViewById(R.id.rv_deviceList)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rc_deviceList?.layoutManager = layoutManager
        //setHSAdapter
        HSAdapterDevice = ZakAdapterDevicesList(this, avalableP2pDevices)
        rc_deviceList?.adapter = HSAdapterDevice
    }


    override fun onDestroy() {
        super.onDestroy()
        ZakWifiP2PConnectionUtils.server?.close()
        ZakWifiP2PConnectionUtils.socket?.close()
        ZakWifiP2PConnectionUtils.clientSocket?.close()
        ZakWifiP2PConnectionUtils.socket = null
        ZakWifiP2PConnectionUtils.server = null
        ZakWifiP2PConnectionUtils.clientSocket = null
        isRunning = false
        decodingDialogue?.dismiss();
        decodingDialogue = null

    }

    override fun selfDeviceInfo(wifiP2pDevice: WifiP2pDevice) {
        // wifiP2PConnectioinClass!!.createGroup()
        /* findViewById<TextView>(R.id.tv_myDeviceName)?.text=wifiP2pDevice?.deviceName!!
         findViewById<TextView>(R.id.tv_myDeviceAddress)?.text=wifiP2pDevice?.deviceAddress
         findViewById<TextView>(R.id.tv_myDeviceStatus)?.text=HSAdapterDevice!!.getDeviceStatus(wifiP2pDevice?.status)*/

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(permissions.isEmpty()||grantResults.isEmpty()){
            return
        }


        if (requestCode == ZakMyConstants.LOCATION_PERMISIION_REQUEST_CODE) {
            if (ZakMyPermissions.hasLocationPermission(this@ZakWifiP2PJoinGroupAndSend)) {
                if (ZakMyPermissions.hasStoragePermission(this@ZakWifiP2PJoinGroupAndSend)) {
                    joinGroup()
                } else {
                    ZakMyPermissions.showStorageExplanation(this@ZakWifiP2PJoinGroupAndSend,
                        object : ZakMyPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                ZakMyPermissions.requestStoragePermission(this@ZakWifiP2PJoinGroupAndSend)
                            }

                            override fun denyPermission() {
                                bothPermissionsDenied()
                            }

                        })
                }
            } else {

                if (Build.VERSION.SDK_INT>=23) {
                    var showRational = shouldShowRequestPermissionRationale(permissions[0])
                    if(!showRational){
                        ZakMyPermissions.showLocationRational(this@ZakWifiP2PJoinGroupAndSend,
                            object : ZakMyPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        ZakMyConstants.LOCATION_PERMISIION_REQUEST_CODE
                                    )
                                }

                                override fun dismissed() {
                                    if (!ZakMyPermissions.hasStoragePermission(this@ZakWifiP2PJoinGroupAndSend)) {
                                        ZakMyPermissions.showStorageExplanation(this@ZakWifiP2PJoinGroupAndSend,
                                            object : ZakMyPermissions.ExplanationCallBack {
                                                override fun requestPermission() {
                                                    ZakMyPermissions.requestStoragePermission(this@ZakWifiP2PJoinGroupAndSend)
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
                    }else{
                        if (!ZakMyPermissions.hasStoragePermission(this@ZakWifiP2PJoinGroupAndSend)) {
                            ZakMyPermissions.showStorageExplanation(this@ZakWifiP2PJoinGroupAndSend,
                                object : ZakMyPermissions.ExplanationCallBack {
                                    override fun requestPermission() {
                                        ZakMyPermissions.requestStoragePermission(this@ZakWifiP2PJoinGroupAndSend)
                                    }

                                    override fun denyPermission() {
                                        bothPermissionsDenied()
                                    }

                                })
                        }else{
                            bothPermissionsDenied()
                        }
                    }
                } else {
                    if (!ZakMyPermissions.hasStoragePermission(this@ZakWifiP2PJoinGroupAndSend)) {
                        ZakMyPermissions.showStorageExplanation(this@ZakWifiP2PJoinGroupAndSend,
                            object : ZakMyPermissions.ExplanationCallBack {
                                override fun requestPermission() {
                                    ZakMyPermissions.requestStoragePermission(this@ZakWifiP2PJoinGroupAndSend)
                                }

                                override fun denyPermission() {
                                    bothPermissionsDenied()
                                }

                            })
                    }else{
                        bothPermissionsDenied()
                    }
                }
            }
        } else if (requestCode == ZakMyConstants.SPRC) {
            if (ZakMyPermissions.hasStoragePermission(this@ZakWifiP2PJoinGroupAndSend)) {
                if (ZakMyPermissions.hasLocationPermission(this@ZakWifiP2PJoinGroupAndSend)) {
                    joinGroup()
                } else {
                    bothPermissionsDenied()
                }
            } else {

                if (Build.VERSION.SDK_INT>=23) {
                    //show rational
                    var showRational = shouldShowRequestPermissionRationale(permissions[0])
                    //user deind permission permannently
                    if(!showRational){
                        ZakMyPermissions.showStorageRational(this@ZakWifiP2PJoinGroupAndSend,
                            object : ZakMyPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
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
                    }else{
                        bothPermissionsDenied()
                    }
                } else {
                    //user deind permission
                    bothPermissionsDenied()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ZakMyConstants.FINISH_ACTIVITY_CODE) {
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
            ZakMyConstants.FILES_TO_SHARE.clear()
            HSWifiP2PConnectioinUtils?.removeGroup()
            finishThisActivity()
        }else if (requestCode == ZakMyConstants.WIFI_TURN_ON) {
            if (ZakWifiUtilsClass(this@ZakWifiP2PJoinGroupAndSend).getWifiState()) {
                if (ZakMyPermissions(
                        applicationContext,
                        this@ZakWifiP2PJoinGroupAndSend
                    ).CheckIfGPSisOn()
                ) {
                    HSWifiP2PConnectioinUtils= ZakWifiP2PConnectionUtils(this)
                    //turn wifi on for connection on both sides
                    // wifiP2PConnectioinClass?.turnWifiOn(true)
                    HSWifiP2PConnectioinUtils?.groupcreator = true
                    HSWifiP2PConnectioinUtils?.removeGroup()
                    //wifiP2PConnectioinClass?.createGroup()
                    HSWifiP2PConnectioinUtils?.discoverPeers()
                } else {
                    ZakMyPermissions(applicationContext, this@ZakWifiP2PJoinGroupAndSend).turnGpsOn()
                }
            } else {
                if (!ZakMyPermissions(
                        applicationContext,
                        this@ZakWifiP2PJoinGroupAndSend
                    ).CheckIfGPSisOn()
                ) {
                    ZakMyPermissions(applicationContext, this@ZakWifiP2PJoinGroupAndSend).turnGpsOn()
                }
            }
        } else if (requestCode == ZakMyConstants.REQUEST_LOCATION_TURNON) {
            if (ZakWifiUtilsClass(this@ZakWifiP2PJoinGroupAndSend).getWifiState()) {
                if (ZakMyPermissions(
                        applicationContext,
                        this@ZakWifiP2PJoinGroupAndSend
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
            if (ZakMyPermissions.hasLocationPermission(this@ZakWifiP2PJoinGroupAndSend)) {
                if (ZakMyPermissions.hasStoragePermission(this@ZakWifiP2PJoinGroupAndSend)) {
                    joinGroup()
                } else {
                    ZakMyPermissions.showStorageExplanation(this@ZakWifiP2PJoinGroupAndSend,
                        object : ZakMyPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                ZakMyPermissions.requestStoragePermission(this@ZakWifiP2PJoinGroupAndSend)
                            }

                            override fun denyPermission() {
                                bothPermissionsDenied()
                            }

                        })
                }
            } else {
                if (!ZakMyPermissions.hasStoragePermission(this@ZakWifiP2PJoinGroupAndSend)) {
                    ZakMyPermissions.showStorageExplanation(this@ZakWifiP2PJoinGroupAndSend,
                        object : ZakMyPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                ZakMyPermissions.requestStoragePermission(this@ZakWifiP2PJoinGroupAndSend)
                            }

                            override fun denyPermission() {
                                bothPermissionsDenied()
                            }

                        })
                }
            }
        } else if (requestCode == ZakMyConstants.SPRC) {
            if (!ZakMyPermissions.hasStoragePermission(this@ZakWifiP2PJoinGroupAndSend)) {
                finish()
            } else {
                if (ZakMyPermissions.hasLocationPermission(this@ZakWifiP2PJoinGroupAndSend)) {
                   joinGroup()
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
        finishThisActivity()
        super.onBackPressed()

    }

    fun finishThisActivity() {
        val intent = Intent()
        setResult(ZakMyConstants.SELCTION_ACTIVITY, intent)
        finish()
    }

    override fun peersAvailable(wifiP2pDeviceList: Collection<WifiP2pDevice>) {

        findViewById<TextView>(R.id.tv_tap).visibility = View.VISIBLE

        avalableP2pDevices = wifiP2pDeviceList.toList()

        Log.d(
            ZakMyConstants.TAG,
            "peersAvailable: ${wifiP2pDeviceList.size} ${this.avalableP2pDevices.size}"
        )
        HSAdapterDevice!!.wifiP2pDeviceList = ArrayList(wifiP2pDeviceList.toList())
        HSAdapterDevice?.notifyDataSetChanged()


        var rand = Random()
        clearPeersView()
        for (i in HSAdapterDevice?.wifiP2pDeviceList?.indices!!) {
            if (i == 5) {
                break
            }
            val indexArray = ArrayList<Int>()
            var index = 0
            while (true) {
                index = rand.nextInt(6)
                if (indexArray.contains(index)) {
                    continue
                } else {
                    indexArray.add(index)
                    break
                }
            }

            showPeers(index, HSAdapterDevice?.wifiP2pDeviceList!![i])

        }
        if (HSAdapterDevice?.wifiP2pDeviceList?.size!! < 6) {
            HSAdapterDevice?.wifiP2pDeviceList = ArrayList()
            HSAdapterDevice?.notifyDataSetChanged()
        }
    }

    override fun deviceInfo(name: String, address: String, status: Int) {

    }

    private fun clearPeersView() {
        var cl_peer = findViewById<ConstraintLayout>(R.id.cl_peer1)
        cl_peer?.visibility = View.INVISIBLE
        cl_peer = findViewById<ConstraintLayout>(R.id.cl_peer2)
        cl_peer?.visibility = View.INVISIBLE
        cl_peer = findViewById<ConstraintLayout>(R.id.cl_peer3)
        cl_peer?.visibility = View.INVISIBLE
        cl_peer = findViewById<ConstraintLayout>(R.id.cl_peer4)
        cl_peer?.visibility = View.INVISIBLE
        cl_peer = findViewById<ConstraintLayout>(R.id.cl_peer5)
        cl_peer?.visibility = View.INVISIBLE
        cl_peer = findViewById<ConstraintLayout>(R.id.cl_peer6)
        cl_peer?.visibility = View.INVISIBLE
    }

    private fun showPeers(index: Int, devic: WifiP2pDevice) {
        var cl_peer: ConstraintLayout? = null

        when (index) {
            0 -> {
                cl_peer = findViewById<ConstraintLayout>(R.id.cl_peer1)
                cl_peer.visibility = View.VISIBLE
                val tv_name = findViewById<TextView>(R.id.tv_p1)
                tv_name.setText(devic.deviceName)

            }
            1 -> {
                cl_peer = findViewById<ConstraintLayout>(R.id.cl_peer2)
                cl_peer.visibility = View.VISIBLE
                val tv_name = findViewById<TextView>(R.id.tv_p2)
                tv_name.setText(devic.deviceName)
            }
            2 -> {
                cl_peer = findViewById<ConstraintLayout>(R.id.cl_peer3)
                cl_peer.visibility = View.VISIBLE
                val tv_name = findViewById<TextView>(R.id.tv_p3)
                tv_name.setText(devic.deviceName)
            }
            3 -> {
                cl_peer = findViewById<ConstraintLayout>(R.id.cl_peer4)
                cl_peer.visibility = View.VISIBLE
                val tv_name = findViewById<TextView>(R.id.tv_p4)
                tv_name.setText(devic.deviceName)
            }
            4 -> {
                cl_peer = findViewById<ConstraintLayout>(R.id.cl_peer5)
                cl_peer.visibility = View.VISIBLE
                val tv_name = findViewById<TextView>(R.id.tv_p5)
                tv_name.setText(devic.deviceName)
            }
            5 -> {
                cl_peer = findViewById<ConstraintLayout>(R.id.cl_peer6)
                cl_peer.visibility = View.VISIBLE
                val tv_name = findViewById<TextView>(R.id.tv_p6)
                tv_name.setText(devic.deviceName)
            }
        }


        cl_peer?.setOnClickListener{
            HSWifiP2PConnectioinUtils?.removeGroup()
            if(devic!=null){
                decodeData()
                Handler().postDelayed({
                    HSWifiP2PConnectioinUtils?.connect( devic)
                },1000)
                Handler().postDelayed({
                    decodingDialogue?.dismiss()
                },30000)
            }
        }
    }

    override fun onPause() {
        decodingDialogue?.dismiss()
        decodingDialogue = null
        HSWifiP2PConnectioinUtils?.paused()
        HSWifiP2PConnectioinUtils?.stopPeerDiscovery()
        isRunning = false
        super.onPause()
    }

    override fun deviceClicked(position: Int) {
        decodeData()
        HSWifiP2PConnectioinUtils?.removeGroup()
        Handler().postDelayed({
            HSWifiP2PConnectioinUtils?.connect( avalableP2pDevices[position])
        },1000)
        Handler().postDelayed({
            decodingDialogue?.dismiss()
        },30000)
    }

    var decodingDialogue: Dialog? = null
    fun decodeData() {
        decodingDialogue = Dialog(this@ZakWifiP2PJoinGroupAndSend)
        decodingDialogue?.setCancelable(false)
        decodingDialogue?.setContentView(R.layout.zak_db_decoding_data)
        decodingDialogue?.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tv_title = decodingDialogue?.findViewById<TextView>(R.id.tv_deco_msg)
        tv_title?.text = "Please wait while establishing Connection"

        decodingDialogue?.show()
    }
/*     fun loadFbBannerAdd() {

         val adView = AdView(
             this@HSHSWifiP2PJoinGroupAndSend,
             this@HSHSWifiP2PJoinGroupAndSend.resources.getString(R.string.banner_add),
             AdSize.BANNER_HEIGHT_50
         )

         val adListener: AdListener = object : AdListener {

             override fun onError(ad: Ad, adError: AdError) {
                 if (com.facebook.ads.BuildConfig.DEBUG) {
              *//*        Toast.makeText(
                        this@HSSplashScreen,
                        "Error: " + adError.errorMessage,
                        Toast.LENGTH_LONG
                    )
                        .show()*//*
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

        val mAdView = com.google.android.gms.ads.AdView(this@HSHSWifiP2PJoinGroupAndSend)
        val adSize: com.google.android.gms.ads.AdSize = HSIntersitialAdHelper.getAdSize(this@HSHSWifiP2PJoinGroupAndSend)
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
                Log.d(HSMyConstants.TAG, "onAdFailedToLoad: ${p0}")
                //adViewLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
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

    }*/

}