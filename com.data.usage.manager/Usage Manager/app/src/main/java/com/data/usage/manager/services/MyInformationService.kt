package com.data.usage.manager.services

import android.Manifest
import android.app.*
import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.os.Process.SYSTEM_UID
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.data.usage.manager.activities.SplashScreen
import com.data.usage.manager.broadcasts.MarkasReadBrodcast
import com.data.usage.manager.broadcasts.Restarter
import com.data.usage.manager.constants.Constants
import com.data.usage.manager.R
import com.data.usage.manager.sharedpreferences.MySharedPreferences
import com.data.usage.manager.usefullclasses.App.Companion.CHANNEL_ID
import com.data.usage.manager.usefullclasses.App.Companion.NOTIFY_CHANNELID
import com.data.usage.manager.usefullclasses.MyAppPermissions
import java.lang.NullPointerException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MyInformationService : Service() {


    var subscriberid: String? = null
    var networkStatsManager: NetworkStatsManager? = null
    var TAG = "MYSERVICE";
    var handler = Handler();
    lateinit var mySharedPreferences: MySharedPreferences
    lateinit var myPermission: MyAppPermissions
    override fun onBind(intent: Intent?): IBinder? {
        return null;
    }

    override fun onCreate() {
        super.onCreate()
        myPermission = MyAppPermissions(applicationContext, baseContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ");
        var text = "SIM Data: 0.00 MB   WIFI Data: 0.00 MB";
        startForeground(Constants.FOREGROUND_SERVICE, createNotification(text))
        mySharedPreferences = MySharedPreferences(this)
        networkStatsManager =
            this.getSystemService(Context.NETWORK_STATS_SERVICE) as (NetworkStatsManager)
        if (myPermission.isRuntimePermissions() && myPermission.isSystemLevelPermissionGiven()) {
            getSubIdOf()
            //do heavy work on a background thread
            handler.postDelayed(myRunnable, 5000)
        }
        return START_STICKY;
    }

    private val myRunnable: Runnable = object : Runnable {
        override fun run() {
            if (myPermission.isSystemLevelPermissionGiven()) {
                runBakgroundThreed()
            }
            handler.postDelayed(this, 15000)
        }
    }

    private fun getProperDate(date: Int, month: Int, year: Int): String {
        var d = ""
        var m = ""
        var y = ""
        if (date <= 9) {
            d = "0$date"
        } else {
            d = "$date"
        }
        var mm = month
        mm += 1
        if (mm <= 9) {
            m = "0$mm"
        } else {
            m = "$mm"
        }
        if (year <= 9) {
            y = "0$year"
        } else {
            y = "$year"
        }

        return "$d/$m/$y"
    }

    private fun runBakgroundThreed() {
        var bucket: NetworkStats.Bucket? = null;
        try {
            var startingDate = ""
            var todayDate = ""
            if (mySharedPreferences.isWifiDataPlanSet()) {
                var x = mySharedPreferences.getWifiDataPlan()
                startingDate = x?.planStartingDate.toString()
                startingDate = "$startingDate 00:00:00"
            } else {
                var date = Calendar.getInstance().get(Calendar.DATE)
                var month = Calendar.getInstance().get(Calendar.MONTH)
                var year = Calendar.getInstance().get(Calendar.YEAR)
                startingDate = getProperDate(date, month, year)
                startingDate = "$startingDate 00:00:00"
            }
            var date = Calendar.getInstance().get(Calendar.DATE)
            var month = Calendar.getInstance().get(Calendar.MONTH)
            var year = Calendar.getInstance().get(Calendar.YEAR)
            todayDate = getProperDate(date, month, year)
            todayDate = "$todayDate 00:00:00"
            try {
                bucket = networkStatsManager?.querySummaryForDevice(
                    Constants.WIFII_NETWORK,
                    "",
                    getDateinMilies("$startingDate"),
                    System.currentTimeMillis()
                )
            } catch (ex: NullPointerException) {
                ex.printStackTrace()
                bucket = null
            } catch (e: SecurityException) {
                e.printStackTrace()
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
            var WifiData: Long? = null
            if (bucket != null) {
                WifiData = bucket?.getRxBytes()!!
                WifiData = WifiData!! + bucket!!.getTxBytes()
            } else {
                WifiData = 0
            }
            if (mySharedPreferences.isDataPlanSet()) {
                startingDate = "${mySharedPreferences.getDataPlan()?.planStartingDate} 00:00:00"
            } else {
                var date = Calendar.getInstance().get(Calendar.DATE)
                var month = Calendar.getInstance().get(Calendar.MONTH)
                var year = Calendar.getInstance().get(Calendar.YEAR)
                startingDate = getProperDate(date, month, year)
                startingDate = "$startingDate 00:00:00"
            }
            var MobileData: Long = 0
            if (myPermission.isRuntimePermissions()) {
                try {
                    bucket = networkStatsManager?.querySummaryForDevice(
                        Constants.CELLULAR_NETWORK,
                        getSubIdOf(),
                        getDateinMilies("$startingDate"),
                        System.currentTimeMillis()
                    )
                } catch (ex: NullPointerException) {
                    ex.printStackTrace()
                    bucket=null
                } catch (e: SecurityException) {
                    e.printStackTrace()
                } catch (e: RemoteException) {
                    e.printStackTrace()
                } catch (e:java.lang.Exception){}
                if (bucket != null) {
                    MobileData = bucket!!.getRxBytes()
                    MobileData += bucket!!.getTxBytes()
                } else {
                    MobileData = 0
                }
                MobileData += getExtraUsage(Constants.UID_REMOVED, startingDate!!)
                MobileData += getExtraUsage(Constants.UID_TETHRING, startingDate!!)
                MobileData += getExtraUsage(SYSTEM_UID, startingDate!!)
            }
            postNotification(WifiData!!, MobileData)
            if (mySharedPreferences.isDataPlanSet()) {
                checkDataLimit(MobileData, todayDate)
            }
            if (mySharedPreferences.isWifiDataPlanSet()) {
                checkDataLimit(todayDate, WifiData!!)
            }
        }catch (ex:NullPointerException){
            ex.printStackTrace()
        }catch (e:SecurityException){
            e.printStackTrace()
        }catch (e:RemoteException){
            e.printStackTrace()
        } catch (e:Exception){

        }

}

private fun checkDataLimit(todayDate: String, wifiData: Long) {
    val wifidataplan = mySharedPreferences.getWifiDataPlan()
    if (wifidataplan!!.alertbytes <= wifiData) {
        //generate notifcation telling plan will be over soon <200mbs
        val notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        val text = "Your remaining MBs are less than 200.Plz turn off your WIFI."
        notificationManagerCompat.notify(
            Constants.WIFI_NOTIFY,
            alertNotification(text, true, Constants.WIFI_NOTIFY)
        )
        //mySharedPreferences.setDataPlan(false)

    }

    var sdf = SimpleDateFormat("yyyy/MM/dd")
    var date = sdf.parse(wifidataplan!!.alerDate)
    var planalertmiliies = date.time
    date = sdf.parse(todayDate)
    var todaymillies = date.time

    if (todaymillies >= planalertmiliies && !mySharedPreferences.sharedPreferences!!.getBoolean(
            Constants.ALERTED_A_DAY_AGO_WIFI,
            false
        )
    ) {
//            var editor= mySharedPreferences.getPreferencesEditor()
//            editor.putBoolean(Constants.ALERTED_A_DAY_AGO,true)
        val notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext())
        val text = "Your Wifi Data will be over Tomorrow. Plz use it Carefully."
        notificationManagerCompat.notify(
            Constants.WIFI_NOTIFY,
            alertNotification(text, false, Constants.WIFI_NOTIFY)
        )
        //generate notification limit is going to be over tomorrow
    }
    date = sdf.parse(wifidataplan.planEndingDate)
    var planendingdatemillies = date.time
    if (todaymillies >= planendingdatemillies) {
        //generate notificatio telling plan reached ending date
        val notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        val text = "Your Wifi Data plan has reached its DeadLine.Plz turn off your Wifi."
        notificationManagerCompat.notify(
            Constants.WIFI_NOTIFY,
            alertNotification(text, true, Constants.WIFI_NOTIFY)
        )
    }
}

private fun checkDataLimit(mobileData: Long, todayDate: String) {
    val dataPlan = mySharedPreferences.getDataPlan()
    Log.d(TAG, "alert bytes: ${dataPlan!!.alertbytes}")
    Log.d(TAG, "data plan : ${mobileData}")
    Log.d(TAG, "today date : $todayDate")
    Log.d(TAG, "alert date: ${dataPlan!!.alerDate}")
    Log.d(TAG, "checkDataLimit: ${dataPlan.planEndingDate!!}")

    if (dataPlan!!.alertbytes <= mobileData) {
        //generate notifcation telling plan will be over soon <200mbs
        val notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        val text = "Your remaining MBs are less than 200.Plz turn off your Data."
        notificationManagerCompat.notify(
            Constants.NOTIFY,
            alertNotification(text, true, Constants.NOTIFY)
        )
        //mySharedPreferences.setDataPlan(false)

    }

    var sdf = SimpleDateFormat("yyyy/MM/dd")
    var date = sdf.parse(dataPlan!!.alerDate)
    var planalertmiliies = date.time
    date = sdf.parse(todayDate)
    var todaymillies = date.time
    if (todaymillies >= planalertmiliies && !mySharedPreferences.sharedPreferences!!.getBoolean(
            Constants.ALERTED_A_DAY_AGO,
            false
        )
    ) {
//            var editor= mySharedPreferences.getPreferencesEditor()
//            editor.putBoolean(Constants.ALERTED_A_DAY_AGO,true)
        val notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        val text = "Your Data will be over Tomorrow. Plz use it Carefully."
        notificationManagerCompat.notify(
            Constants.NOTIFY,
            alertNotification(text, false, Constants.NOTIFY)
        )
        //generate notification limit is going to be over tomorrow
    }
    date = sdf.parse(dataPlan.planEndingDate)
    var planendingdatemillies = date.time
    if (todaymillies >= planendingdatemillies) {
        //generate notificatio telling plan reached ending date
        val notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        val text = "Your Data plan has reached its DeadLine.Plz turn off your Data."
        notificationManagerCompat.notify(
            Constants.NOTIFY,
            alertNotification(text, true, Constants.NOTIFY)
        )
    }

}

private fun getExtraUsage(uid: Int, startingdate: String): Long {
    var stats: NetworkStats? = null
    try {
        stats = networkStatsManager?.queryDetailsForUid(
            Constants.CELLULAR_NETWORK,
            getSubIdOf(),
            getDateinMilies(startingdate),
            System.currentTimeMillis(),
            uid
        )
    } catch (ex: NullPointerException) {
        stats = null
    } catch (e: RemoteException) {
        e.printStackTrace()
    } catch (e: SecurityException) {
        e.printStackTrace()
    }catch (e:Exception){

    }
    var data: Long? = 0
    if (stats != null) {
        if (data != null) {
            while (stats?.hasNextBucket() == true) {
                var bucket: NetworkStats.Bucket = NetworkStats.Bucket()
                stats.getNextBucket(bucket)
                data += bucket.getRxBytes()
                data += bucket.getTxBytes()
            }
        }
    } else {
        return 0
    }
    stats?.close();
    return data!!;
}

private fun createNotification(text: String): Notification {
    var notificationIntent = Intent(this, SplashScreen::class.java)
    val pendingIntent = PendingIntent.getActivity(
        this,
        0, notificationIntent, 0
    );
    val notification = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("Monitoring Data Usage")
        .setContentText(text)
        .setOnlyAlertOnce(true)
        .setSmallIcon(R.drawable.icon_usage)
        .setContentIntent(pendingIntent)
        .setAutoCancel(false)
        .setShowWhen(false)
        .setPriority(NotificationManager.IMPORTANCE_HIGH)
        .build();
    return notification;
}

private fun alertNotification(text: String, dataplan: Boolean, notificationID: Int): Notification {

    val snoozeIntent = Intent(this, MarkasReadBrodcast::class.java)
    snoozeIntent.action = Constants.ACTION_SNOOZE
    snoozeIntent.putExtra(Constants.NOTIFICATION_ID, notificationID)
    snoozeIntent.putExtra(Constants.DATAPLAN, dataplan)
    val snoozePendingIntent: PendingIntent =
        PendingIntent.getBroadcast(
            this,
            Constants.NOTIFY,
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    var title = if (dataplan) "Delete data plan." else "Mark as read"
    val notification = NotificationCompat.Builder(this, NOTIFY_CHANNELID)
        .setContentTitle("Data Usage Alert")
        .setContentText(text)
        .setSmallIcon(R.drawable.icon_usage)
        .addAction(R.drawable.icon_usage, title, snoozePendingIntent)
        .setShowWhen(true)
        .setAutoCancel(true)
        .setPriority(NotificationManager.IMPORTANCE_MAX)
        .build()
    return notification;
}

override fun onDestroy() {

    var broadcastIntent = Intent();
    broadcastIntent.setAction("restartservice");
    broadcastIntent.setClass(this, Restarter::class.java)
    this.sendBroadcast(broadcastIntent);
    super.onDestroy()
}

override fun onTaskRemoved(rootIntent: Intent?) {
    super.onTaskRemoved(rootIntent)
    var restartService = Intent(getApplicationContext(), MyInformationService::class.java)
    restartService.setPackage(getPackageName());
    var restartServicePI = PendingIntent.getService(
        getApplicationContext(), 1, restartService,
        PendingIntent.FLAG_ONE_SHOT
    );
    var alarmService =
        getApplicationContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmService.set(
        AlarmManager.ELAPSED_REALTIME,
        SystemClock.elapsedRealtime() + 1000,
        restartServicePI
    );
}


//local functions

private fun postNotification(wifiData: Long, mobileData: Long) {
    Log.d(TAG, "postNotification: ");
    val notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
    val text =
        "SIM Data: " + convertToproperStorageType(mobileData.toDouble()) + "  WIFI Data: " + convertToproperStorageType(
            wifiData.toDouble()
        );
    notificationManagerCompat.notify(Constants.FOREGROUND_SERVICE, createNotification(text));
}


fun getSubIdOf(): String? {
    if (Build.VERSION.SDK_INT >= 29) {
        return null
    } else {
        var subscriptionId: Int
        var simSlot = MySharedPreferences(applicationContext).getSimSlot()
        // Log.d(Constants.TAG, "getSubIdOf: sim slot $simSlot")
        val subscriptionManager: SubscriptionManager =
            applicationContext.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return ""
        }
        // Log.d(Constants.TAG, "getSubIdOf: info ${subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(simSlot)}")
        subscriptionId =
            if (subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(simSlot) != null && subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(
                    simSlot
                ).subscriptionId == null
            )
                0
            else {
                if (subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(simSlot) != null)
                    subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(simSlot)?.subscriptionId!!
                else
                    0
            }

        try {
            var telephonyManager =
                applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (Build.VERSION.SDK_INT >= 24 && Build.VERSION.SDK_INT < 29) {
                subscriberid = telephonyManager.createForSubscriptionId(subscriptionId).subscriberId
                //  Log.d(Constants.TAG, "getSubIdOf: yes ${subscriberid}")
            } else {
                subscriberid = telephonyManager.getSubscriberId()
                // Log.d(Constants.TAG, "getSubIdOf: No  $subscriberid")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: RemoteException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
    return subscriberid
}

private fun getDateinMilies(date: String): Long {
    var milliseconds: Long = 0
    var f = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    try {
        val d = f.parse(date)
        milliseconds = d.getTime()
    } catch (e: ParseException) {
        e.printStackTrace()
    } catch (e:java.lang.Exception){}
    return milliseconds;
}

private fun convertToproperStorageType(data: Double): String {
    var d: Double = 0.0
    if (data >= (1024.0 * 1000.0 * 1000.0 * 1000.0)) {
        d = data / (1000.0 * 1000.0 * 1000.0 * 1000.0);
        return String.format("%.2f", d) + " TB";
    }
    if (data >= (1024.0 * 1000.0 * 1000.0)) {
        d = data / (1024.0 * 1000.0 * 1000.0)
        return String.format("%.2f", d) + " GB";
    }
    if (data >= (1024.0 * 1000.0)) {
        d = data / (1024.0 * 1000.0);
        return String.format("%.2f", d) + " MB";
    }
    if (data >= 1024.0) {
        d = data / 1024.0;
        return String.format("%.2f", d) + " KB";
    }
    if (d == 0.0) {
        return String.format("%.2f", d) + " KB";
    }
    return String.format("%.2f", d) + " By";
}
}