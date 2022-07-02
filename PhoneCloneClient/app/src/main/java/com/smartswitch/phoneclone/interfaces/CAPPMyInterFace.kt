package com.smartswitch.phoneclone.interfaces

interface CAPPMyInterFace {
    fun finished(isFinished:Boolean)
    fun updateProgress(progress:Int,currentSent:Long)
    fun updateProgress(progress:Int,currentSent:Long,size:Long?)
    fun errorOccurred()
    fun dataHasBeenSaved()
}
