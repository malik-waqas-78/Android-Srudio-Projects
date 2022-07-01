package com.data.usage.manager.activities

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.data.usage.manager.BuildConfig
import com.data.usage.manager.constants.Constants
import com.data.usage.manager.fragments.Fragment_Mobile_Data
import com.data.usage.manager.fragments.Fragment_Plan_Data
import com.data.usage.manager.fragments.Fragment_Wifi_Data
import com.data.usage.manager.interfaces.DialogueClickListner
import com.data.usage.manager.interfaces.InteractWithUi
import com.data.usage.manager.R
import com.data.usage.manager.sharedpreferences.MySharedPreferences
import com.data.usage.manager.usefullclasses.MyAppPermissions
import com.data.usage.manager.usefullclasses.MyDialogueBoxes
import java.util.*
import  android.os.Handler
import android.view.Gravity
import android.widget.RelativeLayout
import com.data.usage.manager.adds.IntersitialAdds
import com.data.usage.manager.adds.NativeAdds
import com.data.usage.manager.fragments.Fragment_Speed_Data
import com.facebook.ads.*
import java.util.concurrent.ExecutionException


class Navigation_View_Activity : AppCompatActivity(), InteractWithUi, DialogueClickListner {

    lateinit var btn_nav_mobile: ImageView
    lateinit var btn_nav_wifi: ImageView
    lateinit var btn_nav_plan: ImageView
    lateinit var btn_nav_speed: ImageView

    lateinit var txt_nav_mobile: TextView
    lateinit var txt_nav_wifi: TextView
    lateinit var txt_nav_plan: TextView
    lateinit var txt_nav_speed: TextView
    var mobileFragment: Fragment_Mobile_Data? = null
    var wifiFragment: Fragment_Wifi_Data? = null
    lateinit var speedFragment: Fragment_Speed_Data
    var planFragment: Fragment_Plan_Data? = null

