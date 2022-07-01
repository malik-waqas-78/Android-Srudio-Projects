package com.zak.clone.zakactivity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager


import com.zak.clone.R
import com.zak.clone.zakadapters.ZakAdapterSendingData

import com.zak.clone.zakconstants.ZakMyConstants
import com.zak.clone.zakconstants.ZakMyConstants.get.TESTING_TAG
import com.zak.clone.databinding.ZakActivitySendDataBinding
import com.zak.clone.zakdatautils.*



import com.zak.clone.zakinterfaces.ZakClientCallBacks
import com.zak.clone.zakmodelclasses.ZakTransferDataModel


import com.zak.clone.zakmodelclasses.ZakDetailsInfoToTransferClass

import com.zak.clone.zaksockets.ZakSendingClient
import com.zak.clone.zakutills.ZakWifiP2PConnectionUtils


class ZakActivitySendData : AppCompatActivity(), ZakClientCallBacks {

    private var timerCount: Long = 0L
    private var stop = false

    var SendingClient: ZakSendingClient? = null

    var transferHSDetailsClass: ZakDetailsInfoToTransferClass? = null


    var mDataList = ArrayList<ZakTransferDataModel>()

    var HSAdapter: ZakAdapterSendingData? = null

    lateinit var binding: ZakActivitySendDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ZakActivitySendDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

         /*loadFbBannerAdd()
         //HSIntersitialAdHelper.showAdd()
         //  IntersitialAdds.showAdmobIntersitial(this@LaunchSendData)
         admobBanner()*/


        // initializeView()
        transferHSDetailsClass = ZakDetailsInfoToTransferClass()
        transferHSDetailsClass?.let {
            it.getTotalSizeToSend()
            it.convertToProperStorageType()
        }
        transferHSDetailsClass?.noOfContactsToShare=ZakDataSelectionActivity.selectedContactsList.size
        transferHSDetailsClass?.noOfCalendarEventsToShare=ZakDataSelectionActivity.selectedCalendarEventsList.size

        initView()
        startTimer()
        sendConnectionRequestToSendTransferDetails()

        binding.btnBack.setOnClickListener {
            btnBackPressed()
        }

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
        SendingClient = transferHSDetailsClass?.let { ZakSendingClient(this, it) }

        if (intent.hasExtra("owner")) {
            var owner = intent.getBooleanExtra("owner", false)
            if (owner) {
                Log.d(TESTING_TAG, "createConnection: owner")
                ZakSendingClient.socket = ZakWifiP2PConnectionUtils.socket
            } else {
                Log.d(TESTING_TAG, "createConnection: not owner")
                ZakSendingClient.socket = ZakWifiP2PConnectionUtils.clientSocket
            }
        }
        SendingClient?.startSendingConnectionRequest()
    }



    private fun initView() {
        mDataList.add(
            ZakTransferDataModel(
                ZakMyConstants.FILE_TYPE_CONTACTS,
                "Size ${transferHSDetailsClass?.getSizeInHumanFormat(transferHSDetailsClass?.totalContactsBytes?.toDouble())},${transferHSDetailsClass?.noOfContactsToShare} items",
                ZakDetailsInfoToTransferClass.CONTACT_ICON
            )
        )
        mDataList.add(
            ZakTransferDataModel(
                ZakMyConstants.FILE_TYPE_PICS,
                "Size ${transferHSDetailsClass?.getSizeInHumanFormat(transferHSDetailsClass?.totalPhotosBytes?.toDouble())},${transferHSDetailsClass?.noOfPhotosToShare} items",
                ZakDetailsInfoToTransferClass.IMAGES_ICON
            )
        )
        mDataList.add(
            ZakTransferDataModel(
                ZakMyConstants.FILE_TYPE_CALENDAR,
                "Size ${transferHSDetailsClass?.getSizeInHumanFormat(transferHSDetailsClass?.totalCalendarBytes?.toDouble())},${transferHSDetailsClass?.noOfCalendarEventsToShare} items",
                ZakDetailsInfoToTransferClass.CALEDAR_ICON
            )
        )
        mDataList.add(
            ZakTransferDataModel(
                ZakMyConstants.FILE_TYPE_VIDEOS,
                "Size ${transferHSDetailsClass?.getSizeInHumanFormat(transferHSDetailsClass?.totalVideosBytes?.toDouble())},${transferHSDetailsClass?.noOfVideosToShare} items",
                ZakDetailsInfoToTransferClass.Videos_ICON
            )
        )
        mDataList.add(
            ZakTransferDataModel(
                ZakMyConstants.FILE_TYPE_APPS,
                "Size ${transferHSDetailsClass?.getSizeInHumanFormat(transferHSDetailsClass?.totalAppsBytes?.toDouble())},${transferHSDetailsClass?.noOfAppsToShare} items",
                ZakDetailsInfoToTransferClass.APPS_ICON
            )
        )
        mDataList.add(
            ZakTransferDataModel(
                ZakMyConstants.FILE_TYPE_AUDIOS,
                "Size ${transferHSDetailsClass?.getSizeInHumanFormat(transferHSDetailsClass?.totalAudiosBytes?.toDouble())},${transferHSDetailsClass?.noOfAudiosToShare} items",
                ZakDetailsInfoToTransferClass.AUDIOS_ICON
            )
        )
        mDataList.add(
            ZakTransferDataModel(
                ZakMyConstants.FILE_TYPE_DOCS,
                "Size ${transferHSDetailsClass?.getSizeInHumanFormat(transferHSDetailsClass?.totalDocBytes?.toDouble())},${transferHSDetailsClass?.noOfDocsToShare} items",
                ZakDetailsInfoToTransferClass.DOCS_ICON
            )
        )

        HSAdapter = ZakAdapterSendingData(this@ZakActivitySendData, mDataList)

        binding.rcDatalist.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        binding.rcDatalist.adapter = HSAdapter

        binding.tvTotalSendData.text = transferHSDetailsClass?.sizeInProperFormat+" Data"

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
        val dialog = Dialog(this@ZakActivitySendData)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.zak_db_backpressed)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
                setResult(ZakMyConstants.FINISH_ACTIVITY_CODE)
                finish()
            }
        }
    }


    fun startTimer() {
        Handler(Looper.getMainLooper()).postDelayed({
            timerCount+=1000
            val time: String = timerCount.getDuration()
            runOnUiThread {
                findViewById<TextView>(R.id.tv_timer).setText("Time Taken "+time)
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


    override fun updateView(transferHSDetailsClass: ZakDetailsInfoToTransferClass) {
        runOnUiThread {
            val totalProgress = transferHSDetailsClass.getTotalProgress()
            binding.pbarTotalprogress.progress = totalProgress
            binding.tvTotalDataSent.text =
               transferHSDetailsClass.getSizeInHumanFormat(transferHSDetailsClass.totalBytesTransferred.toDouble())+" Sent"
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
            val dialog = Dialog(this@ZakActivitySendData)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.zak_db_transfer_completed)
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val positive: Button = dialog.findViewById(R.id.btn_error_ok)

            positive.setOnClickListener {
                dialog.dismiss()
                setResult(ZakMyConstants.FINISH_ACTIVITY_CODE)
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
                setResult(ZakMyConstants.FINISH_ACTIVITY_CODE)
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