package com.example.simplechat.utills

import android.Manifest
import android.app.Activity
import android.content.Context

import android.content.pm.PackageManager

import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.checkSelfPermission



class MyPermissions() {

    companion object{
        var contactsPermission = arrayOf(
            Manifest.permission.READ_CONTACTS,
        )
        var storagePermission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

        fun havePermissions(context:Activity):Boolean{
            if(checkSelfPermission(context, contactsPermission[0])!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(context, contactsPermission,9272)
                return false
            }else{
                return true;
            }
        }

        fun haveStoragePermission(context: Activity):Boolean{
            if(checkSelfPermission(context,storagePermission[0])!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(context, storagePermission,9273)
                return false
            }else{
                return true
            }
        }

    }
    /* private val cameraPermission= Manifest.permission.CAMERA*/


}