    lateinit var fragmentTransaction: FragmentTransaction
    lateinit var mySharedPreferences: MySharedPreferences
    lateinit var myDialogueBoxes: MyDialogueBoxes
    lateinit var myAppPermissions: MyAppPermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.VERSION_CODE < 29) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }


        setContentView(R.layout.activity_bottom_navigation)
        if(IntersitialAdds.isAppInstalledFromPlay(this@Navigation_View_Activity)) {
            loadBannerAdd()
        }
        mySharedPreferences = MySharedPreferences(this)
        myDialogueBoxes = MyDialogueBoxes(this, this, layoutInflater)
        //hide action bar text
        supportActionBar?.title = ""
        //init buttons
        btn_nav_mobile = findViewById(R.id.nav_ic_mobile)
        btn_nav_wifi = findViewById(R.id.nav_ic_wifi)
        btn_nav_plan = findViewById(R.id.nav_ic_dataplan)
        btn_nav_speed = findViewById(R.id.nav_ic_speed)
        //init text le
        txt_nav_mobile = findViewById(R.id.nav_txt_mobile)
        txt_nav_wifi = findViewById(R.id.nav_txt_wifi)
        txt_nav_plan = findViewById(R.id.nav_txt_plan)
        txt_nav_speed = findViewById(R.id.nav_txt_speed)
        //init click listeners
        btn_nav_mobile.setOnClickListener(nav_mobile_clicked)
        btn_nav_wifi.setOnClickListener(nav_wifi_clicked)
        btn_nav_speed.setOnClickListener(nav_speed_clicked)
        btn_nav_plan.setOnClickListener(nav_plan_clicked)
        mobileFragment = Fragment_Mobile_Data()
        wifiFragment = Fragment_Wifi_Data()
        planFragment = Fragment_Plan_Data()
        speedFragment = Fragment_Speed_Data()
        //default fragment
        myAppPermissions = MyAppPermissions(this.applicationContext, this)

        if (intent.hasExtra(Constants.FRAGMENT_NAME)) {
            val frga = intent.getStringExtra(Constants.FRAGMENT_NAME)
            when {
                frga == Constants.FRAGMENT_MOBILE -> {
                    mobileIsSelected()
                    if (mobileFragment == null) {
                        mobileFragment = Fragment_Mobile_Data()
                    }
                    replacefragment(mobileFragment!!, Constants.FRAGMENT_MOBILE)
                }
                frga == Constants.FRAGMENT_WIFI -> {
                    wifiIsSelected()
                    if (wifiFragment == null) {
                        wifiFragment = Fragment_Wifi_Data()
                    }
                    replacefragment(wifiFragment!!, Constants.FRAGMENT_WIFI)
                }
                frga == Constants.FRAGMENT_SPEED_TEST -> {
                    speedIsSelected()
                    replacefragment(speedFragment, Constants.FRAGMENT_SPEED_TEST)
                }
                else -> {
                    mobileIsSelected()
                    if (mobileFragment == null) {
                        mobileFragment = Fragment_Mobile_Data()
                    }
                    replacefragment(mobileFragment!!, Constants.FRAGMENT_MOBILE)
                }
            }


//            if(intent.getBooleanExtra(Constants.MOBILE_PERMISSIONS,false)){
//                mobileIsSelected()
//                replacefragment(mobileFragment)
//            }else{
//                wifiIsSelected()
//                replacefragment(wifiFragment)
//            }
        } else {
            wifiIsSelected()
            if (wifiFragment == null) {
                wifiFragment = Fragment_Wifi_Data()
            }
            replacefragment(wifiFragment!!, Constants.FRAGMENT_WIFI)
        }

        /*val nativeAdLayout=findViewById<NativeAdLayout>(R.id.native_ad_container)
        if(NativeAdds.mynativeAds!!.isAdLoaded){
            NativeAdds.showAd(this,nativeAdLayout,NativeAdds.mynativeAds!!)
        }else{
            nativeAdLayout.visibility-View.GONE
        }*/

    }

    var nav_mobile_clicked = View.OnClickListener {

        if (mobileIsSelected()) {
            if (mobileFragment == null) {
                mobileFragment = Fragment_Mobile_Data()
            }
            replacefragment(mobileFragment!!, Constants.FRAGMENT_MOBILE)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode.equals(7724)) {
            if (myAppPermissions.isSystemLevelPermissionGiven()) {
                if (myAppPermissions.isRuntimePermissions()) {
                    mobileIsSelected()
                    if (mobileFragment == null) {
                        mobileFragment = Fragment_Mobile_Data()
                    }
                    replacefragment(mobileFragment!!, Constants.FRAGMENT_MOBILE)
                } else {
                    wifiIsSelected()
                    if (wifiFragment == null) {
                        wifiFragment = Fragment_Wifi_Data()
                    }
                    replacefragment(wifiFragment!!, Constants.FRAGMENT_WIFI)
                }
            } else {
                if (planFragment == null) {
                    planFragment = Fragment_Plan_Data()
                }
                replacefragment(planFragment!!, Constants.FRAGMENT_PLAN)
            }
        }
    }

    fun mobileIsSelected(): Boolean {
        if (!myAppPermissions.isRuntimePermissions()) {
            if (!mySharedPreferences.canAskForPermissions()) {
                myDialogueBoxes.cantAskForPermissions()
            } else {
                myAppPermissions.requestForRunTimePermissions()
            }
            return false
        }
        //#####
        //handle nav view
        btn_nav_mobile.visibility = View.INVISIBLE
        btn_nav_wifi.visibility = View.VISIBLE
        btn_nav_plan.visibility = View.VISIBLE
        btn_nav_speed.visibility = View.VISIBLE

        txt_nav_mobile.visibility = View.VISIBLE
        txt_nav_wifi.visibility = View.INVISIBLE
        txt_nav_plan.visibility = View.INVISIBLE
        txt_nav_speed.visibility = View.INVISIBLE
        return true;
        //#####
    }

    var nav_wifi_clicked = View.OnClickListener {

        if (myAppPermissions.isSystemLevelPermissionGiven()) {
            wifiIsSelected()
            if (wifiFragment == null) {
                wifiFragment = Fragment_Wifi_Data()
            }
            replacefragment(wifiFragment!!, Constants.FRAGMENT_WIFI)
        } else {
            myDialogueBoxes.systemLevelPermissionsWarning(
                "Permission Required",
                "This Application needs Usage Access Permission to perform data Monitoring." +
                        "Plz Allow Usage Access Permission to continue!!!"
            )

        }
    }

    fun wifiIsSelected() {
        //#####
        //handle nav view
        btn_nav_mobile.visibility = View.VISIBLE
        btn_nav_wifi.visibility = View.INVISIBLE
        btn_nav_plan.visibility = View.VISIBLE
        btn_nav_speed.visibility = View.VISIBLE

        txt_nav_mobile.visibility = View.INVISIBLE
        txt_nav_wifi.visibility = View.VISIBLE
        txt_nav_plan.visibility = View.INVISIBLE
        txt_nav_speed.visibility = View.INVISIBLE
        //#####
    }

    var nav_plan_clicked = View.OnClickListener {

        openPlanFragment()

    }

    private fun openPlanFragment() {
        planIsSelected()
        if (planFragment == null) {
            planFragment = Fragment_Plan_Data()
        }
        replacefragment(planFragment!!, Constants.FRAGMENT_PLAN)
    }

    fun planIsSelected() {
        //#####
        //handle nav view
        btn_nav_mobile.visibility = View.VISIBLE
        btn_nav_wifi.visibility = View.VISIBLE
        btn_nav_plan.visibility = View.INVISIBLE
        btn_nav_speed.visibility = View.VISIBLE

        txt_nav_mobile.visibility = View.INVISIBLE
        txt_nav_wifi.visibility = View.INVISIBLE
        txt_nav_plan.visibility = View.VISIBLE
        txt_nav_speed.visibility = View.INVISIBLE
        //#####
    }

    var nav_speed_clicked = View.OnClickListener {

        speedIsSelected()

        replacefragment(speedFragment, Constants.FRAGMENT_SPEED_TEST)

    }

    fun speedIsSelected() {
        //#####
        //handle nav view
        btn_nav_mobile.visibility = View.VISIBLE
        btn_nav_wifi.visibility = View.VISIBLE
        btn_nav_plan.visibility = View.VISIBLE
        btn_nav_speed.visibility = View.INVISIBLE

        txt_nav_mobile.visibility = View.INVISIBLE
        txt_nav_wifi.visibility = View.INVISIBLE
        txt_nav_plan.visibility = View.INVISIBLE
        txt_nav_speed.visibility = View.VISIBLE
        //#####
    }

    override fun goToPlanActivity() {
        openPlanFragment()
    }

    fun replacefragment(frag: Fragment, tag: String) {
//        if(supportFragmentManager.findFragmentByTag(tag)!=null&&supportFragmentManager.findFragmentByTag(tag)!!.isAdded){
//            fragmentTransaction=supportFragmentManager.beginTransaction()
//            fragmentTransaction.show(frag).commit()
//        }else{
//            fragmentTransaction=supportFragmentManager.beginTransaction()
//            fragmentTransaction.add(R.id.fragments_framelayout,frag,tag).commit()
//        }

        try {
            val fragmentMobileData: Fragment? =
                supportFragmentManager.findFragmentByTag(tag)
            if (fragmentMobileData != null && fragmentMobileData.isVisible) {
                return
            }
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragments_framelayout, frag, tag).commit()
        } catch (e: Exception) {

        }
    }

    override fun positiveRunTimeButton() {
        myAppPermissions.requestForRunTimePermissions()
    }

    override fun negativeRunTimeButton() {

    }

    override fun positiveSyatemLevelButton() {
        startActivityForResult(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), 7724);
    }

    override fun negativeSyatemLevelButton() {
        speedIsSelected()
        replacefragment(speedFragment, Constants.FRAGMENT_SPEED_TEST)
    }

    override fun turnPermissionsOn() {
        val i = Intent()
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        i.addCategory(Intent.CATEGORY_DEFAULT)
        i.setData(Uri.parse("package:" + this.getPackageName()))
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        this.startActivity(i)
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
        TODO("Not yet implemented")
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        if (Build.VERSION.SDK_INT > 23) {
            // super.onSaveInstanceState(outState, outPersistentState)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //  Toast.makeText(this,"Permission result",Toast.LENGTH_SHORT).show()
        /* if(requestCode==9876||requestCode==Constants.REQUEST_LOCATION_TURNON){
             super.onRequestPermissionsResult(requestCode,permissions,grantResults)
             return
         }*/

//        speedIsSelected()
//        replacefragment(speedFragment, Constants.FRAGMENT_SPEED_TEST)
        /*    if (requestCode == 9876 && permissions.isNotEmpty()) {
                if (grantResults.isNotEmpty() && grantResults[0].equals(PackageManager.PERMISSION_GRANTED)) {
                    //check if gps is on
                    if (myAppPermissions!!.checkIfGPSisOn()) {
                        speedIsSelected()
                        replacefragment(speedFragment, Constants.FRAGMENT_SPEED_TEST)
                    } else {
                        myAppPermissions!!.turnGpsOn()
                    }
                } else {
                    Toast.makeText(this!!, "Permissions Denied", Toast.LENGTH_SHORT).show()
                }
            } else
                if (requestCode == Constants.REQUEST_LOCATION_TURNON) {
                if (myAppPermissions!!.checkIfGPSisOn()) {
                    speedIsSelected()
                    replacefragment(speedFragment, Constants.FRAGMENT_SPEED_TEST)
                } else {
                    Toast.makeText(this!!, "Permissions Denied", Toast.LENGTH_SHORT).show()
                }
            } else*/
        if (grantResults.size == 0 || permissions.size == 0)
            return
        else if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (!myAppPermissions.isSystemLevelPermissionGiven()) {
                myDialogueBoxes.systemLevelPermissionsWarning(
                    "Permission Required",
                    "This Application needs Usage-Access-Permission to perform data Monitoring.\nPlz Allow Usage-Access-Permission to continue!!!"
                )
            } else {
                if (Build.VERSION.SDK_INT > 23) {
                    mobileIsSelected()
                    if (mobileFragment == null) {
                        mobileFragment = Fragment_Mobile_Data()
                    }
                    replacefragment(mobileFragment!!, Constants.FRAGMENT_MOBILE)
                } else {
                    Handler().postDelayed({
                        mobileIsSelected()
                        if (mobileFragment == null) {
                            mobileFragment = Fragment_Mobile_Data()
                        }
                        replacefragment(mobileFragment!!, Constants.FRAGMENT_MOBILE)
                    }, 200)
                }
            }
            //                       else{
//                           proceedToMainActivity(Constants.FRAGMENT_MOBILE)
//                       }
        } else {
            val showRationale = shouldShowRequestPermissionRationale(permissions[0])
            if (!showRationale) {
                val editor = mySharedPreferences.getPreferencesEditor()
                editor.putBoolean(Constants.ASK_FOR_PERMISSION, false)
                editor.commit()
                if (!myAppPermissions.isSystemLevelPermissionGiven()) {
                    myDialogueBoxes.systemLevelPermissionsWarning(
                        "Permission Required",
                        "This Application needs Usage Access Permission to perform data Monitoring.\nPlz Allow Usage Access Permission to continue!!!"
                    )
                } else {
                    wifiIsSelected()
                    if (wifiFragment == null) {
                        wifiFragment = Fragment_Wifi_Data()
                    }
                    replacefragment(wifiFragment!!, Constants.FRAGMENT_WIFI)
                }
            } else {
                if (!myAppPermissions.isSystemLevelPermissionGiven()) {
                    myDialogueBoxes.systemLevelPermissionsWarning(
                        "Permission Required",
                        "This Application needs Usage Access Permission to perform data Monitoring.\nPlz Allow Usage Access Permission to continue!!!"
                    )
                } else {
                    wifiIsSelected()
                    if (wifiFragment == null) {
                        wifiFragment = Fragment_Wifi_Data()
                    }
                    replacefragment(wifiFragment!!, Constants.FRAGMENT_WIFI)
                    myDialogueBoxes.cantAskForPermissions()
                }
            }
        }

    }

    override fun onBackPressed() {
        IntersitialAdds.showFullAdAdmob(this@Navigation_View_Activity)
        exit_Dialog()
    }

    private fun exit_Dialog() {
        val dialog = Dialog(this@Navigation_View_Activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        // dialog.setCancelable(false)
        dialog.setContentView(R.layout.exit_dialoguebox_layout)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val ok = dialog.findViewById<TextView>(R.id.btn_dismiss)
        val no = dialog.findViewById<TextView>(R.id.btn_no)
        val rate_us = dialog.findViewById<TextView>(R.id.btn_allow)
        rate_us.setOnClickListener { rateApp() }
        no.setOnClickListener { dialog.dismiss() }
        ok.setOnClickListener { finishAffinity() }
        val nativeAdLayout = dialog.findViewById<NativeAdLayout>(R.id.native_ad_container)
        if (NativeAdds.mynativeAds != null && NativeAdds.mynativeAds?.isAdLoaded == true) {
            NativeAdds.showAd(this, nativeAdLayout, NativeAdds.mynativeAds!!)
        } else {
            nativeAdLayout.visibility = View.GONE
        }
        dialog.show()
    }

    private fun rateIntentForUrl(url: String): Intent? {
        var intent: Intent? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    url + Objects.requireNonNull(this@Navigation_View_Activity).getPackageName()
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
            val rateIntent = rateIntentForUrl("market://details?id=")
            startActivity(rateIntent)
        } catch (e: ActivityNotFoundException) {
            val rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details?id=")
            startActivity(rateIntent)
        }
    }

    fun loadBannerAdd() {

        val adView = AdView(
            this@Navigation_View_Activity,
            this@Navigation_View_Activity.resources.getString(R.string.banner_add),
            AdSize.BANNER_HEIGHT_50
        )

        val adListener: AdListener = object : AdListener {

            override fun onError(ad: Ad, adError: AdError) {
                // Ad error callback
                if (BuildConfig.DEBUG) {
                    /*       Toast.makeText(
                               this@Navigation_View_Activity,
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
}