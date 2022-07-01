package com.phoneclone.data.activity

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
import com.phoneclone.data.R
import com.phoneclone.data.adapters.AdapterDevicesList
import com.phoneclone.data.constants.MyConstants
import com.phoneclone.data.interfaces.WifiP2PCallBacks
import java.util.*
import kotlin.collections.ArrayList
import android.os.Handler
import android.provider.Settings
import android.widget.Button
import android.widget.RelativeLayout
import com.facebook.ads.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.phoneclone.data.ads.IntersitialAdHelper
import com.phoneclone.data.ads.isAppInstalledFromPlay
import com.phoneclone.data.utills.MyDialogues
import com.phoneclone.data.utills.MyPermissions
import com.phoneclone.data.utills.WifiP2PConnectionUtils
import com.phoneclone.data.utills.WifiUtilsClass


class WifiP2PJoinGroupAndSend : AppCompatActivity(), WifiP2PCallBacks {
    var wifiP2PConnectioinUtils: WifiP2PConnectionUtils? = null
    var rc_deviceList: RecyclerView? = null
    var adapterDevice: AdapterDevicesList? = null
    var avalableP2pDevices: List<WifiP2pDevice> = ArrayList()
    var isRunning = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_group)

        IntersitialAdHelper.adCallBack=object : IntersitialAdHelper.Companion.AdLoadCallBack{
            override fun adClosed() {
                android.os.Handler().postDelayed({

                    if (isRunning) {
                        findViewById<TextView>(R.id.tv_retry).visibility = View.VISIBLE
                        findViewById<TextView>(R.id.tv_retry).text =
                            "If you are not able to find your device please retry."
                        findViewById<Button>(R.id.retry).visibility = View.VISIBLE
                        findViewById<Button>(R.id.retry).setOnClickListener {
                            val intent = Intent()
                            setResult(MyConstants.RESTART_JOIN, intent)
                            finish()
                        }
                    }
                }, 30000)
                wifiP2PConnectioinUtils = WifiP2PConnectionUtils(this@WifiP2PJoinGroupAndSend)
                wifiP2PConnectioinUtils?.resumed()
                checkForpermissions()
            }


        }

        if(isAppInstalledFromPlay(this)) {
            IntersitialAdHelper.showAdmobIntersitial(this@WifiP2PJoinGroupAndSend)
            loadFbBannerAdd()
            admobBanner()
        }

        initView()



    }

    override fun onResume() {
        super.onResume()
        decodingDialogue = null
        wifiP2PConnectioinUtils?.resumed()
        isRunning=true
    }
    fun joinGroup(){
        if (WifiUtilsClass(this@WifiP2PJoinGroupAndSend).getWifiState()) {
            if (MyPermissions(
                    applicationContext,
                    this@WifiP2PJoinGroupAndSend
                ).CheckIfGPSisOn()
            ) {
                wifiP2PConnectioinUtils?.removeGroup()
                wifiP2PConnectioinUtils?.discoverPeers()
            } else {
                MyPermissions(applicationContext, this@WifiP2PJoinGroupAndSend).turnGpsOn()
            }
        } else {
            if (Build.VERSION.SDK_INT >= 29) {
                WifiUtilsClass(this@WifiP2PJoinGroupAndSend).changeWifiState(true)
            } else {
                WifiUtilsClass(this@WifiP2PJoinGroupAndSend).changeWifiState(true)
                if (MyPermissions(
                        applicationContext,
                        this@WifiP2PJoinGroupAndSend
                    ).CheckIfGPSisOn()
                ) {
                    wifiP2PConnectioinUtils?.removeGroup()
                    wifiP2PConnectioinUtils?.discoverPeers()
                } else {
                    MyPermissions(applicationContext, this@WifiP2PJoinGroupAndSend).turnGpsOn()
                }
            }
        }


    }

    private fun checkForpermissions() {
        if (MyPermissions.hasLocationPermission(this@WifiP2PJoinGroupAndSend)) {
            if (MyPermissions.hasStoragePermission(this@WifiP2PJoinGroupAndSend)) {
               joinGroup()
            } else {
                MyPermissions.showStorageExplanation(this@WifiP2PJoinGroupAndSend,
                    object : MyPermissions.ExplanationCallBack {
                        override fun requestPermission() {
                            MyPermissions.requestStoragePermission(this@WifiP2PJoinGroupAndSend)
                        }

                        override fun denyPermission() {
                            bothPermissionsDenied()
                        }

                    })
            }
        } else {
            MyPermissions.showLocationExplanation(this@WifiP2PJoinGroupAndSend,
                object : MyPermissions.ExplanationCallBack {
                    override fun requestPermission() {
                        MyPermissions.requestLocationPermission(this@WifiP2PJoinGroupAndSend)
                    }

                    override fun denyPermission() {
                        if (!MyPermissions.hasStoragePermission(this@WifiP2PJoinGroupAndSend)) {
                            MyPermissions.showStorageExplanation(this@WifiP2PJoinGroupAndSend,
                                object : MyPermissions.ExplanationCallBack {
                                    override fun requestPermission() {
                                        MyPermissions.requestStoragePermission(this@WifiP2PJoinGroupAndSend)
                                    }

                                    override fun denyPermission() {
                                        bothPermissionsDenied()
                                    }

                                })
                        }else{
                            if(MyPermissions.hasLocationPermission(this@WifiP2PJoinGroupAndSend)){
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
        MyDialogues.showPermissionsRequired(this@WifiP2PJoinGroupAndSend,
            "This app needs following permissions in order to discover nearby devices and share data.\n1.Location permission\n2.Storage Permission"
            ,object:MyPermissions.PermissionsDeniedCallBack{
                override fun retryPermissions() {
                    startActivity(Intent(this@WifiP2PJoinGroupAndSend,WifiP2PCreateGroupAndReceive::class.java))
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
        //setAdapter
        adapterDevice = AdapterDevicesList(this, avalableP2pDevices)
        rc_deviceList?.adapter = adapterDevice
    }


    override fun onDestroy() {
        super.onDestroy()
        WifiP2PConnectionUtils.server?.close()
        WifiP2PConnectionUtils.socket?.close()
        WifiP2PConnectionUtils.clientSocket?.close()
        WifiP2PConnectionUtils.socket = null
        WifiP2PConnectionUtils.server = null
        WifiP2PConnectionUtils.clientSocket = null
        isRunning = false
        decodingDialogue?.dismiss();
        decodingDialogue = null

    }

    override fun selfDeviceInfo(wifiP2pDevice: WifiP2pDevice) {
        // wifiP2PConnectioinClass!!.createGroup()
        /* findViewById<TextView>(R.id.tv_myDeviceName)?.text=wifiP2pDevice?.deviceName!!
         findViewById<TextView>(R.id.tv_myDeviceAddress)?.text=wifiP2pDevice?.deviceAddress
         findViewById<TextView>(R.id.tv_myDeviceStatus)?.text=adapterDevice!!.getDeviceStatus(wifiP2pDevice?.status)*/

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


        if (requestCode == MyConstants.LOCATION_PERMISIION_REQUEST_CODE) {
            if (MyPermissions.hasLocationPermission(this@WifiP2PJoinGroupAndSend)) {
                if (MyPermissions.hasStoragePermission(this@WifiP2PJoinGroupAndSend)) {
                    joinGroup()
                } else {
                    MyPermissions.showStorageExplanation(this@WifiP2PJoinGroupAndSend,
                        object : MyPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                MyPermissions.requestStoragePermission(this@WifiP2PJoinGroupAndSend)
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
                        MyPermissions.showLocationRational(this@WifiP2PJoinGroupAndSend,
                            object : MyPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        MyConstants.LOCATION_PERMISIION_REQUEST_CODE
                                    )
                                }

                                override fun dismissed() {
                                    if (!MyPermissions.hasStoragePermission(this@WifiP2PJoinGroupAndSend)) {
                                        MyPermissions.showStorageExplanation(this@WifiP2PJoinGroupAndSend,
                                            object : MyPermissions.ExplanationCallBack {
                                                override fun requestPermission() {
                                                    MyPermissions.requestStoragePermission(this@WifiP2PJoinGroupAndSend)
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
                        if (!MyPermissions.hasStoragePermission(this@WifiP2PJoinGroupAndSend)) {
                            MyPermissions.showStorageExplanation(this@WifiP2PJoinGroupAndSend,
                                object : MyPermissions.ExplanationCallBack {
                                    override fun requestPermission() {
                                        MyPermissions.requestStoragePermission(this@WifiP2PJoinGroupAndSend)
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
                    if (!MyPermissions.hasStoragePermission(this@WifiP2PJoinGroupAndSend)) {
                        MyPermissions.showStorageExplanation(this@WifiP2PJoinGroupAndSend,
                            object : MyPermissions.ExplanationCallBack {
                                override fun requestPermission() {
                                    MyPermissions.requestStoragePermission(this@WifiP2PJoinGroupAndSend)
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
        } else if (requestCode == MyConstants.SPRC) {
            if (MyPermissions.hasStoragePermission(this@WifiP2PJoinGroupAndSend)) {
                if (MyPermissions.hasLocationPermission(this@WifiP2PJoinGroupAndSend)) {
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
                        MyPermissions.showStorageRational(this@WifiP2PJoinGroupAndSend,
                            object : MyPermissions.RationalCallback {
                                override fun openSettings() {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
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
        if (resultCode == MyConstants.FINISH_ACTIVITY_CODE) {
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
            MyConstants.FILES_TO_SHARE.clear()
            wifiP2PConnectioinUtils?.removeGroup()
            finishThisActivity()
        }else if (requestCode == MyConstants.WIFI_TURN_ON) {
            if (WifiUtilsClass(this@WifiP2PJoinGroupAndSend).getWifiState()) {
                if (MyPermissions(
                        applicationContext,
                        this@WifiP2PJoinGroupAndSend
                    ).CheckIfGPSisOn()
                ) {
                    wifiP2PConnectioinUtils= WifiP2PConnectionUtils(this)
                    //turn wifi on for connection on both sides
                    // wifiP2PConnectioinClass?.turnWifiOn(true)
                    wifiP2PConnectioinUtils?.groupcreator = true
                    wifiP2PConnectioinUtils?.removeGroup()
                    //wifiP2PConnectioinClass?.createGroup()
                    wifiP2PConnectioinUtils?.discoverPeers()
                } else {
                    MyPermissions(applicationContext, this@WifiP2PJoinGroupAndSend).turnGpsOn()
                }
            } else {
                if (!MyPermissions(
                        applicationContext,
                        this@WifiP2PJoinGroupAndSend
                    ).CheckIfGPSisOn()
                ) {
                    MyPermissions(applicationContext, this@WifiP2PJoinGroupAndSend).turnGpsOn()
                }
            }
        } else if (requestCode == MyConstants.REQUEST_LOCATION_TURNON) {
            if (WifiUtilsClass(this@WifiP2PJoinGroupAndSend).getWifiState()) {
                if (MyPermissions(
                        applicationContext,
                        this@WifiP2PJoinGroupAndSend
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
            if (MyPermissions.hasLocationPermission(this@WifiP2PJoinGroupAndSend)) {
                if (MyPermissions.hasStoragePermission(this@WifiP2PJoinGroupAndSend)) {
                    joinGroup()
                } else {
                    MyPermissions.showStorageExplanation(this@WifiP2PJoinGroupAndSend,
                        object : MyPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                MyPermissions.requestStoragePermission(this@WifiP2PJoinGroupAndSend)
                            }

                            override fun denyPermission() {
                                bothPermissionsDenied()
                            }

                        })
                }
            } else {
                if (!MyPermissions.hasStoragePermission(this@WifiP2PJoinGroupAndSend)) {
                    MyPermissions.showStorageExplanation(this@WifiP2PJoinGroupAndSend,
                        object : MyPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                MyPermissions.requestStoragePermission(this@WifiP2PJoinGroupAndSend)
                            }

                            override fun denyPermission() {
                                bothPermissionsDenied()
                            }

                        })
                }
            }
        } else if (requestCode == MyConstants.SPRC) {
            if (!MyPermissions.hasStoragePermission(this@WifiP2PJoinGroupAndSend)) {
                finish()
            } else {
                if (MyPermissions.hasLocationPermission(this@WifiP2PJoinGroupAndSend)) {
                   joinGroup()
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
        finishThisActivity()
        super.onBackPressed()

    }

    fun finishThisActivity() {
        val intent = Intent()
        setResult(MyConstants.SELCTION_ACTIVITY, intent)
        finish()
    }

    override fun peersAvailable(wifiP2pDeviceList: Collection<WifiP2pDevice>) {

        findViewById<TextView>(R.id.tv_tap).visibility = View.VISIBLE

        avalableP2pDevices = wifiP2pDeviceList.toList()

        Log.d(
            MyConstants.TAG,
            "peersAvailable: ${wifiP2pDeviceList.size} ${this.avalableP2pDevices.size}"
        )
        adapterDevice!!.wifiP2pDeviceList = ArrayList(wifiP2pDeviceList.toList())
        adapterDevice?.notifyDataSetChanged()


        var rand = Random()
        clearPeersView()
        for (i in adapterDevice?.wifiP2pDeviceList?.indices!!) {
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

            showPeers(index, adapterDevice?.wifiP2pDeviceList!![i])

        }
        if (adapterDevice?.wifiP2pDeviceList?.size!! < 6) {
            adapterDevice?.wifiP2pDeviceList = ArrayList()
            adapterDevice?.notifyDataSetChanged()
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
            wifiP2PConnectioinUtils?.removeGroup()
            if(devic!=null){
                decodeData()
                Handler().postDelayed({
                    wifiP2PConnectioinUtils?.connect( devic)
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
        wifiP2PConnectioinUtils?.paused()
        wifiP2PConnectioinUtils?.stopPeerDiscovery()
        isRunning = false
        super.onPause()
    }

    override fun deviceClicked(position: Int) {
        decodeData()
        wifiP2PConnectioinUtils?.removeGroup()
        Handler().postDelayed({
            wifiP2PConnectioinUtils?.connect( avalableP2pDevices[position])
        },1000)
        Handler().postDelayed({
            decodingDialogue?.dismiss()
        },30000)
    }

    var decodingDialogue: Dialog? = null
    fun decodeData() {
        decodingDialogue = Dialog(this@WifiP2PJoinGroupAndSend)
        decodingDialogue?.setCancelable(false)
        decodingDialogue?.setContentView(R.layout.db_decoding_data)
        decodingDialogue?.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tv_title = decodingDialogue?.findViewById<TextView>(R.id.tv_deco_msg)
        tv_title?.text = "Please wait while establishing Connection"

        decodingDialogue?.show()
    }
     fun loadFbBannerAdd() {

         val adView = AdView(
             this@WifiP2PJoinGroupAndSend,
             this@WifiP2PJoinGroupAndSend.resources.getString(R.string.banner_add),
             AdSize.BANNER_HEIGHT_50
         )

         val adListener: AdListener = object : AdListener {

             override fun onError(ad: Ad, adError: AdError) {
                 if (com.facebook.ads.BuildConfig.DEBUG) {
              /*        Toast.makeText(
                        this@SplashScreen,
                        "Error: " + adError.errorMessage,
                        Toast.LENGTH_LONG
                    )
                        .show()*/
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

        val mAdView = com.google.android.gms.ads.AdView(this@WifiP2PJoinGroupAndSend)
        val adSize: com.google.android.gms.ads.AdSize = IntersitialAdHelper.getAdSize(this@WifiP2PJoinGroupAndSend)
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
                Log.d(MyConstants.TAG, "onAdFailedToLoad: ${p0}")
                //adViewLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
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