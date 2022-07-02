package com.ppt.walkie.actvities


import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Dialog
import android.content.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.annotation.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.AdSize
import com.facebook.ads.AdView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.Strategy
import com.ppt.walkie.R
import com.ppt.walkie.adapters.AdapterEndPointsOKRA
import com.ppt.walkie.callbacks.ServiceCallBacksOKRA
import com.ppt.walkie.databinding.DbWaitingForRequestResponceOkraBinding
import com.ppt.walkie.databinding.MainScreenOkraBinding


import com.ppt.walkie.services.ConnectionsServiceOKRA
import com.ppt.walkie.services.MyServiceOKRA
import com.ppt.walkie.utils.*
import com.walkie.talkie.ads.BannerAdHelperOKRA
import com.walkie.talkie.ads.InterAdHelperOKRA
import com.walkie.talkie.ads.InterAdHelperOKRA.Companion.isAppInstalledFromPlay
import com.walkie.talkie.ads.NativeAdHelperOKRA

import java.util.*

class MainActivityOKRA : AppCompatActivity(), ServiceCallBacksOKRA {

    private var mName: String? = null
    var handler: Handler?=null
    var runnable: Runnable? =null

    var handler2: Handler?=null
     var runnable2: Runnable?=null


    var callerName:String?=null
    var value:Int =0
    var mService: MyServiceOKRA? = null
    private var mBound: Boolean = false

    var dialogOnce:Boolean=true
    companion object{
        var advisibility:Boolean=true
    }


    private val SERVICE_ID = "com.walkie.talkie.SERVICE_ID"
    private val STRATEGY: Strategy = Strategy.P2P_STAR

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as MyServiceOKRA.MyServiceBinder
            mService = binder.getService()
            mBound = true
            mService?.setServiceCallBacks(this@MainActivityOKRA)
            mName = mService?.name
//            binding.tvName.text = mName
            if (intent.hasExtra(ConstantsOKRA.CALl_ACCEPTED)) {
                val mConnectionRequest = mService?.getConnectionRequestObject()
                mConnectionRequest?.mConnectionInfo?.let {
                    acceptCall(mService?.getConnectionRequestObject()?.requestingEndpoint)
                }
                mService?.emptyConnectionRequestObject()
            } else if (intent.hasExtra("restart")) {
                mService?.setState(State.SEARCHING)
                prepareSearchingDevicesView()
                list.clear()
                adapter?.notifyDataSetChanged()
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }


    /** The phone's original media volume.  */
    private var mOriginalVolume = 0

    lateinit var binding: MainScreenOkraBinding

    var adapter: AdapterEndPointsOKRA? = null
    var makingConnectionRequest = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        binding = MainScreenOkraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar
            ?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.actionBar))

        handler = Handler()
        handler2 = Handler()

        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow)

        binding.toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })

           if (isAppInstalledFromPlay(this@MainActivityOKRA)) {
               BannerAdHelperOKRA.loadMediatedAdmobBanner(this,binding.topBanner)
//               loadAdmobBannerAd()
//               loadFbBannerAd()
           }

        if (!(NativeAdHelperOKRA.googleNativeAd == null)) {
            NativeAdHelperOKRA.showAdmobNativeAd(this@MainActivityOKRA, binding.adFrame)
        }

        if (!this@MainActivityOKRA.isMyServiceRunning(MyServiceOKRA::class.java)) {
            val intent = Intent(this@MainActivityOKRA, MyServiceOKRA::class.java)
            ContextCompat.startForegroundService(this@MainActivityOKRA, intent)
        }

        val sharePrefHelper = SharePrefHelperOKRA(this@MainActivityOKRA)
