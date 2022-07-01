package com.phone.clone.interfaces

import com.phone.clone.modelclasses.HSDetailsInfoToTransferClass

interface HSClientCallBacks {
    fun updateView(transferHSDetailsClass: HSDetailsInfoToTransferClass)
    fun transferFinished()
    fun errorOccurred()
}