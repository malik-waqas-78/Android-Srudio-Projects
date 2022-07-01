package com.phoneclone.data.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.facebook.ads.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError

import com.phoneclone.data.R
import com.phoneclone.data.adapters.AdapterSendingData
import com.phoneclone.data.ads.IntersitialAdHelper
import com.phoneclone.data.ads.isAppInstalledFromPlay

import com.phoneclone.data.constants.MyConstants
import com.phoneclone.data.constants.MyConstants.get.TESTING_TAG
import com.phoneclone.data.databinding.ActivitySendDataBinding
import com.phoneclone.data.datautils.*



import com.phoneclone.data.interfaces.ClientCallBacks
import com.phoneclone.data.modelclasses.TransferDataModel


import com.phoneclone.data.modelclasses.DetailsInfoToTransferClass

import com.phoneclone.data.sockets.SendingClient
import com.phoneclone.data.utills.WifiP2PConnectionUtils


class ActivitySendData : AppCompatActivity(), ClientCallBacks {

    private var timerCount: Long = 0L
    private var stop = false

    var sendingClient: SendingClient? = null

    var transferDetailsClass: DetailsInfoToTransferClass? = null


    var mDataList = ArrayList<TransferDataModel>()

    var adapter: AdapterSendingData? = null

    lateinit var binding: ActivitySendDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(isAppInstalledFromPlay(this)){
            loadFbBannerAdd()
            admobBanner()
        }


        // initializeView()
        transferDetailsClass = DetailsInfoToTransferClass()
        transferDetailsClass?.let {
            it.getTotalSizeToSend()
            it.convertToProperStorageType()
        }
        transferDetailsClass?.noOfContactsToShare=SelectionActivity.selectedContactsList.size
        transferDetailsClass?.noOfCalendarEventsToShare=SelectionActivity.selectedCalendarEventsList.size

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
        sendingClient = transferDetailsClass?.let { SendingClient(this, it) }

