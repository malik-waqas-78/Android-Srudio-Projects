package com.zak.clone.zakinterfaces

import com.zak.clone.zakmodelclasses.ZakDetailsInfoToTransferClass

interface ZakServerCallBacks {
    fun receivedTransferInfo(transferInfoClassHS: ZakDetailsInfoToTransferClass)
    fun transferFinished()
    fun updateView(transferClassHS: ZakDetailsInfoToTransferClass?)
    fun errorOccurred()
}