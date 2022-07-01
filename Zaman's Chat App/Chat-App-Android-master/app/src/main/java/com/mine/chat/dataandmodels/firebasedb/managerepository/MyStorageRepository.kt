package com.mine.chat.dataandmodels.firebasedb.managerepository

import android.net.Uri
import com.mine.chat.dataandmodels.firebasedb.remotedb.FBStorageSource
import com.mine.chat.dataandmodels.ModelResult

class MyStorageRepository {
    private val firebaseStorageService = FBStorageSource()

    fun updateUserProfileImage(userID: String, byteArray: ByteArray, b: (ModelResult<Uri>) -> Unit) {
        b.invoke(ModelResult.Loading)
        firebaseStorageService.uploadUserImage(userID, byteArray).addOnSuccessListener {
            b.invoke((ModelResult.Success(it)))
        }.addOnFailureListener {
            b.invoke(ModelResult.Error(it.message))
        }
    }
}