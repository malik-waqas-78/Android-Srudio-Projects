package com.smartswitch.phoneclone.utills

import android.Manifest
import android.app.Activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager

import android.util.Log

import androidx.core.app.ActivityCompat

import com.smartswitch.phoneclone.constants.CAPPMConstants
import com.smartswitch.phoneclone.constants.CAPPMConstants.get.TAG
import com.smartswitch.phoneclone.interfaces.CAPPDirectCallBack

import com.smartswitch.phoneclone.interfaces.CAPPWifiP2PCallBacks
import android.os.Handler;
import android.system.ErrnoException
import com.smartswitch.phoneclone.activities.CAPPActivityReceive
import com.smartswitch.phoneclone.activities.CAPPActivitySendData
import java.io.*
import java.lang.Exception
import java.net.*


class CAPPWifiPeer2PConnectionUtils(var context: Context) {
    private var wifiP2pManager: WifiP2pManager? = null
    val wifiP2PActionListener = context as CAPPWifiP2PCallBacks
    var wifip2pinformation: WifiP2pInfo? = null
    private var connectionInfoAvailable = false
    private var channel: WifiP2pManager.Channel? = null
    private var broadcastReceiver: BroadcastReceiver? = null
    private var groupOwner=false;

    var groupcreator = false
    var launchedSendData = false;
    var launchedReceiveData = false
    var acceptingConnectionRequests = false;

    var keepRunning = false

    companion object {
        var clientSocket: Socket? = null
        var socket: Socket? = null
        var server: ServerSocket? = null
    }

    val HSDirectActionListener: CAPPDirectCallBack = object :
        CAPPDirectCallBack {
        override fun wifiP2pEnabled(enabled: Boolean) {
            // Log.d("wifiP2pEnabled: $enabled")
            Log.d(TAG, "wifiP2pEnabled: $enabled")
        }

        override fun onConnectionInfoAvailable(wifiP2pInfo: WifiP2pInfo) {

            Log.d(TAG, "onConnectionInfoAvailable: " + wifiP2pInfo.isGroupOwner);
            wifip2pinformation = wifiP2pInfo;
            Log.d("92727：", "group formed" + wifiP2pInfo.groupFormed)

            if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
                Log.d(TAG, "onConnectionInfoAvailable: ")

                acceptConnections()
            } else if (wifiP2pInfo.groupFormed) {

                Log.d(
                    TAG,
                    "onConnectionInfoAvailable: ${wifiP2pInfo.groupOwnerAddress.hostAddress}"
                )

                    sendConnectionRequest(wifiP2pInfo.groupOwnerAddress.hostAddress)

            }
        }

        override fun onDisconnection() {
            // connectionInfoAvailable = false
            Log.d(TAG, "onDisconnection: disconnected")
            // log("onDisconnection")
        }

        override fun onSelfDeviceAvailable(wifiP2pDevice: WifiP2pDevice) {
            //  log("onSelfDeviceAvailable")
            // log(wifiP2pDevice.toString())
            wifiP2PActionListener.selfDeviceInfo(wifiP2pDevice)
            Log.d(TAG, "onSelfDeviceAvailable: ${wifiP2pDevice.deviceName}")
        }

        override fun onPeersAvailable(wifiP2pDeviceList: Collection<WifiP2pDevice>) {
            Log.d(TAG, "peers available" + wifiP2pDeviceList.size)
            wifiP2PActionListener.peersAvailable(wifiP2pDeviceList)
        }

