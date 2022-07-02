package com.smartswitch.phoneclone

import android.app.Application

//import com.facebook.ads.AudienceNetworkAds
//import com.google.android.gms.ads.MobileAds
//import com.google.android.gms.ads.initialization.AdapterStatus


class CAPPMApplication : Application() {
    override fun onCreate() {
        super.onCreate()
//       AudienceNetworkAds.initialize(this)

//         MobileAds.initialize(
//             this
//         ) { initializationStatus ->
//             val map = initializationStatus.getAdapterStatusMap();
//             for ( keyValuePair in map)
//             {
//                 val className = keyValuePair.key;
//                 val status = keyValuePair.value;
//                 when (status.initializationState)
//                 {
//                    AdapterStatus.State.NOT_READY->{
//                        // The HSAdapter initialization did not complete.
//                        Log.d("5555","Adapter: " + className + " not ready.");
//                    }
//
//                     AdapterStatus.State.READY->{
//                         // The HSAdapter was successfully initialized.
//                         Log.d("55555","Adapter: " + className + " is initialized.");
//                     }
//
//                 }
//             }
//
//             //LOAD ADDS HERE
//         }
    }
}