package com.smartswitch.phoneclone.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper


import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager

import com.smartswitch.phoneclone.R
import com.smartswitch.phoneclone.adapters.CAPPAdapterReceivingData
import com.smartswitch.phoneclone.constants.CAPPMConstants

import com.smartswitch.phoneclone.databinding.ActivityReceiveDataCappBinding


import com.smartswitch.phoneclone.interfaces.*
import com.smartswitch.phoneclone.modelclasses.CAPPTransferDataModel
import com.smartswitch.phoneclone.modelclasses.CAPPDetailsInfoToTransferClass
import com.smartswitch.phoneclone.sockets.CAPPReceivingServer
import com.smartswitch.phoneclone.utills.CAPPWifiPeer2PConnectionUtils
import java.util.*
import kotlin.collections.ArrayList




class CAPPActivityReceive : AppCompatActivity(), CAPPServerCallBacks {

    lateinit var HSReceivingServer: CAPPReceivingServer
    private var timerCount:Long=0L
    private var stop=false


    var mHSTransferData: ArrayList<CAPPTransferDataModel> = ArrayList()

    var HSAdapter_data: CAPPAdapterReceivingData? = null

    lateinit var binding: ActivityReceiveDataCappBinding
    var mTransferHSDetailsClass:CAPPDetailsInfoToTransferClass?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityReceiveDataCappBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationIcon(R.drawable.ic_back_cross_black)
        binding.toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        /*loadFbBannerAdd()
        //HSIntersitialAdHelper.showAdd()
        // IntersitialAdds.showAdmobIntersitial(this@LaunchReceiveData)
        admobBanner()*/

