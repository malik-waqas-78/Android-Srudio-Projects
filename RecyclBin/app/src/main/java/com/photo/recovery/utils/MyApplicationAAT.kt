package com.photo.recovery.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.AdapterStatus
import com.photo.recovery.R
import com.photo.recovery.ads.AppOpenManagerAAT
//import com.datarecovery.recyclebindatarecovery.ads.AppOpenManagerAAT
//import com.facebook.ads.AudienceNetworkAds
//import com.google.android.gms.ads.MobileAds
//import com.google.android.gms.ads.initialization.AdapterStatus
import io.realm.Realm

class MyApplicationAAT: Application() {

    var ONGOING_NOTIFICATION_CHANNEL_ID : String="Recycle Bin"
    var FILE_INFO_CHANNEL_ID : String="FILE_INFO"
    private var appOpenManagerAAT: AppOpenManagerAAT? = null
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

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

                        Log.d("92727586243", "Adapter: " + className + " not ready.");
                    }

                    AdapterStatus.State.READY -> {

                        Log.d("92727586243", "Adapter: " + className + " is initialized.");
                    }

                }
            }



            appOpenManagerAAT= AppOpenManagerAAT(this);
//             MediationTestSuite.launch(this@SplashScreen);

        }



        createNotificationChannel(ONGOING_NOTIFICATION_CHANNEL_ID)
        createNotificationChannel(FILE_INFO_CHANNEL_ID)
    }

    private fun createNotificationChannel(channel_id: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                channel_id,
                getString(R.string.notification_listener_service),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
            //
        }
    }

}