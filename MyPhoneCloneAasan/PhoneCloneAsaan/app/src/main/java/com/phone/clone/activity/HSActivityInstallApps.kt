package com.phone.clone.activity

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.phone.clone.BuildConfig
import com.phone.clone.R
import com.phone.clone.adapters.HSAdapterApps

import com.phone.clone.constants.HSMyConstants
import com.phone.clone.constants.HSMyConstants.get.TAG
import com.phone.clone.modelclasses.HSAppsModel
import java.io.File
import kotlin.collections.ArrayList

class HSActivityInstallApps :AppCompatActivity() {

    var appsList=ArrayList<HSAppsModel>()

    var rc_contacts: RecyclerView?=null

    var HSAdapter_install_apps: HSAdapterApps?=null

    override fun onCreate(

        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.install_apps)

/*
        loadFbBannerAdd()
        admobBanner()*/

    }




    override fun onResume() {
        super.onResume()
       LoadData().execute()
    }
    inner class LoadData : AsyncTask<Void, Void, Void>() {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ConstraintLayout>(R.id.cl_progress).visibility=View.VISIBLE
            findViewById<ConstraintLayout>(R.id.cl_appslist).visibility=View.GONE
            findViewById<ConstraintLayout>(R.id.cl_noapps).visibility=View.GONE
        }
        override fun doInBackground(vararg params: Void?): Void? {
            //load data
            loadAppsList()
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)

            if (this@HSActivityInstallApps.isDestroyed || this@HSActivityInstallApps.isFinishing) {
                return
            }

            if (appsList.size > 0) {
                Log.d(TAG, "onViewCreated: appsListSize ${appsList.size}")
                findViewById<ConstraintLayout>(R.id.cl_progress).visibility=View.GONE
                findViewById<ConstraintLayout>(R.id.cl_appslist).visibility=View.VISIBLE
                findViewById<ConstraintLayout>(R.id.cl_noapps).visibility=View.GONE
                //manage RecycleView
                rc_contacts = findViewById(R.id.rc_apps)
                var linearLayoutManager = LinearLayoutManager(this@HSActivityInstallApps)
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                rc_contacts?.layoutManager = linearLayoutManager
                //create HSAdapter
                HSAdapter_install_apps = HSAdapterApps(this@HSActivityInstallApps, appsList)
                //connect HSAdapter to recycleView
                rc_contacts?.adapter = HSAdapter_install_apps
            } else {
                findViewById<ConstraintLayout>(R.id.cl_progress).visibility=View.GONE
                findViewById<ConstraintLayout>(R.id.cl_appslist).visibility=View.GONE
                findViewById<ConstraintLayout>(R.id.cl_noapps).visibility=View.VISIBLE
                //no contacts available
                findViewById<TextView>(R.id.tv_noapps).text = "No Contacts Received"
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== HSMyConstants.INSTALL_APPS){
            LoadData().execute()
        }
    }
    private fun loadAppsList() {
        appsList.clear()
        var installedApps=loadApps()
        val rootpath = Environment.getExternalStorageDirectory().absolutePath +  "/"+resources.getString(R.string.app_name)
        var appsDir= File(rootpath)
        var filsList=appsDir.listFiles()
        Log.d(TAG, "loadAppsList: ${filsList.size}")
        for(i in filsList){
            Log.d(TAG, "loadAppsList: ${i.extension}")
            if(i.extension.contains("apk")){
                Log.d(TAG, "loadAppsList: ${i.name}")
                var apk=HSAppsModel()
                apk.srcDir=i.absolutePath
               for(j in installedApps){
                   Log.d(TAG, "loadAppsList: ${i.name} ${j.apkName}")
                   if(i.name.contains(j.apkName)){
                       apk.installed=true
                       break
                   }
               }
                var packageInfo = packageManager.getPackageArchiveInfo(apk.srcDir, 0);
                if(packageInfo!=null)
                    appsList.add(apk)

            }
        }
    }

    override fun onBackPressed() {
        exitorcontinue()
    }
    fun exitorcontinue() {
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.db_appsalert)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val positive: Button = dialog.findViewById(R.id.btn_yes)
        positive.setOnClickListener {
            dialog.dismiss()
        }
        val negative: Button = dialog.findViewById(R.id.btn_exit)
        negative.setOnClickListener {
            setResult(HSMyConstants.INSTALL_APPS)
            finish()
        }

        dialog.setOnCancelListener {
            //dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.dismiss()
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
    fun loadApps(): ArrayList<HSAppsModel> {
        return checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA))
    }

    private fun checkForLaunchIntent(list: List<ApplicationInfo>): ArrayList<HSAppsModel> {
        Log.d(TAG, "checkForLaunchIntent: list size ${list.size}")
        val appList = ArrayList<HSAppsModel>()
        /*var count = 0*/
        for (info in list) {
            Log.d(TAG, "checkForLaunchIntent: src dir ${info.sourceDir}")
            Log.d(TAG, "checkForLaunchIntent: name  ${info.name}")
            Log.d(TAG, "checkForLaunchIntent: package name ${info.packageName}")
            try {
                if (packageManager.getLaunchIntentForPackage(info.packageName) != null) {
                    if (!isSystemPackage(
                            packageManager.getPackageInfo(
                                info.packageName,
                                PackageManager.GET_META_DATA
                            )
                        ) && BuildConfig.APPLICATION_ID != info.packageName
                    ) {
                        var app = HSAppsModel().apply {
                            apkName =info.loadLabel(packageManager).toString()
                            srcDir = info.sourceDir
                            var f = File(info.sourceDir)
                            size = f.length()
                            Log.d(TAG, "checkForLaunchIntent: added ${info.packageName}")
                            var packageInfo = packageManager.getPackageArchiveInfo(srcDir, 0);
                            if(packageInfo!=null)
                                appList.add(this)
                        }

                        /*count++
                        if (count >= 3) {
                            Log.d(TAG, "checkForLaunchIntent: ${info.packageName}")
                            break
                        }*/

                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG, "checkForLaunchIntent: exception inserting app ${e.message}")
            }
        }
        Log.d(TAG, "checkForLaunchIntent: size of app ${appList.size}")
        return appList
    }
    private fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        Log.d(TAG, "isSystemPackage: system package ${pkgInfo.packageName}")
    }
   /* fun loadFbBannerAdd() {

        val adView = AdView(
            this@HSActivityInstallApps,
            this@HSActivityInstallApps.resources.getString(R.string.banner_add),
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
        findViewById<RelativeLayout>(R.id.bottom_banner).addView(adView)
    }

    fun admobBanner() {

        val mAdView = com.google.android.gms.ads.AdView(this@HSActivityInstallApps)
        val adSize: com.google.android.gms.ads.AdSize = HSIntersitialAdHelper.getAdSize(this@HSActivityInstallApps)
        mAdView.adSize = adSize

        mAdView.adUnitId = resources.getString(R.string.admob_banner)

        val adRequest = AdRequest.Builder().build()

        val adViewLayout = findViewById<View>(R.id.top_banner) as RelativeLayout
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
                Log.d(TAG, "onAdLoaded: ")
            }
        }

    }*/
}