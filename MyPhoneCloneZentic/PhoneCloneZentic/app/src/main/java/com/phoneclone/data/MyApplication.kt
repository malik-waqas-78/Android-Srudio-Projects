package com.phoneclone.data

import android.app.Application
import android.util.Log
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.AdapterStatus
import com.phoneclone.data.ads.AppOpenManager


class MyApplication : Application() {
    private var appOpenManager: AppOpenManager? = null
    override fun onCreate() {
        super.onCreate()
       AudienceNetworkAds.initialize(this)

         MobileAds.initialize(
             this
         ) { initializationStatus ->
             val map = initializationStatus.getAdapterStatusMap();
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
             appOpenManager= AppOpenManager(this);
         }
    }
}