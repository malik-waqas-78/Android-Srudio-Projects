package com.zak.clone.zakactivity

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
import androidx.cardview.widget.CardView
import com.airbnb.lottie.LottieAnimationView

import com.zak.clone.BuildConfig
import com.zak.clone.R
import com.zak.clone.zakconstants.ZakMyConstants
import com.zak.clone.zakmodelclasses.ZakCalendarEventsModel
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*

class ZakActivityTransferDone : AppCompatActivity() {


    var isCalendar = false
    var isCalllogs = false
    var isContacts = false
    var isPictures = false
    var isVideos = false
    var isApps = false
    var isDocs = false
    var isAudios = false


    var isContactsSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zak_activity_transfer_done)
/*
          admobBanner()
          loadFbBannerAdd()*/

        isCalllogs = intent.getBooleanExtra(ZakMyConstants.CALLLOGS, false)
        isCalendar = intent.getBooleanExtra(ZakMyConstants.CALENDAR, false)
        isContacts = intent.getBooleanExtra(ZakMyConstants.CONTACTS, false)
        isPictures = intent.getBooleanExtra(ZakMyConstants.PICS, false)
        isVideos = intent.getBooleanExtra(ZakMyConstants.VIDEOS, false)
        isApps = intent.getBooleanExtra(ZakMyConstants.APPS, false)
        isAudios = intent.getBooleanExtra(ZakMyConstants.Audios, false)
        isDocs = intent.getBooleanExtra(ZakMyConstants.Docments, false)
        transferCompletedOnOldDevice()

        findViewById<CardView>(R.id.cv_btns).visibility = View.GONE

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
                findViewById<CardView>(R.id.cl_pics).visibility = View.VISIBLE
            }, multiplier * ZakMyConstants.animationTime)
            multiplier++
        }
        if (isVideos) {
            Handler().postDelayed({
                findViewById<CardView>(R.id.cl_videos).visibility = View.VISIBLE
            }, multiplier * ZakMyConstants.animationTime)
            multiplier++
        }
        //after that show a dialog telling user to press next to save contacts
        if (isContacts) {
            Handler().postDelayed({
                showSaveContactsDialogue()
            }, multiplier * ZakMyConstants.animationTime)
        } else if (isApps) {
            Handler().postDelayed({
                showSaveAppsDialogue()
            }, multiplier * ZakMyConstants.animationTime)
        } else {
            if (isAudios) {
                Handler().postDelayed({
                    findViewById<CardView>(R.id.cl_audios).visibility = View.VISIBLE
                }, multiplier * ZakMyConstants.animationTime)
                multiplier++
            }
            if (isDocs) {
                Handler().postDelayed({
                    findViewById<CardView>(R.id.cl_docs).visibility = View.VISIBLE
                    findViewById<TextView>(R.id.textView24).text =
                        resources.getString(R.string.data_stored)
                    findViewById<CardView>(R.id.cv_btns).visibility = View.VISIBLE
                }, multiplier * ZakMyConstants.animationTime)
                multiplier++
            }else{
                findViewById<TextView>(R.id.textView24).text =
                    resources.getString(R.string.data_stored)
                findViewById<CardView>(R.id.cv_btns).visibility = View.VISIBLE
            }
        }
    }

    fun saveContacts() {
        val intent = Intent(this, ZakActivityStoreContact::class.java)
        startActivityForResult(intent, ZakMyConstants.STORE_CONTACTS)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ZakMyConstants.STORE_CONTACTS) {

            if (data!!.hasExtra(ZakMyConstants.isContactsSaved)) {
                isContactsSaved = data.getBooleanExtra(ZakMyConstants.isContactsSaved, false)
            }

            //then show a dialogue saying that user should install apps manually by clicking next
            findViewById<CardView>(R.id.cl_contacts).visibility = View.VISIBLE
            if (!isContactsSaved) {
                findViewById<LottieAnimationView>(R.id.animationViewc2).visibility = View.VISIBLE
                findViewById<LottieAnimationView>(R.id.animationViewc).visibility = View.GONE
            }
            if (isApps) {
                Handler().postDelayed({
                    showSaveAppsDialogue()
                }, 1 * ZakMyConstants.animationTime)
            } else {
                if (isAudios) {
                    Handler().postDelayed({
                        findViewById<CardView>(R.id.cl_audios).visibility = View.VISIBLE
                    }, 2 * ZakMyConstants.animationTime)
                }
                if (isDocs) {
                    Handler().postDelayed({
                        findViewById<CardView>(R.id.cl_docs).visibility = View.VISIBLE
                        findViewById<TextView>(R.id.textView24).text =
                            resources.getString(R.string.data_stored)
                        findViewById<CardView>(R.id.cv_btns).visibility = View.VISIBLE
                    }, 3 * ZakMyConstants.animationTime)

                }else{
                    findViewById<TextView>(R.id.textView24).text =
                        resources.getString(R.string.data_stored)
                    findViewById<CardView>(R.id.cv_btns).visibility = View.VISIBLE
                }

            }
        } else if (requestCode == ZakMyConstants.INSTALL_APPS) {
            //apps have been installed
            findViewById<CardView>(R.id.cl_apps).visibility = View.VISIBLE
            findViewById<LottieAnimationView>(R.id.animationView).visibility = View.VISIBLE
            if (isAudios) {
                Handler().postDelayed({
                    findViewById<CardView>(R.id.cl_audios).visibility = View.VISIBLE
                }, 2 * ZakMyConstants.animationTime)
            }
            if (isDocs) {
                Handler().postDelayed({
                    findViewById<CardView>(R.id.cl_docs).visibility = View.VISIBLE
                    findViewById<TextView>(R.id.textView24).text =
                        resources.getString(R.string.data_stored)
                    findViewById<CardView>(R.id.cv_btns).visibility = View.VISIBLE
                }, 3 * ZakMyConstants.animationTime)
            } else {
                findViewById<CardView>(R.id.cv_btns).visibility = View.VISIBLE
                findViewById<TextView>(R.id.textView24).text =
                    resources.getString(R.string.data_stored)
            }

        }
    }

    fun showSaveContactsDialogue() {
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.zak_db_savecontacts)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val positive: Button = dialog.findViewById(R.id.btn_next)
        val negative: Button = dialog.findViewById(R.id.btn_cancel)
        positive.setOnClickListener {
            dialog.dismiss()
            saveContacts()
        }
        negative.setOnClickListener {
            dialog.dismiss()

            findViewById<CardView>(R.id.cl_contacts).visibility = View.VISIBLE
            findViewById<LottieAnimationView>(R.id.animationViewc2).visibility = View.VISIBLE
            findViewById<LottieAnimationView>(R.id.animationViewc).visibility = View.GONE
            var m = 1
            if (isApps) {
                Handler().postDelayed({
                    showSaveAppsDialogue()
                }, m++ * ZakMyConstants.animationTime)
            } else {
                if (isAudios) {
                    android.os.Handler().postDelayed({
                        findViewById<CardView>(com.zak.clone.R.id.cl_audios).visibility =
                            android.view.View.VISIBLE
                    }, m++ * com.zak.clone.zakconstants.ZakMyConstants.animationTime)

                }
                if (isDocs) {
                    Handler().postDelayed({
                        findViewById<CardView>(R.id.cl_docs).visibility = View.VISIBLE
                        findViewById<CardView>(R.id.cv_btns).visibility = View.VISIBLE
                        findViewById<TextView>(R.id.textView24).text =
                            resources.getString(R.string.data_stored)
                    }, m++ * ZakMyConstants.animationTime)
                } else {
                    findViewById<CardView>(R.id.cv_btns).visibility = View.VISIBLE
                    findViewById<TextView>(R.id.textView24).text =
                        resources.getString(R.string.data_stored)
                }
            }
        }
        dialog.setOnCancelListener {
            //dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.dismiss()
            findViewById<CardView>(R.id.cl_contacts).visibility = View.VISIBLE
            findViewById<LottieAnimationView>(R.id.animationViewc2).visibility = View.VISIBLE
            findViewById<LottieAnimationView>(R.id.animationViewc).visibility = View.GONE
            var m = 1
            if (isApps) {
                Handler().postDelayed({
                    showSaveAppsDialogue()
                }, m++ * ZakMyConstants.animationTime)
            } else {
                if (isAudios) {
                    Handler().postDelayed({
                        findViewById<CardView>(R.id.cl_audios).visibility = View.VISIBLE
                    }, m++ * ZakMyConstants.animationTime)

                }
                if (isDocs) {
                    Handler().postDelayed({
                        findViewById<CardView>(R.id.cl_docs).visibility = View.VISIBLE
                        findViewById<CardView>(R.id.cv_btns).visibility = View.VISIBLE
                        findViewById<TextView>(R.id.textView24).text =
                            resources.getString(R.string.data_stored)
                    }, m++ * ZakMyConstants.animationTime)
                } else {
                    findViewById<CardView>(R.id.cv_btns).visibility = View.VISIBLE
                    findViewById<TextView>(R.id.textView24).text =
                        resources.getString(R.string.data_stored)
                }
            }
        }
        dialog.show()
    }

    fun showSaveAppsDialogue() {
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.zak_db_savecontacts)
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
            val intent = Intent(this, ZakActivityInstallApps::class.java)
            startActivityForResult(intent, ZakMyConstants.INSTALL_APPS)
        }
        negative.setOnClickListener {
            dialog.dismiss()

            findViewById<CardView>(R.id.cl_apps).visibility = View.VISIBLE
            findViewById<LottieAnimationView>(R.id.animationViewa).visibility = View.VISIBLE
            findViewById<LottieAnimationView>(R.id.animationView).visibility = View.GONE
            var m = 1
            Handler().postDelayed({
                exitorcontinue()
            }, m * ZakMyConstants.animationTime)
        }
        dialog.setOnCancelListener {
            //dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.dismiss()
            findViewById<CardView>(R.id.cl_apps).visibility = View.VISIBLE
            findViewById<LottieAnimationView>(R.id.animationViewa).visibility = View.VISIBLE
            findViewById<LottieAnimationView>(R.id.animationView).visibility = View.GONE
            var m = 1
            Handler().postDelayed({
                exitorcontinue()
            }, m * ZakMyConstants.animationTime)
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
        dialog.setContentView(R.layout.zak_db_transfer_completedon_receiving_end)
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
        dialog.setContentView(R.layout.zak_db_appsalert)
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
            val intent = Intent(this, ZakActivityInstallApps::class.java)
            startActivityForResult(intent, ZakMyConstants.INSTALL_APPS)
        }
        val negative: Button = dialog.findViewById(R.id.btn_exit)
        negative.setOnClickListener {
            dialog.dismiss()
            var m = 1
            if (isAudios) {
                Handler().postDelayed({
                    findViewById<CardView>(R.id.cl_audios).visibility = View.VISIBLE
                }, m++ * ZakMyConstants.animationTime)

            }
            if (isDocs) {
                Handler().postDelayed({
                    findViewById<CardView>(R.id.cl_docs).visibility = View.VISIBLE
                    findViewById<CardView>(R.id.cv_btns).visibility = View.VISIBLE
                    findViewById<TextView>(R.id.textView24).text =
                        resources.getString(R.string.data_stored)
                }, m++ * ZakMyConstants.animationTime)
            } else {
                findViewById<CardView>(R.id.cv_btns).visibility = View.VISIBLE
                findViewById<TextView>(R.id.textView24).text =
                    resources.getString(R.string.data_stored)
            }
        }
        dialog.setOnCancelListener {
            //dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.dismiss()
            var m = 1
            if (isAudios) {
                Handler().postDelayed({
                    findViewById<CardView>(R.id.cl_audios).visibility = View.VISIBLE
                }, m++ * ZakMyConstants.animationTime)

            }
            if (isDocs) {
                Handler().postDelayed({
                    findViewById<CardView>(R.id.cl_docs).visibility = View.VISIBLE
                    findViewById<CardView>(R.id.cv_btns).visibility = View.VISIBLE
                    findViewById<TextView>(R.id.textView24).text =
                        resources.getString(R.string.data_stored)
                }, m++ * ZakMyConstants.animationTime)
            } else {
                findViewById<CardView>(R.id.cv_btns).visibility = View.VISIBLE
                findViewById<TextView>(R.id.textView24).text =
                    resources.getString(R.string.data_stored)
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
                    url + Objects.requireNonNull(this@ZakActivityTransferDone).packageName
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
        dialog.setContentView(R.layout.zak_db_exitdialogue)
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
        ZakMyConstants.FILES_TO_SHARE.clear()
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
                        "${ZakMyConstants.EventsFileName}"
                fileReader = BufferedReader(FileReader(path))

                // Read CSV header
                fileReader.readLine()

                // Read the file line by line starting from the second line
                line = fileReader.readLine()
                while (line != null) {
                    val tokens = line.split(",")
                    if (tokens.size > 0) {
                        var duration =
                            if (tokens[ZakCalendarEventsModel.DURATION_INDEX] == null || tokens[ZakCalendarEventsModel.DURATION_INDEX].contains(
                                    "null"
                                )
                            ) {
                                "PT1H"
                            } else {
                                tokens[ZakCalendarEventsModel.DURATION_INDEX]
                            }
                        val values = ContentValues()
                        values.put(
                            CalendarContract.Events.DESCRIPTION,
                            tokens[ZakCalendarEventsModel.DESCRIPTION_INDEX]
                        )
                        values.put(
                            CalendarContract.Events.DTSTART,
                            tokens[ZakCalendarEventsModel.START_INDEX]
                        )
                        values.put(
                            CalendarContract.Events.EVENT_TIMEZONE,
                            tokens[ZakCalendarEventsModel.TIME_ZONE_INDEX]
                        )
                        values.put(
                            CalendarContract.Events.TITLE,
                            tokens[ZakCalendarEventsModel.TITLE_INDEX]
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
            findViewById<CardView>(R.id.cl_calendar).visibility = View.VISIBLE
            storeOtherData(multiplier)
        }

    }

}