        if (intent.hasExtra("owner")) {
            var owner = intent.getBooleanExtra("owner", false)
            if (owner) {
                Log.d(TESTING_TAG, "createConnection: owner")
                SendingClient.socket = WifiP2PConnectionUtils.socket
            } else {
                Log.d(TESTING_TAG, "createConnection: not owner")
                SendingClient.socket = WifiP2PConnectionUtils.clientSocket
            }
        }
        sendingClient?.startSendingConnectionRequest()
    }



    private fun initView() {
        mDataList.add(
            TransferDataModel(
                MyConstants.FILE_TYPE_CONTACTS,
                "Size ${transferDetailsClass?.getSizeInHumanFormat(transferDetailsClass?.totalContactsBytes?.toDouble())},${transferDetailsClass?.noOfContactsToShare} items",
                DetailsInfoToTransferClass.CONTACT_ICON
            )
        )
        mDataList.add(
            TransferDataModel(
                MyConstants.FILE_TYPE_PICS,
                "Size ${transferDetailsClass?.getSizeInHumanFormat(transferDetailsClass?.totalPhotosBytes?.toDouble())},${transferDetailsClass?.noOfPhotosToShare} items",
                DetailsInfoToTransferClass.IMAGES_ICON
            )
        )
        mDataList.add(
            TransferDataModel(
                MyConstants.FILE_TYPE_CALENDAR,
                "Size ${transferDetailsClass?.getSizeInHumanFormat(transferDetailsClass?.totalCalendarBytes?.toDouble())},${transferDetailsClass?.noOfCalendarEventsToShare} items",
                DetailsInfoToTransferClass.CALEDAR_ICON
            )
        )
        mDataList.add(
            TransferDataModel(
                MyConstants.FILE_TYPE_VIDEOS,
                "Size ${transferDetailsClass?.getSizeInHumanFormat(transferDetailsClass?.totalVideosBytes?.toDouble())},${transferDetailsClass?.noOfVideosToShare} items",
                DetailsInfoToTransferClass.Videos_ICON
            )
        )
        mDataList.add(
            TransferDataModel(
                MyConstants.FILE_TYPE_APPS,
                "Size ${transferDetailsClass?.getSizeInHumanFormat(transferDetailsClass?.totalAppsBytes?.toDouble())},${transferDetailsClass?.noOfAppsToShare} items",
                DetailsInfoToTransferClass.APPS_ICON
            )
        )
        mDataList.add(
            TransferDataModel(
                MyConstants.FILE_TYPE_AUDIOS,
                "Size ${transferDetailsClass?.getSizeInHumanFormat(transferDetailsClass?.totalAudiosBytes?.toDouble())},${transferDetailsClass?.noOfAudiosToShare} items",
                DetailsInfoToTransferClass.AUDIOS_ICON
            )
        )
        mDataList.add(
            TransferDataModel(
                MyConstants.FILE_TYPE_DOCS,
                "Size ${transferDetailsClass?.getSizeInHumanFormat(transferDetailsClass?.totalDocBytes?.toDouble())},${transferDetailsClass?.noOfDocsToShare} items",
                DetailsInfoToTransferClass.DOCS_ICON
            )
        )

        adapter = AdapterSendingData(this@ActivitySendData, mDataList)

        binding.rcDatalist.layoutManager = GridLayoutManager(this@ActivitySendData, 2)
        binding.rcDatalist.adapter = adapter

        binding.tvTotalSendData.text = transferDetailsClass?.sizeInProperFormat

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
        val dialog = Dialog(this@ActivitySendData)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.db_backpressed)
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
                setResult(MyConstants.FINISH_ACTIVITY_CODE)
                finish()
            }
        }
    }


    fun startTimer() {
        Handler(Looper.getMainLooper()).postDelayed({
            timerCount+=1000
            val time: String = timerCount.getDuration()
            runOnUiThread {
                findViewById<TextView>(R.id.tv_timer).setText(time)
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


    override fun updateView(transferDetailsClass: DetailsInfoToTransferClass) {
        runOnUiThread {
            val totalProgress = transferDetailsClass.getTotalProgress()
            binding.pbarTotalprogress.progress = totalProgress
            binding.tvTotalDataSent.text =
                transferDetailsClass.getSizeInHumanFormat(transferDetailsClass.totalBytesTransferred.toDouble())
            binding.pbarPercent.text = "$totalProgress%"
            mDataList[0].progress = transferDetailsClass.getContactsProgress()
            mDataList[1].progress = transferDetailsClass.getPhotosProgress()
            mDataList[2].progress = transferDetailsClass.getEventsProgress()
            mDataList[3].progress = transferDetailsClass.getVideosProgress()
            mDataList[4].progress = transferDetailsClass.getAppsProgress()
            mDataList[5].progress = transferDetailsClass.getAudiosProgress()
            mDataList[6].progress = transferDetailsClass.getDocsProgress()
            adapter?.notifyDataSetChanged()
        }
    }

    override fun transferFinished() {
        stop = true
        runOnUiThread {
            val dialog = Dialog(this@ActivitySendData)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.db_transfer_completed)
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val positive: Button = dialog.findViewById(R.id.btn_error_ok)

            positive.setOnClickListener {
                dialog.dismiss()
                setResult(MyConstants.FINISH_ACTIVITY_CODE)
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
                setResult(MyConstants.FINISH_ACTIVITY_CODE)
                finish()
            }
        }
    }

    fun loadFbBannerAdd() {

        val adView = AdView(
            this@ActivitySendData,
            this@ActivitySendData.resources.getString(R.string.banner_add),
            AdSize.BANNER_HEIGHT_50
        )

        val adListener: AdListener = object : AdListener {

            override fun onError(ad: Ad, adError: AdError) {
              /*  if (com.facebook.ads.BuildConfig.DEBUG) {
                    Toast.makeText(
                        this@ActivitySendData,
                        "Error: " + adError.errorMessage,
                        Toast.LENGTH_LONG
                    )
                        .show()
                }*/
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

        val mAdView = com.google.android.gms.ads.AdView(this@ActivitySendData)
        val adSize: com.google.android.gms.ads.AdSize = IntersitialAdHelper.getAdSize(this@ActivitySendData)
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