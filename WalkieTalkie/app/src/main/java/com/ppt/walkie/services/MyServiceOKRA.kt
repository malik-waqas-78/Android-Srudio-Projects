package com.ppt.walkie.services

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.Strategy
import com.ppt.walkie.R
import com.ppt.walkie.actvities.IncomingCallOKRA
import com.ppt.walkie.actvities.MainActivityOKRA
import com.ppt.walkie.actvities.SplashScreenOKRA
import com.ppt.walkie.callbacks.RestarterOKRA
import com.ppt.walkie.callbacks.ServiceCallBacksOKRA
import com.ppt.walkie.utils.*
import com.ppt.walkie.utils.ConstantsOKRA.TAG
import java.io.IOException


class MyServiceOKRA : ConnectionsServiceOKRA() {

    private val SERVICE_ID = "com.walkie.talkie.SERVICE_ID"

    private val STRATEGY: Strategy = Strategy.P2P_STAR

    private val binder = MyServiceBinder()

    var mServiceCallBacks: ServiceCallBacksOKRA? = null

    var mSharePrefHelper: SharePrefHelperOKRA? = null

    private var mState = MainActivityOKRA.State.UNKNOWN

    private var mName: String? = null


    private var mRecorder: AudioRecorderOKRA? = null


    private var mAudioPlayer: AudioPlayerOKRA? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        m = MediaPlayer()
        startForeground(ConstantsOKRA.SERVICE_NOTIFICATION_ID, createNotification())
        /* NotificationManagerCompat.from(applicationContext).notify(978,
             createIncomingCallNotification()
         )
         playBeep()*/

