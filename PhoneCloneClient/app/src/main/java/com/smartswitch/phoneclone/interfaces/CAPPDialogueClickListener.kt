package com.smartswitch.phoneclone.interfaces

interface CAPPDialogueClickListener {

    fun positiveHotSpotTurnOFF()
    fun reTryWifiTurnON(state:Boolean)

    fun negativeHotSpotTurnOFF()
    fun WifiTurnON(state:Boolean)

    fun gotPassword(SSID:String,PASS:String)

    fun allowPermission(permission:String)

    fun transferFinished()
    fun isAndroid10()
    fun isNotAndroid10()
    fun saveContacts()

    fun androidRStoragePermission()
}
