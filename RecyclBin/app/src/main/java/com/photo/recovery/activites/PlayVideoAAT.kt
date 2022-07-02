package com.photo.recovery.activites

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.photo.recovery.R
import com.photo.recovery.ads.BannerAdHelperAAT
import com.photo.recovery.ads.isAppInstalledFromPlay
//import com.datarecovery.recyclebindatarecovery.ads.BannerAdHelperAAT
//import com.datarecovery.recyclebindatarecovery.ads.isAppInstalledFromPlay
import com.photo.recovery.databinding.ActivityPlayVideoAatBinding
import java.io.File

class PlayVideoAAT : AppCompatActivity() {

    var path: String? = null
    lateinit var binding: ActivityPlayVideoAatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayVideoAatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(isAppInstalledFromPlay(this@PlayVideoAAT)){
            //loadFbBannerAd()
            //loadAdmobBannerAd()
            BannerAdHelperAAT.loadMediatedAdmobBanner(this,binding.topBanner)
//            BannerAdHelperAAT.loadNormalAdmobBanner(this,binding.bottomBanner)
        }

        ShowImageAAT.open=true
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.appcolorgreen)


        val intent = intent
        path = intent.getStringExtra("url")

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title=File(path).nameWithoutExtension

        playVideo()
        if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.title = File(path).nameWithoutExtension
            binding.toolbar?.setNavigationOnClickListener {
                finish()
            }
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            binding.ivBack?.setOnClickListener {
                finish()
            }
        }
        binding.floatingActionButton.setOnClickListener {
            val orientation = this.resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } else {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }

    }

    private fun playVideo() {
        val m = MediaController(this)
        binding.videoView.setMediaController(m)
        val u = Uri.parse(path)
        binding.videoView.setVideoURI(u)
        binding.videoView.start()
    }

    override fun onBackPressed() {
        stopVideo()
        super.onBackPressed()
    }

    private fun stopVideo() {
        if (binding.videoView.isPlaying) {
            binding.videoView.suspend()
        }
    }
  /*  fun loadFbBannerAd() {

        val adView = AdView(
            this@PlayVideo,
            this@PlayVideo.resources.getString(R.string.fb_banner_ad),
            AdSize.BANNER_HEIGHT_50
        )
        val s = this@PlayVideo.resources.getString(R.string.fb_banner_ad2)
        val adListener: com.facebook.ads.AdListener = object : com.facebook.ads.AdListener {

            override fun onError(ad: Ad, adError: AdError) {
                Log.d(MyConstants.TAG, "onError: ")
                Log.d(
                    MyConstants.TAG,
                    "onError  Banner: " + ad!!.placementId + "\n" + adError!!.errorMessage
                )
            }

            override fun onAdLoaded(ad: Ad) {
                // Ad loaded callback
                Log.d(MyConstants.TAG, "onAdLoaded: Banner=" + ad!!.placementId)
            }

            override fun onAdClicked(ad: Ad) {
                Log.d(MyConstants.TAG, "onAdClicked: ")
            }

            override fun onLoggingImpression(ad: Ad) {
                // Ad impression logged callback
                Log.d(MyConstants.TAG, "onLoggingImpression: ")
            }
        }


        adView?.loadAd(adView?.buildLoadAdConfig()?.withAdListener(adListener)?.build())
        binding.topBanner.addView(adView)
    }*/
}