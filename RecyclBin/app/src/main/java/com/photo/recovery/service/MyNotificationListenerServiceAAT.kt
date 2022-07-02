package com.photo.recovery.service

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Icon
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.photo.recovery.R
import com.photo.recovery.constants.MyConstantsAAT
import com.photo.recovery.constants.MyConstantsAAT.Companion.NL_TAG
import com.photo.recovery.database.ManageChatsAAT
import com.photo.recovery.utils.ManageObserver
import java.io.ByteArrayOutputStream
import java.text.DateFormat
import java.util.*


class MyNotificationListenerServiceAAT : NotificationListenerService() {

    private var ONGOING_NOTIFICATION_CHANNEL_ID: String? = null
    var observer:ManageObserver?=null

    companion object{
        var isServiceRunning=false
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        Log.d(NL_TAG, "onNotificationPosted: ${sbn?.notification?.extras}")

        if(sbn==null){
            return
        }



        for (i in sbn.notification?.extras?.keySet()!!) {
            Log.d(NL_TAG, "onNotificationPosted: key = $i value = ${sbn.notification.extras.get(i)}")
        }

        when (sbn.packageName) {

            MyConstantsAAT.WHATSAPP_PACKAGE_NAME -> {
                insertChats(sbn,MyConstantsAAT.TYPE_WHATSAPP)
            }
            MyConstantsAAT.GB_WHATSAPP_PACKAGE_NAME -> {
                insertChats(sbn,MyConstantsAAT.TYPE_WHATSAPP)
            }
            MyConstantsAAT.WHATSAPP_BUSINESS_PACKAGE_NAME -> {
                insertChats(sbn,MyConstantsAAT.TYPE_WHATSAPP)
            }
            MyConstantsAAT.MESSENGER_PACKAGE_NAME -> {
                insertChats(sbn,MyConstantsAAT.TYPE_MESSENGER)
            }
            MyConstantsAAT.MESSENGER_LITE_PACKAGE_NAME -> {
                insertChats(sbn,MyConstantsAAT.TYPE_MESSENGER)
            }
            MyConstantsAAT.MAIL_PACKAGE_NAME->{
                insertChats(sbn,MyConstantsAAT.TYPE_MAIL,sbn?.notification?.extras?.get("android.bigText").toString())
            }
            MyConstantsAAT.MAIL_LITE_PACKAGE_NAME->{
                insertChats(sbn,MyConstantsAAT.TYPE_MAIL,sbn?.notification?.extras?.get("android.bigText").toString())
            }
        }
    }

    private fun startObserving() {
        android.os.Handler().postDelayed({
            observer= ManageObserver.managerInstance
            val rootPath=applicationContext?.filesDir?.absolutePath
            observer?.setPath("$rootPath/backUp Bin", "$rootPath/recycle Bin")
            observer?.observe()

        },500)
    }

    private fun insertChats(sbn: StatusBarNotification?, type:String, text:String?=""){
        val msgText = if(text==null||text.isEmpty()){
            sbn?.notification?.extras?.getString("android.text")
        }else{
            text
        }
        val titleText = sbn?.notification?.extras?.getString("android.title") ?: return
        if (msgText != null && !msgText.contains("new messages")) {
            Log.d(
                NL_TAG,
                "onNotificationPosted: " + sbn?.notification?.extras?.getString("android.title")
            )
            var bitmap: Bitmap? = null
            if (Build.VERSION.SDK_INT >= 29) {
                val icon = sbn.notification.extras["android.largeIcon"] as Icon?
                if (icon != null) {
                    val d = icon.loadDrawable(applicationContext)
                    bitmap = (d as BitmapDrawable).bitmap
                }
            } else {
                bitmap = sbn.notification.extras["android.largeIcon"] as Bitmap?
            }
            if (bitmap != null) {
                val date= DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
                    .format(
                        Calendar.getInstance().time
                    )

                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()

                val manageChat=ManageChatsAAT(titleText,msgText,date,System.currentTimeMillis().toString(),byteArray,type)
                manageChat.insertData()
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (intent != null && intent.action != null && intent.action == "rebind.request") {
            //  Log.d(NL_TAG, "TRYING REBIND SERVICE at "+HotUtils.formatDate(new Date()));
            tryReconnectService() //switch on/off component and rebind
        }

        //START_STICKY  to order the system to restart your service as soon as possible when it was killed.
        return START_STICKY
    }


    override fun onListenerConnected() {
        super.onListenerConnected()
        ONGOING_NOTIFICATION_CHANNEL_ID =
            applicationContext.resources.getString(R.string.ongoing_notification_id)
        val notification =
            ONGOING_NOTIFICATION_CHANNEL_ID?.let {
                NotificationCompat.Builder(this, it)
                    .setContentTitle(getString(R.string.notification_listener_service))
                    .setContentText(getString(R.string.notification_text))
                    .setSmallIcon(R.drawable.ic_notification)
                    .setAutoCancel(false)
                    .setOnlyAlertOnce(true)
                    .build()
            }
        startForeground(MyConstantsAAT.ONGOING_NOTIFICATION_ID, notification)
        Log.d(NL_TAG, "onListenerConnected: ")
      //  MyConstants.rootDir=applicationContext.filesDir.absolutePath
        isServiceRunning=true
        startObserving()
       // showNotification()
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(NL_TAG, "onBind: ")
        return super.onBind(intent)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        requestRebind(
            ComponentName(
                applicationContext.packageName,
                applicationContext.javaClass.simpleName
            )
        )
    }

    private fun showNotification() {

        ONGOING_NOTIFICATION_CHANNEL_ID =
            applicationContext.resources.getString(R.string.ongoing_notification_id)

        // notificationId is a unique int for each notification that you must define
        val notificationId = MyConstantsAAT.ONGOING_NOTIFICATION_ID

        var notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val notifications: Array<StatusBarNotification> =
                notificationManager.activeNotifications
            for (notification in notifications) {
                if (notification.id == notificationId) {
                    return
                }
            }
        }
        notificationManager.notify(notificationId, notificationBuilder()?.build())
    }

    private fun notificationBuilder(): NotificationCompat.Builder? {
        return ONGOING_NOTIFICATION_CHANNEL_ID?.let {
            NotificationCompat.Builder(this, it)
                .setContentTitle(getString(R.string.notification_listener_service))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.ic_notification)
                .setOnlyAlertOnce(true)
        }
    }

    private fun tryReconnectService() {
        toggleNotificationListenerService()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val componentName = ComponentName(
                applicationContext,
                MyNotificationListenerServiceAAT::class.java
            )

            //It say to Notification Manager RE-BIND your service to listen notifications again inmediatelly!
            requestRebind(componentName)
        }
    }

    /**
     * Try deactivate/activate your component service
     */
    private fun toggleNotificationListenerService() {
        val pm = packageManager
        pm.setComponentEnabledSetting(
            ComponentName(this, MyNotificationListenerServiceAAT::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
        )
        pm.setComponentEnabledSetting(
            ComponentName(this, MyNotificationListenerServiceAAT::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
        )
    }
    override fun onTaskRemoved(rootIntent: Intent?) {
        val intent = Intent(applicationContext, MyNotificationListenerServiceAAT::class.java)
        val pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 3000] =
            pendingIntent
        super.onTaskRemoved(rootIntent)
        Log.e("92727", "onTask removed")
    }

    override fun onDestroy() {
        isServiceRunning=false
        val intent = Intent(applicationContext, MyNotificationListenerServiceAAT::class.java)
        val pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 3 * 1000] =
            pendingIntent
        super.onDestroy()
    }


}