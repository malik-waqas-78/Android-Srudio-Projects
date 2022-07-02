package com.ppt.walkie.utils

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
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*


class MyPermissionsOKRA() {


    companion object {


        private const val locationPermissions = Manifest.permission.ACCESS_FINE_LOCATION
        private const val recordAudioPermission=Manifest.permission.RECORD_AUDIO
        fun hasRecordingPermission(activity: Activity) :Boolean {
            if (ContextCompat.checkSelfPermission(activity, recordAudioPermission) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
            return true
        }

        fun requestRecordingPermission(activity: Activity,sprcParam:Int=0) {
            var requestCode=if(sprcParam==0){
                ConstantsOKRA.RAP
            }else
            {
                sprcParam
            }
            ActivityCompat.requestPermissions(
                activity, arrayOf(recordAudioPermission),
                requestCode
            )
        }

        fun showRecordingExplanation(activity: Activity, callBack: ExplanationCallBack, msgParam:String="") {
            val title = """Record Audio Permission Required.""";
            val msg =
               if(msgParam.isEmpty()){
                   "This App needs Record Audio Permission in order to send your voice to the other Phone.";
               }else{
                   msgParam
               }
            MyDialogueOKRA.showExplanation(activity, recordAudioPermission, title, msg,callBack)
        }

        fun showRecordingRational(activity: Activity, callBack: RationalCallback, msgParam:String="") {
            val title = """Record Audio Permission Denied.""";
            val msg =
                if(msgParam.isEmpty()){
                    "This App needs Record Audio Permission in order to send your voice to the other Phone.\n Otherwise we will not be able to record and send your voice to the other phone.";
                }else{
                    msgParam
                }
            MyDialogueOKRA.showRational(activity, recordAudioPermission, title, msg,callBack)
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
                ConstantsOKRA.LOCATION_PERMISSION_REQUEST_CODE
            )
        }

        fun showLocationExplanation(activity: Activity,callBack: ExplanationCallBack) {
            val title = """Location Permission Required.""";
            val msg =
                "This App needs Location Permission in order to find nearby Devices.";
            MyDialogueOKRA.showExplanation(activity, locationPermissions, title, msg,callBack)
        }
        fun showLocationRational(activity: Activity,callBack: RationalCallback) {
            val title = """Location Permission Denied.""";
            val msg =
                "This App needs Location Permission in order to find nearby Devices. Without this permission you will not be able to find and connect to other devices.";
            MyDialogueOKRA.showRational(activity, locationPermissions, title, msg,callBack)
        }

        fun isGpsOn(applicationsContext:Activity): Boolean {
            val lm = applicationsContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
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

        public fun turnGpsOn(activity: Activity) {
            enableLoc(activity)
        }

        private fun enableLoc(activity: Activity) {
            var googleApiClient: GoogleApiClient? = null
            if (googleApiClient == null) {
                googleApiClient = GoogleApiClient.Builder(activity)
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
                                activity,
                                ConstantsOKRA.REQUEST_LOCATION_TURNON
                            )
                        } catch (e: SendIntentException) {
                            // Ignore the error.
                        }
                    }
                }
            }
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





}
