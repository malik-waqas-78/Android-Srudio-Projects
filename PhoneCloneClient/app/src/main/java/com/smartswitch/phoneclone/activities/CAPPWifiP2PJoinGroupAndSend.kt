package com.smartswitch.phoneclone.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.net.wifi.p2p.WifiP2pDevice
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartswitch.phoneclone.R

import com.smartswitch.phoneclone.constants.CAPPMConstants
import com.smartswitch.phoneclone.interfaces.CAPPWifiP2PCallBacks
import java.util.*
import android.os.Handler
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.smartswitch.phoneclone.adapters.CAPPAdapterDevicesList
//import com.switchphone.transferdata.ads.AATIntersitialAdHelper
//import com.switchphone.transferdata.ads.AATNativeAdHelper

import com.smartswitch.phoneclone.utills.CAPPMDialogues
import com.smartswitch.phoneclone.utills.CAPPMPermissions
import com.smartswitch.phoneclone.utills.CAPPWifiPeer2PConnectionUtils
import com.smartswitch.phoneclone.utills.CAPPWifiUtilsClass


class CAPPWifiP2PJoinGroupAndSend : AppCompatActivity(), CAPPWifiP2PCallBacks {
    var HSWifiP2PConnectioinUtils: CAPPWifiPeer2PConnectionUtils? = null
    var rc_deviceList: RecyclerView? = null
    var HSAdapterDevice: CAPPAdapterDevicesList? = null
    var avalableP2pDevices: List<WifiP2pDevice> = ArrayList()
    var isRunning = true

    var toolbar:Toolbar?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_group_capp)


//        AATNativeAdHelper.showAdmobNativeAd(this, findViewById(R.id.ad_frame))

        /*android.os.Handler().postDelayed({
            if (isRunning) {
                findViewById<TextView>(R.id.tv_retry).visibility = View.VISIBLE
                findViewById<TextView>(R.id.tv_retry).text =
                    "If you are not able to find your device please retry."
                findViewById<Button>(R.id.retry).visibility = View.VISIBLE
                findViewById<Button>(R.id.retry).setOnClickListener {
                    val intent = Intent()
                    setResult(AATMConstants.RESTART_JOIN, intent)
                    finish()
                }
            }
        }, 30000)
        HSWifiP2PConnectioinUtils = AATWifiPeer2PConnectionUtils(this@AATWifiP2PJoinGroupAndSend)
        HSWifiP2PConnectioinUtils?.resumed()
        checkForpermissions()
        initView()*/


//        if (AATIntersitialAdHelper.isAppInstalledFromPlay(this)) {
//            AATIntersitialAdHelper.showAdmobIntersitial_mediated(
//                this@AATWifiP2PJoinGroupAndSend,
//                object : AATIntersitialAdHelper.Companion.AdLoadCallBack {
//                    override fun adClosed() {
//                        initfunction()
//                    }
//                })
//        } else {
            initfunction()

            toolbar?.setNavigationIcon(R.drawable.ic_group_back_white)
            toolbar?.setNavigationOnClickListener(View.OnClickListener {
                onBackPressed()
            })
//        }


