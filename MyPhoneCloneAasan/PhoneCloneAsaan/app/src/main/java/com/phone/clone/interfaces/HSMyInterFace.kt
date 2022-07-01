package com.phone.clone.interfaces

interface HSMyInterFace {
    fun finished(isFinished:Boolean)
    fun updateProgress(progress:Int,currentSent:Long)
    fun updateProgress(progress:Int,currentSent:Long,size:Long?)
    fun errorOccurred()
    fun dataHasBeenSaved()
}
