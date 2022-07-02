package com.ppt.walkie.utils

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE
import android.content.Intent

class BluetoothUtilsOKRA {
    companion object {
        fun isBluetoothOn(activity: Activity) : Boolean{
            //check if bluetooth is enabled
            //if not enable it
            val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            return (mBluetoothAdapter.isEnabled)
        }

        fun turnBluetoothOn(activity: Activity){
            val intent = Intent(ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(intent, ConstantsOKRA.REQUEST_BLUETOOTH_TURN_ON)
        }
    }
}