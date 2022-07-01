package com.phoneclone.data.interfaces

import com.phoneclone.data.modelclasses.DetailsInfoToTransferClass

interface ClientCallBacks {
    fun updateView(transferDetailsClass: DetailsInfoToTransferClass)
    fun transferFinished()
    fun errorOccurred()
}