package com.data.usage.manager.usefullclasses

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.AdapterStatus


class App : Application() {

    companion object {
        const val CHANNEL_ID = "Network_Info_Service";
        const val NOTIFY_CHANNELID = "DOD-OTP Sent";
    }

    override fun onCreate() {
        super.onCreate();
        AudienceNetworkAds.initialize(this);

        MobileAds.initialize(
            this
        ) { initializationStatus ->
            val map = initializationStatus.adapterStatusMap;
            for ( keyValuePair in map)
            {
                val className = keyValuePair.key;
                val status = keyValuePair.value;
                when (status.initializationState)
                {
                    AdapterStatus.State.NOT_READY->{
                        // The adapter initialization did not complete.
                        Log.d("92727586243","Adapter: " + className + " not ready.");
                    }

                    AdapterStatus.State.READY->{
                        // The adapter was successfully initialized.
                        Log.d("92727586243","Adapter: " + className + " is initialized.");
                    }

                }
            }

            //LOAD ADDS HERE
        }
        createNotificationChannel();
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "NETWORK USAGE UPDATES",
                NotificationManager.IMPORTANCE_DEFAULT
            );

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
            val serviceChannel2 = NotificationChannel(
                NOTIFY_CHANNELID,
                "DATA BUNDLE FINISHED",
                NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(serviceChannel2);
        }
    }


}