        mSharePrefHelper = SharePrefHelperOKRA(applicationContext)
        mName = mSharePrefHelper?.getName()
        startAdvertising()
        return START_STICKY
    }

    override fun onDestroy() {
        stopBeep()
        if (mSharePrefHelper?.canReceiveCallsInBackground() == true) {
            var broadcastIntent = Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, RestarterOKRA::class.java)
            this.sendBroadcast(broadcastIntent);
        } else {
            stopAdvertising()
        }
        setState(MainActivityOKRA.State.UNKNOWN)
        onActivityStopped()
        super.onDestroy()
    }

    fun getState(): MainActivityOKRA.State {
        return mState
    }

    fun getConnectionRequestObject(): ConnectionRequestOKRA? {
        return mConnectionRequest
    }

    fun emptyConnectionRequestObject() {
        mConnectionRequest = null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        stopBeep()
        if (mSharePrefHelper?.canReceiveCallsInBackground() == true) {
            val service = PendingIntent.getService(
                applicationContext,
                1001,
                Intent(applicationContext, MyServiceOKRA::class.java),
                PendingIntent.FLAG_ONE_SHOT
            )

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000] = service
        }
        stopAdvertising()
        onActivityStopped()
        super.onTaskRemoved(rootIntent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    public override fun getName(): String {
        return mName ?: ""
    }

    public override fun getServiceId(): String {
        return SERVICE_ID
    }


    public override fun getStrategy(): Strategy {
        return STRATEGY
    }

    public override fun onReceive(endpoint: Endpoint?, payload: Payload?) {
        if (payload?.type == Payload.Type.STREAM) {
            if (mAudioPlayer != null) {
                mAudioPlayer?.stop()
                mAudioPlayer = null
            }
            val player: AudioPlayerOKRA =
                object : AudioPlayerOKRA(payload.asStream()?.asInputStream()) {
                    @WorkerThread
                    override fun onFinish() {

                        mServiceCallBacks?.onFinishedPlaying()
                        mAudioPlayer = null

                    }
                }
            stopRecording()
            payload?.let { mServiceCallBacks?.onReceive(endpoint, it) }
            mAudioPlayer = player
            player.start()
        }

    }

    /** Stops streaming sound from the microphone.  */

    public override fun onConnectionFailed(endpoint: Endpoint?) {
        stopBeep()
        mServiceCallBacks?.onConnectionFailed(endpoint)
    }

    public override fun onEndpointDisconnected(endpoint: Endpoint?) {
        stopBeep()
        setState(MainActivityOKRA.State.SEARCHING)
        endpoint?.let { mServiceCallBacks?.onEndpointDisconnected(it) }
        emptyConnectionRequestObject()
    }

    public override fun onEndpointConnected(endpoint: Endpoint?) {
        stopBeep()
        if (endpoint != null) {
            setState(MainActivityOKRA.State.CONNECTED)
            stopDiscovering()
            mServiceCallBacks?.onEndpointConnected(endpoint)
            emptyConnectionRequestObject()
        }
    }

    public override fun onConnectionInitiated(
        endpoint: Endpoint?,
        connectionInfo: ConnectionInfo?
    ) {

        mConnectionRequest = endpoint?.let {
            connectionInfo?.let { it1 ->
                ConnectionRequestOKRA(
                    it,
                    it1
                )
            }
        }
        if (mServiceCallBacks != null) {
            connectionInfo?.let { mServiceCallBacks?.onConnectionInitiated(endpoint, it) }
        } else {
            if (Build.VERSION.SDK_INT <= 27) {
                startActivity(Intent(applicationContext, IncomingCallOKRA::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra(ConstantsOKRA.CALLER_NAME, endpoint?.name)
                })
            } else {

                endpoint?.let { createIncomingCallNotification(it) }?.let {
                    NotificationManagerCompat.from(applicationContext).notify(
                        978,
                        it
                    )
                }
                playBeep()
            }
        }
    }


    public override fun onEndpointDiscovered(endpoint: Endpoint?) {
        mServiceCallBacks?.onEndpointDiscovered(endpoint)
    }


    fun setServiceCallBacks(serviceCallBacks: ServiceCallBacksOKRA?) {
        mServiceCallBacks = serviceCallBacks
    }

    private fun isRecording(): Boolean {
        return mRecorder != null && mRecorder?.isRecording() == true
    }

    fun stopRecording() {
//        logV("stopRecording()")
        mServiceCallBacks?.onStopRecording()
        if (mRecorder != null) {
            mRecorder?.stop()
            mRecorder = null
        }
    }

    fun startRecording() {
//        logV("startRecording()")
        mServiceCallBacks?.onStartRecording()
        try {
            val payloadPipe = ParcelFileDescriptor.createPipe()

            // Send the first half of the payload (the read side) to Nearby Connections.
            send(Payload.fromStream(payloadPipe[0]))

            // Use the second half of the payload (the write side) in AudioRecorder.
            mRecorder =
                AudioRecorderOKRA(payloadPipe[1])
            mRecorder?.start()
        } catch (e: IOException) {
//            logE("startRecording() failed", e)
        }
    }

    fun setState(state: MainActivityOKRA.State) {
        if (mState == state) {
            /* logW("State set to $state but already in that state")*/
            return
        }
        /* logD("State set to $state")*/
        val oldState = mState
        mState = state
        onStateChanged(oldState, state)
    }

    private fun onStateChanged(oldState: MainActivityOKRA.State, newState: MainActivityOKRA.State) {

        when (newState) {
            MainActivityOKRA.State.SEARCHING -> {
                disconnectFromAllEndpoints()
                stopDiscovering()
                stopAdvertising()
                startDiscovering()
                startAdvertising()
                mServiceCallBacks?.onStateSearching()
            }
            MainActivityOKRA.State.CONNECTED -> {
                stopDiscovering()
                if (mSharePrefHelper?.canReceiveCallsInBackground() == false) {
                    stopAdvertising()
                }
                mServiceCallBacks?.onStateConnected()
            }
            MainActivityOKRA.State.UNKNOWN -> {
                stopAllEndpoints()
                mServiceCallBacks?.onStateUnKnow()
                stopAdvertising()
                startAdvertising()
            }
            else -> {
            }
        }
    }

    fun onActivityStopped() {
        // Restore the original volume.


        if (isRecording()) {
            stopRecording()
        }
        if (isPlaying()) {
            stopPlaying()
        }
        stopBeep()
    }

    private fun stopPlaying() {
        mServiceCallBacks?.onStopPlaying()
        if (mAudioPlayer != null) {
            mAudioPlayer?.stop()
            mAudioPlayer = null
        }
    }

    private fun isPlaying(): Boolean {
        return mAudioPlayer != null
    }


    inner class MyServiceBinder : Binder() {
        fun getService(): MyServiceOKRA {
            return this@MyServiceOKRA
        }
    }


    private fun createNotification(): Notification {
        var notificationIntent = Intent(this, SplashScreenOKRA::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        );
        val notification = NotificationCompat.Builder(this, ConstantsOKRA.RECEIVING_CALLS_CHANNEL_ID)
            .setContentTitle("Walkie Talkie")
            .setContentText("Call receiving can be switch off from settings")
            .setOnlyAlertOnce(true)
            .setSmallIcon(R.drawable.walkie_talkie_simple_icon)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .setShowWhen(false)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .build();
        return notification;
    }

    private fun createIncomingCallNotification(endpoint: Endpoint?): Notification {
        var notificationIntent = Intent(this, IncomingCallOKRA::class.java)
        notificationIntent.putExtra(ConstantsOKRA.INCOMING_CALL, true)
        notificationIntent.putExtra(ConstantsOKRA.CALLER_NAME, endpoint?.name)
        val pendingIntent = PendingIntent.getActivity(
            this,
            3, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT
        );
        val deleteIntent = Intent(this, RestarterOKRA::class.java)
        val pendingDeleteIntent = PendingIntent.getBroadcast(this, 0, deleteIntent, 0)

        val notification = NotificationCompat.Builder(this, ConstantsOKRA.INCOMING_CALLS_CHANNEL_ID)
            .setContentTitle(getString(R.string.incoming_call))
            .setContentText("${endpoint?.name} is calling you. Slide to ignore")
            .setOnlyAlertOnce(true)
            .setSmallIcon(R.drawable.walkie_talkie_simple_icon)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setShowWhen(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setDeleteIntent(pendingDeleteIntent)
            .setTimeoutAfter(ConstantsOKRA.CALL_TIME_OUT)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .build();
        return notification
    }

    private fun playBeep() {
        try {
            android.os.Handler().postDelayed({
                try {
                    if (m?.isPlaying == true) {
                        m?.stop()
                        m?.reset()
                        m = MediaPlayer()
                    }
                    val descriptor = assets.openFd("calling_tune.mp3")
                    m?.setDataSource(
                        descriptor.fileDescriptor,
                        descriptor.startOffset,
                        descriptor.length
                    )
                    descriptor.close()
                    m?.prepare()
                    m?.setVolume(1f, 1f)
                    m?.isLooping = true
                    m?.start()

                    android.os.Handler().postDelayed({
                        try {
                            if (m?.isPlaying == true) {
                                m?.stop()
                                m?.reset()
                                m = MediaPlayer()
                            }
                        } catch (e: Exception) {
                            logD("error")
                        }
                    }, ConstantsOKRA.CALL_TIME_OUT)

                } catch (e: Exception) {
                    Log.d(TAG, "playBeep: ")
                }
            }, 800)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    companion object {
        var m: MediaPlayer? = null
        private var mConnectionRequest: ConnectionRequestOKRA? = null
        fun stopBeep() {
            try {
                if (m?.isPlaying == true) {
                    m?.stop()
                    m?.reset()
                }

            } catch (e: java.lang.Exception) {

            }
        }

        fun userRejectedConnection() {
            rejectConnection(getConnectionRequestObject()?.requestingEndpoint)
            emptyConnectionRequestObject()
        }

        private fun getConnectionRequestObject(): ConnectionRequestOKRA? {
            return mConnectionRequest
        }

        private fun emptyConnectionRequestObject() {
            mConnectionRequest = null
        }

    }


}