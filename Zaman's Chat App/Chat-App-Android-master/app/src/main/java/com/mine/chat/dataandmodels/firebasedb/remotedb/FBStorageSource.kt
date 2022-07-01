package com.mine.chat.dataandmodels.firebasedb.remotedb

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage

// Task based
class FBStorageSource {
    private val storageInstance = FirebaseStorage.getInstance()

    fun uploadUserImage(userID: String, bArr: ByteArray): Task<Uri> {
        val path = "user_photos/$userID/profile_image"
        val ref = storageInstance.reference.child(path)

        return ref.putBytes(bArr).continueWithTask {
            ref.downloadUrl
        }
    }
}