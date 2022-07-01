package com.zak.clone.zakinterfaces

import com.zak.clone.zakmodelclasses.ZakDetailsInfoToTransferClass

interface ZakClientCallBacks {
    fun updateView(transferHSDetailsClass: ZakDetailsInfoToTransferClass)
    fun transferFinished()
    fun errorOccurred()
}