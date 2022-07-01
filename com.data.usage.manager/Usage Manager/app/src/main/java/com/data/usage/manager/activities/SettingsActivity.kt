package com.data.usage.manager.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.data.usage.manager.BuildConfig
import com.data.usage.manager.interfaces.DialogueClickListner
import com.data.usage.manager.usefullclasses.MyDialogueBoxes
import com.data.usage.manager.R
import com.data.usage.manager.sharedpreferences.MySharedPreferences
import com.data.usage.manager.usefullclasses.MyAppPermissions
import com.data.usage.manager.adds.IntersitialAdds
import com.facebook.ads.*

class SettingsActivity : AppCompatActivity() ,DialogueClickListner{

    lateinit var sw_runTime: Switch
    lateinit var sw_usageAccess: Switch
    //lateinit var sw_runInBack: Switch
    lateinit var myDialogueBoxes: MyDialogueBoxes
    lateinit var myPermission : MyAppPermissions
    var sp_sim_selected:Spinner?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(BuildConfig.VERSION_CODE <30){
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        setContentView(R.layout.activity_settings)
        if(IntersitialAdds.isAppInstalledFromPlay(this@SettingsActivity)) {
            loadBannerAdd()
            IntersitialAdds.showAdd()
        }
        myDialogueBoxes= MyDialogueBoxes(this,this,layoutInflater)
        myPermission  = MyAppPermissions(appContext = applicationContext,
            activityContext = this@SettingsActivity)

        sw_runTime=findViewById(R.id.sw_runtime)
        sp_sim_selected=findViewById(R.id.sp_selected_simslot_settings)
        sw_usageAccess=findViewById(R.id.sw_usageaccess)
        //sw_runInBack=findViewById(R.id.sw_runinbackground)
        sw_runTime.isChecked = myPermission.isRuntimePermissions()
        sw_usageAccess.isChecked = myPermission.isSystemLevelPermissionGiven()
        //sw_runInBack.isChecked = myPermission.isIgnoringBatteryOptimization()
        sp_sim_selected?.setSelection(MySharedPreferences(applicationContext).getDefaultSimSelected())
        sw_runTime.setOnClickListener {view:View->
            if (sw_runTime.isChecked) {
                myPermission.isRunTimePermissionsGiven()
            } else {
                //showDialogue explaing disadvatages of disallowin permissions
                    myDialogueBoxes.showWarning()
            }
        }
/*        sw_runInBack.setOnClickListener {view:View->
            if (sw_runInBack.isChecked) {
                //showDialogueBox then do the following on allow
                //myDialogueBoxes.ignoreBatteryOptimization()
                myPermission.requestIgnoreBatteryOptimization()
            } else {
                //showDialogue explaing disadvatages of disallowing permissions
                myDialogueBoxes.ignoreBatteryOptimization()
//                myPermission.ignoreBatteryOptimization()
            }
        }*/
        sp_sim_selected?.onItemSelectedListener= ItemSelected()
        sw_usageAccess.setOnClickListener {view:View->
            if (sw_usageAccess.isChecked) {
                myDialogueBoxes.systemLevelPermissionsWarning("Permission Required",
                    "This Application needs Usage Access Permission to perform data Monitoring.\nAllow/dismiss Usage Access Permission to continue!!!"
                )
            } else {
                //showDialogue explaing disadvatages of disallowin permissions
                myDialogueBoxes.removeusagepermission("Warning",
                    "This Application needs Usage Access Permission to get Data Usage Access.\nDo you still want to continue?!!"
                )
            }
        }

    }
    inner class ItemSelected :OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            var sharedPreferences=MySharedPreferences(applicationContext)
            sharedPreferences.setDefaultSelectedSim(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

    }

    override fun onResume(){
        super.onResume()
        val myPermission : MyAppPermissions = MyAppPermissions(appContext = applicationContext,
            activityContext = this)
        sw_runTime.isChecked = myPermission.isRuntimePermissions()
        sw_usageAccess.isChecked = myPermission.isSystemLevelPermissionGiven()
        //sw_runInBack.isChecked = myPermission.isIgnoringBatteryOptimization()
    }

    override fun positiveRunTimeButton() {
    }

    override fun negativeRunTimeButton() {
    }

    override fun positiveSyatemLevelButton() {
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }

    override fun negativeSyatemLevelButton() {
        sw_usageAccess.isChecked=!sw_usageAccess.isChecked
    }

    override fun turnPermissionsOn() {
        myPermission.ignoreRunTimePermission()
    }

    override fun dismissed() {
    sw_runTime.isChecked=!sw_runTime.isChecked
    }

    override fun ignoreBatteryOptimization() {
        myPermission.ignoreBatteryOptimization()
    }

    override fun ignoreDismissed() {
       // sw_runInBack.isChecked=!sw_runInBack.isChecked
    }

    override fun systemRemoved() {
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }

    override fun sysytemdismissed() {
       sw_usageAccess.isChecked=!sw_usageAccess.isChecked
    }

    override fun cancelListener() {
        sw_runTime.isChecked = myPermission.isRuntimePermissions()
        sw_usageAccess.isChecked = myPermission.isSystemLevelPermissionGiven()
      //  sw_runInBack.isChecked = myPermission.isIgnoringBatteryOptimization()
    }

    override fun retrySpeedTest() {

    }

    override fun deletePlan() {

    }

    fun loadBannerAdd(){

        val adView= AdView(this@SettingsActivity, this@SettingsActivity.resources.getString(R.string.banner_add), AdSize.BANNER_HEIGHT_50)

        val adListener: AdListener = object : AdListener {

            override fun onError(ad: Ad, adError: AdError) {
                // Ad error callback
               if(BuildConfig.DEBUG){
                   /*Toast.makeText(
                       this@SettingsActivity,
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