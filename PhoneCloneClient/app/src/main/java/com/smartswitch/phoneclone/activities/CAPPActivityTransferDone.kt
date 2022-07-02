package com.smartswitch.phoneclone.activities

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.*
import android.provider.CalendarContract
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieAnimationView

import com.smartswitch.phoneclone.BuildConfig
import com.smartswitch.phoneclone.R
//import com.switchphone.transferdata.ads.AATIntersitialAdHelper
//import com.switchphone.transferdata.ads.AATNativeAdHelper
import com.smartswitch.phoneclone.constants.CAPPMConstants
import com.smartswitch.phoneclone.modelclasses.CAPPCalendarEventsModel
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*

class CAPPActivityTransferDone : AppCompatActivity() {


    var isCalendar = false
    var isCalllogs = false
    var isContacts = false
    var isPictures = false
    var isVideos = false
    var isApps = false
    var isDocs = false
    var isAudios = false

    var toolbar:Toolbar?=null

    var isContactsSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_done_capp)



        toolbar=findViewById(R.id.toolbar)
        toolbar?.setNavigationIcon(R.drawable.ic_group_back_white)
        toolbar?.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })
/*
          admobBanner()
          loadFbBannerAdd()*/

//        AATIntersitialAdHelper.loadAdmobBanner(
//            this,
//            findViewById(R.id.top_banner),
//            resources.getString(R.string.admob_banner)
//        )


        isCalllogs = intent.getBooleanExtra(CAPPMConstants.CALLLOGS, false)
        isCalendar = intent.getBooleanExtra(CAPPMConstants.CALENDAR, false)
        isContacts = intent.getBooleanExtra(CAPPMConstants.CONTACTS, false)
        isPictures = intent.getBooleanExtra(CAPPMConstants.PICS, false)
        isVideos = intent.getBooleanExtra(CAPPMConstants.VIDEOS, false)
        isApps = intent.getBooleanExtra(CAPPMConstants.APPS, false)
        isAudios = intent.getBooleanExtra(CAPPMConstants.Audios, false)
        isDocs = intent.getBooleanExtra(CAPPMConstants.Docments, false)
        transferCompletedOnOldDevice()

        findViewById<ConstraintLayout>(R.id.cv_btns).visibility = View.GONE

        findViewById<Button>(R.id.btn_exit).setOnClickListener {
            finish()
        }
        findViewById<Button>(R.id.btn_send_more).setOnClickListener {
            val urlToShare =
                "${applicationContext.resources.getString(R.string.play_store_link)}${BuildConfig.APPLICATION_ID}"
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

    fun transferFinished() {
        //do the next thing
        //show that every thing is compl;eted with animations except apps and contacts
        var multiplier = 0
        if (isCalendar) {
            AsyncTaskStoreData(1).execute()
            multiplier++
        } else {
            storeOtherData(multiplier)
        }
        /*if (isCalllogs) {
            Handler().postDelayed({
                findViewById<ConstraintLayout>(R.id.cl_calllogs).visibility = View.VISIBLE
            }, multiplier * Constants.animationTime)
            multiplier++
        }*/


    }

    fun storeOtherData(m: Int) {
        var multiplier = m
        if (isPictures) {
            Handler().postDelayed({
                findViewById<ConstraintLayout>(R.id.cl_pics).visibility = View.VISIBLE
            }, multiplier * CAPPMConstants.animationTime)
            multiplier++
        }
        if (isVideos) {
            Handler().postDelayed({
                findViewById<ConstraintLayout>(R.id.cl_videos).visibility = View.VISIBLE
            }, multiplier * CAPPMConstants.animationTime)
            multiplier++
        }
        //after that show a dialog telling user to press next to save contacts
        if (isContacts) {
            Handler().postDelayed({
                showSaveContactsDialogue()
            }, multiplier * CAPPMConstants.animationTime)
        } else if (isApps) {
            Handler().postDelayed({
                showSaveAppsDialogue()
            }, multiplier * CAPPMConstants.animationTime)
        } else {
            if (isAudios) {
                Handler().postDelayed({
                    findViewById<ConstraintLayout>(R.id.cl_audios).visibility = View.VISIBLE
                }, multiplier * CAPPMConstants.animationTime)
                multiplier++
            }
            if (isDocs) {
                Handler().postDelayed({
                    findViewById<ConstraintLayout>(R.id.cl_docs).visibility = View.VISIBLE
//                    findViewById<TextView>(R.id.textView24).text =
//                        resources.getString(R.string.data_stored)
                    findViewById<ConstraintLayout>(R.id.cv_btns).visibility = View.VISIBLE
                }, multiplier * CAPPMConstants.animationTime)
                multiplier++
            }else{
//                findViewById<TextView>(R.id.textView24).text =
//                    resources.getString(R.string.data_stored)
                findViewById<ConstraintLayout>(R.id.cv_btns).visibility = View.VISIBLE
            }
        }
    }

    fun saveContacts() {
        val intent = Intent(this, CAPPActivityStoreContact::class.java)
        startActivityForResult(intent, CAPPMConstants.STORE_CONTACTS)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPPMConstants.STORE_CONTACTS) {

            if (data!!.hasExtra(CAPPMConstants.isContactsSaved)) {
                isContactsSaved = data.getBooleanExtra(CAPPMConstants.isContactsSaved, false)
            }

            //then show a dialogue saying that user should install apps manually by clicking next
            findViewById<ConstraintLayout>(R.id.cl_contacts).visibility = View.VISIBLE
            if (!isContactsSaved) {
                findViewById<LottieAnimationView>(R.id.animationViewc2).visibility = View.VISIBLE
                findViewById<LottieAnimationView>(R.id.animationViewc).visibility = View.GONE
            }
            if (isApps) {
                Handler().postDelayed({
                    showSaveAppsDialogue()
                }, 1 * CAPPMConstants.animationTime)
            } else {
                if (isAudios) {
                    Handler().postDelayed({
                        findViewById<ConstraintLayout>(R.id.cl_audios).visibility = View.VISIBLE
                    }, 2 * CAPPMConstants.animationTime)
                }
                if (isDocs) {
                    Handler().postDelayed({
                        findViewById<ConstraintLayout>(R.id.cl_docs).visibility = View.VISIBLE
//                        findViewById<TextView>(R.id.textView24).text =
//                            resources.getString(R.string.data_stored)
                        findViewById<ConstraintLayout>(R.id.cv_btns).visibility = View.VISIBLE
                    }, 3 * CAPPMConstants.animationTime)

                }else{
//                    findViewById<TextView>(R.id.textView24).text =
//                        resources.getString(R.string.data_stored)
                    findViewById<ConstraintLayout>(R.id.cv_btns).visibility = View.VISIBLE
                }

            }
        } else if (requestCode == CAPPMConstants.INSTALL_APPS) {
            //apps have been installed
            findViewById<ConstraintLayout>(R.id.cl_apps).visibility = View.VISIBLE
            findViewById<LottieAnimationView>(R.id.animationView).visibility = View.VISIBLE
            if (isAudios) {
                Handler().postDelayed({
                    findViewById<ConstraintLayout>(R.id.cl_audios).visibility = View.VISIBLE
                }, 2 * CAPPMConstants.animationTime)
            }
            if (isDocs) {
                Handler().postDelayed({
                    findViewById<ConstraintLayout>(R.id.cl_docs).visibility = View.VISIBLE
//                    findViewById<TextView>(R.id.textView24).text =
//                        resources.getString(R.string.data_stored)
                    findViewById<ConstraintLayout>(R.id.cv_btns).visibility = View.VISIBLE
                }, 3 * CAPPMConstants.animationTime)
            } else {
                findViewById<ConstraintLayout>(R.id.cv_btns).visibility = View.VISIBLE
//                findViewById<TextView>(R.id.textView24).text =
//                    resources.getString(R.string.data_stored)
            }

        }
    }

    fun showSaveContactsDialogue() {
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.db_savecontacts_capp)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val positive: Button = dialog.findViewById(R.id.btn_next)
        val negative: Button = dialog.findViewById(R.id.btn_cancel)
        positive.setOnClickListener {
            dialog.dismiss()
            saveContacts()
        }
        negative.setOnClickListener {
            dialog.dismiss()

            findViewById<ConstraintLayout>(R.id.cl_contacts).visibility = View.VISIBLE
            findViewById<LottieAnimationView>(R.id.animationViewc2).visibility = View.VISIBLE
            findViewById<LottieAnimationView>(R.id.animationViewc).visibility = View.GONE
            var m = 1
            if (isApps) {
                Handler().postDelayed({
                    showSaveAppsDialogue()
                }, m++ * CAPPMConstants.animationTime)
            } else {
                if (isAudios) {
                    android.os.Handler().postDelayed({
                        findViewById<ConstraintLayout>(com.smartswitch.phoneclone.R.id.cl_audios).visibility =
                            android.view.View.VISIBLE
                    }, m++ * com.smartswitch.phoneclone.constants.CAPPMConstants.animationTime)

                }
                if (isDocs) {
                    Handler().postDelayed({
                        findViewById<ConstraintLayout>(R.id.cl_docs).visibility = View.VISIBLE
                        findViewById<ConstraintLayout>(R.id.cv_btns).visibility = View.VISIBLE
//                        findViewById<TextView>(R.id.textView24).text =
//                            resources.getString(R.string.data_stored)
                    }, m++ * CAPPMConstants.animationTime)
                } else {
                    findViewById<ConstraintLayout>(R.id.cv_btns).visibility = View.VISIBLE
//                    findViewById<TextView>(R.id.textView24).text =
//                        resources.getString(R.string.data_stored)
                }
            }
        }
        dialog.setOnCancelListener {
            //dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.dismiss()
            findViewById<ConstraintLayout>(R.id.cl_contacts).visibility = View.VISIBLE
            findViewById<LottieAnimationView>(R.id.animationViewc2).visibility = View.VISIBLE
            findViewById<LottieAnimationView>(R.id.animationViewc).visibility = View.GONE
            var m = 1
            if (isApps) {
                Handler().postDelayed({
                    showSaveAppsDialogue()
                }, m++ * CAPPMConstants.animationTime)
            } else {
                if (isAudios) {
                    Handler().postDelayed({
                        findViewById<ConstraintLayout>(R.id.cl_audios).visibility = View.VISIBLE
                    }, m++ * CAPPMConstants.animationTime)

                }
                if (isDocs) {
                    Handler().postDelayed({
                        findViewById<ConstraintLayout>(R.id.cl_docs).visibility = View.VISIBLE
                        findViewById<ConstraintLayout>(R.id.cv_btns).visibility = View.VISIBLE
//                        findViewById<TextView>(R.id.textView24).text =
//                            resources.getString(R.string.data_stored)
                    }, m++ * CAPPMConstants.animationTime)
                } else {
                    findViewById<ConstraintLayout>(R.id.cv_btns).visibility = View.VISIBLE
//                    findViewById<TextView>(R.id.textView24).text =
//                        resources.getString(R.string.data_stored)
                }
            }
        }
        dialog.show()
    }

    fun showSaveAppsDialogue() {
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.db_savecontacts_capp)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val tv_title = dialog.findViewById<TextView>(R.id.textView23)
        tv_title.setText("Install Applications")
        val tv_msg = dialog.findViewById<TextView>(R.id.textView25)
        tv_msg.setText("To install received applications plz click next...")
        val positive: Button = dialog.findViewById(R.id.btn_next)
        val negative: Button = dialog.findViewById(R.id.btn_cancel)
        positive.setOnClickListener {
            dialog.dismiss()
            // take it to installl apps actiovity
            val intent = Intent(this, CAPPActivityInstallApps::class.java)
            startActivityForResult(intent, CAPPMConstants.INSTALL_APPS)
        }
        negative.setOnClickListener {
            dialog.dismiss()

            findViewById<ConstraintLayout>(R.id.cl_apps).visibility = View.VISIBLE
            findViewById<LottieAnimationView>(R.id.animationViewa).visibility = View.VISIBLE
            findViewById<LottieAnimationView>(R.id.animationView).visibility = View.GONE
            var m = 1
            Handler().postDelayed({
                exitorcontinue()
            }, m * CAPPMConstants.animationTime)
        }
        dialog.setOnCancelListener {
            //dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.dismiss()
            findViewById<ConstraintLayout>(R.id.cl_apps).visibility = View.VISIBLE
            findViewById<LottieAnimationView>(R.id.animationViewa).visibility = View.VISIBLE
            findViewById<LottieAnimationView>(R.id.animationView).visibility = View.GONE
            var m = 1
            Handler().postDelayed({
                exitorcontinue()
            }, m * CAPPMConstants.animationTime)
        }
        dialog.show()
    }

    override fun onBackPressed() {
        //IntersitialAdds.showAdmobIntersitial(this@TransferCompletedActivity)
        exit()
    }

    fun transferCompletedOnOldDevice() {
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.db_transfer_completedon_receiving_end_capp)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val positive: Button = dialog.findViewById(R.id.btn_error_ok)

        positive.setOnClickListener {
            dialog.dismiss()
            transferFinished()
        }

        dialog.setOnCancelListener {
            //dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.dismiss()
        }
        dialog.show()
    }

    fun exitorcontinue() {

        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.db_appsalert_capp)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val tv_msg = dialog.findViewById<TextView>(R.id.tv_alertdetails)
        tv_msg.setText("Please make sure you have installed all the applications. If you want to install apps latter you can install by opening file manager -> Phone Clone.")
        val positive: Button = dialog.findViewById(R.id.btn_yes)
        positive.setOnClickListener {
            val intent = Intent(this, CAPPActivityInstallApps::class.java)
            startActivityForResult(intent, CAPPMConstants.INSTALL_APPS)
        }
        val negative: Button = dialog.findViewById(R.id.btn_exit)
        negative.setOnClickListener {
            dialog.dismiss()
            var m = 1
            if (isAudios) {
                Handler().postDelayed({
                    findViewById<ConstraintLayout>(R.id.cl_audios).visibility = View.VISIBLE
                }, m++ * CAPPMConstants.animationTime)

            }
            if (isDocs) {
                Handler().postDelayed({
                    findViewById<ConstraintLayout>(R.id.cl_docs).visibility = View.VISIBLE
                    findViewById<ConstraintLayout>(R.id.cv_btns).visibility = View.VISIBLE
//                    findViewById<TextView>(R.id.textView24).text =
//                        resources.getString(R.string.data_stored)
                }, m++ * CAPPMConstants.animationTime)
            } else {
                findViewById<ConstraintLayout>(R.id.cv_btns).visibility = View.VISIBLE
//                findViewById<TextView>(R.id.textView24).text =
//                    resources.getString(R.string.data_stored)
            }
        }
        dialog.setOnCancelListener {
            //dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.dismiss()
            var m = 1
            if (isAudios) {
                Handler().postDelayed({
                    findViewById<ConstraintLayout>(R.id.cl_audios).visibility = View.VISIBLE
                }, m++ * CAPPMConstants.animationTime)

            }
            if (isDocs) {
                Handler().postDelayed({
                    findViewById<ConstraintLayout>(R.id.cl_docs).visibility = View.VISIBLE
                    findViewById<ConstraintLayout>(R.id.cv_btns).visibility = View.VISIBLE
//                    findViewById<TextView>(R.id.textView24).text =
//                        resources.getString(R.string.data_stored)
                }, m++ * CAPPMConstants.animationTime)
            } else {
                findViewById<ConstraintLayout>(R.id.cv_btns).visibility = View.VISIBLE
//                findViewById<TextView>(R.id.textView24).text =
//                    resources.getString(R.string.data_stored)
            }
        }
        dialog.show()
    }

    private fun rateIntentForUrl(url: String): Intent? {
        var intent: Intent? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    url + Objects.requireNonNull(this@CAPPActivityTransferDone).packageName
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
            val rateIntent =
                rateIntentForUrl("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
            startActivity(rateIntent)
        }
    }

    fun exit() {
        val dialog = Dialog(this)
        dialog.setCancelable(false)
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
        /*val frameLayout=dialog.findViewById<NativeAdLayout>(R.id.native_ad_container)
        NativeAdds.showAd(this@TransferCompletedActivity,frameLayout)*/

        val ad_frame = dialog.findViewById<FrameLayout>(R.id.ad_frame)
//        AATNativeAdHelper.showAdmobNativeAd(this@AATActivityTransferDone, ad_frame)

        dialog.show()
    }
