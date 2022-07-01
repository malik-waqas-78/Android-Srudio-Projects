package com.phoneclone.data.adapters

import android.content.Context
import android.net.wifi.p2p.WifiP2pDevice
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.phoneclone.data.R
import com.phoneclone.data.constants.MyConstants.get.TAG

import com.phoneclone.data.interfaces.WifiP2PCallBacks

class AdapterDevicesList(var context: Context, var wifiP2pDeviceList: List<WifiP2pDevice>) :
    RecyclerView.Adapter<AdapterDevicesList.DevicesViewHolder>() {

    val wifiP2PCallBacks:WifiP2PCallBacks=context as WifiP2PCallBacks

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevicesViewHolder {
        Log.d(TAG, "onCreateViewHolder: ")
        return DevicesViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_device,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DevicesViewHolder, position: Int) {
        holder.tv_deviceName.text = wifiP2pDeviceList[position].deviceName
        Log.d(TAG, "onBindViewHolder: ${wifiP2pDeviceList[position].deviceName}")
        /*holder.tv_deviceAddress.text = wifiP2pDeviceList[position].deviceAddress*/
        holder.tv_deviceDetails.text = getDeviceStatus(wifiP2pDeviceList[position].status)
    }

    override fun getItemCount(): Int {
        //  Log.d(TAG, "getItemCount: ${wifiP2pDeviceList.size}")
        return wifiP2pDeviceList.size
    }

    inner class DevicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_deviceName = itemView.findViewById<TextView?>(R.id.tv_deviceName)
       /* val tv_deviceAddress = itemView.findViewById<TextView?>(R.id.tv_deviceAddress)*/
        val tv_deviceDetails = itemView.findViewById<TextView?>(R.id.tv_deviceDetails)

        init {
            itemView.setOnClickListener {
                //inform activity that itemView is clicked
                wifiP2PCallBacks.deviceClicked(adapterPosition)
            }
        }
    }

    fun getDeviceStatus(deviceStatus: Int): String? {
        return when (deviceStatus) {
            WifiP2pDevice.AVAILABLE -> "Usable"
            WifiP2pDevice.INVITED -> "Inviting"
            WifiP2pDevice.CONNECTED -> "Connected"
            WifiP2pDevice.FAILED -> "Lose"
            WifiP2pDevice.UNAVAILABLE -> "Unavailable"
            else -> "Unknown"
        }
    }
    fun setList(list:List<WifiP2pDevice>){
        wifiP2pDeviceList=list
    }
}