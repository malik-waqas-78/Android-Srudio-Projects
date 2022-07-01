package com.phone.clone.utills

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.phone.clone.constants.HSMyConstants
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*


class HSMyPermissions(var appContext: Context, var activityContext: Context) {
    companion object {
        val contactsPermissions = arrayOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
        )
        val calendarPermission = arrayOf(
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR,
        )
        private val storagePermission = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        const val locationPermissions = Manifest.permission.ACCESS_FINE_LOCATION

        fun hasStoragePermission(activity: Activity) :Boolean {
            if (ContextCompat.checkSelfPermission(activity, storagePermission[0]) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, storagePermission[1]) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
            return true
        }

        fun requestStoragePermission(activity: Activity,sprcParam:Int=0) {
            var requestCode=if(sprcParam==0){
                HSMyConstants.SPRC
            }else
            {
                sprcParam
            }
            ActivityCompat.requestPermissions(
                activity, storagePermission,
                requestCode
            )
        }

        fun showStorageExplanation(activity: Activity,callBack:ExplanationCallBack,msgParam:String="") {
            val title = """Storage Permission Required.""";
            val msg =
                if(msgParam.isEmpty()){
                    "This App needs Storage Permission to access Images/Videos/Audios/Docs that you want to share b/w your devices.";
                }else{
                    msgParam
                }
            HSMyDialogues.showRational(activity,storagePermission[1], title, msg,callBack)
        }

        fun showStorageRational(activity: Activity,callBack:RationalCallback,msgParam:String="") {
            val title = """Storage Permission Denied.""";
            val msg =
                if(msgParam.isEmpty()){
                    "This App needs Storage Permission to access Images/Videos/Audios/Docs that you want to share b/w your devices.";
                }else{
                    msgParam
                }
            HSMyDialogues.showRational(activity,storagePermission[1], title, msg,callBack)
        }

        fun hasContactsPermission(activity: Activity) :Boolean{
            if (ContextCompat.checkSelfPermission(activity, contactsPermissions[0]) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, contactsPermissions[1]) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
            return true
        }

        fun requestContactsPermission(activity: Activity) {
            ActivityCompat.requestPermissions(
                activity, contactsPermissions,
                HSMyConstants.CONTACTS_PERMISSIONS_REQUEST_CODE
            )
        }

        fun showContactsExplanation(activity: Activity,callBack:ExplanationCallBack) {
            val title = """Contacts Permission Required.""";
            val msg =
                "This App needs Contacts Permission to share Your Contacts b/w devices/";
            HSMyDialogues.showRational(activity,contactsPermissions[1], title, msg,callBack)
        }
        fun showContactsRational(activity: Activity,callBack:RationalCallback) {
            val title = """Contacts Permission Denied.""";
            val msg =
                "This App needs Contacts Permission to share Your Contacts b/w devices/";
            HSMyDialogues.showRational(activity,contactsPermissions[1], title, msg,callBack)
        }

        fun hasCalendarPermission(activity: Activity):Boolean {
            if (ContextCompat.checkSelfPermission(activity, calendarPermission[0]) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, calendarPermission[1]) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
            return true
        }

        fun requestCalendarPermission(activity: Activity) {
            ActivityCompat.requestPermissions(
                activity, calendarPermission,
                HSMyConstants.CALEDAR_PERMISSION_REQUEST_CODE
            )
        }

        fun showCalendarExplanation(activity: Activity,callBack:ExplanationCallBack) {
            val title = """Calendar Permission Required.""";
            val msg =
                "This App needs Calendar Permission to share Your Calendar events b/w devices.";
            HSMyDialogues.showRational(activity,calendarPermission[0], title, msg,callBack)
        }
        fun showCalendarRational(activity: Activity,callBack:RationalCallback) {
            val title = """Calendar Permission Denied.""";
            val msg =
                "This App needs Calendar Permission to share Your Calendar events b/w devices.";
            HSMyDialogues.showRational(activity,calendarPermission[0], title, msg,callBack)
        }

        fun hasLocationPermission(activity: Activity):Boolean {
            if (ContextCompat.checkSelfPermission(activity, locationPermissions) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, locationPermissions) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
            return true
        }

        fun requestLocationPermission(activity: Activity) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(locationPermissions),
                HSMyConstants.LOCATION_PERMISIION_REQUEST_CODE
            )
        }

        fun showLocationExplanation(activity: Activity,callBack:ExplanationCallBack) {
            val title = """Location Permission Required.""";
            val msg =
                "This App needs Location Permission in order to find nearby Devices.";
            HSMyDialogues.showRational(activity,locationPermissions, title, msg,callBack)
        }
        fun showLocationRational(activity: Activity,callBack:RationalCallback) {
            val title = """Location Permission Denied.""";
            val msg =
                "This App needs Location Permission in order to find nearby Devices.";
            HSMyDialogues.showRational(activity,locationPermissions, title, msg,callBack)
        }
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
    interface DeletionCallBack{
        fun deleteFiles()
        fun dismiss()
    }



    public fun CheckIfGPSisOn(): Boolean {
        val lm = appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        try {
            if (Build.VERSION.SDK_INT >= 28) {
                gps_enabled = lm.isLocationEnabled
            } else
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            //show msg exception
        }
        return gps_enabled;
    }

    public fun turnGpsOn() {
        enableLoc()
    }

    private fun enableLoc() {
        var googleApiClient: GoogleApiClient? = null
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(appContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(bundle: Bundle?) {}
                    override fun onConnectionSuspended(i: Int) {
                        googleApiClient!!.connect()
                    }
                })
                .addOnConnectionFailedListener { connectionResult ->
                    Log.d(
                        "Location error",
                        "Location error " + connectionResult.errorCode
                    )
                }.build()
            googleApiClient.connect()
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = (1000).toLong()
            locationRequest.fastestInterval = (1000 / 2).toLong()
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
            builder.setAlwaysShow(true)
            val result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
            result.setResultCallback { result ->
                val status = result.status
                when (status.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(
                            activityContext as Activity?,
                            HSMyConstants.REQUEST_LOCATION_TURNON
                        )
                    } catch (e: SendIntentException) {
                        // Ignore the error.
                    }
                }
            }
        }
    }

}