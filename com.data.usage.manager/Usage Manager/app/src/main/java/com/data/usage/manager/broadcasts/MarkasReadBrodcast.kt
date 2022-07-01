package com.data.usage.manager.broadcasts

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.data.usage.manager.constants.Constants
import com.data.usage.manager.sharedpreferences.MySharedPreferences

class MarkasReadBrodcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(Constants.TAG, "onReceive: ")
        var notificationId = intent!!.getIntExtra(Constants.NOTIFICATION_ID, 0)
        val mySharedPreferences = MySharedPreferences(context!!)
        if (intent!!.extras!!.getBoolean(Constants.DATAPLAN)) {
            mySharedPreferences.setDataPlan(false)
           // Toast.makeText(context, "Data Plan Deleted", Toast.LENGTH_SHORT).show()
        } else {
            val editor = mySharedPreferences.getPreferencesEditor()
            editor.putBoolean(Constants.ALERTED_A_DAY_AGO, true)
        }
        if (intent!!.extras!!.getBoolean(Constants.DATAPLAN)) {
            if (notificationId.equals(Constants.WIFI_NOTIFY)) {
                mySharedPreferences.setWifiDataPlanSet(false)
            } else
                mySharedPreferences.setDataPlan(false)
           // Toast.makeText(context, "Data Plan Deleted", Toast.LENGTH_SHORT).show()
        } else {
            if (notificationId.equals(Constants.WIFI_NOTIFY)) {
                val editor = mySharedPreferences.getPreferencesEditor()
                editor.putBoolean(Constants.ALERTED_A_DAY_AGO_WIFI, true)
                editor.apply()
            } else {
                val editor = mySharedPreferences.getPreferencesEditor()
                editor.putBoolean(Constants.ALERTED_A_DAY_AGO, true)
                editor.apply()
            }
        }
        if (notificationId.equals(Constants.NOTIFY)) {
            val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(Constants.NOTIFY)
        }
        if (notificationId.equals(Constants.WIFI_NOTIFY)) {
            val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(Constants.WIFI_NOTIFY)
        }
    }
}