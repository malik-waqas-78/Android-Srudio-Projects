package com.phoneclone.data.interfaces

import com.phoneclone.data.modelclasses.DetailsInfoToTransferClass

interface ServerCallBacks {
    fun receivedTransferInfo(transferInfoClass: DetailsInfoToTransferClass)
    fun transferFinished()
    fun updateView(transferClass: DetailsInfoToTransferClass?)
    fun errorOccurred()
}