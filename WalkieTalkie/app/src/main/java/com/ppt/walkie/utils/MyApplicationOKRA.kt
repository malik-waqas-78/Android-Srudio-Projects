package com.ppt.walkie.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.AdapterStatus
import com.ppt.walkie.R
import com.walkie.talkie.ads.AppOpenManagerOKRA

/*import android.util.Log
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.AdapterStatus*/

class MyApplicationOKRA : Application() {

    var appOpenManager:AppOpenManagerOKRA?=null

    override fun onCreate() {
        super.onCreate()

        AudienceNetworkAds.initialize(this)

        MobileAds.initialize(
            this
        ) { initializationStatus ->
            val map = initializationStatus.adapterStatusMap;
            for (keyValuePair in map) {
                val className = keyValuePair.key;
                val status = keyValuePair.value;
                when (status.initializationState) {
                    AdapterStatus.State.NOT_READY -> {
                        // The adapter initialization did not complete.
                        Log.d("555555", "ads Adapter: " + className + " not ready.");
                    }

                    AdapterStatus.State.READY -> {
                        // The adapter was successfully initialized.
                        Log.d("555555", "ads Adapter: " + className + " is initialized.");
                    }
                }
            }

            appOpenManager=AppOpenManagerOKRA(this)
//             MediationTestSuite.launch(this@SplashScreen);
        }

        createNotificationChannel(ConstantsOKRA.RECEIVING_CALLS_CHANNEL_ID)
    }

    private fun createNotificationChannel(channel_id: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                channel_id,
                getString(R.string.channel_msg),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val incomingCallChannel = NotificationChannel(
                ConstantsOKRA.INCOMING_CALLS_CHANNEL_ID,
                getString(R.string.incoming_call),
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            val callSound=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            incomingCallChannel.enableVibration(true)
            incomingCallChannel.enableLights(true)

            incomingCallChannel.vibrationPattern=
                longArrayOf(100L,200L,300L,400L,300L,200L,300L,400L,200L,100L)
//            incomingCallChannel.setSound(callSound,AudioAttributes.Builder().build())//Uri.fromFile(File("file://android_asset/call_ring.mp3")
            incomingCallChannel.description="Receive calls in background"
            incomingCallChannel.importance=IMPORTANCE_HIGH
            incomingCallChannel.lockscreenVisibility=NotificationCompat.VISIBILITY_PUBLIC
            manager.createNotificationChannel(serviceChannel)
            manager.createNotificationChannel(incomingCallChannel)
            //
        }
    }



}