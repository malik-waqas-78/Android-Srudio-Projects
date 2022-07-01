package com.phoneclone.data.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper


import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.facebook.ads.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError


import com.phoneclone.data.R
import com.phoneclone.data.adapters.AdapterReceivingData
import com.phoneclone.data.ads.IntersitialAdHelper
import com.phoneclone.data.ads.isAppInstalledFromPlay
import com.phoneclone.data.constants.MyConstants
import com.phoneclone.data.databinding.ActivityReceiveDataBinding


import com.phoneclone.data.datautils.*
import com.phoneclone.data.interfaces.*
import com.phoneclone.data.modelclasses.TransferDataModel
import com.phoneclone.data.modelclasses.DetailsInfoToTransferClass
import com.phoneclone.data.sockets.ReceivingServer
import com.phoneclone.data.utills.WifiP2PConnectionUtils
import java.util.*
import kotlin.collections.ArrayList




class ActivityReceiveData : AppCompatActivity(), ServerCallBacks {

    lateinit var receivingServer: ReceivingServer
    private var timerCount:Long=0L
    private var stop=false


    var mTransferData: ArrayList<TransferDataModel> = ArrayList()

    var adapter_data: AdapterReceivingData? = null

    lateinit var binding: ActivityReceiveDataBinding
    var mTransferDetailsClass:DetailsInfoToTransferClass?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityReceiveDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(isAppInstalledFromPlay(this)){
            loadFbBannerAdd()
            admobBanner()
        }

