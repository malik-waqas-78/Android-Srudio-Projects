package com.ppt.walkie.actvities

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.core.content.ContextCompat
import com.ppt.walkie.R
import com.ppt.walkie.databinding.ActivityHomeOkraBinding


import com.ppt.walkie.utils.SharePrefHelperOKRA
import com.walkie.talkie.ads.BannerAdHelperOKRA
import com.walkie.talkie.ads.InterAdHelperOKRA
import com.walkie.talkie.ads.NativeAdHelperOKRA


import java.util.*

class ActivityHomeOKRA : AppCompatActivity() {
    lateinit var binding: ActivityHomeOkraBinding
    private lateinit var sharedPreferences: SharePrefHelperOKRA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        binding = ActivityHomeOkraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        InterAdHelperOKRA.shouldShowFullAd = true

        BannerAdHelperOKRA.loadMediatedAdmobBanner(this, binding.topBanner)

        sharedPreferences = SharePrefHelperOKRA(this@ActivityHomeOKRA)

        if(sharedPreferences.getImagePath()!=null) {
            binding.imageAvatar.setImageURI(Uri.parse(sharedPreferences.getImagePath()))
        }
        binding.textName.setText(sharedPreferences.getName())


        if (intent.hasExtra("restart")) {
            /*android.os.Handler().postDelayed({*/
            val intent = Intent(this@ActivityHomeOKRA, MainActivityOKRA::class.java)
            intent.putExtra("restart", true)
            startActivity(intent)
            /*  },100)*/
        }

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this@ActivityHomeOKRA, SettingsActivityOKRA::class.java))
        }
        binding.btnWalkieTalkie.setOnClickListener {
            InterAdHelperOKRA.shouldShowFullAd = true
            startActivity(Intent(this@ActivityHomeOKRA, MainActivityOKRA::class.java))
        }
    }

    override fun onBackPressed() {
        InterAdHelperOKRA.showMediatedAdmobIntersitial(this,null);
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
        NativeAdHelperOKRA.showAdmobNativeAd(this, dialog.findViewById(R.id.ad_frame))
        dialog.setOnCancelListener {
//            dialogueClickListner.negativeHotSpotTurnOFF()
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
                    url + Objects.requireNonNull(this@ActivityHomeOKRA).packageName
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
}