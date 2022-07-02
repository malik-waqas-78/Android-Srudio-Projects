package com.ppt.walkie.actvities


import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ppt.walkie.R
import com.ppt.walkie.databinding.ActivitySplashScreenOkraBinding
import com.ppt.walkie.services.MyServiceOKRA
import com.ppt.walkie.utils.SharePrefHelperOKRA
import com.walkie.talkie.ads.BannerAdHelperOKRA
import com.walkie.talkie.ads.InterAdHelperOKRA
import com.walkie.talkie.ads.NativeAdHelperOKRA
import java.util.*

class SplashScreenOKRA : AppCompatActivity() {

    lateinit var binding: ActivitySplashScreenOkraBinding

    // lateinit var sharedPreferences: SharePrefHelperOKRA
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)


        binding = ActivitySplashScreenOkraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BannerAdHelperOKRA.loadMediatedAdmobBanner(this,binding.topBanner)

        InterAdHelperOKRA.loadMediatedAdmobInterstitial(this@SplashScreenOKRA)
        NativeAdHelperOKRA.loadAdmobNativeAd(this@SplashScreenOKRA)

        val mSharePrefHelper = SharePrefHelperOKRA(applicationContext)
        if (mSharePrefHelper.canReceiveCallsInBackground() && !mSharePrefHelper.isFirstTime()) {
            val intent = Intent(this@SplashScreenOKRA, MyServiceOKRA::class.java)
            ContextCompat.startForegroundService(this@SplashScreenOKRA, intent)
        }

        binding.btnStart.setOnClickListener {
/*

            if(binding.etName.text.isNotEmpty()&&binding.etName.text.toString()!= SharePrefHelperOKRA.name){
                sharedPreferences?.setName(binding.etName.text.toString())
            }
*/

            /*  if(SharePrefHelperOKRA.name!="Enter Your Name"){*/
            if (!SharePrefHelperOKRA(this@SplashScreenOKRA).isFirstTime()) {
                startActivity(Intent(this@SplashScreenOKRA, ActivityHomeOKRA::class.java))
                finish()
            } else {

                startActivity(Intent(this@SplashScreenOKRA, GetUserNameOKRA::class.java))
                finish()

                //TODO intro not available yet but we have to change it and have to remove above line
                /* startActivity(Intent(this@SplashScreenOKRA, MyIntroActivityOKRA::class.java))
                 finish()*/
            }
            /* }else{
                 binding.etName.error = "Please Enter Your Name First"
             }*/


        }

        android.os.Handler().postDelayed({
            binding.btnStart.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }, 4000)

        binding.policy.setOnClickListener {
            privacyDialog()
        }

    }

    override fun onBackPressed() {
        InterAdHelperOKRA.showMediatedAdmobIntersitial(this,null)
        exitorcontinue()
    }


    fun exitorcontinue() {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.db_exitdialogue_okra)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
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
            dialog.dismiss()
        }
        dialog.setOnCancelListener {
            //dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.dismiss()
        }
        NativeAdHelperOKRA.showAdmobNativeAd(this,dialog.findViewById(R.id.ad_frame))
        dialog.show()
    }

    private fun rateIntentForUrl(url: String): Intent? {
        var intent: Intent? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    url + Objects.requireNonNull(this@SplashScreenOKRA).packageName
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
    fun Activity.setTransparentStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun privacyDialog() {
        val dialog = Dialog(this@SplashScreenOKRA, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.db_privacy_okra)
        dialog.setCanceledOnTouchOutside(false)
        dialog.getWindow()?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
        val ok = dialog.findViewById<Button>(R.id.btn_okay)
        ok.setOnClickListener {
            dialog.dismiss()
        }
    }


}