        override fun onChannelDisconnected() {
            //  log("onChannelDisconnected")
            Log.e(TAG, "onChannelDisconnected: disconnected")
        }
    }

    init {
        wifiP2pManager = context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?

        /*if (wifiP2pManager == null) {

        }*/
        channel = wifiP2pManager?.initialize(context, context.mainLooper, HSDirectActionListener)
        channel?.also {
            broadcastReceiver =
                CAPPPeer2PBroadcastReceiver(
                    wifiP2pManager,
                    channel,
                    HSDirectActionListener
                )

        }

    }


    fun acceptConnections() {
        keepRunning=true
        if (server != null) {
            return
        }
        Thread(Runnable {
            acceptingConnectionRequests = true
            server = ServerSocket()
            server?.reuseAddress = true
            server?.bind(InetSocketAddress(8080))
            var outputStream: OutputStream
            var inputStream: InputStream
            var input: BufferedReader
            var output: PrintWriter
            try {
                socket = server?.accept()
                outputStream = socket?.getOutputStream()!!
                inputStream = socket!!.getInputStream()
                input = BufferedReader(InputStreamReader(inputStream))
                output = PrintWriter(outputStream!!)

                while (keepRunning) {
                    val msg = input.readLine()
                    if (msg == "connected") {
                        output.println("received")
                        output.flush()
                        connectionInfoAvailable = true
                        groupOwner=true

                        keepRunning = false


                        if(groupcreator){
                            val intent = Intent(context, CAPPActivityReceive::class.java)
                            intent.putExtra("owner",groupOwner)
                            (context as Activity).startActivityForResult(
                                intent,
                                CAPPMConstants.FINISH_ACTIVITY_CODE
                            )
                            /* },100)*/
                            launchedReceiveData = true
                        }else{
                            val intent = Intent(context, CAPPActivitySendData::class.java)
                            intent.putExtra("owner",groupOwner)
                            (context as Activity).startActivityForResult(
                                intent,
                                CAPPMConstants.FINISH_ACTIVITY_CODE
                            )
                        }

                        Log.d(TAG, "onConnectionInfoAvailable: group owner")

                        break
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                // socket?.close()
                //  server?.close()
            }
        }).start()
    }

    fun sendConnectionRequest(ServerAddress: String) {
        keepRunning=true
        if (clientSocket != null) {
            return
        }
        Log.d(TAG, "sendConnectionRequest: $ServerAddress")
        var ip = ServerAddress
        Thread(Runnable {
            clientSocket = Socket()
            clientSocket?.reuseAddress = true
           try{
               clientSocket?.connect(InetSocketAddress(ip, 8080))
           }catch (e:NoRouteToHostException){
               clientSocket =null
               sendConnectionRequest(ServerAddress)
           }catch (e:   ErrnoException){
               clientSocket =null
               sendConnectionRequest(ServerAddress)
               //keepRunning=false
           }catch(e:SocketException){
               clientSocket =null
               sendConnectionRequest(ServerAddress)
           }catch (e: ConnectException){
               clientSocket =null
               sendConnectionRequest(ServerAddress)
           }
            var outputStream: OutputStream
            var inputStream: InputStream
            var input: BufferedReader
            var output: PrintWriter
            while (keepRunning) {
                if (clientSocket?.isConnected == true) {

                    try{
                        outputStream = clientSocket?.getOutputStream()!!
                        inputStream = clientSocket!!.getInputStream()
                        input = BufferedReader(InputStreamReader(inputStream))
                        output = PrintWriter(outputStream!!)
                        try {
                            output.println("connected")
                            output.flush()
                            while (keepRunning) {
                                val msg = input.readLine()
                                if (msg == "received") {
                                    connectionInfoAvailable = true
                                    keepRunning = false
                                    groupOwner=false
                                    Log.d(
                                        TAG,
                                        "onConnectionInfoAvailable: $ServerAddress not group owner"
                                    )
                                    stopPeerDiscovery()

                                    if(groupcreator){
                                        val intent = Intent(context!!, CAPPActivityReceive::class.java)
                                        intent.putExtra("owner",groupOwner)
                                        (context as Activity).startActivityForResult(
                                            intent,
                                            CAPPMConstants.FINISH_ACTIVITY_CODE
                                        )

                                        launchedReceiveData = true
                                    }else{
                                        val intent = Intent(context!!, CAPPActivitySendData::class.java)
                                        intent.putExtra("owner",groupOwner)
                                        (context as Activity).startActivityForResult(
                                            intent,
                                            CAPPMConstants.FINISH_ACTIVITY_CODE
                                        )
                                    }
                                    launchedSendData = true
                                    break
                                }
                            }
                            break;
                        }catch (e: IOException) {
                            e.printStackTrace()
                        } finally {
                            // socket.close()
                        }
                    }catch (e:Exception){

                    }



                }
            }
        }).start()
    }

    fun discoverPeers() {
        //discover peers
        Handler().postDelayed({
            if (!stopDiscovery) {
                stopPeerDiscovery()
                discoverPeers()
            }
        }, 10000)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //  (context as Activity).finish()
        }
        wifiP2pManager!!.discoverPeers(channel, object : WifiP2pManager.ActionListener {

            override fun onSuccess() {
                // Code for when the discovery initiation is successful goes here.
                // No services have actually been discovered yet, so this method
                // can often be left blank. Code for peer discovery goes in the
                // onReceive method, detailed below.
                Log.d(TAG, "onSuccess: Discovery Statrted")

            }

            override fun onFailure(reasonCode: Int) {
                // Code for when the discovery initiation fails goes here.
                // Alert the user that something went wrong.
                Log.d(TAG, "onFailure: discovery peers")
            }
        })
    }


    var stopDiscovery = false;
    fun stopPeerDiscovery() {
        stopDiscovery = true
        wifiP2pManager?.stopPeerDiscovery(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Log.d(TAG, "onSuccess: discovery stoped")
                stopDiscovery = true
            }

            override fun onFailure(reason: Int) {
                Log.d(TAG, "onFailure: failed to stop discovery")
            }

        })
    }

    fun resumed() {
        if(broadcastReceiver==null){
            broadcastReceiver=
                CAPPPeer2PBroadcastReceiver(
                    wifiP2pManager,
                    channel,
                    HSDirectActionListener
                )
        }
        broadcastReceiver?.let {
            if(context!=null){
                context?.registerReceiver(broadcastReceiver, CAPPPeer2PBroadcastReceiver.getIntentFilter())
                keepRunning = true
            }
        }
    }

    fun paused() {
        if(broadcastReceiver==null){
            return
        }
        broadcastReceiver?.let {
            context?.unregisterReceiver(broadcastReceiver)
            keepRunning = false
            broadcastReceiver=null
            //server?.close()
        }
    }

    fun removeGroup() {
        wifiP2pManager!!.removeGroup(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                // log("removeGroup onSuccess")
                Log.d(TAG, "onSuccess: group removed")
                //  showToast("onSuccess")
            }

            override fun onFailure(reason: Int) {
                //    log("removeGroup onFailure")
                Log.d(TAG, "onFailure: failed group")
                //  showToast("onFailure")
            }
        })
    }

    fun createGroup() {
        /*Handler().postDelayed({
            if(!launchedReceiveData){
                createGroup()
            }
        },10000)*/
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        wifiP2pManager!!.createGroup(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Log.d(TAG, "onSuccess: group created")
              //  Toast.makeText(context, "created", Toast.LENGTH_SHORT).show()
                // log("createGroup onSuccess")
                //  dismissLoadingDialog()
                //   showToast("onSuccess")
            }

            override fun onFailure(reason: Int) {
                Log.d(TAG, "onFailure: failed to create group")
               // Toast.makeText(context, "not created $reason", Toast.LENGTH_SHORT).show()
                //  log("createGroup onFailure: $reason")
                //  dismissLoadingDialog()
                //  showToast("onFailure")
                //  createGroup()
            }
        })
    }


    fun connect(mWifiP2pDevice: WifiP2pDevice) {
        val config = WifiP2pConfig()
        if (mWifiP2pDevice?.deviceAddress != null) {
            config.deviceAddress = mWifiP2pDevice.deviceAddress
            config.groupOwnerIntent=0
            config.wps.setup = WpsInfo.PBC
            //  showLoadingDialog("Connecting to " + mWifiP2pDevice.deviceName)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            wifiP2pManager?.connect(channel, config, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    Log.d(CAPPMConstants.TAG, "Connect onSuccess")


                }

                override fun onFailure(reason: Int) {
                    Log.d(CAPPMConstants.TAG, "Failure")
                    //      showToast("Connection failed $reason")
                    //   dismissLoadingDialog()
                }
            })
        }
    }

}