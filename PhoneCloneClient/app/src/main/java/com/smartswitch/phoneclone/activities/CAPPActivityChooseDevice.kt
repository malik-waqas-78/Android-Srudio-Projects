package com.smartswitch.phoneclone.activities

import android.annotation.SuppressLint
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
import android.view.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.smartswitch.phoneclone.BuildConfig
import com.smartswitch.phoneclone.R
import com.smartswitch.phoneclone.constants.CAPPMConstants
import com.smartswitch.phoneclone.constants.CAPPMConstants.get.HOTOSPOT_SETTINGS_CODE
import com.smartswitch.phoneclone.interfaces.CAPPDialogueClickListener
import com.yarolegovich.slidingrootnav.SlideGravity
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder
import com.yarolegovich.slidingrootnav.callback.DragListener


//import com.switchphone.transferdata.ads.AATIntersitialAdHelper
//import com.switchphone.transferdata.ads.AATNativeAdHelper


class CAPPActivityChooseDevice : AppCompatActivity(), CAPPDialogueClickListener,NavigationView.OnNavigationItemSelectedListener {
    /* var HSMyPermissions: HSMyPermissions? = null
     var myDialogueBoxes: HSMyDialogues? = null
     var wifiUtilsClass: HSWifiUtilsClass? = null*/

    //var turnWifiOff = false
    var launchNewPhone = false
    var launchOldPhone = false
    var navigationView:NavigationView?=null
    var dLayout: DrawerLayout? = null

    var navSlider: SlidingRootNav? =null
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_mobile_capp)

         navSlider = SlidingRootNavBuilder(this)
            .withMenuLayout(R.layout.menu_layout)
             .withGravity(SlideGravity.LEFT) //If LEFT you can swipe a menu from left to right, if RIGHT - the direction is opposite.
            .withSavedState(savedInstanceState) //If you call the method, layout will restore its opened/closed state
            .withContentClickableWhenMenuOpened(true)
             .withDragDistance(140) //Horizontal translation of a view. Default == 180dp
             .withRootViewScale(0.7f) //Content view's scale will be interpolated between 1f and 0.7f. Default == 0.65f;
             .withRootViewElevation(10) //Content view's elevation will be interpolated between 0 and 10dp. Default == 8.
             .withRootViewYTranslation(4)
             .withContentClickableWhenMenuOpened(true)
             .addDragListener(DragListener {
                 if(it==0f){
                     val window: Window = window
                     window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                     window.statusBarColor = resources.getColor(R.color.appcolorpurple)
                 }else{
                     val window: Window = window
                     window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                     window.statusBarColor = resources.getColor(R.color.buttonsColor)
                 }
             })
            .inject()

            findViewById<ConstraintLayout>(R.id.constMain).setOnClickListener(View.OnClickListener {
                if(navSlider?.isMenuOpened == true){
                    navSlider?.closeMenu()
                }
            })

            findViewById<TextView>(R.id.newPhone).setOnClickListener(View.OnClickListener {
                handleNewPhoneClicked()
                navSlider?.closeMenu()
                val window: Window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = resources.getColor(R.color.appcolorpurple)
            })
        findViewById<TextView>(R.id.oldPhone).setOnClickListener(View.OnClickListener {
            handleOldPhoneClicked()
            navSlider?.closeMenu()
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.appcolorpurple)
        })
        findViewById<TextView>(R.id.rate).setOnClickListener(View.OnClickListener {
            rateApp()
            navSlider?.closeMenu()
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.appcolorpurple)
        })
        findViewById<TextView>(R.id.share).setOnClickListener(View.OnClickListener {
            shareMyApp()
            navSlider?.closeMenu()
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.appcolorpurple)
        })
        findViewById<TextView>(R.id.exit).setOnClickListener(View.OnClickListener {
            onBackPressed()
            navSlider?.closeMenu()
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.appcolorpurple)
        })