        createConnection()

    }


    override fun onBackPressed() {
        btnBackPressd()
    }

    fun btnBackPressd() {
        val dialog = Dialog(this@CAPPActivityReceive)
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
        btn_exit.setOnClickListener{
            //exit the ativuty
            stop=true
            setResult(CAPPMConstants.FINISH_ACTIVITY_CODE)
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
                setResult(CAPPMConstants.FINISH_ACTIVITY_CODE)
                finish()
            }
        }
    }

    fun startTimer(){
        Handler(Looper.getMainLooper()).postDelayed({
            timerCount+=1000
            val time:String=timerCount.getDuration()
            runOnUiThread {
                findViewById<TextView>(R.id.tv_timer).setText("Time Taken : "+time)
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
        HSReceivingServer = CAPPReceivingServer(this)
        if(intent.hasExtra("owner")){
            var owner=intent.getBooleanExtra("owner",false)
            if(owner){
                Log.d("92727", "createConnection: owner")
                     HSReceivingServer.socket= CAPPWifiPeer2PConnectionUtils.socket
            }else{
                Log.d("92727", "createConnection: not owner")
                HSReceivingServer.socket= CAPPWifiPeer2PConnectionUtils.clientSocket
            }
        }
        HSReceivingServer.startAcceptingConnections()
        startTimer()
    }


    private fun initView(transferHSDetailsClass:CAPPDetailsInfoToTransferClass) {
        mTransferHSDetailsClass=transferHSDetailsClass

        mHSTransferData.add(CAPPTransferDataModel(CAPPMConstants.FILE_TYPE_CONTACTS,"Size ${transferHSDetailsClass.getSizeInHumanFormat(transferHSDetailsClass.totalContactsBytes.toDouble())},${transferHSDetailsClass.noOfContactsToShare} items",CAPPDetailsInfoToTransferClass.CONTACT_ICON))
        mHSTransferData.add(CAPPTransferDataModel(CAPPMConstants.FILE_TYPE_PICS,"Size ${transferHSDetailsClass.getSizeInHumanFormat(transferHSDetailsClass.totalPhotosBytes.toDouble())},${transferHSDetailsClass.noOfPhotosToShare} items",CAPPDetailsInfoToTransferClass.IMAGES_ICON))
        mHSTransferData.add(CAPPTransferDataModel(CAPPMConstants.FILE_TYPE_CALENDAR,"Size ${transferHSDetailsClass.getSizeInHumanFormat(transferHSDetailsClass.totalCalendarBytes.toDouble())},${transferHSDetailsClass.noOfCalendarEventsToShare} items",CAPPDetailsInfoToTransferClass.CALEDAR_ICON))
        mHSTransferData.add(CAPPTransferDataModel(CAPPMConstants.FILE_TYPE_VIDEOS,"Size ${transferHSDetailsClass.getSizeInHumanFormat(transferHSDetailsClass.totalVideosBytes.toDouble())},${transferHSDetailsClass.noOfVideosToShare} items",CAPPDetailsInfoToTransferClass.Videos_ICON))
        mHSTransferData.add(CAPPTransferDataModel(CAPPMConstants.FILE_TYPE_APPS,"Size ${transferHSDetailsClass.getSizeInHumanFormat(transferHSDetailsClass.totalAppsBytes.toDouble())},${transferHSDetailsClass.noOfAppsToShare} items",CAPPDetailsInfoToTransferClass.APPS_ICON))
        mHSTransferData.add(CAPPTransferDataModel(CAPPMConstants.FILE_TYPE_AUDIOS,"Size ${transferHSDetailsClass.getSizeInHumanFormat(transferHSDetailsClass.totalAudiosBytes.toDouble())},${transferHSDetailsClass.noOfAudiosToShare} items",CAPPDetailsInfoToTransferClass.AUDIOS_ICON))
        mHSTransferData.add(CAPPTransferDataModel(CAPPMConstants.FILE_TYPE_DOCS,"Size ${transferHSDetailsClass.getSizeInHumanFormat(transferHSDetailsClass.totalDocBytes.toDouble())},${transferHSDetailsClass.noOfDocsToShare} items",CAPPDetailsInfoToTransferClass.DOCS_ICON))

        HSAdapter_data= CAPPAdapterReceivingData(this@CAPPActivityReceive,mHSTransferData)

        binding.rcDatalist.layoutManager=
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rcDatalist.adapter=HSAdapter_data

        binding.tvTotalDataToReceive.text=transferHSDetailsClass?.sizeInProperFormat+" Data"
        binding.clGroup.visibility=View.GONE
        binding.clList.visibility=View.VISIBLE
    }

    var decodingDialogue:Dialog?=null;
    fun decodeData() {
        decodingDialogue = Dialog(this@CAPPActivityReceive)
        decodingDialogue?.setCancelable(false)
        decodingDialogue?.setContentView(R.layout.db_decoding_data_capp)
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
    override fun receivedTransferInfo(transferInfoClassHS: CAPPDetailsInfoToTransferClass) {
        runOnUiThread {
            initView(transferInfoClassHS)
        }
    }

    override fun transferFinished() {
        stop=true
        runOnUiThread {
            val dialog = Dialog(this@CAPPActivityReceive)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.db_transfer_completed_capp)
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val positive: Button = dialog.findViewById(R.id.btn_error_ok)

            positive.setOnClickListener {
                dialog.dismiss()
                //go to next activity
                val intent=Intent(this@CAPPActivityReceive,CAPPActivityTransferDone::class.java)
                var bundle=Bundle()
                bundle.putBoolean(CAPPMConstants.CALENDAR,mTransferHSDetailsClass?.noOfCalendarEventsToShare!=0)
                /*  bundle.putBoolean(Constants.CALLLOGS,dataList!![1].seleted)*/
                bundle.putBoolean(CAPPMConstants.CONTACTS,mTransferHSDetailsClass?.noOfContactsToShare!=0)
                bundle.putBoolean(CAPPMConstants.PICS,mTransferHSDetailsClass?.noOfPhotosToShare!=0)
                bundle.putBoolean(CAPPMConstants.VIDEOS,mTransferHSDetailsClass?.noOfVideosToShare!=0)
                bundle.putBoolean(CAPPMConstants.APPS,mTransferHSDetailsClass?.noOfAppsToShare!=0)
                bundle.putBoolean(CAPPMConstants.Audios,mTransferHSDetailsClass?.noOfAudiosToShare!=0)
                bundle.putBoolean(CAPPMConstants.Docments,mTransferHSDetailsClass?.noOfDocsToShare!=0)
                intent.putExtras(bundle)
                startActivity(intent)
                setResult(CAPPMConstants.FINISH_ACTIVITY_CODE)
                finish()
            }
            if(!this.isFinishing){
                dialog.show()
            }
        }
    }

    override fun updateView(transferClassHS: CAPPDetailsInfoToTransferClass?) {
       runOnUiThread {
           val totalProgress=transferClassHS?.getTotalProgress()
           binding.pbarTotalprogress.progress=totalProgress!!
           binding.tvDataReceived.text="Received "+transferClassHS.getSizeInHumanFormat(transferClassHS.totalBytesTransferred.toDouble())
           binding.pbarPercent.text="$totalProgress%"
           mHSTransferData[0].progress=transferClassHS.getContactsProgress()
           mHSTransferData[1].progress=transferClassHS.getPhotosProgress()
           mHSTransferData[2].progress=transferClassHS.getEventsProgress()
           mHSTransferData[3].progress=transferClassHS.getVideosProgress()
           mHSTransferData[4].progress=transferClassHS.getAppsProgress()
           mHSTransferData[5].progress=transferClassHS.getAudiosProgress()
           mHSTransferData[6].progress=transferClassHS.getDocsProgress()
           HSAdapter_data?.notifyDataSetChanged()
       }
    }

    override fun errorOccurred() {
       runOnUiThread {
           binding.clErroroccoured.visibility=View.VISIBLE
           binding.mincludedView.btnErrorOk.setOnClickListener {
               setResult(CAPPMConstants.FINISH_ACTIVITY_CODE)
               finish()
           }
       }

    }
/*
    fun loadFbBannerAdd() {

        val adView = AdView(
            this@HSActivityReceive,
            this@HSActivityReceive.resources.getString(R.string.banner_add),
            AdSize.BANNER_HEIGHT_50
        )

        val adListener: AdListener = object : AdListener {

            override fun onError(ad: Ad, adError: AdError) {
                if (com.facebook.ads.BuildConfig.DEBUG) {
                     Toast.makeText(
                        this@HSActivityReceive,
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

    val mAdView = com.google.android.gms.ads.AdView(this@HSActivityReceive)
    val adSize: com.google.android.gms.ads.AdSize = HSIntersitialAdHelper.getAdSize(this@HSActivityReceive)
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