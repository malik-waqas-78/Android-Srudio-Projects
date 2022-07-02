package com.photo.recovery.activites

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.view.Window
import android.view.WindowManager

import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.photo.recovery.R
import com.photo.recovery.ads.BannerAdHelperAAT
import com.photo.recovery.ads.isAppInstalledFromPlay
//import com.datarecovery.recyclebindatarecovery.ads.BannerAdHelperAAT
//import com.datarecovery.recyclebindatarecovery.ads.isAppInstalledFromPlay
import com.photo.recovery.databinding.ActivityPlayAudioAatBinding

import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class PlayAudioAAT : AppCompatActivity() {

    lateinit var binding: ActivityPlayAudioAatBinding
    var url: String? = null
    var service: ScheduledExecutorService? = null
    var mp: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayAudioAatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(isAppInstalledFromPlay(this@PlayAudioAAT)){
           // loadFbBannerAd()
            //loadAdmobBannerAd()
            BannerAdHelperAAT.loadMediatedAdmobBanner(this,binding.topBanner)
//            BannerAdHelperAAT.loadNormalAdmobBanner(this,binding.bottomBanner)
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        ShowImageAAT.open=true
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.appcolorgreen)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }


        if (intent.hasExtra("url")) {
            url = intent.getStringExtra("url")
            binding.tvName.text = intent.getStringExtra("name")
            supportActionBar?.title = binding.tvName.text
        } else {
            finish()
        }
        binding.vumeter.pause()
        playAudio()
    }

    private fun playAudio() {


        mp = MediaPlayer()
        val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
        mp?.setAudioAttributes(
            audioAttributes
        )

        try {
            mp?.setDataSource(url)
        } catch (e: IllegalArgumentException) {

            e.printStackTrace()
        } catch (e: IllegalStateException) {

            e.printStackTrace()
        } catch (e: IOException) {

            e.printStackTrace()
        }
        try {
            mp?.prepare()
        } catch (e: IllegalStateException) {

            e.printStackTrace()
        } catch (e: IOException) {

            e.printStackTrace()
        }
        //total duration of audio
        val duration = mp?.duration.getDuration()
        binding.tvTotalPlayingTime.text = duration
        binding.pbarAudio.max = mp!!.duration
      //  Log.d(TESTING_TAG, "onProgressChanged: ${mp?.duration}")
        service = Executors.newScheduledThreadPool(0)
        binding.btnPlayAudio.setOnClickListener {
            if (mp?.isPlaying == true) {
                binding.btnPlayAudio.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@PlayAudioAAT,
                        R.drawable.ic_play
                    )
                )
                mp?.pause()
                binding.vumeter.pause()
            } else {
                binding.btnPlayAudio.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@PlayAudioAAT,
                        R.drawable.ic_pause
                    )
                )
                binding.vumeter.resume(true)
                startservice()
                mp?.setOnCompletionListener(OnCompletionListener { //mp.reset();
                    binding.btnPlayAudio.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@PlayAudioAAT,
                            R.drawable.ic_play
                        )
                    )
                    mp?.seekTo(0)
                    binding.vumeter.pause()
                    try {
                        binding.pbarAudio.progress = 0
                        service?.awaitTermination(10, TimeUnit.MICROSECONDS)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    return@OnCompletionListener
                })


                binding.pbarAudio.setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        binding.tvCurrentlyPlayed.text = progress.getDuration()

                        if (fromUser) {
                        //    Log.d(TESTING_TAG, "onProgressChanged: $progress")
                            mp?.seekTo(progress)
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {

                    }

                })
                mp?.start()
            }
        }


    }

    private fun startservice() {
        var scheduledFuture: ScheduledFuture<*>? = null
        val finalScheduledFuture = scheduledFuture
        scheduledFuture = service?.scheduleWithFixedDelay(
            Runnable {
                mp?.currentPosition?.let {
                    binding.pbarAudio.progress = it
                }
            },
            1,
            1,
            TimeUnit.MICROSECONDS
        )
    }

    fun Int?.getDuration(): String? {
        val durationInMillie = this?.toLong()
        var totalSec = durationInMillie?.div(1000)
        var totalMin = totalSec?.div(60)
        totalSec = totalSec?.rem(60)
        val totalHours = totalMin?.div(60)
        totalMin = totalMin?.rem(60)
        val th = if (totalHours!! < 10) """0$totalHours""" else totalHours
        val tm = if (totalMin!! < 10) """0$totalMin""" else totalMin
        val ts = if (totalSec!! < 10) """0$totalSec""" else totalSec
        return """$th:$tm:$ts"""

    }

    override fun onPause() {
        super.onPause()
        if (mp?.isPlaying == true) {
            binding.btnPlayAudio.setImageDrawable(
                ContextCompat.getDrawable(
                    this@PlayAudioAAT,
                    R.drawable.ic_play
                )
            )
            binding.vumeter.pause()
            mp?.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mp?.isPlaying == true) {
            service?.shutdown()
            mp?.stop()
        }
    }
  /*  fun loadFbBannerAd() {

        val adView = AdView(
            this@PlayAudio,
            this@PlayAudio.resources.getString(R.string.fb_banner_ad),
            AdSize.BANNER_HEIGHT_50
        )
        val s = this@PlayAudio.resources.getString(R.string.fb_banner_ad2)
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