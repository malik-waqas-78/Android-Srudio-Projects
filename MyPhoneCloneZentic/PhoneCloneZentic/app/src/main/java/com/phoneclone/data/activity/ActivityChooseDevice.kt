package com.phoneclone.data.activity

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.phoneclone.data.R
import com.phoneclone.data.constants.MyConstants
import com.phoneclone.data.constants.MyConstants.get.HOTOSPOT_SETTINGS_CODE
import com.phoneclone.data.interfaces.DialogueClickListener
import android.view.Window
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.facebook.ads.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.phoneclone.data.BuildConfig
import com.phoneclone.data.ads.IntersitialAdHelper
import com.phoneclone.data.ads.NativeAdHelper
import com.phoneclone.data.ads.isAppInstalledFromPlay

/*import com.facebook.ads.*
import com.google.android.gms.ads.AdListener

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.zentic.clonemyphone.adds.IntersitialAdds*/


class ActivityChooseDevice : AppCompatActivity(), DialogueClickListener {
   /* var myPermissions: MyPermissions? = null
    var myDialogueBoxes: MyDialogues? = null
    var wifiUtilsClass: WifiUtilsClass? = null*/

    //var turnWifiOff = false
    var launchNewPhone = false
    var launchOldPhone = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_mobile)

       /* if(intent.hasExtra("newversion")){
            var version=intent.getStringExtra("newversion")
            if(version!=null&&version?.isNotEmpty()){
                showDialogueForUpdate(version)
            }

        }*/

       // IntersitialAdHelper.showAdmobIntersitial(this@ActivityChooseDevice)
        //IntersitialAdHelper.showStartEndAdmobInterstitial(this@ActivityChooseDevice)

  if(isAppInstalledFromPlay(this)){
      admobBanner()
      loadFbBannerAdd()
  }

        /*wifiUtilsClass = WifiUtilsClass(this)
        myDialogueBoxes = MyDialogues(this)
        myPermissions = MyPermissions(applicationContext, this@ActivityChooseDevice)*/

        //new phone choosen
        findViewById<CardView>(R.id.send_cv).setOnClickListener {
            handleOldPhoneClicked()
        }
        findViewById<ConstraintLayout>(R.id.btn_send_cv).setOnClickListener {
            handleOldPhoneClicked()
        }
        findViewById<CardView>(R.id.rec_cv).setOnClickListener {
            handleNewPhoneClicked()
        }
        //old phone choosen
        findViewById<ConstraintLayout>(R.id.btn_rec_cv).setOnClickListener {
            handleNewPhoneClicked()
        }
        //copyVideoFromAssetstoGallery()
        //checkBluetooth()

    }
    interface OpenSettingsForPermissions{
        fun allowCameraPermissions(context: Context);
    }

 /*   private fun showDialogueForUpdate(newVersion:String) {
        val pInfo: PackageInfo = this@ActivityChooseDevice.packageManager.getPackageInfo(this@ActivityChooseDevice.packageName, 0)
        val version = pInfo.versionName
        val arrNewVersion=newVersion.split(".")
        val arrOldVersion=version.split(".")
        if(arrNewVersion[0]>arrOldVersion[0]){
            showUpdateDialogue(newVersion)
        }else if(arrNewVersion[0]==arrOldVersion[0]&&arrNewVersion[1]>arrOldVersion[1]){
            showUpdateDialogue(newVersion)
        }else if(arrNewVersion[0]==arrOldVersion[0]&&arrNewVersion[1]==arrOldVersion[1]&&arrNewVersion[2]>arrOldVersion[2]){
            showUpdateDialogue(newVersion)
        }

    }*/

 /*   private fun showUpdateDialogue(newVersion:String) {
        val pInfo: PackageInfo = this@ActivityChooseDevice.packageManager.getPackageInfo(this@ActivityChooseDevice.packageName, 0)
        val version = pInfo.versionName
        if ((version) != newVersion.toString()) {
            //perform your task here like show alert dialogue "Need to upgrade app"
            MyDialogues.updateToNewVersion(this@ActivityChooseDevice,
                this.resources.getString(R.string.update_title),
                this.resources.getString(R.string.update_msg),
                object :OpenSettingsForPermissions {
                    override fun allowCameraPermissions(context: Context) {
                        try {
                            val updateIntent: Intent = rateIntentForUrl(
                                "market://details?id=${BuildConfig.APPLICATION_ID}",
                                context
                            )
                            startActivity(updateIntent)
                        } catch (e: ActivityNotFoundException) {

                            Toast.makeText(context, "PLAY STORE NOT FOUND", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    private fun rateIntentForUrl(s: String, context: Context): Intent {
                        var intent: Intent? = null
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(
                                    s
                                )
                            )
                        }
                        var flags =
                            Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                        flags = if (Build.VERSION.SDK_INT >= 21) {
                            flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
                        } else {
                            flags or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
                        }
                        intent!!.addFlags(flags)
                        return intent
                    }

                })
        }
    }*/

    /*  private fun copyVideoFromAssetstoGallery() {
          var fileinput: InputStream? = null;
          var fileout: OutputStream? = null;
          val assetManager: AssetManager = this.assets
          val filename="myvideo.mp4"
          val outDir = Environment.getExternalStorageDirectory().absolutePath + "/Movies"
          try {

              fileinput = assetManager.open(filename);



              var outFile = File(outDir, filename);
              if(outFile.exists()){
                  return
              }

              fileout = FileOutputStream(outFile);
              val buffer=ByteArray(1000)
              var len=fileinput.read(buffer)
              while(len!=-1){
                  fileout.write(buffer)
                  fileout.flush()
                  len=fileinput.read(buffer)
              }
              fileinput.close();
              fileinput = null;
              fileout.flush();
              fileout.close();
              fileout = null;
          } catch ( e: IOException) {
              Log.e("tag", "Failed to copy asset file: " + filename, e);
          }
          MediaScannerConnection.scanFile(
              this.applicationContext, arrayOf("""${outDir+"/"+filename}"""), null
          ) { path: String, uri: Uri ->//on scan completed
              Log.i("ExternalStorage", "Scanned $path:");
              Log.i("ExternalStorage", "-> uri=$uri");
          }
      }*/
    /* private fun checkBluetooth() {
         //check if bluetooth is enabled
         //if not enable it
         val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
         if(!mBluetoothAdapter.isEnabled){
            val intent=Intent(ACTION_REQUEST_ENABLE)
             startActivity(intent)
         }
     }*/

    /* private fun checkLocationAndLaunchNewPhoneActivity() {
         if (myAppPermissions?.CheckIfGPSisOn() == true) {
             //check if hotspot is off

             if (wifiUtils?.getWifiApState() == true) {
                 //if on then turn it off by showing user a dialogue box
                 Log.d(TAG, "checkLocationAndGoToNextActivity: ")
                 myDialogueBoxes?.turnHotSpotOFF()
             } else {
                 //go to next activity
                 //launch new phone activity
                     // ask user to turn wifi off if he is on android 10
                 launchNewPhoneActivity()
                 Log.d(TAG, "onRequestPermissionsResult: HotSpot is off")
             }

         } else {
             //turn permission on
             myAppPermissions?.turnGpsOn()
         }
     }
     private fun checkLocationAndLaunchOldPhoneActivity() {
         if (myAppPermissions?.CheckIfGPSisOn() == true) {
             //check if hotspot is off


                 //go to next activity
                 //launch old phone activity
                 launchOldPhoneActivity()
                 Log.d(TAG, "onRequestPermissionsResult: HotSpot is off")
         } else {
             //turn permission on
             myAppPermissions?.turnGpsOn()
         }
     }*/

    /* override fun onResume() {
         super.onResume()
        *//* var wifiUtils = WifiUtils(this)
        //turn wifi off
        //wifiUtils.changeWifiState(true)
        if (wifiUtils.getWifiState()) {
           if(Build.VERSION.SDK_INT>=29){
               //ask user to turn wifi off
               myDialogueBoxes?.tuneWifiOFF(false)
           }else{
               wifiUtils.changeWifiState(false)
           }
        }*//*
        //turn hotspot off
//         if (wifiUtils.getWifiApState()) {
//             wifiUtils.changeWifiApState(false)
////             wifiUtils.changeWifiState(true)
////             wifiUtils.changeWifiState(false)
//         }
    }*/

   /* override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult: $requestCode")
        if (requestCode == MyConstants.PERMISSIONS_REQUSTCODE) {
            if (myPermissions?.isRuntimePermissions()!!) {
                if (launchNewPhone) {
                    launchNewPhoneActivity()
                } else {
                    launchOldPhoneActivity()
                }
            } else {
                myPermissions?.requestRunTimePermissions()
            }
        } else if (requestCode == MyConstants.LOCATION_PERMISIION_REQUEST_CODE && permissions.isNotEmpty() && grantResults.isNotEmpty()) {
            if (grantResults[0].equals(PackageManager.PERMISSION_GRANTED)) {
                if (launchNewPhone) {
                    launchNewPhoneActivity()
                } else {
                    launchOldPhoneActivity()
                }
            } else {
                //permission denied do nothing
            }
        } else if (requestCode == MyConstants.REQUEST_LOCATION_TURNON && permissions.isNotEmpty() && grantResults.isNotEmpty()) {
            if (grantResults[0].equals(PackageManager.PERMISSION_GRANTED)) {
                //location is turned on
                if (launchNewPhone) {
                    launchNewPhoneActivity()
                } else {
                    launchOldPhoneActivity()
                }
                //goto next activty
            } else {
                //location is turned off
            }
        }
    }*/

    private fun handleNewPhoneClicked() {
        //perform checks
        launchNewPhone = true
        launchOldPhone = false
        //check for location permission
      /*  if (myPermissions?.isRuntimePermissions()!!) {*/
            launchNewPhoneActivity()
        /*} else {
            myPermissions?.requestRunTimePermissions()
        }*/
    }

    private fun handleOldPhoneClicked() {
        //perform checks
        launchNewPhone = false
        launchOldPhone = true
        //check if have camera permission
        //if do go ahead
        /*if (myPermissions?.isRuntimePermissions()!!) {*/
            launchOldPhoneActivity()
        /*} else {
            myPermissions?.requestRunTimePermissions()
        }*/
    }

    override fun positiveHotSpotTurnOFF() {
        turnAPoff()
    }

    override fun reTryWifiTurnON(state: Boolean) {
        //check if wifi turned off or not
        // var wifiUtils = WifiUtils(this)
        //turn wifi off
        //wifiUtils.changeWifiState(true)
        /*if (wifiUtilsClass?.getWifiState() != state) {
            if (Build.VERSION.SDK_INT >= 29) {
                //ask user to turn wifi off
                myDialogueBoxes?.tuneWifiOFF(state)
            } else {
                wifiUtilsClass?.changeWifiState(state)
            }
        }*/
    }

    private fun turnAPoff() {
        if (Build.VERSION.SDK_INT <= 23) {
            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            val cn = ComponentName("com.android.settings", "com.android.settings.TetherSettings")
            intent.component = cn
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivityForResult(intent, HOTOSPOT_SETTINGS_CODE)
        } else {
            var intent = Intent()
            intent.action = Settings.ACTION_WIRELESS_SETTINGS
            //turnWifiOff = true
            startActivityForResult(intent, HOTOSPOT_SETTINGS_CODE)
        }
    }

    override fun negativeHotSpotTurnOFF() {

    }

    override fun WifiTurnON(state: Boolean) {
        //use intent to turn wifi on or not
      //  wifiUtilsClass?.changeWifiState(state)
    }

    override fun gotPassword(SSID: String, PASS: String) {

    }

    override fun allowPermission(permission: String) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        var requestCode = 0
        startActivityForResult(intent, MyConstants.PERMISSIONS_REQUSTCODE)
    }

    override fun transferFinished() {

    }

    override fun isAndroid10() {
        val intent = Intent(this, WifiP2PCreateGroupAndReceive::class.java)
        startActivity(intent)
    }

    override fun isNotAndroid10() {
        val intent = Intent(this, WifiP2PCreateGroupAndReceive::class.java)
        startActivity(intent)
        /*val intent = Intent(this@ChooseMobile, LaunchNewPhone::class.java)
        startActivityForResult(intent, LAUNCH_NEW_PHONE_ACTIVITY)*/
    }

    override fun saveContacts() {

    }

    override fun androidRStoragePermission() {

    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: $requestCode")
        Log.d(TAG, "onActivityResult: $resultCode")
        Log.d(TAG, "onActivityResult: ${data?.data}")
        if (requestCode == MyConstants.WIFI_TURN_ON) {
            if (launchNewPhone) {
                launchNewPhoneActivity()
            } else {
                launchOldPhoneActivity()
            }
        } else if (requestCode == MyConstants.PERMISSIONS_REQUSTCODE) {
            if (myPermissions?.isRuntimePermissions()!!) {
                if (launchNewPhone) {
                    launchNewPhoneActivity()
                } else {
                    launchOldPhoneActivity()
                }
            } else {
                myPermissions?.requestRunTimePermissions()
            }
        } else if (requestCode == REQUEST_LOCATION_TURNON && resultCode == -1) {
            //  if (wifiUtils?.getWifiApState() == false) {
            // launch new phone activity
            if (launchNewPhone) {
                launchNewPhoneActivity()
            } else {
                launchOldPhoneActivity()
            }
            Log.d(TAG, "onActivityResult: HOT SPOT IS off")
        }
        *//* else {
                     //hotspot did not turned off
                     myDialogueBoxes!!.turnHotSpotOFF()
                 }*//*
        //   }
        *//*  else if (requestCode == HOTOSPOT_SETTINGS_CODE) {
              //CHECK IF HOTSPOT IS OFF
              if (!wifiUtils!!.getWifiApState()) {
                  //IF OFF GO TO NEXT ACTIVITY
                  if (launchNewPhone)
                      launchNewPhoneActivity()
                  else
                      launchOldPhoneActivity()
              }
              //ELSE DO NOTHING
          }*//*
    }*/


    private fun launchNewPhoneActivity() {
        //   wifiUtils?.changeWifiState(false)
        //show dialogue asking user if the old phone is android 10 than
        //if 10 than use wifi p2p ele carry on
        /*if (!wifiUtilsClass!!.getWifiState()) {
            if (Build.VERSION.SDK_INT >= 29) {
                myDialogueBoxes!!.tuneWifiOFF(true)
            } else {
                wifiUtilsClass!!.changeWifiState(true)
                if (myPermissions!!.CheckIfGPSisOn()) {
                    Handler().postDelayed({
                        val intent = Intent(this, WifiP2PCreateGroupAndReceive::class.java)
                        startActivity(intent)
                    }, 1000)
                } else {
                    myPermissions!!.turnGpsOn()
                }

            }
        } else {
            if (myPermissions!!.CheckIfGPSisOn()) {*/
                val intent = Intent(this, WifiP2PCreateGroupAndReceive::class.java)
                startActivity(intent)
           /* } else {
                myPermissions!!.turnGpsOn()
            }*/

        /*       val intent = Intent(this@ChooseMobile, LaunchNewPhone::class.java)
               startActivityForResult(intent, LAUNCH_NEW_PHONE_ACTIVITY)*/
    }

    fun launchOldPhoneActivity() {

        /*if (!wifiUtilsClass!!.getWifiState()) {
            if (Build.VERSION.SDK_INT >= 29) {
                myDialogueBoxes!!.tuneWifiOFF(true)
            } else {
                wifiUtilsClass!!.changeWifiState(true)
            }
        } else {
            if (myPermissions!!.CheckIfGPSisOn()) {*/
                val intent = Intent(this, SelectionActivity::class.java)
                startActivity(intent)
            /*} else {
                myPermissions!!.turnGpsOn()
            }
        }*/

        /*if(Build.VERSION.SDK_INT<29){
            val intent = Intent(this@ChooseMobile, WifiP2PJoinGroup::class.java)
            startActivityForResult(intent, LAUNCH_OLD_PHONE_ACTIVITY)
           *//* val intent = Intent(this@ChooseMobile, LaunchOldPhone::class.java)
           startActivityForResult(intent, LAUNCH_OLD_PHONE_ACTIVITY)*//*
       }else{
           //code for android 10
           // create a p2p connection
           //make it a group owner
           val intent = Intent(this@ChooseMobile, WifiP2PJoinGroup::class.java)
           startActivityForResult(intent, LAUNCH_OLD_PHONE_ACTIVITY)
       }*/
    }

    override fun onBackPressed() {
       IntersitialAdHelper.showStartEndAdmobInterstitial(this@ActivityChooseDevice)
        exitorcontinue()
    }

    fun exitorcontinue() {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.db_exitdialogue)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        val positive: Button = dialog.findViewById(R.id.btn_yes)
        positive.setOnClickListener {
            dialog.dismiss()
            finishAffinity()
        }
        val negative: Button = dialog.findViewById(R.id.btn_exit)
        negative.setOnClickListener {
            dialog.dismiss()
        }
        val rateUs: Button = dialog.findViewById(R.id.btn_rate_us)
        rateUs.setOnClickListener {
            rateApp()
            finishAffinity()
        }
        dialog.setOnCancelListener {
            //dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.dismiss()
        }


       if(NativeAdHelper.googleNativeAd!=null){
           val nativeAdLayout = dialog.findViewById<FrameLayout>(R.id.ad_frame)
           nativeAdLayout.visibility=View.VISIBLE
           dialog.findViewById<ImageView>(R.id.iv_logo).visibility=View.GONE
           NativeAdHelper.showAdmobNativeAd(this, nativeAdLayout)
       }else{
           dialog.findViewById<ImageView>(R.id.iv_logo).visibility=View.VISIBLE
           dialog.findViewById<FrameLayout>(R.id.ad_frame).visibility=View.GONE
       }

        dialog.show()
    }

    private fun rateIntentForUrl(url: String): Intent? {
        var intent: Intent? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    url
                )
            )
        }
        var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        flags = if (Build.VERSION.SDK_INT >= 21) {
            flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        } else {
            flags or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
        }
        intent!!.addFlags(flags)
        return intent
    }

    private fun rateApp() {
        try {
            val rateIntent = rateIntentForUrl("market://details?id=${BuildConfig.APPLICATION_ID}")
            startActivity(rateIntent)
        } catch (e: ActivityNotFoundException) {
            val rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
            startActivity(rateIntent)
        }
    }


    fun admobBanner() {

        val mAdView = com.google.android.gms.ads.AdView(this@ActivityChooseDevice)
        val adSize: com.google.android.gms.ads.AdSize = IntersitialAdHelper.getAdSize(this@ActivityChooseDevice)
        mAdView.adSize = adSize

        mAdView.adUnitId = resources.getString(R.string.admob_banner)

        val adRequest = AdRequest.Builder().build()

        val adViewLayout = findViewById<View>(R.id.bottom_banner) as RelativeLayout
        adViewLayout.addView(mAdView)

        mAdView.loadAd(adRequest)

        mAdView.adListener = object : AdListener() {
            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
                adViewLayout.visibility = View.INVISIBLE
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                adViewLayout.visibility = View.VISIBLE
            }

            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdImpression() {
                super.onAdImpression()
            }
        }

    }

    fun loadFbBannerAdd() {

        val adView = AdView(
            this@ActivityChooseDevice,
            this@ActivityChooseDevice.resources.getString(R.string.banner_add),
            AdSize.BANNER_HEIGHT_50
        )

        val adListener: com.facebook.ads.AdListener = object : com.facebook.ads.AdListener {

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
    }

    override fun onDestroy() {
        //NativeAdds.googleNativeAd?.destroy()
        super.onDestroy()
    }


}