/*
    fun loadFbBannerAdd() {

        val adView = AdView(
            this@HSActivityTransferDone,
            this@HSActivityTransferDone.resources.getString(R.string.banner_add),
            AdSize.BANNER_HEIGHT_50
        )

        val adListener: AdListener = object : AdListener {

            override fun onError(ad: Ad, adError: AdError) {
                *//*if (com.facebook.ads.BuildConfig.DEBUG) {
                     Toast.makeText(
                        this@HSSplashScreen,
                        "Error: " + adError.errorMessage,
                        Toast.LENGTH_LONG
                    )
                        .show()
                }*//*
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

/*    fun admobBanner() {

        val mAdView = com.google.android.gms.ads.AdView(this@HSActivityTransferDone)
        val adSize: com.google.android.gms.ads.AdSize =
            HSIntersitialAdHelper.getAdSize(this@HSActivityTransferDone)
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

    override fun onDestroy() {
        //  NativeAdds.googleNativeAd?.destroy()
        CAPPMConstants.FILES_TO_SHARE.clear()
        super.onDestroy()
    }

    inner class AsyncTaskStoreData(var multiplier: Int) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            var fileReader: BufferedReader? = null

            try {
                var line: String?
                val path = Environment.getExternalStorageDirectory().absolutePath + "/${
                    applicationContext.resources.getString(R.string.app_name)
                }/" +
                        "${CAPPMConstants.EventsFileName}"
                fileReader = BufferedReader(FileReader(path))

                // Read CSV header
                fileReader.readLine()

                // Read the file line by line starting from the second line
                line = fileReader.readLine()
                while (line != null) {
                    val tokens = line.split(",")
                    if (tokens.size > 0) {
                        var duration =
                            if (tokens[CAPPCalendarEventsModel.DURATION_INDEX] == null || tokens[CAPPCalendarEventsModel.DURATION_INDEX].contains(
                                    "null"
                                )
                            ) {
                                "PT1H"
                            } else {
                                tokens[CAPPCalendarEventsModel.DURATION_INDEX]
                            }
                        val values = ContentValues()
                        values.put(
                            CalendarContract.Events.DESCRIPTION,
                            tokens[CAPPCalendarEventsModel.DESCRIPTION_INDEX]
                        )
                        values.put(
                            CalendarContract.Events.DTSTART,
                            tokens[CAPPCalendarEventsModel.START_INDEX]
                        )
                        values.put(
                            CalendarContract.Events.EVENT_TIMEZONE,
                            tokens[CAPPCalendarEventsModel.TIME_ZONE_INDEX]
                        )
                        values.put(
                            CalendarContract.Events.TITLE,
                            tokens[CAPPCalendarEventsModel.TITLE_INDEX]
                        )
                        values.put(CalendarContract.Events.CALENDAR_ID, 1)
                        values.put(CalendarContract.Events.DURATION, duration)
                        try {
                            var x = applicationContext.contentResolver.insert(
                                CalendarContract.Events.CONTENT_URI,
                                values
                            )
                        } catch (exc: java.lang.Exception) {
                        }
                    }

                    line = fileReader.readLine()
                }
            } catch (e: Exception) {
                println("Reading CSV Error!")
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                } catch (e: IOException) {
                    println("Closing fileReader Error!")
                    e.printStackTrace()
                }
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            findViewById<ConstraintLayout>(R.id.cl_calendar).visibility = View.VISIBLE
            storeOtherData(multiplier)
        }

    }

}