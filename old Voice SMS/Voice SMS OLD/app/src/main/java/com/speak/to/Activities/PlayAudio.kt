package com.speak.to.Activities

import android.app.Dialog
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.speak.to.Dialogs.GeneralDialog_VoiceSMS
import com.speak.to.Dialogs.Rename_file_Dialog_Voice_SMS
import com.speak.to.Interfaces.GeneralDialogInterface_voiceSMS
import com.speak.to.Interfaces.RenameFileDialog_Interface
import com.speak.to.R
import com.speak.to.Utils.Constants.NAME
import com.speak.to.Utils.Constants.URL
import com.speak.to.databinding.ActivityPlayAudioBinding
import com.facebook.ads.*
import java.io.File
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class PlayAudio : AppCompatActivity() {

    lateinit var binding: ActivityPlayAudioBinding
    var url: String? = null
    var service: ScheduledExecutorService? = null
    var mp: MediaPlayer? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadBannerAdd()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        if (intent.hasExtra(URL)) {
            url = intent.getStringExtra(URL)
            binding.tvName.text = intent.getStringExtra(NAME)
        } else {
            finish()
        }
        binding.vumeter.pause()
        playAudio()
        startPlaying()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.playing_item_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.recording_menu_rename -> {
                Rename_file_Dialog_Voice_SMS.CreateRenameDialogBox(
                    this,
                    object : RenameFileDialog_Interface {
                        override fun RenameFile(fileNameNew: String?) {
                            val file = File(url?.replace(File(url).name, fileNameNew.toString()))
                            if (file.exists()) {
                                Toast.makeText(
                                    this@PlayAudio,
                                    "File Already Exists, Choose a new Name",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                File(url).renameTo(file)
                                finish()
                            }
                        }
                    })
            }
            R.id.recording_menu_delete -> {
                val file = File(url)
                GeneralDialog_VoiceSMS.CreateGeneralDialog(
                    this@PlayAudio,
                    getString(R.string.delete_title),
                    getString(R.string.delete_desc),
                    getString(R.string.delete),
                    getString(R.string.cancel),
                    object : GeneralDialogInterface_voiceSMS {
                        override fun Positive(dialog: Dialog?) {
                            file.delete()
                            dialog?.dismiss()
                            finish()
                        }

                        override fun Negative(dialog: Dialog?) {
                            dialog?.dismiss()
                        }
                    }
                )
            }
            R.id.recording_menu_share -> {
                val file = File(url)
                if (file.exists()) {
                    val uri =
                        FileProvider.getUriForFile(this, "com.speak.to.fileprovider", file)
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.type = "audio /*"
                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun playAudio() {
        mp = MediaPlayer()
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
            .build()
        mp?.setAudioAttributes(audioAttributes)
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
        Log.d("testing_tag", "onProgressChanged: ${mp?.duration}")
        service = Executors.newScheduledThreadPool(0)
        binding.btnPlayAudio.setOnClickListener {
            if (mp?.isPlaying == true) {
                binding.btnPlayAudio.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@PlayAudio,
                        R.drawable.ic_play
                    )
                )
                mp?.pause()
                binding.vumeter.pause()
            } else {
                binding.btnPlayAudio.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@PlayAudio,
                        R.drawable.ic_pause
                    )
                )
                binding.vumeter.resume(true)
                startService()
                mp?.setOnCompletionListener(OnCompletionListener { //mp.reset();
                    binding.btnPlayAudio.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@PlayAudio,
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
                            Log.d("testing_tag", "onProgressChanged: $progress")
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

    private fun startService() {
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
                    this@PlayAudio,
                    R.drawable.ic_play
                )
            )
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

    fun loadBannerAdd() {
        val adView =
            AdView(this, this.resources.getString(R.string.banner_add), AdSize.BANNER_HEIGHT_50)
        val adListener: AdListener = object : AdListener {
            override fun onError(ad: Ad, adError: AdError) {
                Log.d("TAG", "onError: " + adError.errorMessage)
            }

            override fun onAdLoaded(ad: Ad) {
                Log.d("TAG", "Ad loaded")
            }

            override fun onAdClicked(ad: Ad) {}
            override fun onLoggingImpression(ad: Ad) {}
        }
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build())
        val relativeLayout = findViewById<RelativeLayout>(R.id.top_banner)
        relativeLayout.addView(adView)
    }

    fun startPlaying() {
        binding.btnPlayAudio.setImageDrawable(
            ContextCompat.getDrawable(
                this@PlayAudio,
                R.drawable.ic_pause
            )
        )
        binding.vumeter.resume(true)
        startService()
        mp?.setOnCompletionListener (
            OnCompletionListener {
                binding.btnPlayAudio.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@PlayAudio,
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
            }
        )

        binding.pbarAudio.setOnSeekBarChangeListener(object :
        SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                binding.tvCurrentlyPlayed.text = progress.getDuration()
                if (fromUser) {
                    Log.d("testing_tag", "onProgressChanged: $progress")
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