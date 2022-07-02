package com.smartswitch.phoneclone.activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager


import com.smartswitch.phoneclone.R
import com.smartswitch.phoneclone.adapters.CAPPAdapterSendingData
//import com.switchphone.transferdata.ads.AATIntersitialAdHelper

import com.smartswitch.phoneclone.constants.CAPPMConstants
import com.smartswitch.phoneclone.constants.CAPPMConstants.get.TESTING_TAG
import com.smartswitch.phoneclone.databinding.ActivitySendDataCappBinding


import com.smartswitch.phoneclone.interfaces.CAPPClientCallBacks
import com.smartswitch.phoneclone.modelclasses.CAPPTransferDataModel


import com.smartswitch.phoneclone.modelclasses.CAPPDetailsInfoToTransferClass

import com.smartswitch.phoneclone.sockets.CAPPSendingClient
import com.smartswitch.phoneclone.utills.CAPPWifiPeer2PConnectionUtils


class CAPPActivitySendData : AppCompatActivity(), CAPPClientCallBacks {

    private var timerCount: Long = 0L
    private var stop = false

    var sendingClient: CAPPSendingClient? = null

    var transferHSDetailsClass: CAPPDetailsInfoToTransferClass? = null


    var mDataList = ArrayList<CAPPTransferDataModel>()

    var HSAdapter: CAPPAdapterSendingData? = null

    lateinit var binding: ActivitySendDataCappBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendDataCappBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationIcon(R.drawable.ic_back_cross)
        binding.toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })

         /*loadFbBannerAdd()
         //HSIntersitialAdHelper.showAdd()
         //  IntersitialAdds.showAdmobIntersitial(this@LaunchSendData)
         admobBanner()*/