        createConnection()

    }


    override fun onBackPressed() {
        btnBackPressd()
    }

    fun btnBackPressd() {
        val dialog = Dialog(this@ActivityReceiveData)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.db_backpressed)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btn_ok: Button = dialog.findViewById(R.id.btn_error_ok)
        val btn_exit: Button = dialog.findViewById(R.id.btn_exit)
        btn_ok.setOnClickListener {
            dialog.dismiss()
        }
        btn_exit.setOnClickListener{
            //exit the ativuty
            stop=true
            setResult(MyConstants.FINISH_ACTIVITY_CODE)
            finish()
        }
        dialog.setOnCancelListener {
            //dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.cancel()
        }
        dialog.show()
    }

    fun socketErrorOccoured() {
        if(findViewById<ConstraintLayout>(R.id.cl_erroroccoured)!=null){
            findViewById<ConstraintLayout>(R.id.cl_erroroccoured).visibility=View.VISIBLE
            findViewById<Button>(R.id.btn_error_ok).setOnClickListener{
                stop=true
                setResult(MyConstants.FINISH_ACTIVITY_CODE)
                finish()
            }
        }
    }

    fun startTimer(){
        Handler(Looper.getMainLooper()).postDelayed({
            timerCount+=1000
            val time:String=timerCount.getDuration()
            runOnUiThread {
                findViewById<TextView>(R.id.tv_timer).setText(time)
            }
            if(!stop){
                startTimer()
            }
        },1000)
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

    private fun createConnection() {
       // Toast.makeText(this, SERVERIP, Toast.LENGTH_SHORT).show()
        receivingServer = ReceivingServer(this)
        if(intent.hasExtra("owner")){
            var owner=intent.getBooleanExtra("owner",false)
            if(owner){
                Log.d("92727", "createConnection: owner")
                     ReceivingServer?.socket= WifiP2PConnectionUtils.socket
            }else{
                Log.d("92727", "createConnection: not owner")
                ReceivingServer?.socket= WifiP2PConnectionUtils.clientSocket
            }
        }
        receivingServer.startAcceptingConnections()
        startTimer()
    }


    private fun initView(transferDetailsClass:DetailsInfoToTransferClass) {
        mTransferDetailsClass=transferDetailsClass

        mTransferData.add(TransferDataModel(MyConstants.FILE_TYPE_CONTACTS,"Size ${transferDetailsClass.getSizeInHumanFormat(transferDetailsClass.totalContactsBytes.toDouble())},${transferDetailsClass.noOfContactsToShare} items",DetailsInfoToTransferClass.CONTACT_ICON))
        mTransferData.add(TransferDataModel(MyConstants.FILE_TYPE_PICS,"Size ${transferDetailsClass.getSizeInHumanFormat(transferDetailsClass.totalPhotosBytes.toDouble())},${transferDetailsClass.noOfPhotosToShare} items",DetailsInfoToTransferClass.IMAGES_ICON))
        mTransferData.add(TransferDataModel(MyConstants.FILE_TYPE_CALENDAR,"Size ${transferDetailsClass.getSizeInHumanFormat(transferDetailsClass.totalCalendarBytes.toDouble())},${transferDetailsClass.noOfCalendarEventsToShare} items",DetailsInfoToTransferClass.CALEDAR_ICON))
        mTransferData.add(TransferDataModel(MyConstants.FILE_TYPE_VIDEOS,"Size ${transferDetailsClass.getSizeInHumanFormat(transferDetailsClass.totalVideosBytes.toDouble())},${transferDetailsClass.noOfVideosToShare} items",DetailsInfoToTransferClass.Videos_ICON))
        mTransferData.add(TransferDataModel(MyConstants.FILE_TYPE_APPS,"Size ${transferDetailsClass.getSizeInHumanFormat(transferDetailsClass.totalAppsBytes.toDouble())},${transferDetailsClass.noOfAppsToShare} items",DetailsInfoToTransferClass.APPS_ICON))
        mTransferData.add(TransferDataModel(MyConstants.FILE_TYPE_AUDIOS,"Size ${transferDetailsClass.getSizeInHumanFormat(transferDetailsClass.totalAudiosBytes.toDouble())},${transferDetailsClass.noOfAudiosToShare} items",DetailsInfoToTransferClass.AUDIOS_ICON))
        mTransferData.add(TransferDataModel(MyConstants.FILE_TYPE_DOCS,"Size ${transferDetailsClass.getSizeInHumanFormat(transferDetailsClass.totalDocBytes.toDouble())},${transferDetailsClass.noOfDocsToShare} items",DetailsInfoToTransferClass.DOCS_ICON))

        adapter_data= AdapterReceivingData(this@ActivityReceiveData,mTransferData)

        binding.rcDatalist.layoutManager=GridLayoutManager(this@ActivityReceiveData,2)
        binding.rcDatalist.adapter=adapter_data

        binding.tvTotalDataToReceive.text=transferDetailsClass?.sizeInProperFormat
        binding.clGroup.visibility=View.GONE
        binding.clList.visibility=View.VISIBLE
    }

    var decodingDialogue:Dialog?=null;
    fun decodeData() {
        decodingDialogue = Dialog(this@ActivityReceiveData)
        decodingDialogue?.setCancelable(false)
        decodingDialogue?.setContentView(R.layout.db_decoding_data)
        decodingDialogue?.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        decodingDialogue?.show()
    }


    var imagesBytesReceived:Long=0

    private fun convertToProperStorageType(data: Double?): String {
        var d: Double = 0.0
        if(data==null){
            return "0.00 B"
        }
        if (data >= (1000.0 * 1000.0 * 1000.0 * 1000.0)) {
            d = data / (1000.0 * 1000.0 * 1000.0 * 1000.0);
            return String.format("%.2f", d) + " TB";
        }
        if (data >= (1000.0 * 1000.0 * 1000.0)) {
            d = data / (1000.0 * 1000.0 * 1000.0);
            return String.format("%.2f", d) + " GB";
        }
        if (data >= (1000.0 * 1000.0)) {
            d = data / (1000.0 * 1000.0);
            return String.format("%.2f", d) + " MB";
        }
        if (data >= 1000.0) {
            d = data / 1000.0;
            return String.format("%.2f", d) + " KB";
        }
        return String.format("%.2f", d) + " B";
    }
    override fun receivedTransferInfo(transferInfoClass: DetailsInfoToTransferClass) {
        runOnUiThread {
            initView(transferInfoClass)
        }
    }

    override fun transferFinished() {
        stop=true
        runOnUiThread {
            val dialog = Dialog(this@ActivityReceiveData)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.db_transfer_completed)
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val positive: Button = dialog.findViewById(R.id.btn_error_ok)

            positive.setOnClickListener {
                dialog.dismiss()
                //go to next activity
                val intent=Intent(this@ActivityReceiveData,ActivityTransferDone::class.java)
                var bundle=Bundle()
                bundle.putBoolean(MyConstants.CALENDAR,mTransferDetailsClass?.noOfCalendarEventsToShare!=0)
                /*  bundle.putBoolean(Constants.CALLLOGS,dataList!![1].seleted)*/
                bundle.putBoolean(MyConstants.CONTACTS,mTransferDetailsClass?.noOfContactsToShare!=0)
                bundle.putBoolean(MyConstants.PICS,mTransferDetailsClass?.noOfPhotosToShare!=0)
                bundle.putBoolean(MyConstants.VIDEOS,mTransferDetailsClass?.noOfVideosToShare!=0)
                bundle.putBoolean(MyConstants.APPS,mTransferDetailsClass?.noOfAppsToShare!=0)
                bundle.putBoolean(MyConstants.Audios,mTransferDetailsClass?.noOfAudiosToShare!=0)
                bundle.putBoolean(MyConstants.Docments,mTransferDetailsClass?.noOfDocsToShare!=0)
                intent.putExtras(bundle)
                startActivity(intent)
                setResult(MyConstants.FINISH_ACTIVITY_CODE)
                finish()
            }
            if(!this.isFinishing){
                dialog.show()
            }
        }
    }

    override fun updateView(transferClass: DetailsInfoToTransferClass?) {
       runOnUiThread {
           val totalProgress=transferClass?.getTotalProgress()
           binding.pbarTotalprogress.progress=totalProgress!!
           binding.tvDataReceived.text=transferClass.getSizeInHumanFormat(transferClass.totalBytesTransferred.toDouble())
           binding.pbarPercent.text="$totalProgress%"
           mTransferData[0].progress=transferClass.getContactsProgress()
           mTransferData[1].progress=transferClass.getPhotosProgress()
           mTransferData[2].progress=transferClass.getEventsProgress()
           mTransferData[3].progress=transferClass.getVideosProgress()
           mTransferData[4].progress=transferClass.getAppsProgress()
           mTransferData[5].progress=transferClass.getAudiosProgress()
           mTransferData[6].progress=transferClass.getDocsProgress()
           adapter_data?.notifyDataSetChanged()
       }
    }

    override fun errorOccurred() {
       runOnUiThread {
           binding.clErroroccoured.visibility=View.VISIBLE
           binding.mincludedView.btnErrorOk.setOnClickListener {
               setResult(MyConstants.FINISH_ACTIVITY_CODE)
               finish()
           }
       }

    }

    fun loadFbBannerAdd() {

        val adView = AdView(
            this@ActivityReceiveData,
            this@ActivityReceiveData.resources.getString(R.string.banner_add),
            AdSize.BANNER_HEIGHT_50
        )

        val adListener: AdListener = object : AdListener {

            override fun onError(ad: Ad, adError: AdError) {
                if (com.facebook.ads.BuildConfig.DEBUG) {
                     Toast.makeText(
                        this@ActivityReceiveData,
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
        findViewById<RelativeLayout>(R.id.top_banner).addView(adView)
    }
    fun admobBanner() {

    val mAdView = com.google.android.gms.ads.AdView(this@ActivityReceiveData)
    val adSize: com.google.android.gms.ads.AdSize = IntersitialAdHelper.getAdSize(this@ActivityReceiveData)
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