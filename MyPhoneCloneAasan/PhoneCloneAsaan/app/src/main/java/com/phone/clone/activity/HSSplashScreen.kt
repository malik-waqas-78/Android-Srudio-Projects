package com.phone.clone.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.provider.Settings.*
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


import com.phone.clone.utills.HSMyPermissions


import com.phone.clone.R
import com.phone.clone.interfaces.HSDialogueClickListener
import com.phone.clone.utills.HSAppVersionChecker
import com.rohitss.uceh.UCEHandler
import com.phone.clone.BuildConfig


class HSSplashScreen : AppCompatActivity(), HSDialogueClickListener,HSAppVersionChecker.VersionCallBack {


    var iv_start: ImageView? = null

    var tv_privacyPolicy: TextView? = null

    var HSMyPermissions: HSMyPermissions? = null

    var mPbar: ProgressBar? = null

    var newVersion=""

    //lateinit var versionChecker:HSAppVersionChecker

    override fun onStart() {
        super.onStart()
        Log.d("TAG", "onStart: ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

       // versionChecker = HSAppVersionChecker(this)
      /*  MediationTestSuite.launch(this@HSSplashScreen);*/
       /* HSNativeAdHelper.prepareAdd(this@HSSplashScreen)
        HSIntersitialAdHelper.prepareAdd(this@HSSplashScreen)
        HSIntersitialAdHelper.loadStartEndAdmobInterstitial(this@HSSplashScreen)
        HSIntersitialAdHelper.loadAdmobInterstitial(this@HSSplashScreen)
        loadFbBannerAdd()*/
        /*HSNativeAdHelper.loadAd(this@HSSplashScreen)*/

        iv_start = findViewById(R.id.iv_start)

        tv_privacyPolicy = findViewById(R.id.tv_privacypolicy)

        mPbar = findViewById(R.id.pbar_loadAdds)

        //libreray to show errors logs on screen in dialogboxes
        if (BuildConfig.DEBUG) {
            UCEHandler.Builder(applicationContext).build()
        }


        Handler().postDelayed({
            mPbar?.visibility = View.GONE
            iv_start?.visibility = View.VISIBLE
        }, 3000)

        /*  var dialog=MyDialogueBoxes(this@HSSplashScreen,this)
          dialog.transferCompleted()*/

        //start button
        iv_start?.setOnClickListener {
            //ask for permissions
            /*HSMyPermissions = HSMyPermissions(applicationContext, this@HSSplashScreen);
            if (!HSMyPermissions!!.haveSplashPermissions()) {
                HSMyPermissions?.getSplashPermissions()
            } else {*/
                launchChooseMobile()
           /* }*/
        }


        //privacy button
        tv_privacyPolicy?.setOnClickListener {
            privacyDialog()
        }


    }

    override fun onPostResume() {
        super.onPostResume()
        /*android.os.Handler(Looper.getMainLooper()).postDelayed({
            checkAndGetNewVersion()
        },200)*/
    }

    /* override fun onResume() {
         super.onResume()
         var wifiUtils = WifiUtils(this)
         //turn wifi off
         //wifiUtils.changeWifiState(true)
          if (wifiUtils.getWifiState()) {
             if(Build.VERSION.SDK_INT>=29){
                 //turn wifi off
             }else{
                 wifiUtils.changeWifiState(false)
             }
          }
 //         //turn hotspot off
 //         if (wifiUtils.getWifiApState()) {
 //             wifiUtils.changeWifiApState(false)
 ////             wifiUtils.changeWifiState(true)
 ////             wifiUtils.changeWifiState(false)
 //         }
     }*/

   /* @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == HSMyConstants.LOCATION_PERMISIION_REQUEST_CODE || requestCode == HSMyConstants.STORAGE_PERMISSION || requestCode == HSMyConstants.ANDROIDR_PERMISSION) {
            if (HSMyPermissions!!.isLocationPermissionGiven() && HSMyPermissions!!.isStoragePermissionGiven()*//*&&HSMyPermissions!!.hasAndroidRStoragePermission()*//*) {
                launchChooseMobile()
            } else {
                //location permission is denied or storage is denied
                if (requestCode == HSMyConstants.LOCATION_PERMISIION_REQUEST_CODE && shouldShowRequestPermissionRationale(
                        HSMyPermissions!!.accessFineLocation
                    )
                ) {
                    val myDialogueBoxes = HSMyDialogues(this@HSSplashScreen)
                    val title = """ Allow "Phone Clone " to access your Location.""";
                    val msg =
                        "This will allow us to search for near by devices that you want to connect to.";
                    myDialogueBoxes.showRational(HSMyPermissions!!.accessFineLocation, title, msg)
                } else if (requestCode == HSMyConstants.STORAGE_PERMISSION && shouldShowRequestPermissionRationale(
                        HSMyPermissions!!.storageReadPermission
                    )
                ) {
                    val myDialogueBoxes = HSMyDialogues(this@HSSplashScreen)
                    val title = """ Allow "Phone Clone " to access your media Files.""";
                    val msg = "This will share files between your new and old devices.";
                    myDialogueBoxes.showRational(HSMyPermissions!!.accessFineLocation, title, msg)
                } else if (requestCode == HSMyConstants.ANDROIDR_PERMISSION) {

                }
            }
        }
    }*/

    /*@RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissions.isNotEmpty() && grantResults.isNotEmpty()) {
            if (HSMyPermissions!!.isLocationPermissionGiven() && HSMyPermissions!!.isStoragePermissionGiven()) {
                *//* if(HSMyPermissions!!.hasAndroidRStoragePermission()){*//*
                launchChooseMobile()
                *//*   }else{
                       if(Build.VERSION.SDK_INT>=30){
                           val title=""" Allow "Phone Clone" to Manage External Storage.""";
                           val msg="On Android 11 we need this permission to Transfer and Store data between your Devices.";
                           val myDialogueBoxes=MyDialogueBoxes(this@HSSplashScreen)
                           myDialogueBoxes.showPermissionDialogueForR(title,msg)
                       }
                   }*//*

            } else {
                //location permission is denied or storage is denied
            }
        } else {
            //location permission is denied or storage is denied
            if (requestCode == HSMyConstants.LOCATION_PERMISIION_REQUEST_CODE && shouldShowRequestPermissionRationale(
                    HSMyPermissions!!.accessFineLocation
                )
            ) {
                val myDialogueBoxes = HSMyDialogues(this@HSSplashScreen)
                val title = """ Allow "Phone Clone " to access your Location.""";
                val msg =
                    "This will allow us to search for near by devices that you want to connect to.";
                myDialogueBoxes.showRational(HSMyPermissions!!.accessFineLocation, title, msg)
            } else if (requestCode == HSMyConstants.STORAGE_PERMISSION && shouldShowRequestPermissionRationale(
                    HSMyPermissions!!.storageReadPermission
                )
            ) {
                val myDialogueBoxes = HSMyDialogues(this@HSSplashScreen)
                val title = """ Allow "Phone Clone " to access your media Files.""";
                val msg = "This will share files between your new and old devices.";
                myDialogueBoxes.showRational(HSMyPermissions!!.accessFineLocation, title, msg)
            } else {

            }
        }

    }*/


    private fun privacyDialog() {
        val dialog = Dialog(this@HSSplashScreen, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.dialog_privacy)
        dialog.setCanceledOnTouchOutside(false)
        dialog.getWindow()!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
        val ok = dialog.findViewById<Button>(R.id.btn_okay)
        ok.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun launchChooseMobile() {
        var intent = Intent(this@HSSplashScreen, HSActivityChooseDevice::class.java)
        //intent.putExtra("newversion",newVersion)
        startActivity(intent)
    }

    override fun positiveHotSpotTurnOFF() {

    }

    override fun reTryWifiTurnON(state: Boolean) {

    }

    override fun negativeHotSpotTurnOFF() {

    }

    override fun WifiTurnON(state: Boolean) {

    }

    override fun gotPassword(SSID: String, PASS: String) {

    }

    override fun allowPermission(permission: String) {

    }

    /*override fun allowPermission(permission: String) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        var requestCode = 0
        if (permission == HSMyPermissions?.accessFineLocation) {
            requestCode = HSMyConstants.LOCATION_PERMISIION_REQUEST_CODE
        } else if (permission == HSMyPermissions?.storageReadPermission || permission == HSMyPermissions?.storageWritePermission) {
            requestCode = HSMyConstants.STORAGE_PERMISSION
        } else {
            requestCode = 2344
        }
        startActivityForResult(intent, requestCode)
    }*/

    override fun transferFinished() {

    }

    override fun isAndroid10() {

    }

    override fun isNotAndroid10() {

    }

    override fun saveContacts() {

    }

    override fun androidRStoragePermission() {

    }

    /*
    override fun androidRStoragePermission() {
        val intent=Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        intent.data=Uri.parse("""package:$packageName""")
        try {
            startActivityForResult(intent,Constants.ANDROIDR_PERMISSION)
        }catch (e:ActivityNotFoundException){

        }

    }
*/
/*    fun loadFbBannerAdd() {

        val adView = AdView(
            this@HSSplashScreen,
            this@HSSplashScreen.resources.getString(R.string.banner_add),
            AdSize.BANNER_HEIGHT_50
        )

        val adListener: AdListener = object : AdListener {

            override fun onError(ad: Ad, adError: AdError) {

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
    }*/

    /*fun admobBanner() {

        val mAdView = com.google.android.gms.ads.AdView(this@HSSplashScreen)
        val adSize: com.google.android.gms.ads.AdSize = IntersitialAdds.getAdSize(this@HSSplashScreen)
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

            override fun onAdFailedToLoad(errorCode: Int) {
                super.onAdFailedToLoad(errorCode)
                Log.d(Constants.TAG, "onAdFailedToLoad: ${errorCode}")
                //adViewLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
                adViewLayout.visibility = View.INVISIBLE
            }

            override fun onAdLeftApplication() {
                Log.d(Constants.TAG, "onAdLeftApplication: ")
                super.onAdLeftApplication()
            }

            override fun onAdOpened() {
                super.onAdOpened()
                Log.d(Constants.TAG, "onAdOpened: ")
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                adViewLayout.visibility = View.VISIBLE
                Log.d(Constants.TAG, "onAdLoaded: ")
            }
        }

    }*/
/*    fun checkAndGetNewVersion(){

        try {
            versionChecker.execute()
        }catch (e:IllegalStateException){
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
    }*/

    override fun onDestroy() {
        super.onDestroy()
 /*       versionChecker.hlat=true
        versionChecker?.cancel(true)*/
    }

    override fun versionChecked(s: String) {

    }

    /*override fun versionChecked(s: String) {
        if(s.isNotEmpty()){
            newVersion=s
        }
    }*/
}