//        AATIntersitialAdHelper.loadAdmobBanner(
//            this,
//            findViewById(R.id.top_banner),
//            resources.getString(R.string.admob_banner)
//        )


        // initializeView()
        transferHSDetailsClass = CAPPDetailsInfoToTransferClass()
        transferHSDetailsClass?.let {
            it.getTotalSizeToSend()
            it.convertToProperStorageType()
        }
        transferHSDetailsClass?.noOfContactsToShare=CAPPDataSelectionMainActivity.selectedContactsList.size
        transferHSDetailsClass?.noOfCalendarEventsToShare=CAPPDataSelectionMainActivity.selectedCalendarEventsList.size

        initView()
        startTimer()
        sendConnectionRequestToSendTransferDetails()



        /*serverSocket = ServerSocket(this)
        if(intent.hasExtra("owner")){
            var owner=intent.getBooleanExtra("owner",false)
            if(owner){
                Log.d("92727", "createConnection: owner")
                serverSocket?.socket=WifiP2PConnectionClass.socket
            }else{
                Log.d("92727", "createConnection: not owner")
                serverSocket?.socket=WifiP2PConnectionClass.clientSocket
            }
        }
       // serverSocket?.socket=WifiP2PConnectionClass.clientSocket
        if (intent.hasExtra("ip"))
        {
            intent.getStringExtra("ip")?.let { serverSocket?.setIpAddress(it) }
           // Toast.makeText(this@LaunchSendData,"ip ${intent.getStringExtra("ip")}",Toast.LENGTH_SHORT).show()
        }
        serverSocket?.receiveConnection()*/
    }

    private fun sendConnectionRequestToSendTransferDetails() {
        sendingClient = transferHSDetailsClass?.let { CAPPSendingClient(this, it) }

        if (intent.hasExtra("owner")) {
            var owner = intent.getBooleanExtra("owner", false)
            if (owner) {
                Log.d(TESTING_TAG, "createConnection: owner")
                CAPPSendingClient.socket = CAPPWifiPeer2PConnectionUtils.socket
            } else {
                Log.d(TESTING_TAG, "createConnection: not owner")
                CAPPSendingClient.socket = CAPPWifiPeer2PConnectionUtils.clientSocket
            }
        }
        sendingClient?.startSendingConnectionRequest()
    }



    private fun initView() {
        mDataList.add(
            CAPPTransferDataModel(
                CAPPMConstants.FILE_TYPE_CONTACTS,
                "Size ${transferHSDetailsClass?.getSizeInHumanFormat(transferHSDetailsClass?.totalContactsBytes?.toDouble())},${transferHSDetailsClass?.noOfContactsToShare} items",
                CAPPDetailsInfoToTransferClass.CONTACT_ICON
            )
        )
        mDataList.add(
            CAPPTransferDataModel(
                CAPPMConstants.FILE_TYPE_PICS,
                "Size ${transferHSDetailsClass?.getSizeInHumanFormat(transferHSDetailsClass?.totalPhotosBytes?.toDouble())},${transferHSDetailsClass?.noOfPhotosToShare} items",
                CAPPDetailsInfoToTransferClass.IMAGES_ICON
            )
        )
        mDataList.add(
            CAPPTransferDataModel(
                CAPPMConstants.FILE_TYPE_CALENDAR,
                "Size ${transferHSDetailsClass?.getSizeInHumanFormat(transferHSDetailsClass?.totalCalendarBytes?.toDouble())},${transferHSDetailsClass?.noOfCalendarEventsToShare} items",
                CAPPDetailsInfoToTransferClass.CALEDAR_ICON
            )
        )
        mDataList.add(
            CAPPTransferDataModel(
                CAPPMConstants.FILE_TYPE_VIDEOS,
                "Size ${transferHSDetailsClass?.getSizeInHumanFormat(transferHSDetailsClass?.totalVideosBytes?.toDouble())},${transferHSDetailsClass?.noOfVideosToShare} items",
                CAPPDetailsInfoToTransferClass.Videos_ICON
            )
        )
        mDataList.add(
            CAPPTransferDataModel(
                CAPPMConstants.FILE_TYPE_APPS,
                "Size ${transferHSDetailsClass?.getSizeInHumanFormat(transferHSDetailsClass?.totalAppsBytes?.toDouble())},${transferHSDetailsClass?.noOfAppsToShare} items",
                CAPPDetailsInfoToTransferClass.APPS_ICON
            )
        )
        mDataList.add(
            CAPPTransferDataModel(
                CAPPMConstants.FILE_TYPE_AUDIOS,
                "Size ${transferHSDetailsClass?.getSizeInHumanFormat(transferHSDetailsClass?.totalAudiosBytes?.toDouble())},${transferHSDetailsClass?.noOfAudiosToShare} items",
                CAPPDetailsInfoToTransferClass.AUDIOS_ICON
            )
        )
        mDataList.add(
            CAPPTransferDataModel(
                CAPPMConstants.FILE_TYPE_DOCS,
                "Size ${transferHSDetailsClass?.getSizeInHumanFormat(transferHSDetailsClass?.totalDocBytes?.toDouble())},${transferHSDetailsClass?.noOfDocsToShare} items",
                CAPPDetailsInfoToTransferClass.DOCS_ICON
            )
        )

        HSAdapter = CAPPAdapterSendingData(this@CAPPActivitySendData, mDataList)

        binding.rcDatalist.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        binding.rcDatalist.adapter = HSAdapter

        binding.tvTotalSendData.text = transferHSDetailsClass?.sizeInProperFormat+""

        binding.clReceivingconnection.visibility = View.GONE
        binding.clReceivingList.visibility = View.VISIBLE
    }


    override fun onBackPressed() {
        btnBackPressed()
    }

    override fun onResume() {
        super.onResume()

    }

    fun btnBackPressed() {
        val dialog = Dialog(this@CAPPActivitySendData)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.db_backpressed_capp)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        val btn_ok: Button = dialog.findViewById(R.id.btn_error_ok)
        val btn_exit: Button = dialog.findViewById(R.id.btn_exit)
        btn_ok.setOnClickListener {
            dialog.dismiss()

        }
        btn_exit.setOnClickListener {
            //exit activity
            stop = true
            dialog.dismiss()
            finish()
        }
        dialog.setOnCancelListener {
            //dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.cancel()
        }
        dialog.show()
    }


    fun socketErrorOccoured() {

        if (findViewById<ConstraintLayout>(R.id.cl_erroroccoured) != null) {
            findViewById<ConstraintLayout>(R.id.cl_erroroccoured).visibility = View.VISIBLE
            findViewById<Button>(R.id.btn_error_ok).setOnClickListener {
                stop = true
                setResult(CAPPMConstants.FINISH_ACTIVITY_CODE)
                finish()
            }
        }
    }


    fun startTimer() {
        Handler(Looper.getMainLooper()).postDelayed({
            timerCount+=1000
            val time: String = timerCount.getDuration()
            runOnUiThread {
                findViewById<TextView>(R.id.tv_timer).setText("Time "+time)
            }
            if (!stop) {
                startTimer()
            }
        }, 1000)
    }

    fun Long?.getDuration(): String {
        val durationInMillie = this
        var totalSec = durationInMillie?.div(1000)
        var totalMin = totalSec?.div(60)
        totalSec = totalSec?.rem(60)
        val totalHours = totalMin?.div(60)
        totalMin = totalMin?.rem(60)
        val th = if (totalHours!! < 10) """0$totalHours""" else totalHours
        val tm = if (totalMin!! < 10) """0$totalMin""" else totalMin
        val ts = if (totalSec!! < 10) """0$totalSec""" else totalSec
        return """$th:$tm:$ts"""

    }


    override fun updateView(transferHSDetailsClass: CAPPDetailsInfoToTransferClass) {
        runOnUiThread {
            val totalProgress = transferHSDetailsClass.getTotalProgress()
            binding.pbarTotalprogress.progress = totalProgress
            binding.tvTotalDataSent.text =
               "Sent "+ transferHSDetailsClass.getSizeInHumanFormat(transferHSDetailsClass.totalBytesTransferred.toDouble())
            binding.pbarPercent.text = "$totalProgress%"
            mDataList[0].progress = transferHSDetailsClass.getContactsProgress()
            mDataList[1].progress = transferHSDetailsClass.getPhotosProgress()
            mDataList[2].progress = transferHSDetailsClass.getEventsProgress()
            mDataList[3].progress = transferHSDetailsClass.getVideosProgress()
            mDataList[4].progress = transferHSDetailsClass.getAppsProgress()
            mDataList[5].progress = transferHSDetailsClass.getAudiosProgress()
            mDataList[6].progress = transferHSDetailsClass.getDocsProgress()
            HSAdapter?.notifyDataSetChanged()
        }
    }

    override fun transferFinished() {
        stop = true
        runOnUiThread {
            val dialog = Dialog(this@CAPPActivitySendData)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.db_transfer_completed_capp)
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val positive: Button = dialog.findViewById(R.id.btn_error_ok)

            positive.setOnClickListener {
                dialog.dismiss()
                setResult(CAPPMConstants.FINISH_ACTIVITY_CODE)
                finish()
            }
            if(!this.isFinishing){
                dialog.show()
            }
        }
    }

    override fun errorOccurred() {
        runOnUiThread{
            binding.clErroroccoured.visibility=View.VISIBLE
            binding.includedView.btnErrorOk.setOnClickListener {
                setResult(CAPPMConstants.FINISH_ACTIVITY_CODE)
                finish()
            }
        }
    }

/*    fun loadFbBannerAdd() {

        val adView = AdView(
            this@HSActivitySendData,
            this@HSActivitySendData.resources.getString(R.string.banner_add),
            AdSize.BANNER_HEIGHT_50
        )

        val adListener: AdListener = object : AdListener {

            override fun onError(ad: Ad, adError: AdError) {
              *//*  if (com.facebook.ads.BuildConfig.DEBUG) {
                    Toast.makeText(
                        this@HSActivitySendData,
                        "Error: " + adError.errorMessage,
                        Toast.LENGTH_LONG
                    )
                        .show()
                }*//*
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
        findViewById<RelativeLayout>(R.id.top_banner).addView(adView)
    }
    fun admobBanner() {

        val mAdView = com.google.android.gms.ads.AdView(this@HSActivitySendData)
        val adSize: com.google.android.gms.ads.AdSize = HSIntersitialAdHelper.getAdSize(this@HSActivitySendData)
        mAdView.adSize = adSize

        mAdView.adUnitId = resources.getString(R.string.admob_banner)

        val adRequest = AdRequest.Builder().build()

        val adViewLayout = findViewById<View>(R.id.bottom_banner) as RelativeLayout
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

    }*/

}