//        navigationView=findViewById(R.id.nav_view)
//        dLayout=findViewById(R.id.drawerLayout)
//        navigationView?.setNavigationItemSelectedListener(this)
//
        findViewById<ImageView>(R.id.btnnavigation).setOnClickListener(View.OnClickListener {
            navSlider?.openMenu()
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.buttonsColor)

        })


        /* if(intent.hasExtra("newversion")){
             var version=intent.getStringExtra("newversion")
             if(version!=null&&version?.isNotEmpty()){
                 showDialogueForUpdate(version)
             }

         }*/
        // HSIntersitialAdHelper.showAdmobIntersitial(this@HSActivityChooseDevice)
        // HSIntersitialAdHelper.showStartEndAdmobInterstitial(this@HSActivityChooseDevice)
        /*  admobBanner()
          loadFbBannerAdd()*/

        /*wifiUtilsClass = HSWifiUtilsClass(this)
        myDialogueBoxes = HSMyDialogues(this)
        HSMyPermissions = HSMyPermissions(applicationContext, this@HSActivityChooseDevice)*/
//
//        AshIntersitialAdHelper.loadAdmobBanner(
//            this,
//            findViewById(R.id.top_banner),
//            resources.getString(R.string.admob_banner)
//        )

        //new phone choosen
        findViewById<ConstraintLayout>(R.id.send_cv).setOnClickListener {
            handleOldPhoneClicked()
        }
        findViewById<ConstraintLayout>(R.id.rec_cv).setOnClickListener {
            handleNewPhoneClicked()
        }
        //old phone choosen
        //copyVideoFromAssetstoGallery()
        //checkBluetooth()

    }

    interface OpenSettingsForPermissions {
        fun allowCameraPermissions(context: Context);
    }

    /*   private fun showDialogueForUpdate(newVersion:String) {
           val pInfo: PackageInfo = this@HSActivityChooseDevice.packageManager.getPackageInfo(this@HSActivityChooseDevice.packageName, 0)
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
           val pInfo: PackageInfo = this@HSActivityChooseDevice.packageManager.getPackageInfo(this@HSActivityChooseDevice.packageName, 0)
           val version = pInfo.versionName
           if ((version) != newVersion.toString()) {
               //perform your task here like show alert dialogue "Need to upgrade app"
               HSMyDialogues.updateToNewVersion(this@HSActivityChooseDevice,
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
         if (requestCode == HSMyConstants.PERMISSIONS_REQUSTCODE) {
             if (HSMyPermissions?.isRuntimePermissions()!!) {
                 if (launchNewPhone) {
                     launchNewPhoneActivity()
                 } else {
                     launchOldPhoneActivity()
                 }
             } else {
                 HSMyPermissions?.requestRunTimePermissions()
             }
         } else if (requestCode == HSMyConstants.LOCATION_PERMISIION_REQUEST_CODE && permissions.isNotEmpty() && grantResults.isNotEmpty()) {
             if (grantResults[0].equals(PackageManager.PERMISSION_GRANTED)) {
                 if (launchNewPhone) {
                     launchNewPhoneActivity()
                 } else {
                     launchOldPhoneActivity()
                 }
             } else {
                 //permission denied do nothing
             }
         } else if (requestCode == HSMyConstants.REQUEST_LOCATION_TURNON && permissions.isNotEmpty() && grantResults.isNotEmpty()) {
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
        if(navSlider?.isMenuOpened == true){
            navSlider?.closeMenu()
        }
        launchNewPhone = true
        launchOldPhone = false
        //check for location permission
        /*  if (HSMyPermissions?.isRuntimePermissions()!!) {*/
        launchNewPhoneActivity()
        /*} else {
            HSMyPermissions?.requestRunTimePermissions()
        }*/
    }

    private fun handleOldPhoneClicked() {
        //perform checks
        if(navSlider?.isMenuOpened == true){
            navSlider?.closeMenu()
        }
        launchNewPhone = false
        launchOldPhone = true
        //check if have camera permission
        //if do go ahead
        /*if (HSMyPermissions?.isRuntimePermissions()!!) {*/
        launchOldPhoneActivity()
        /*} else {
            HSMyPermissions?.requestRunTimePermissions()
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
        startActivityForResult(intent, CAPPMConstants.PERMISSIONS_REQUSTCODE)
    }

    override fun transferFinished() {

    }

    override fun isAndroid10() {
        val intent = Intent(this, CAPPWifiP2PCreateGroupAndReceive::class.java)
        startActivity(intent)
    }

    override fun isNotAndroid10() {
        val intent = Intent(this, CAPPWifiP2PCreateGroupAndReceive::class.java)
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
        if (requestCode == HSMyConstants.WIFI_TURN_ON) {
            if (launchNewPhone) {
                launchNewPhoneActivity()
            } else {
                launchOldPhoneActivity()
            }
        } else if (requestCode == HSMyConstants.PERMISSIONS_REQUSTCODE) {
            if (HSMyPermissions?.isRuntimePermissions()!!) {
                if (launchNewPhone) {
                    launchNewPhoneActivity()
                } else {
                    launchOldPhoneActivity()
                }
            } else {
                HSMyPermissions?.requestRunTimePermissions()
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
                if (HSMyPermissions!!.CheckIfGPSisOn()) {
                    Handler().postDelayed({
                        val intent = Intent(this, HSHSWifiP2PCreateGroupAndReceive::class.java)
                        startActivity(intent)
                    }, 1000)
                } else {
                    HSMyPermissions!!.turnGpsOn()
                }

            }
        } else {
            if (HSMyPermissions!!.CheckIfGPSisOn()) {*/
        val intent = Intent(this, CAPPWifiP2PCreateGroupAndReceive::class.java)
        startActivity(intent)
        /* } else {
             HSMyPermissions!!.turnGpsOn()
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
            if (HSMyPermissions!!.CheckIfGPSisOn()) {*/
        val intent = Intent(this, CAPPDataSelectionMainActivity::class.java)
        startActivity(intent)
        /*} else {
            HSMyPermissions!!.turnGpsOn()
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
        /* HSIntersitialAdHelper.showStartEndAdmobInterstitial(this@HSActivityChooseDevice)*/

//        AshIntersitialAdHelper.showAdmobIntersitial_mediated(this@AshActivityChooseDevice, null)
        if(navSlider?.isMenuOpened == true){
            navSlider?.closeMenu()
        }
            exitorcontinue()



    }

    fun exitorcontinue() {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.db_exitdialogue_capp)
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