//        binding.ivUser.background = getDrawable(sharePrefHelper.getAvatar())

        binding.rvListEndpoints.layoutManager =
            LinearLayoutManager(this@MainActivityOKRA, LinearLayoutManager.VERTICAL, false)
        adapter = AdapterEndPointsOKRA(list, object : AdapterEndPointsOKRA.OnItemSelected {
            override fun itemSelected(endpoint: ConnectionsServiceOKRA.Endpoint) {
                makingConnectionRequest = true
                mService?.connectToEndpoint(endpoint)
                //show dialogue connecting
                showDialogue(endpoint.name)
                callerName=endpoint.name

                binding.tvConnectedto.text="Connected to $callerName"
            }


        })
        binding.rvListEndpoints.adapter = adapter
    }

    var waitingDialog: Dialog? = null
    private fun showDialogue(name: String) {
        waitingDialog = Dialog(this@MainActivityOKRA).apply {
            val binding =
                DbWaitingForRequestResponceOkraBinding.inflate(LayoutInflater.from(this@MainActivityOKRA))
            setContentView(binding.root)
            setCancelable(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            binding.tvWaitingMsg.text =
                "Waiting for $name To Accept the Connection Request.\n He/She will shortly receive your request."
            binding.btnWaiting.setOnClickListener {
                if (!isFinishing && !isDestroyed) {
                    this.dismiss()
                }
            }
        }
        waitingDialog?.show()

        android.os.Handler().postDelayed({
            if (!isFinishing && !isDestroyed) {
                waitingDialog?.cancel()
                waitingDialog = null
            }
        }, ConstantsOKRA.CALL_TIME_OUT)

    }

    fun Context.isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return manager.getRunningServices(Int.MAX_VALUE)
            .any { it.service.className == serviceClass.name }
    }

    override fun onResume() {
        super.onResume()
        if (mService != null) {
            when (getState()) {
                State.CONNECTED -> {
                    onStateConnected()
                }
                State.UNKNOWN -> {
                    onStateUnKnow()
                }
                State.SEARCHING -> {
                    onStateSearching()
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()

        // Set the media volume to max.
        volumeControlStream = AudioManager.STREAM_MUSIC
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        mOriginalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        callerName=intent.getStringExtra(ConstantsOKRA.CALLER_NAME)
        if(callerName!=null){
            binding.tvConnectedto.text="Connected to $callerName"
        }
        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0
        )



        Intent(this, MyServiceOKRA::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }


        binding.btnRefresh.setOnClickListener(View.OnClickListener {
            if (MyPermissionsOKRA.hasLocationPermission(this@MainActivityOKRA)) {
                if (MyPermissionsOKRA.isGpsOn(this@MainActivityOKRA)) {
                    if (WifiUtilsOKRA.isWifiEnabled(this@MainActivityOKRA)) {
                        if (BluetoothUtilsOKRA.isBluetoothOn(this@MainActivityOKRA)) {
                            mService?.setState(State.SEARCHING)
                            prepareSearchingDevicesView()
                            list.clear()
                            adapter?.notifyDataSetChanged()
                        } else {
                            BluetoothUtilsOKRA.turnBluetoothOn(this@MainActivityOKRA)
                        }
                    } else {
                        MyDialogueOKRA.requestTurnWifiOn(this@MainActivityOKRA, object :
                            MyDialogueOKRA.WifiStateChangeRequest {
                            override fun requestApproved() {
                                WifiUtilsOKRA.changeWifiState(this@MainActivityOKRA, true)
                            }

                            override fun requestDenied() {
                                //do nothing
                            }

                        })
                    }
                } else {
                    MyPermissionsOKRA.turnGpsOn(this@MainActivityOKRA)
                }

            } else {
                MyPermissionsOKRA.showLocationExplanation(this@MainActivityOKRA,
                    object : MyPermissionsOKRA.ExplanationCallBack {
                        override fun requestPermission() {
                            MyPermissionsOKRA.requestLocationPermission(this@MainActivityOKRA)
                        }

                        override fun denyPermission() {
                            //do nothing
                        }

                    })
            }
        })



        binding.btnPowerState.setOnClickListener {

                if (MyPermissionsOKRA.hasLocationPermission(this@MainActivityOKRA)) {
                    if (MyPermissionsOKRA.isGpsOn(this@MainActivityOKRA)) {
                        if (WifiUtilsOKRA.isWifiEnabled(this@MainActivityOKRA)) {
                            if (BluetoothUtilsOKRA.isBluetoothOn(this@MainActivityOKRA)) {
                                mService?.setState(State.SEARCHING)
                                prepareSearchingDevicesView()
                                list.clear()
                                adapter?.notifyDataSetChanged()
                            } else {
                                BluetoothUtilsOKRA.turnBluetoothOn(this@MainActivityOKRA)
                            }
                        } else {
                            MyDialogueOKRA.requestTurnWifiOn(this@MainActivityOKRA, object :
                                MyDialogueOKRA.WifiStateChangeRequest {
                                override fun requestApproved() {
                                    WifiUtilsOKRA.changeWifiState(this@MainActivityOKRA, true)
                                }

                                override fun requestDenied() {
                                    //do nothing
                                }

                            })
                        }
                    } else {
                        MyPermissionsOKRA.turnGpsOn(this@MainActivityOKRA)
                    }

                } else {
                    MyPermissionsOKRA.showLocationExplanation(this@MainActivityOKRA,
                        object : MyPermissionsOKRA.ExplanationCallBack {
                            override fun requestPermission() {
                                MyPermissionsOKRA.requestLocationPermission(this@MainActivityOKRA)
                            }

                            override fun denyPermission() {
                                //do nothing
                            }

                        })
                }

        }

//        binding.seekbar11.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                if (progress == 1) {
//                    if (getState() == State.CONNECTED) {
//                        mService?.stopRecording()
//                        binding.textView7.setText(resources.getText(R.string.dragtospeak))
//                    }
//                } else if (progress == 30) {
//                    if (getState() == State.CONNECTED) {
//                        binding.textView7.setText(resources.getText(R.string.leavetosend))
//                            // start your timer
//                            if (MyPermissionsOKRA.hasRecordingPermission(this@MainActivityOKRA)) {
//
//                                mService?.startRecording()
//                            } else {
//                                MyPermissionsOKRA.showRecordingExplanation(this@MainActivityOKRA,
//                                    object : MyPermissionsOKRA.ExplanationCallBack {
//                                        override fun requestPermission() {
//                                            MyPermissionsOKRA.requestRecordingPermission(this@MainActivityOKRA)
//                                        }
//
//                                        override fun denyPermission() {
//                                            //do nothing
//                                        }
//
//                                    })
//
//                        }
//                    }else{
//                        showNotConnectedDialog()
//                    }
//                }
//                if(runnable!=null) {
//                    handler?.removeCallbacks(runnable!!)
//                }
//                //                if(value==0){
////                    seekBar.setProgress(1);
////                }
//                if (progress == 30) {
//                    binding.lottieAnimation.setVisibility(View.VISIBLE)
//                } else {
//                    binding.lottieAnimation.setVisibility(View.INVISIBLE)
//                }
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) {}
//            override fun onStopTrackingTouch(seekBar: SeekBar) {
//                if (getState() == State.CONNECTED) {
//                    value = seekBar.progress
//                    //                  if(value==0){
////                      seekBar.setProgress(1);
////                  }
//                    runnable = Runnable {
//                        if (value > 0) {
//                            value--
//                            seekBar.progress = value
//                        }
//                        handler?.postDelayed(runnable!!, 1)
//                    }
//                    handler?.postDelayed(runnable!!, 1)
//                }
//            }
//        })
        binding.btnCutcall.setOnClickListener(View.OnClickListener {
            if (getState() == State.CONNECTED) {
                mService?.setState(State.UNKNOWN)
//                binding.tvCallState.text = resources.getString(R.string.makecall)
//                binding.imageView7.setImageDrawable(getDrawable(R.drawable.ic_make_a_call))
//                binding.btnPowerState.setCardBackgroundColor(resources.getColor(R.color.Red1))
//                binding.btnHoldToTalk.visibility = View.INVISIBLE

                binding.animationListening.visibility=View.GONE
                binding.textView8.visibility=View.VISIBLE
                binding.viewMain.setBackgroundResource(R.drawable.walkietalkie_bg)
                binding.constrintcutcall.visibility=View.GONE
                binding.btnPowerState.visibility=View.VISIBLE

                MyDialogueOKRA.alertDisconnected(this@MainActivityOKRA)
            }
        })

        binding.btnTalk.setOnTouchListener(View.OnTouchListener { v, event ->
            if (getState() == State.CONNECTED) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // start your timer
                    if (MyPermissionsOKRA.hasRecordingPermission(this@MainActivityOKRA)) {
//                        binding.btnHoldToTalk.background =
//                            getDrawable(R.drawable.ic_orange_button_selected)
                        binding.lottieAnimation.setVisibility(View.VISIBLE)
                        mService?.startRecording()
                    } else {
                        MyPermissionsOKRA.showRecordingExplanation(this@MainActivityOKRA,
                            object : MyPermissionsOKRA.ExplanationCallBack {
                                override fun requestPermission() {
                                    MyPermissionsOKRA.requestRecordingPermission(this@MainActivityOKRA)
                                }

                                override fun denyPermission() {
                                    //do nothing
                                }

                            })
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // stop your timer.
//                    binding.btnHoldToTalk.background = getDrawable(R.drawable.ic_orange_button)
                    binding.lottieAnimation.setVisibility(View.INVISIBLE)
                    mService?.stopRecording()
                }
            }else{
                if(dialogOnce) {
                   dialogOnce=false
                    showNotConnectedDialog()
                }
            }

            return@OnTouchListener false
        })
    }

    private fun showNotConnectedDialog() {

        val alertDialog = AlertDialog.Builder(this@MainActivityOKRA)
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val dialogView: View = LayoutInflater.from(this@MainActivityOKRA).inflate(R.layout.dialog_call_not_connected_okra, viewGroup, false)
        alertDialog.setView(dialogView)
        val dialog2 = alertDialog.create()
        dialog2.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog2.show()

        var btn =dialogView.findViewById<Button>(R.id.btnOk)

        btn.setOnClickListener(View.OnClickListener {
            dialogOnce=true
            dialog2.dismiss()
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissions.isNotEmpty() && grantResults.isNotEmpty()) {
            if (requestCode == ConstantsOKRA.RAP) {
                if (MyPermissionsOKRA.hasRecordingPermission(this@MainActivityOKRA)) {
                    /*binding.btnHoldToTalk.setBackgroundDrawable(getDrawable(R.drawable.green_circle))
                    startRecording()*/
                } else {
                    if (Build.VERSION.SDK_INT >= 26) {
                        val showRational = shouldShowRequestPermissionRationale(permissions[0])
                        if (!showRational) {
                            MyPermissionsOKRA.showRecordingRational(this@MainActivityOKRA,
                                object : MyPermissionsOKRA.RationalCallback {
                                    override fun openSettings() {
                                        val intent =
                                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        val uri: Uri = Uri.fromParts("package", packageName, null)
                                        intent.data = uri
                                        startActivityForResult(
                                            intent,
                                            ConstantsOKRA.RAP
                                        )
                                    }

                                    override fun dismissed() {
                                        //dp nothing
                                    }

                                })
                        }
                    }
                }
            } else if (requestCode == ConstantsOKRA.LOCATION_PERMISSION_REQUEST_CODE) {
                if (MyPermissionsOKRA.hasLocationPermission(this@MainActivityOKRA)) {
                    if (MyPermissionsOKRA.isGpsOn(this@MainActivityOKRA)) {
                        if (WifiUtilsOKRA.isWifiEnabled(this@MainActivityOKRA)) {
                            if (BluetoothUtilsOKRA.isBluetoothOn(this@MainActivityOKRA)) {
                                mService?.setState(State.SEARCHING)
                                prepareSearchingDevicesView()
                                list.clear()
                                adapter?.notifyDataSetChanged()
                            } else {
                                BluetoothUtilsOKRA.turnBluetoothOn(this@MainActivityOKRA)
                            }
                        } else {
                            MyDialogueOKRA.requestTurnWifiOn(this@MainActivityOKRA, object :
                                MyDialogueOKRA.WifiStateChangeRequest {
                                override fun requestApproved() {
                                    WifiUtilsOKRA.changeWifiState(this@MainActivityOKRA, true)
                                }

                                override fun requestDenied() {
                                    //do nothing
                                }

                            })
                        }
                    } else {
                        MyPermissionsOKRA.turnGpsOn(this@MainActivityOKRA)
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= 26) {
                        val showRational = shouldShowRequestPermissionRationale(permissions[0])
                        if (!showRational) {
                            MyPermissionsOKRA.showLocationRational(this@MainActivityOKRA,
                                object : MyPermissionsOKRA.RationalCallback {
                                    override fun openSettings() {
                                        val intent =
                                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        val uri: Uri = Uri.fromParts("package", packageName, null)
                                        intent.data = uri
                                        startActivityForResult(
                                            intent,
                                            ConstantsOKRA.LOCATION_PERMISSION_REQUEST_CODE
                                        )
                                    }

                                    override fun dismissed() {
                                        //dp nothing
                                    }

                                })
                        }
                    }
                }
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ConstantsOKRA.LOCATION_PERMISSION_REQUEST_CODE) {
            if (MyPermissionsOKRA.hasLocationPermission(this@MainActivityOKRA)) {
                if (MyPermissionsOKRA.isGpsOn(this@MainActivityOKRA)) {
                    if (WifiUtilsOKRA.isWifiEnabled(this@MainActivityOKRA)) {
                        if (BluetoothUtilsOKRA.isBluetoothOn(this@MainActivityOKRA)) {
                            mService?.setState(State.SEARCHING)
                            prepareSearchingDevicesView()
                            list.clear()
                            adapter?.notifyDataSetChanged()
                        } else {
                            BluetoothUtilsOKRA.turnBluetoothOn(this@MainActivityOKRA)
                        }
                    } else {
                        MyDialogueOKRA.requestTurnWifiOn(this@MainActivityOKRA, object :
                            MyDialogueOKRA.WifiStateChangeRequest {
                            override fun requestApproved() {
                                WifiUtilsOKRA.changeWifiState(this@MainActivityOKRA, true)
                            }

                            override fun requestDenied() {
                                //do nothing
                            }

                        })
                    }
                } else {
                    MyPermissionsOKRA.turnGpsOn(this@MainActivityOKRA)
                }
            }
        } else if (requestCode == ConstantsOKRA.RAP) {
            /*if(MyPermissionsOKRA.hasRecordingPermission(this@MainActivityOKRA)){
                binding.btnHoldToTalk.setBackgroundDrawable(getDrawable(R.drawable.green_circle))
                startRecording()
            }*/
        } else if (requestCode == ConstantsOKRA.REQUEST_LOCATION_TURNON) {
            if (MyPermissionsOKRA.isGpsOn(this@MainActivityOKRA)) {
                mService?.setState(State.SEARCHING)
                prepareSearchingDevicesView()
                list.clear()
                adapter?.notifyDataSetChanged()
            }
        } else if (requestCode == ConstantsOKRA.REQUEST_BLUETOOTH_TURN_ON) {
            if (BluetoothUtilsOKRA.isBluetoothOn(this@MainActivityOKRA) && MyPermissionsOKRA.isGpsOn(
                    this@MainActivityOKRA
                ) && WifiUtilsOKRA.isWifiEnabled(
                    this@MainActivityOKRA
                )
            ) {
                mService?.setState(State.SEARCHING)
                prepareSearchingDevicesView()
                list.clear()
                adapter?.notifyDataSetChanged()
            }
        } else if (requestCode == ConstantsOKRA.REQUEST_WIFI_TURN_ON) {
            if (BluetoothUtilsOKRA.isBluetoothOn(this@MainActivityOKRA) && MyPermissionsOKRA.isGpsOn(
                    this@MainActivityOKRA
                ) && WifiUtilsOKRA.isWifiEnabled(
                    this@MainActivityOKRA
                )
            ) {
                mService?.setState(State.SEARCHING)
                prepareSearchingDevicesView()
                list.clear()
                adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onStop() {

        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mOriginalVolume, 0)
        volumeControlStream = AudioManager.USE_DEFAULT_STREAM_TYPE

        stopMyService()
        super.onStop()
    }

    private fun stopMyService() {
        mService?.setServiceCallBacks(null)
        if (mBound) {
            unbindService(connection)
        }
        mBound = false

        mService?.onActivityStopped()
    }

    override fun onDestroy() {
        super.onDestroy()
        // After our Activity stops, we disconnect from Nearby Connections.
        mService?.setState(State.UNKNOWN)
        if (!SharePrefHelperOKRA(this@MainActivityOKRA).canReceiveCallsInBackground()) {
            stopService(Intent(this@MainActivityOKRA, MyServiceOKRA::class.java))
        }
    }

    override fun onBackPressed() {
        if (getState() == State.CONNECTED) {
            mService?.setState(State.SEARCHING)
            binding.animationListening.visibility=View.GONE
            binding.textView8.visibility=View.VISIBLE
            binding.viewMain.setBackgroundResource(R.drawable.walkietalkie_bg)
            binding.constrintcutcall.visibility=View.GONE
            binding.btnPowerState.visibility=View.VISIBLE
            MyDialogueOKRA.alertDisconnected(this@MainActivityOKRA)
            return
        } else {
            super.onBackPressed()
//            InterAdHelperOKRA.showAdmobIntersitial(this@MainActivityOKRA)
            // exitorcontinue()
        }
    }

    fun hideDisconnectedText() {
//        binding.tvDisconnected.visibility = View.GONE
//        binding.ivUser.visibility = View.VISIBLE
//        binding.ivPeer.visibility = View.VISIBLE
//        binding.tvName.visibility = View.VISIBLE
//        binding.tvPeer.visibility = View.VISIBLE
//        binding.tvComunicationState.visibility = View.VISIBLE
//        binding.tvConnectionState.visibility = View.VISIBLE
//        binding.ivLight.setImageDrawable(getDrawable(R.drawable.green_circle_OKRA))

//        binding.tvCallState.text = "End Call"
//        binding.imageView7.setImageDrawable(getDrawable(R.drawable.ic_disconnect_a_call))
//        binding.btnPowerState.setCardBackgroundColor(resources.getColor(R.color.disconnectcall))
    }

    fun showDisConnectedText() {
//        binding.tvDisconnected.visibility = View.VISIBLE
//        binding.ivUser.visibility = View.GONE
//        binding.ivPeer.visibility = View.GONE
//        binding.tvName.visibility = View.GONE
//        binding.tvPeer.visibility = View.GONE
//        binding.tvComunicationState.visibility = View.GONE
//        binding.tvConnectionState.visibility = View.GONE
//        binding.ivLight.setImageDrawable(getDrawable(R.drawable.red_circle))
//        binding.tvCallState.text = "Call"
//        binding.btnPowerState.setImageDrawable(getDrawable(R.drawable.ic_call))

//        binding.tvCallState.text = resources.getString(R.string.makecall)
//        binding.imageView7.setImageDrawable(getDrawable(R.drawable.ic_make_a_call))
//        binding.btnPowerState.setCardBackgroundColor(resources.getColor(R.color.Red1))
    }


    private var list = ArrayList<ConnectionsServiceOKRA.Endpoint>()

    override fun onEndpointDiscovered(endpoint: ConnectionsServiceOKRA.Endpoint?) {
        // We found an advertiser!
        if (!binding.viewMain.isVisible) {

            prepareFoundDevicesView()
        }
        endpoint?.let {

            for (index in list.indices) {
                if (list[index].name == endpoint.name) {
                    list[index] = endpoint
                    adapter?.notifyDataSetChanged()
                    return
                }
            }
            list.add(it)
            adapter?.notifyItemRangeChanged(0, list.size)
            adapter?.notifyDataSetChanged()
        }

        //stopDiscovering()
        //connectToEndpoint(endpoint)
    }

    private fun prepareFoundDevicesView() {
        binding.rvListEndpoints.visibility = View.VISIBLE
        binding.animationView.visibility = View.GONE
        binding.tvAlert.visibility = View.GONE
        binding.tvPleasewait.visibility = View.GONE
        binding.tvTitle.visibility = View.GONE
        binding.tvMsg.visibility = View.VISIBLE
        binding.adFrame.visibility = View.GONE

        binding.userIcon.visibility=View.VISIBLE
        binding.toolbarTitleconnections.visibility=View.VISIBLE
        binding.btnRefresh.visibility=View.VISIBLE
        binding.toolbarTitle.visibility=View.GONE
    }

    private fun prepareSearchingDevicesView() {
        binding.viewMain.visibility = View.GONE
        binding.topBanner.visibility=View.GONE
        advisibility=false
        binding.viewSearching.visibility = View.VISIBLE

        binding.rvListEndpoints.visibility = View.GONE
        binding.animationView.visibility = View.VISIBLE
        binding.tvAlert.visibility = View.VISIBLE
        binding.tvPleasewait.visibility = View.VISIBLE
        binding.tvTitle.visibility = View.GONE
        binding.tvMsg.visibility = View.GONE

        binding.userIcon.visibility=View.GONE


    }

    override fun onConnectionInitiated(
        endpoint: ConnectionsServiceOKRA.Endpoint?,
        connectionInfo: ConnectionInfo
    ) {
        // A connection to another device has been initiated! We'll use the auth token, which is the
        // same on both devices, to pick a color to use when we're connected. This way, users can
        // visually see which device they connected with.
        // mConnectedColor = COLORS[connectionInfo.getAuthenticationToken().hashCode() % COLORS.size]

        // We accept the connection immediately.

        if (!makingConnectionRequest) {
            MyDialogueOKRA.connectRequest(
                this@MainActivityOKRA,
                endpoint?.name,
                object : MyDialogueOKRA.ConnectionRequest {
                    override fun connectToEndPoint() {
                        acceptCall(endpoint)
                    }

                    override fun rejectConnection() {
                        MyServiceOKRA?.userRejectedConnection()
                    }

                })
        } else {
            mService?.acceptConnection(endpoint)
//            binding.tvName.text = SharePrefHelperHS.name
//            binding.tvPeer.text = endpoint?.name
        }


    }

    private fun acceptCall(endpoint: ConnectionsServiceOKRA.Endpoint?) {
        mService?.acceptConnection(endpoint)
//        binding.tvName.text = SharePrefHelperHS.name
//        binding.tvPeer.text = endpoint?.name
//        binding.tvCallState.text = "End Call"
//        binding.btnPowerState.setImageDrawable(getDrawable(R.drawable.ic_end_call))

//        binding.tvCallState.text = "End Call"
//        binding.imageView7.setImageDrawable(getDrawable(R.drawable.ic_disconnect_a_call))
//        binding.btnPowerState.setCardBackgroundColor(resources.getColor(R.color.disconnectcall))
//
//        binding.textView8.visibility=View.INVISIBLE
//        binding.textView7.setText(resources.getText(R.string.dragtospeak))

        binding.tvConnectedto.text="Connected to "+endpoint?.name

        binding.textView8.visibility=View.INVISIBLE
        binding.animationListening.visibility=View.INVISIBLE
        binding.viewMain.setBackgroundResource(R.drawable.callscreen)
        binding.constrintcutcall.visibility=View.VISIBLE
        binding.btnPowerState.visibility=View.INVISIBLE

        mService?.stopDiscovering()
    }

    override fun onEndpointConnected(endpoint: ConnectionsServiceOKRA.Endpoint) {
        /*   Toast.makeText(
               this, getString(R.string.toast_connected, endpoint.getName()), Toast.LENGTH_SHORT
           )
               .show()*/
        makingConnectionRequest = false
        runOnUiThread {
            if (!isFinishing && !isDestroyed && waitingDialog?.isShowing == true) {
                waitingDialog?.dismiss()
                waitingDialog = null
            }
//            binding.btnHoldToTalk.visibility = View.VISIBLE
//            binding.btnHoldToTalk.isEnabled = true

            binding.toolbarTitleconnections.visibility=View.GONE
            binding.btnRefresh.visibility=View.GONE
            binding.toolbarTitle.visibility=View.VISIBLE

            binding.viewMain.visibility = View.VISIBLE
            advisibility=true
            binding.viewSearching.visibility = View.GONE
            binding.topBanner.visibility=View.VISIBLE
            binding.textView8.visibility=View.INVISIBLE
            binding.animationListening.visibility=View.INVISIBLE
            binding.viewMain.setBackgroundResource(R.drawable.callscreen)
            binding.constrintcutcall.visibility=View.VISIBLE
            binding.btnPowerState.visibility=View.INVISIBLE
//            binding.textView8.visibility=View.INVISIBLE
//            binding.textView7.setText(resources.getText(R.string.dragtospeak))
            InterAdHelperOKRA.showMediatedAdmobIntersitial(this,null)
        }

    }

    override fun onEndpointDisconnected(endpoint: ConnectionsServiceOKRA.Endpoint) {
        /*Toast.makeText(
            this, getString(R.string.toast_disconnected, endpoint.getName()), Toast.LENGTH_SHORT
        )
            .show()*/
        makingConnectionRequest = false
        runOnUiThread {
//            binding.btnHoldToTalk.visibility = View.INVISIBLE
//            binding.btnHoldToTalk.isEnabled = false

            binding.animationListening.visibility=View.GONE
            binding.textView8.visibility=View.VISIBLE
            binding.viewMain.setBackgroundResource(R.drawable.walkietalkie_bg)
            binding.constrintcutcall.visibility=View.GONE
            binding.btnPowerState.visibility=View.VISIBLE

            MyDialogueOKRA.alertDisconnected(this@MainActivityOKRA)
        }
    }

    override fun onConnectionFailed(endpoint: ConnectionsServiceOKRA.Endpoint?) {
        // Let's try someone else.
        if (getState() == State.SEARCHING) {
            mService?.startDiscovering()
        }
        if (waitingDialog?.isShowing == true) {
            waitingDialog?.dismiss()
            if (makingConnectionRequest) {
                showConnectionFailedDialogue()
            }
            makingConnectionRequest = false
        } else {
            if (makingConnectionRequest) {
                showConnectionFailedDialogue()
            }
            makingConnectionRequest = false
        }
    }

    private fun showConnectionFailedDialogue() {
        val connectionFailed = Dialog(this@MainActivityOKRA).apply {
            val binding =
                DbWaitingForRequestResponceOkraBinding.inflate(LayoutInflater.from(this@MainActivityOKRA))
            setContentView(binding.root)
            setCancelable(true)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            binding.tvWaitingMsg.text =
                "Connection failed. Please rescan nearby devices"
            binding.btnWaiting.text = "rescan"
            binding.btnWaiting.visibility = View.VISIBLE
            binding.btnCancel.visibility = View.VISIBLE
            binding.btnWaiting.setOnClickListener {
                if (!isFinishing && !isDestroyed) {
                    this.dismiss()
                    stopMyService()
                    mService?.setState(State.UNKNOWN)
                    if (!SharePrefHelperOKRA(this@MainActivityOKRA).canReceiveCallsInBackground()) {
                        stopService(Intent(this@MainActivityOKRA, MyServiceOKRA::class.java))
                    }
                    val intent = Intent(this@MainActivityOKRA, ActivityHomeOKRA::class.java).apply {
                        this.putExtra("restart", true)
                    }
                    startActivity(intent)
                    finishAffinity()

                    /* android.os.Handler().postDelayed({
                         val intent=Intent(this@MainActivityOKRA,MainActivityOKRA::class.java).apply {
                             this.putExtra("restart",true)
                         }
                         startActivity(intent)
                         finishAffinity()
                        *//* Intent(this@MainActivityOKRA, MyServiceOKRA::class.java).also { intent ->
                            bindService(intent, connection, Context.BIND_AUTO_CREATE)
                        }*//*
                    },3000)*/
                }
            }
            binding.btnCancel.setOnClickListener {
                if (!isFinishing && !isDestroyed) {
                    this.dismiss()
                }
            }
        }
        connectionFailed?.show()
    }


    private fun getState(): State? {
        return mService?.getState()
    }

    override fun onStateSearching() {
        runOnUiThread {
            if (list.size == 0) {
                binding.adFrame.visibility = View.VISIBLE
            } else {
                binding.adFrame.visibility = View.GONE
            }
//
            binding.tvTitle.visibility = View.GONE
            binding.tvMsg.visibility = View.GONE
//            binding.tvConnectionState.text = getString(R.string.status_disconnected)
            showDisConnectedText()
//            binding.btnHoldToTalk.isEnabled = false
        }
    }


    override fun onStateUnKnow() {
        runOnUiThread {
            showDisConnectedText()
//            binding.btnHoldToTalk.isEnabled = false
//            binding.tvName.text = ""
//            binding.tvPeer.text = ""
//            binding.tvCallState.text = "Call"
//            binding.btnHoldToTalk.visibility = View.INVISIBLE
//            binding.btnPowerState.setImageDrawable(getDrawable(R.drawable.ic_call))

//            binding.tvCallState.text = resources.getString(R.string.makecall)
//            binding.imageView7.setImageDrawable(getDrawable(R.drawable.ic_make_a_call))
//            binding.btnPowerState.setCardBackgroundColor(resources.getColor(R.color.Red1))
//
//            binding.textView8.visibility=View.VISIBLE
//            binding.textView7.setText(resources.getText(R.string.callmsg))

            binding.textView8.visibility=View.VISIBLE
            binding.animationListening.visibility=View.GONE
            binding.viewMain.setBackgroundResource(R.drawable.walkietalkie_bg)
            binding.constrintcutcall.visibility=View.GONE
            binding.btnPowerState.visibility=View.VISIBLE

        }
    }

    override fun onStateConnected() {
        runOnUiThread {
            hideDisconnectedText()
//            binding.btnHoldToTalk.isEnabled = true
//            binding.tvConnectionState.text = getString(R.string.status_connected)
            binding.adFrame.visibility = View.GONE

        }
    }


    /** {@see ConnectionsActivity#onReceive(Endpoint, Payload)}  */
    override fun onReceive(endpoint: ConnectionsServiceOKRA.Endpoint?, payload: Payload) {

        runOnUiThread {
//            binding.tvComunicationState.text = "Listening"
//            binding.animSpeakingListening.setAnimation(R.raw.listening_animationfile)
//            binding.btnPowerState.visibility = View.INVISIBLE
//            binding.tvCallState.visibility = View.INVISIBLE
//            binding.animSpeakingListening.visibility = View.VISIBLE
//            binding.animSpeakingListening.playAnimation()
//            binding.btnHoldToTalk.isEnabled = false
//            binding.textView7.visibility=View.INVISIBLE
            binding.animationListening.visibility=View.VISIBLE
            binding.btnTalk.isEnabled=false


        }
    }

    override fun onFinishedPlaying() {
        runOnUiThread {
//            binding.tvComunicationState.setText("Silent")
//            binding.btnHoldToTalk.isEnabled = true
//            binding.btnPowerState.visibility = View.VISIBLE
//            binding.tvCallState.visibility = View.VISIBLE
//            binding.animSpeakingListening.visibility = View.GONE
//            binding.textView7.visibility=View.VISIBLE
            binding.animationListening.visibility=View.INVISIBLE
            binding.btnTalk.isEnabled=true
        }
    }

    /** Stops all currently streaming audio tracks.  */


    override fun onStopPlaying() {
        runOnUiThread {
//            binding.tvComunicationState.text = "Silent"
//            binding.btnHoldToTalk.isEnabled = true
//            binding.btnPowerState.visibility = View.VISIBLE
//            binding.tvCallState.visibility = View.VISIBLE
//            binding.animSpeakingListening.visibility = View.GONE
//            binding.textView7.visibility=View.VISIBLE
            binding.animationListening.visibility=View.INVISIBLE
            binding.btnTalk.isEnabled=true
        }
    }


    override fun onStartRecording() {
        runOnUiThread {
//            binding.tvComunicationState.text = "Speaking"
//            binding.animSpeakingListening.setAnimation(R.raw.speaking_animationfile)
//            binding.btnPowerState.visibility = View.INVISIBLE
//            binding.tvCallState.visibility = View.INVISIBLE
//            binding.animSpeakingListening.visibility = View.VISIBLE
//            binding.animSpeakingListening.playAnimation()
//            binding.textView7.visibility=View.INVISIBLE
//            binding.animationListening.visibility=View.VISIBLE

        }
    }


    override fun onStopRecording() {
        runOnUiThread {
//            binding.tvComunicationState.text = "Silent"
//            binding.btnPowerState.visibility = View.VISIBLE
//            binding.tvCallState.visibility = View.VISIBLE
//            binding.animSpeakingListening.visibility = View.GONE
//            binding.textView7.visibility=View.VISIBLE
//            binding.animationListening.visibility=View.GONE
        }
    }

    override fun getName(): String? {
        return mName
    }

    override fun getServiceId(): String? {
        return SERVICE_ID
    }

    override fun getStrategy(): Strategy? {
        return STRATEGY
    }


    fun exitorcontinue() {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.db_exitdialogue_okra)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val positive: Button = dialog.findViewById(R.id.btn_yes)
        positive.setOnClickListener {
            dialog.dismiss()
            mService?.setState(State.UNKNOWN)
            finishAffinity()
        }
        val negative: Button = dialog.findViewById(R.id.btn_exit)
        negative.setOnClickListener {
            dialog.dismiss()
        }
        val rateUs: Button = dialog.findViewById(R.id.btn_rate_us)
        rateUs.setOnClickListener {
            rateApp()
            dialog.dismiss()
        }
        dialog.setOnCancelListener {
            //dialogueClickListner.negativeHotSpotTurnOFF()
            dialog.dismiss()
        }
//        NativeAdHelperOKRA.showAdmobNativeAd(
//            this@MainActivityOKRA,
//            dialog.findViewById(R.id.ad_frame)
//        )
        dialog.show()
    }

    private fun rateIntentForUrl(url: String): Intent? {
        var intent: Intent? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    url + Objects.requireNonNull(this@MainActivityOKRA).packageName
                )
            )
        }
        var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        flags = if (Build.VERSION.SDK_INT >= 21) {
            flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        } else {
            flags or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
        }
        intent!!.addFlags(flags)
        return intent
    }

    private fun rateApp() {
        try {
            val rateIntent = rateIntentForUrl("market://details?id=")
            startActivity(rateIntent)
        } catch (e: ActivityNotFoundException) {
            val rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details?id=")
            startActivity(rateIntent)
        }
    }


    /** States that the UI goes through.  */
    enum class State {
        UNKNOWN, SEARCHING, CONNECTED
    }

