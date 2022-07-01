package com.data.usage.manager.usefullclasses

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.os.Process.myUid
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.data.usage.manager.constants.Constants
import com.data.usage.manager.sharedpreferences.MySharedPreferences
/*import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes*/


class MyAppPermissions(var appContext: Context?, var activityContext: Context?) {
    private var runTimePermissionsGiven = false
    private var systemLevelPermissionGiven = false
    lateinit var mySharedPreferences: MySharedPreferences
    private val accessFineLocation = Manifest.permission.ACCESS_FINE_LOCATION
    var permissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE
    )

    init {
        mySharedPreferences = MySharedPreferences(appContext!!)
    }

    fun requestForRunTimePermissions(): Boolean {

        if (mySharedPreferences.canAskForPermissions()) {
            runTimePermissionsGiven = checkAndRequestRuntimePermissions()
            return runTimePermissionsGiven
        }
        return false;
    }
//    fun checkAndRequestForSystemLevelPermissions() {
//        if (!checkForSystemLevelPermission(activityContext)) {
//            requestSystemLevelPermission()
//        }
//    }

    //    fun requestSystemLevelPermission() {
//    }
    public fun isLocationPermissionGiven(): Boolean {
        return appContext?.let {
            ContextCompat.checkSelfPermission(
                it,
                accessFineLocation
            )
        } == PackageManager.PERMISSION_GRANTED
    }
    public fun requestLocationPermission(){
        if (Build.VERSION.SDK_INT >= 29) {
            if (appContext?.let {
                    ActivityCompat.checkSelfPermission(
                        it,
                        "android.permission.ACCESS_COARSE_LOCATION"
                    )
                } != 0 || ActivityCompat.checkSelfPermission(
                    appContext!!,
                    "android.permission.ACCESS_FINE_LOCATION"
                ) != 0 || ActivityCompat.checkSelfPermission(
                    appContext!!,
                    "android.permission.ACCESS_BACKGROUND_LOCATION"
                ) != 0
            ) {
                ActivityCompat.requestPermissions(
                    activityContext as Activity,
                    arrayOf(
                        "android.permission.ACCESS_COARSE_LOCATION",
                        "android.permission.ACCESS_FINE_LOCATION",
                        "android.permission.ACCESS_BACKGROUND_LOCATION"
                    ),
                    9876
                )
            }
        } else if (appContext?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    "android.permission.ACCESS_COARSE_LOCATION"
                )
            } != 0 || ActivityCompat.checkSelfPermission(
                appContext!!,
                "android.permission.ACCESS_FINE_LOCATION"
            ) != 0
        ) {
            ActivityCompat.requestPermissions(
                activityContext as Activity,
                arrayOf(
                    "android.permission.ACCESS_COARSE_LOCATION",
                    "android.permission.ACCESS_FINE_LOCATION"
                ),
                9876
            )
        }
    }

    public fun checkIfGPSisOn():Boolean{
        val lm = appContext?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        try{
            if(Build.VERSION.SDK_INT>=28){
                gps_enabled=lm.isLocationEnabled
            }else
                gps_enabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }catch (e: Exception){
            //show msg exception
        }
        return gps_enabled;
    }
    public fun turnGpsOn(){
   //     enableLoc()
    }
  /*  private fun enableLoc() {
        var googleApiClient: GoogleApiClient?=null
        if (googleApiClient == null) {
            googleApiClient = appContext?.let {
                GoogleApiClient.Builder(it)
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
            }
            googleApiClient?.connect()
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = (1000).toLong()
            locationRequest.fastestInterval = (1000/2).toLong()
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
                            Constants.REQUEST_LOCATION_TURNON
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }
                }
            }
        }
    }*/
    public fun checkAndRequestForIgnoreBatteryOptimizationPermission() {
        if (!isIgnoringBatteryOptimization()) {
            requestIgnoreBatteryOptimization();
        }
    }

    public fun ignoreBatteryOptimization() {
        try{
            var i: Intent = Intent()
            i.setAction(Settings.ACTION_BATTERY_SAVER_SETTINGS)
            activityContext?.startActivity(i)
        }catch(ex :ActivityNotFoundException){

        }
    }

    public fun requestIgnoreBatteryOptimization() {
        var i: Intent = Intent()
        i.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
        i.flags = FLAG_ACTIVITY_NEW_TASK

        if(activityContext!=null&&appContext!=null){
            i.data = Uri.parse("package:" + appContext?.packageName)
            try{
                activityContext?.startActivity(i)
            }catch (e:ActivityNotFoundException){
                e.printStackTrace()
            }

        }
    }

    public fun isIgnoringBatteryOptimization(): Boolean {
        val powerManager: PowerManager =
            appContext?.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(appContext?.packageName)
    }

    public fun ignoreRunTimePermission() {
        val i = Intent()
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        i.addCategory(Intent.CATEGORY_DEFAULT)
        i.setData(Uri.parse("package:" + appContext?.getPackageName()))
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        appContext?.startActivity(i)
    }

    private fun checkAndRequestRuntimePermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= 29) {
            return true
        }
        var permissionsNotGranted = ArrayList<String>()

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    appContext!!,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsNotGranted.add(permission);
            }
        }
        permissionsNotGranted.toTypedArray()
        if (!permissionsNotGranted.isEmpty()) {
            ActivityCompat.requestPermissions(
                activityContext as Activity, permissionsNotGranted.toTypedArray(),
                Constants.PERMISSIONS_REQUSTCODE
            );
            return false;
        }
        return true;

    }

    public fun isRuntimePermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= 29) {
            return true
        }
        val editor = mySharedPreferences.getPreferencesEditor()

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    appContext!!,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        editor.putBoolean(Constants.ASK_FOR_PERMISSION, true)
        editor.commit()
        return true
    }

    private fun checkForSystemLevelPermission(context: Context): Boolean {
        var appOps: AppOpsManager =
            context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        var mode = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(OPSTR_GET_USAGE_STATS, myUid(), context.getPackageName())
        } else {
            appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, myUid(), context.getPackageName())
        }
        return mode == MODE_ALLOWED
    }

    fun isRunTimePermissionsGiven(): Boolean {
        runTimePermissionsGiven = checkAndRequestRuntimePermissions();
        return runTimePermissionsGiven;
    }

    fun setRunTimePermissionsGiven(runTimePermissionsGiven: Boolean) {
        this.runTimePermissionsGiven = runTimePermissionsGiven;
    }

    fun isSystemLevelPermissionGiven(): Boolean {
        systemLevelPermissionGiven = checkForSystemLevelPermission(appContext!!);
        return systemLevelPermissionGiven;
    }

    fun setSystemLevelPermissionGiven(systemLevelPermissionGiven: Boolean) {
        this.systemLevelPermissionGiven = systemLevelPermissionGiven;
    }


}