package com.zak.clone.zakinterfaces

interface ZakMyInterFace {
    fun finished(isFinished:Boolean)
    fun updateProgress(progress:Int,currentSent:Long)
    fun updateProgress(progress:Int,currentSent:Long,size:Long?)
    fun errorOccurred()
    fun dataHasBeenSaved()
}
