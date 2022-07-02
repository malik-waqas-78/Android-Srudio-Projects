package com.ppt.walkie.callbacks

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.ppt.walkie.services.MyServiceOKRA
import com.ppt.walkie.utils.ConstantsOKRA

class RestarterOKRA : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d(ConstantsOKRA.TAG, "onReceive: ")
        if (intent?.action == "restartservice") {
            val mIntent = Intent(context, MyServiceOKRA::class.java);
            // Constants.ActivityContext.startService(intent);
            ContextCompat.startForegroundService(context, mIntent);
        } else {
            MyServiceOKRA.stopBeep()
            MyServiceOKRA.userRejectedConnection()
        }

    }
}