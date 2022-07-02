package com.smartswitch.phoneclone.interfaces

import com.smartswitch.phoneclone.modelclasses.CAPPDetailsInfoToTransferClass

interface CAPPServerCallBacks {
    fun receivedTransferInfo(transferInfoClassHS: CAPPDetailsInfoToTransferClass)
    fun transferFinished()
    fun updateView(transferClassHS: CAPPDetailsInfoToTransferClass?)
    fun errorOccurred()
}