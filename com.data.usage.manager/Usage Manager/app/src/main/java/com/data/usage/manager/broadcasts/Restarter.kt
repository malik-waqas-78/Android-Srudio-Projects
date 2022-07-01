package com.data.usage.manager.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.data.usage.manager.constants.Constants
import com.data.usage.manager.services.MyInformationService

class Restarter : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d(Constants.TAG, "onReceive: ")
        val intent=Intent(context, MyInformationService::class.java);
        // Constants.ActivityContext.startService(intent);
        ContextCompat.startForegroundService(context, intent);
    }
}