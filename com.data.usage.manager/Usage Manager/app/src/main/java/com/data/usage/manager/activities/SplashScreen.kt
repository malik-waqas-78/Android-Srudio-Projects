package com.data.usage.manager.activities
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.data.usage.manager.BuildConfig
import com.data.usage.manager.R
import com.data.usage.manager.adds.IntersitialAdds
import com.data.usage.manager.adds.NativeAdds
import com.data.usage.manager.constants.Constants
import com.data.usage.manager.interfaces.DialogueClickListner
import com.data.usage.manager.services.MyInformationService
import com.data.usage.manager.sharedpreferences.MySharedPreferences
import com.data.usage.manager.usefullclasses.MyAppPermissions
import com.data.usage.manager.usefullclasses.MyDialogueBoxes
import com.facebook.ads.*



class SplashScreen : AppCompatActivity(), DialogueClickListner {

    var clickCounter = false

    companion object get {

    }

    lateinit var myDialogueBoxes: MyDialogueBoxes
    lateinit var myAppPermissions: MyAppPermissions
    lateinit var mySharedPreferences: MySharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.VERSION_CODE < 30) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setContentView(R.layout.activity_splash_screen)

//         MediationTestSuite.launch(this@SplashScreen);

        if(IntersitialAdds.isAppInstalledFromPlay(this@SplashScreen)) {
            loadBannerAdd()
            //admobBanner()
            IntersitialAdds.prepareAdd(this@SplashScreen)
            NativeAdds.prepareAdd(this@SplashScreen)
            IntersitialAdds.loadAdmobInterstitial(this@SplashScreen)
        }
        //views initilizaton
        myDialogueBoxes = MyDialogueBoxes(this@SplashScreen, this, layoutInflater)
        mySharedPreferences = MySharedPreferences(this)
        myAppPermissions = MyAppPermissions(appContext = applicationContext, activityContext = this)


        val pbar_loadAdds = findViewById<ProgressBar>(R.id.pbar_loadadds)
        val btn_proceed = findViewById<Button>(R.id.proceed)

        Handler().postDelayed({
            btn_proceed.visibility = View.VISIBLE
            pbar_loadAdds.visibility = View.GONE
        }, 3000)
        btn_proceed.setOnClickListener(proceedClickListener)
        var textView: TextView = findViewById(R.id.privacypolicy)
        textView.setOnClickListener {
            privacy_Dialog();
        }


    }

    private fun privacy_Dialog() {
        val dialog = Dialog(this@SplashScreen, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.dialog_privacy);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow()!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        );
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        val ok = dialog.findViewById<Button>(R.id.btn_okay)
        ok.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT > 23) {
            if (myAppPermissions.isSystemLevelPermissionGiven()) {
                myAppPermissions = MyAppPermissions(this.applicationContext, this)
                //myAppPermissions.checkAndRequestForIgnoreBatteryOptimizationPermission();
                if (myAppPermissions.isRuntimePermissions()) {
                    launchForegroundService()
                }
//            else{
//                myAppPermissions.requestForRunTimePermissions()
////                myDialogueBoxes.runtimePermissionsWarning("Warning",
////                "This Application needs Read-Phone-State permission to perform it's operations.\nPlz Allow permissions to continue!!!")
////                //show dialogue box can't perform any action as permissions are not given
//            }
            }
        }
