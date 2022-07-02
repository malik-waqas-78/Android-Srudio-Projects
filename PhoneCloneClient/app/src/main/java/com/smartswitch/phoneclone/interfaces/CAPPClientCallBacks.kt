package com.smartswitch.phoneclone.interfaces

import com.smartswitch.phoneclone.modelclasses.CAPPDetailsInfoToTransferClass

interface CAPPClientCallBacks {
    fun updateView(transferHSDetailsClass: CAPPDetailsInfoToTransferClass)
    fun transferFinished()
    fun errorOccurred()
}