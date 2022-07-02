package com.photo.recovery.permissions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.photo.recovery.constants.MyConstantsAAT
import com.photo.recovery.constants.MyConstantsAAT.Companion.ManageAllFilePermissionRequestCode
import com.photo.recovery.dialogues.MyDialoguesAAT

class MyPermissionsAAT {
    companion object{
        private var storagePermissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        fun hasStoragePermission(activity: Activity) :Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                return hasAllFileStoragePermission()
            } else {
                if (ContextCompat.checkSelfPermission(
                        activity,
                        storagePermissions[0]
                    ) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                        activity,
                        storagePermissions[1]
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
                return true
            }

        }

        @RequiresApi(Build.VERSION_CODES.R)
        fun hasAllFileStoragePermission(): Boolean {

            if (Environment.isExternalStorageManager()) {
                return true
            }

            return false
        }
        fun requestStoragePermission(context: Context) {

            val permission = arrayOf(storagePermissions[0], storagePermissions[1])

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                checkStoragePermissionForAllVersionndroid(context as Activity)

            } else {

                ActivityCompat.requestPermissions(
                    context as Activity, permission,
                    MyConstantsAAT.STORAGE_PERMISSION_REQUEST_CODE
                )

            }
        }

        fun checkStoragePermissionForAllVersionndroid(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.addCategory("android.intent.category.DEFAULT")
                    intent.data =
                        Uri.parse(
                            String.format(
                                "package:%s",
                                activity.applicationContext.packageName
                            )
                        )
                    activity.startActivityForResult(intent, ManageAllFilePermissionRequestCode!!)
                } catch (e: Exception) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    activity.startActivityForResult(intent, ManageAllFilePermissionRequestCode!!)
                }
            } else {
                //below android 11
            }
        }
        interface OpenSettingsForPermissions {
            fun allowPermissions(context: Context)
            fun doNotAllowPermissions()
        }

        fun openAppPermissionsSettings(context: Context) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            (context as Activity).startActivityForResult(
                intent,
                MyConstantsAAT.PERMISSION_SETTINGS_STORAGE
            )
        }

        fun showStorageExplanation(activity: Activity,callBack:ExplanationCallBack,msgParam:String="") {
            val title = """Storage Permission Required.""";
            val msg =
                if(msgParam.isEmpty()){
                    "This App needs Storage Permission to recover and show Images/Videos/Audios/Documents etc. Please allow storage permission to continue...";
                }else{
                    msgParam
                }
            MyDialoguesAAT.showExplanation(activity,storagePermissions[1], title, msg,callBack)
        }

        fun showStorageRational(activity: Activity,callBack:RationalCallback,msgParam:String="") {
            val title = """Storage Permission Denied.""";
            val msg =
                if(msgParam.isEmpty()){
                    "This App needs Storage Permission to recover and show Images/Videos/Audios/Documents etc." +
                            " Without this permission App will not be able to recover your media files";
                }else{
                    msgParam
                }
            MyDialoguesAAT.showRational(activity,storagePermissions[1], title, msg,callBack)
        }


        interface ExplanationCallBack{
            fun requestPermission()
            fun denyPermission()
        }
        interface RationalCallback{
            fun openSettings()
            fun dismissed()
        }
        interface PermissionsDeniedCallBack{
            fun retryPermissions()
            fun exitApp()
        }

    }
}