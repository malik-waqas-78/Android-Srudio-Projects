package com.phone.clone.interfaces

import com.phone.clone.modelclasses.HSDetailsInfoToTransferClass

interface HSServerCallBacks {
    fun receivedTransferInfo(transferInfoClassHS: HSDetailsInfoToTransferClass)
    fun transferFinished()
    fun updateView(transferClassHS: HSDetailsInfoToTransferClass?)
    fun errorOccurred()
}