//    fun loadFbBannerAd() {
//
//        val adView = AdView(
//            this@MainActivityOKRA,
//            this@MainActivityOKRA.resources.getString(R.string.fb_banner_ad),
//            AdSize.BANNER_HEIGHT_50
//        )
//
//        val adListener: com.facebook.ads.AdListener = object : com.facebook.ads.AdListener {
//
//            override fun onError(ad: Ad, adError: AdError) {
//                Log.d(TAG, "onError: ")
//            }
//
//            override fun onAdLoaded(ad: Ad) {
//                // Ad loaded callback
//                Log.d(TAG, "onAdLoaded: ")
//            }
//
//            override fun onAdClicked(ad: Ad) {
//                Log.d(TAG, "onAdClicked: ")
//            }
//
//            override fun onLoggingImpression(ad: Ad) {
//                // Ad impression logged callback
//                Log.d(TAG, "onLoggingImpression: ")
//            }
//        }
//
//
//        adView?.loadAd(adView?.buildLoadAdConfig()?.withAdListener(adListener)?.build())
//        binding.topBanner.addView(adView)
//    }
//
//fun loadAdmobBannerAd() {
//
//    val mAdView = com.google.android.gms.ads.AdView(this@MainActivityOKRA)
//    val adSize: com.google.android.gms.ads.AdSize =
//        InterAdHelperOKRA.getAdSize(this@MainActivityOKRA)
//    mAdView.adSize = com.google.android.gms.ads.AdSize.BANNER//adSize
//
//    mAdView.adUnitId = resources.getString(R.string.admob_banner)
//
//    val adRequest = AdRequest.Builder().build()
//
//
//    binding.topBanner.addView(mAdView)
//
//    mAdView.loadAd(adRequest)
//
//    mAdView.adListener = object : AdListener() {
//        override fun onAdClosed() {
//            super.onAdClosed()
//            //Toast.makeText(this@ChooseMobile,"",Toast.LENGTH_SHORT).show()
//        }
//
//        override fun onAdFailedToLoad(p0: LoadAdError?) {
//            super.onAdFailedToLoad(p0)
//            binding.topBanner.visibility = View.INVISIBLE
//            //  Toast.makeText(this@ChooseMobile,"msg : ${p0?.message} code : ${p0?.code}",Toast.LENGTH_SHORT).show()
//        }
//
//        override fun onAdOpened() {
//            super.onAdOpened()
//            //  Toast.makeText(this@ChooseMobile,"",Toast.LENGTH_SHORT).show()
//        }
//
//        override fun onAdLoaded() {
//            super.onAdLoaded()
//            binding.topBanner.visibility = View.VISIBLE
//            //   Toast.makeText(this@ChooseMobile,"",Toast.LENGTH_SHORT).show()
//        }
//
//        override fun onAdClicked() {
//            super.onAdClicked()
//            //   Toast.makeText(this@ChooseMobile,"",Toast.LENGTH_SHORT).show()
//        }
//
//        override fun onAdImpression() {
//            super.onAdImpression()
//            //   Toast.makeText(this@ChooseMobile,"",Toast.LENGTH_SHORT).show()
//        }
//    }
//
//}
}