/*
         HSIntersitialAdHelper.showAdd()
         admobBanner()
         loadFbBannerAdd()*/


    }

    fun initfunction() {
        toolbar=findViewById(R.id.toolbar)
        android.os.Handler().postDelayed({
            if (isRunning) {
//                findViewById<TextView>(R.id.tv_retry).visibility = View.VISIBLE
//                findViewById<TextView>(R.id.tv_retry).text =
//                    "If you are not able to find your device please retry."
                findViewById<Button>(R.id.retry).visibility = View.VISIBLE
                findViewById<Button>(R.id.retry).setOnClickListener {
                    val intent = Intent()
                    setResult(CAPPMConstants.RESTART_JOIN, intent)
                    finish()
                }
            }
        }, 30000)
        HSWifiP2PConnectioinUtils = CAPPWifiPeer2PConnectionUtils(this@CAPPWifiP2PJoinGroupAndSend)
        HSWifiP2PConnectioinUtils?.resumed()
        checkForpermissions()
        initView()
    }

    override fun onResume() {
        super.onResume()
        decodingDialogue = null
        HSWifiP2PConnectioinUtils?.resumed()
        isRunning = true
    }

    fun joinGroup() {
        if (CAPPWifiUtilsClass(this@CAPPWifiP2PJoinGroupAndSend).getWifiState()) {
            if (CAPPMPermissions(
                    applicationContext,
                    this@CAPPWifiP2PJoinGroupAndSend
                ).CheckIfGPSisOn()
            ) {
                HSWifiP2PConnectioinUtils?.removeGroup()
                HSWifiP2PConnectioinUtils?.discoverPeers()
            } else {
                CAPPMPermissions(applicationContext, this@CAPPWifiP2PJoinGroupAndSend).turnGpsOn()
            }
        } else {
            if (Build.VERSION.SDK_INT >= 29) {
                CAPPWifiUtilsClass(this@CAPPWifiP2PJoinGroupAndSend).changeWifiState(true)
            } else {
                CAPPWifiUtilsClass(this@CAPPWifiP2PJoinGroupAndSend).changeWifiState(true)
                if (CAPPMPermissions(
                        applicationContext,
                        this@CAPPWifiP2PJoinGroupAndSend
                    ).CheckIfGPSisOn()
                ) {
                    HSWifiP2PConnectioinUtils?.removeGroup()
                    HSWifiP2PConnectioinUtils?.discoverPeers()
                } else {
                    CAPPMPermissions(applicationContext, this@CAPPWifiP2PJoinGroupAndSend).turnGpsOn()
                }
            }
        }


    }

    private fun checkForpermissions() {
        if (CAPPMPermissions.hasLocationPermission(this@CAPPWifiP2PJoinGroupAndSend)) {
            if (CAPPMPermissions.hasStoragePermission(this@CAPPWifiP2PJoinGroupAndSend)) {
                joinGroup()
            } else {
                CAPPMPermissions.showStorageExplanation(this@CAPPWifiP2PJoinGroupAndSend,
                    object : CAPPMPermissions.ExplanationCallBack {
                        override fun requestPermission() {
                            CAPPMPermissions.requestStoragePermission(this@CAPPWifiP2PJoinGroupAndSend)
                        }

                        override fun denyPermission() {
                            bothPermissionsDenied()
                        }

                    })
            }
        } else {
            CAPPMPermissions.showLocationExplanation(this@CAPPWifiP2PJoinGroupAndSend,
                object : CAPPMPermissions.ExplanationCallBack {
                    override fun requestPermission() {
                        CAPPMPermissions.requestLocationPermission(this@CAPPWifiP2PJoinGroupAndSend)
                    }

                    override fun denyPermission() {
                        if (!CAPPMPermissions.hasStoragePermission(this@CAPPWifiP2PJoinGroupAndSend)) {
                            CAPPMPermissions.showStorageExplanation(this@CAPPWifiP2PJoinGroupAndSend,
                                object : CAPPMPermissions.ExplanationCallBack {
                                    override fun requestPermission() {
                                        CAPPMPermissions.requestStoragePermission(this@CAPPWifiP2PJoinGroupAndSend)
                                    }

                                    override fun denyPermission() {
                                        bothPermissionsDenied()
                                    }

                                })
                        } else {
                            if (CAPPMPermissions.hasLocationPermission(this@CAPPWifiP2PJoinGroupAndSend)) {
                                joinGroup()
                            } else {
                                bothPermissionsDenied()
                            }
                        }
                    }

                })
        }
    }

    fun bothPermissionsDenied() {
        CAPPMDialogues.showPermissionsRequired(this@CAPPWifiP2PJoinGroupAndSend,
            "This app needs following permissions in order to discover nearby devices and share data.\n1.Location permission\n2.Storage Permission",
            object : CAPPMPermissions.PermissionsDeniedCallBack {
                override fun retryPermissions() {
                    startActivity(
                        Intent(
                            this@CAPPWifiP2PJoinGroupAndSend,
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

    private fun initView() {
        rc_deviceList = findViewById(R.id.rv_deviceList)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rc_deviceList?.layoutManager = layoutManager
        //setHSAdapter
        HSAdapterDevice = CAPPAdapterDevicesList(this, avalableP2pDevices)
        rc_deviceList?.adapter = HSAdapterDevice
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

        if (permissions.isEmpty() || grantResults.isEmpty()) {
            return
        }


        if (requestCode == CAPPMConstants.LOCATION_PERMISIION_REQUEST_CODE) {
            if (CAPPMPermissions.hasLocationPermission(this@CAPPWifiP2PJoinGroupAndSend)) {
                if (CAPPMPermissions.hasStoragePermission(this@CAPPWifiP2PJoinGroupAndSend)) {
                    joinGroup()
                } else {
                    CAPPMPermissions.showStorageExplanation(this@CAPPWifiP2PJoinGroupAndSend,
                        object : CAPPMPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                CAPPMPermissions.requestStoragePermission(this@CAPPWifiP2PJoinGroupAndSend)
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
                        CAPPMPermissions.showLocationRational(this@CAPPWifiP2PJoinGroupAndSend,
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
                                    if (!CAPPMPermissions.hasStoragePermission(this@CAPPWifiP2PJoinGroupAndSend)) {
                                        CAPPMPermissions.showStorageExplanation(this@CAPPWifiP2PJoinGroupAndSend,
                                            object : CAPPMPermissions.ExplanationCallBack {
                                                override fun requestPermission() {
                                                    CAPPMPermissions.requestStoragePermission(this@CAPPWifiP2PJoinGroupAndSend)
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
                        if (!CAPPMPermissions.hasStoragePermission(this@CAPPWifiP2PJoinGroupAndSend)) {
                            CAPPMPermissions.showStorageExplanation(this@CAPPWifiP2PJoinGroupAndSend,
                                object : CAPPMPermissions.ExplanationCallBack {
                                    override fun requestPermission() {
                                        CAPPMPermissions.requestStoragePermission(this@CAPPWifiP2PJoinGroupAndSend)
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
                    if (!CAPPMPermissions.hasStoragePermission(this@CAPPWifiP2PJoinGroupAndSend)) {
                        CAPPMPermissions.showStorageExplanation(this@CAPPWifiP2PJoinGroupAndSend,
                            object : CAPPMPermissions.ExplanationCallBack {
                                override fun requestPermission() {
                                    CAPPMPermissions.requestStoragePermission(this@CAPPWifiP2PJoinGroupAndSend)
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
            if (CAPPMPermissions.hasStoragePermission(this@CAPPWifiP2PJoinGroupAndSend)) {
                if (CAPPMPermissions.hasLocationPermission(this@CAPPWifiP2PJoinGroupAndSend)) {
                    joinGroup()
                } else {
                    bothPermissionsDenied()
                }
            } else {

                if (Build.VERSION.SDK_INT >= 23) {
                    //show rational
                    var showRational = shouldShowRequestPermissionRationale(permissions[0])
                    //user deind permission permannently
                    if (!showRational) {
                        CAPPMPermissions.showStorageRational(this@CAPPWifiP2PJoinGroupAndSend,
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == CAPPMConstants.FINISH_ACTIVITY_CODE) {
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
            CAPPMConstants.FILES_TO_SHARE.clear()
            HSWifiP2PConnectioinUtils?.removeGroup()
            finishThisActivity()
        } else if (requestCode == CAPPMConstants.WIFI_TURN_ON) {
            if (CAPPWifiUtilsClass(this@CAPPWifiP2PJoinGroupAndSend).getWifiState()) {
                if (CAPPMPermissions(
                        applicationContext,
                        this@CAPPWifiP2PJoinGroupAndSend
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
                    CAPPMPermissions(applicationContext, this@CAPPWifiP2PJoinGroupAndSend).turnGpsOn()
                }
            } else {
                if (!CAPPMPermissions(
                        applicationContext,
                        this@CAPPWifiP2PJoinGroupAndSend
                    ).CheckIfGPSisOn()
                ) {
                    CAPPMPermissions(applicationContext, this@CAPPWifiP2PJoinGroupAndSend).turnGpsOn()
                }
            }
        } else if (requestCode == CAPPMConstants.REQUEST_LOCATION_TURNON) {
            if (CAPPWifiUtilsClass(this@CAPPWifiP2PJoinGroupAndSend).getWifiState()) {
                if (CAPPMPermissions(
                        applicationContext,
                        this@CAPPWifiP2PJoinGroupAndSend
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
            if (CAPPMPermissions.hasLocationPermission(this@CAPPWifiP2PJoinGroupAndSend)) {
                if (CAPPMPermissions.hasStoragePermission(this@CAPPWifiP2PJoinGroupAndSend)) {
                    joinGroup()
                } else {
                    CAPPMPermissions.showStorageExplanation(this@CAPPWifiP2PJoinGroupAndSend,
                        object : CAPPMPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                CAPPMPermissions.requestStoragePermission(this@CAPPWifiP2PJoinGroupAndSend)
                            }

                            override fun denyPermission() {
                                bothPermissionsDenied()
                            }

                        })
                }
            } else {
                if (!CAPPMPermissions.hasStoragePermission(this@CAPPWifiP2PJoinGroupAndSend)) {
                    CAPPMPermissions.showStorageExplanation(this@CAPPWifiP2PJoinGroupAndSend,
                        object : CAPPMPermissions.ExplanationCallBack {
                            override fun requestPermission() {
                                CAPPMPermissions.requestStoragePermission(this@CAPPWifiP2PJoinGroupAndSend)
                            }

                            override fun denyPermission() {
                                bothPermissionsDenied()
                            }

                        })
                }
            }
        } else if (requestCode == CAPPMConstants.SPRC) {
            if (!CAPPMPermissions.hasStoragePermission(this@CAPPWifiP2PJoinGroupAndSend)) {
                finish()
            } else {
                if (CAPPMPermissions.hasLocationPermission(this@CAPPWifiP2PJoinGroupAndSend)) {
                    joinGroup()
                } else {
                    bothPermissionsDenied()
                }
            }
        }

        else if (requestCode == CAPPMConstants.ManageAllFilePermissionRequestCode) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    if (CAPPMPermissions.hasLocationPermission(this@CAPPWifiP2PJoinGroupAndSend)) {
                        joinGroup()
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
        finishThisActivity()
        super.onBackPressed()

    }

    fun finishThisActivity() {
        val intent = Intent()
        setResult(CAPPMConstants.SELCTION_ACTIVITY, intent)
        finish()
    }

    override fun peersAvailable(wifiP2pDeviceList: Collection<WifiP2pDevice>) {

//        findViewById<TextView>(R.id.tv_tap).visibility = View.VISIBLE

        avalableP2pDevices = wifiP2pDeviceList.toList()

        Log.d(
            CAPPMConstants.TAG,
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


        cl_peer?.setOnClickListener {
            HSWifiP2PConnectioinUtils?.removeGroup()
            if (devic != null) {
                decodeData()
                Handler().postDelayed({
                    HSWifiP2PConnectioinUtils?.connect(devic)
                }, 1000)
                Handler().postDelayed({
                    decodingDialogue?.dismiss()
                }, 30000)
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
            HSWifiP2PConnectioinUtils?.connect(avalableP2pDevices[position])
        }, 1000)
        Handler().postDelayed({
            decodingDialogue?.dismiss()
        }, 30000)
    }

    var decodingDialogue: Dialog? = null
    fun decodeData() {
        decodingDialogue = Dialog(this@CAPPWifiP2PJoinGroupAndSend)
        decodingDialogue?.setCancelable(false)
        decodingDialogue?.setContentView(R.layout.db_decoding_data_capp)
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