/*
       if(HSNativeAdHelper.mynativeAds?.isAdLoaded==true&&HSNativeAdHelper.mynativeAds?.isAdInvalidated==false){
          *//* val nativeAdLayout = dialog.findViewById<NativeAdLayout>(R.id.native_ad_container)
           nativeAdLayout.visibility=View.VISIBLE
           dialog.findViewById<ImageView>(R.id.iv_logo).visibility=View.GONE
           HSNativeAdHelper.showAd(this, nativeAdLayout)*//*
       }else{
           dialog.findViewById<ImageView>(R.id.iv_logo).visibility=View.VISIBLE
           dialog.findViewById<NativeAdLayout>(R.id.native_ad_container).visibility=View.GONE
       }*/

        val ad_frame = dialog.findViewById<FrameLayout>(R.id.ad_frame)

//        AshNativeAdHelper.showAdmobNativeAd(this@AshActivityChooseDevice, ad_frame)

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
        if(navSlider?.isMenuOpened == true){
            navSlider?.closeMenu()
        }
        try {
            val rateIntent = rateIntentForUrl("market://details?id=${BuildConfig.APPLICATION_ID}")
            startActivity(rateIntent)
        } catch (e: ActivityNotFoundException) {
            val rateIntent =
                rateIntentForUrl("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
            startActivity(rateIntent)
        }
    }


/*    fun admobBanner() {

        val mAdView = com.google.android.gms.ads.AdView(this@HSActivityChooseDevice)
        val adSize: com.google.android.gms.ads.AdSize = HSIntersitialAdHelper.getAdSize(this@HSActivityChooseDevice)
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

    }*/

/*    fun loadFbBannerAdd() {

        val adView = AdView(
            this@HSActivityChooseDevice,
            this@HSActivityChooseDevice.resources.getString(R.string.banner_add),
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
    }*/

    override fun onDestroy() {
        //NativeAdds.googleNativeAd?.destroy()
        super.onDestroy()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemId: Int = item.getItemId()
        if(itemId==R.id.menuNewPhone){
            handleNewPhoneClicked()
        }else if(itemId==R.id.menuOldPhone){
            handleOldPhoneClicked()
        }else if(itemId==R.id.menuRateUs){
            rateApp()
        }else if(itemId==R.id.menuShare){
            shareMyApp()
        }else if(itemId==R.id.menuExit){
            onBackPressed()
        }

//        dLayout?.closeDrawer(Gravity.LEFT,true)

        return true
    }

    private fun shareMyApp() {
        if(navSlider?.isMenuOpened == true){
            navSlider?.closeMenu()
        }
        val urlToShare="${applicationContext.resources.getString(R.string.play_store_link)}${BuildConfig.APPLICATION_ID}"
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "${applicationContext.resources.getString(R.string.share_message)} $urlToShare"
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }




}