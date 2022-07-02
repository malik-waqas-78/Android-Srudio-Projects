package com.ppt.walkie.actvities

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ppt.walkie.R
import com.ppt.walkie.databinding.ActivityIncomingCallOkraBinding


import com.ppt.walkie.services.MyServiceOKRA
import com.ppt.walkie.utils.ConstantsOKRA
import com.walkie.talkie.ads.BannerAdHelperOKRA
import com.walkie.talkie.ads.InterAdHelperOKRA


class IncomingCallOKRA : AppCompatActivity() {

    lateinit var binding: ActivityIncomingCallOkraBinding
    var m: MediaPlayer? = null
    var v: Vibrator? = null

    var handler:Handler?=null
    lateinit var runnable: Runnable
    var value:Int =0
    companion object{
        var isIncommingCallActivity:Boolean=true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        binding= ActivityIncomingCallOkraBinding.inflate(layoutInflater)
        setContentView(binding.root)


       BannerAdHelperOKRA.loadMediatedAdmobBanner(this,binding.topBanner)
        isIncommingCallActivity=false

        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        m = MediaPlayer()
        val callerName = intent.getStringExtra(ConstantsOKRA.CALLER_NAME)
        binding.tvCallerName.text = "$callerName"



//        binding.btnAccept.setOnClickListener {
//
//            InterAdHelperOKRA.shouldShowFullAd = false
//
//            val intent = Intent(this@IncomingCallHS, MainActivityHS::class.java)
//            intent.putExtra(ConstantsHS.CALl_ACCEPTED, true)
//            startActivity(intent)
//            MyServiceHS.stopBeep()
//            finish()
//        }
//        binding.btnDeny.setOnClickListener {
//            MyServiceHS.stopBeep()
//            MyServiceHS.userRejectedConnection()
//            finishAffinity()
//        }
        binding.seekbar11.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                handler?.removeCallbacks(runnable)
                if (progress == 0) {
                InterAdHelperOKRA.shouldShowFullAd = false
                    val intent = Intent(this@IncomingCallOKRA, MainActivityOKRA::class.java)
                    intent.putExtra(ConstantsOKRA.CALl_ACCEPTED, true)
                    intent.putExtra(ConstantsOKRA.CALLER_NAME,"$callerName");
                    startActivity(intent)
                    MyServiceOKRA.stopBeep()
                    isIncommingCallActivity=true
                    finish()

                } else if (progress == 30) {
                    MyServiceOKRA.stopBeep()
                    MyServiceOKRA.userRejectedConnection()
                    isIncommingCallActivity=true
                    finishAffinity()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                value = seekBar.progress
                if (value > 15) {
                    runnable = Runnable {
                        if (value !== 15) {
                            value--
                            seekBar.progress = value
                        }
                        handler?.postDelayed(runnable, 1)
                    }
                    handler?.post(runnable)
                } else {
                    runnable = Runnable {
                        if (value !== 15) {
                            value++
                            seekBar.progress = value
                        }
                        handler?.postDelayed(runnable, 1)
                    }
                    handler?.post(runnable)
                }
            }
        })

        android.os.Handler().postDelayed({
            MyServiceOKRA.stopBeep()
            finishAffinity()
        }, ConstantsOKRA.CALL_TIME_OUT)

    }



    override fun onStart() {


        v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v?.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            v?.vibrate(500)
        }
        if(Build.VERSION.SDK_INT<=27){
            playBeep()
        }
        super.onStart()
    }

    override fun onStop() {
        try {
            v?.cancel()
            if (m?.isPlaying == true) {
                m?.stop()
                m?.release()
            }

        } catch (e: java.lang.Exception) {

        }
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
        if(m!=null) {
            m?.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if(m!=null) {
            m?.start()
        }
    }

    fun playBeep() {
        try {
            android.os.Handler().postDelayed({
                if (m?.isPlaying == true) {
                    m?.stop()
                    m?.release()
                    m = MediaPlayer()
                }
                val descriptor = assets.openFd("calling_tune.mp3")
                m?.setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
                descriptor.close()
                m?.prepare()
                m?.setVolume(1f, 1f)
                m?.isLooping = true
                m?.start()
            },800)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}