//        else{
////           myDialogueBoxes.systemLevelPermissionsWarning(
////               "Warning",
////               "This Application needs Usage-Access-Permission to perform data Monitoring.\nPlz Allow Usage-Access-Permission to continue!!!"
////           )
//            //show dialogue box can't perform data monitoring as permissions are not given
//        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when {
            requestCode == Constants.PERMISSIONS_REQUSTCODE ->
                if (grantResults.size == 0 || permissions.size == 0)
                    return
                else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!myAppPermissions.isSystemLevelPermissionGiven()) {
                        myDialogueBoxes.systemLevelPermissionsWarning(
                            "Warning",
                            "This Application needs Usage Access Permission to perform data Monitoring.\nPlz Allow Usage Access Permission to continue!!!"
                        )
                    } else {
                        proceedToMainActivity(Constants.FRAGMENT_MOBILE)
                    }
                } else {
                    val showRationale = shouldShowRequestPermissionRationale(permissions[0])
                    if (!showRationale) {
                        val editor = mySharedPreferences.getPreferencesEditor()
                        editor.putBoolean(Constants.ASK_FOR_PERMISSION, false)
                        editor.commit()
                        if (!myAppPermissions.isSystemLevelPermissionGiven()) {
                            myDialogueBoxes.systemLevelPermissionsWarning(
                                "Warning",
                                "This Application needs Usage Access Permission to perform data Monitoring.\nPlz Allow Usage Access Permission to continue!!!"
                            )
                        } else {
                            proceedToMainActivity(Constants.FRAGMENT_WIFI)
                        }
                    } else {
                        if (!myAppPermissions.isSystemLevelPermissionGiven()) {
                            myDialogueBoxes.systemLevelPermissionsWarning(
                                "Warning",
                                "This Application needs Usage Access Permission to perform data Monitoring.\nPlz Allow Usage Access Permission to continue!!!"
                            )
                        } else {
                            proceedToMainActivity(Constants.FRAGMENT_WIFI)
                        }
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    val proceedClickListener = View.OnClickListener {
        clickCounter = true

        if (mySharedPreferences.canAskForPermissions()) {
            if (myAppPermissions.isRuntimePermissions()) {
                if (myAppPermissions.isSystemLevelPermissionGiven()) {
                    proceedToMainActivity(Constants.FRAGMENT_WIFI)
                } else {
                    myDialogueBoxes.systemLevelPermissionsWarning(
                        "Warning",
                        "This Application needs Usage Access Permission to perform data Monitoring. Plz Allow Usage Access Permission to continue!!!"
                    )
                }
            } else {
                askForRunTimePermisiions()
            }
        } else if (myAppPermissions.isSystemLevelPermissionGiven()) {
            proceedToMainActivity(Constants.FRAGMENT_WIFI)
        } else {
            myDialogueBoxes.systemLevelPermissionsWarning(
                "Warning",
                "This Application needs Usage Access Permission to perform data Monitoring. Plz Allow Usage Access Permission to continue!!!"
            );
        }
//                    else{
//                        myDialogueBoxes.systemLevelPermissionsWarning(
//                            "Warning",
//                            "This Application needs Usage Access Permission to perform data Monitoring." +
//                                    "\nPlz Allow Usage Access Permission to continue!!!"
//                        )
//                    }

//                else if (!myAppPermissions.isSystemLevelPermissionGiven()) {
//                    myDialogueBoxes.systemLevelPermissionsWarning(
//                        "Warning",
//                        "This Application needs Usage Access Permission to perform data Monitoring." +
//                                "\nPlz Allow Usage Access Permission to continue!!!"
//                    )
//                } else {
//                    proceedToMainActivity(Constants.FRAGMENT_WIFI)
//                }
//        } else if (!myAppPermissions.isSystemLevelPermissionGiven()) {
//            myDialogueBoxes.systemLevelPermissionsWarning(
//                "Warning",
//                "This Application needs Usage Access Permission to perform data Monitoring." +
//                        "\nPlz Allow Usage-Access-Permission to continue!!!"
//            )
//        } else {
//            proceedToMainActivity(Constants.FRAGMENT_WIFI)
//        }

    }

    private fun proceedToMainActivity() {
        val intent = Intent(applicationContext, Navigation_View_Activity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(intent)
    }

    private fun proceedToMainActivity(fragment: String) {
        val intent = Intent(applicationContext, Navigation_View_Activity::class.java)
        intent.putExtra(Constants.FRAGMENT_NAME, fragment)
        startActivity(intent)
    }

    fun askForRunTimePermisiions(): Boolean {
        return myAppPermissions.requestForRunTimePermissions()
    }

    private fun launchForegroundService() {
        if (myAppPermissions.isRuntimePermissions() && !myAppPermissions.isSystemLevelPermissionGiven()) {
            myDialogueBoxes.systemLevelPermissionsWarning(
                "Warning",
                "This Application needs Usage Access Permission to perform data Monitoring.\nPlz Allow Usage-Access-Permission to continue!!!"
            )
        }
        if (askForRunTimePermisiions()) {
            val intent = Intent(this, MyInformationService::class.java);
            // Constants.ActivityContext.startService(intent);
            ContextCompat.startForegroundService(this, intent);
            // Log.d(TAG, "onCreate: "+);
        }
    }

    private fun checkForPermisions(): Boolean {


        return myAppPermissions.isSystemLevelPermissionGiven();
    }

    override fun positiveRunTimeButton() {
        myAppPermissions.requestForRunTimePermissions()
    }

    override fun negativeRunTimeButton() {
        if (myAppPermissions.isSystemLevelPermissionGiven()) {
            proceedToMainActivity(Constants.FRAGMENT_WIFI)
        } else {
            myDialogueBoxes.systemLevelPermissionsWarning(
                "Warning",
                "This Application needs Usage Access Permission to perform data Monitoring.\nPlz Allow Usage-Access-Permission to continue!!!"
            )
        }
    }

    override fun positiveSyatemLevelButton() {

        val i = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        startActivityForResult(i, 7724)
    }

    override fun negativeSyatemLevelButton() {
        proceedToMainActivity(Constants.FRAGMENT_SPEED_TEST)
    }

    override fun turnPermissionsOn() {

    }

    override fun dismissed() {

    }

    override fun ignoreBatteryOptimization() {

    }

    override fun ignoreDismissed() {
    }

    override fun systemRemoved() {
    }

    override fun sysytemdismissed() {
    }

    override fun cancelListener() {

    }

    override fun retrySpeedTest() {
    }

    override fun deletePlan() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode.equals(7724)) {
            if (myAppPermissions.isSystemLevelPermissionGiven()) {
                if (myAppPermissions.isRuntimePermissions()) {
                    proceedToMainActivity(Constants.FRAGMENT_WIFI)
                } else {
                    proceedToMainActivity(Constants.FRAGMENT_WIFI)
                }
            } else {
                proceedToMainActivity(Constants.FRAGMENT_SPEED_TEST)
            }
        }
    }

    //adds
    fun loadBannerAdd() {

        val adView = AdView(
            this@SplashScreen,
            this@SplashScreen.resources.getString(R.string.banner_add),
            AdSize.BANNER_HEIGHT_50
        )

        val adListener: AdListener = object : AdListener {

            override fun onError(ad: Ad, adError: AdError) {
                if (BuildConfig.DEBUG) {
                    /* Toast.makeText(
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
        findViewById<RelativeLayout>(R.id.bannerContainer).addView(adView)
    }

  /*  fun admobBanner() {

        val mAdView = com.google.android.gms.ads.AdView(this@SplashScreen)
        val adSize: com.google.android.gms.ads.AdSize = IntersitialAdds.getAdSize(this@SplashScreen)
        mAdView.adSize = adSize

        mAdView.adUnitId = resources.getString(R.string.admob_banner)

        val adRequest = AdRequest.Builder().build()

        val adViewLayout = findViewById<View>(R.id.bannerContainer) as RelativeLayout
        adViewLayout.addView(mAdView)

        mAdView.loadAd(adRequest)

        mAdView.adListener = object : com.google.android.gms.ads.AdListener() {
            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdFailedToLoad( errorCode: LoadAdError) {
                super.onAdFailedToLoad(errorCode)
                Log.d(Constants.TAG, "onAdFailedToLoad: ${errorCode}")
                //adViewLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
                adViewLayout.visibility = View.INVISIBLE
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
}