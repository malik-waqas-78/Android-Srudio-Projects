package com.data.usage.manager.usefullclasses

import android.Manifest
import android.app.usage.NetworkStats
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.os.Handler
import android.os.Process
import android.telephony.SubscriptionManager

import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.data.usage.manager.constants.Constants
import com.data.usage.manager.constants.Constants.get.TAG
import com.data.usage.manager.interfaces.DataUsageInterface
import com.data.usage.manager.sharedpreferences.MySharedPreferences


class NetworkStatsHelper(var appContext: Context, var activityContext: Context) {
    var h1: Handler = Handler()
    var h2: Handler = Handler()
    var h3: Handler = Handler()
    var h4: Handler = Handler()
    var h5: Handler = Handler()
    var r1: Runnable? = null
    var r2: Runnable? = null
    var r3: Runnable? = null
    var r4: Runnable? = null
    var r5: Runnable? = null
    var loadStatsinBackground1: LoadStatsinBackground? = null
    var loadStatsinBackground2: LoadStatsinBackground? = null
    var loadStatsinBackground3: LoadStatsinBackground? = null
    var loadStatsinBackground4: LoadStatsinBackground? = null
    var loadStatsinBackground5: LoadStatsinBackground? = null
    public var startDate: Long = 0
        get() = field
        set(value) {
            field = value
        }
    public var endDate: Long = 0
        get() = field
        set(value) {
            field = value
        }
    private var networktype: Int = 0;
    private var uid: Int? = null
    private var subscriberid: String? = null
    private var querry: String? = null
    var total_number_of_app_packages=0
    public fun getDeviceDataUsage(dataUsageInterface: DataUsageInterface) {
        Log.d(TAG, "getDeviceDataUsage: $networktype $startDate $endDate ")
        //whole device for the given network type
        if (getQuerry().equals(Constants.QUERY_SUMMERY_FOR_DEVICE) || getQuerry().equals(Constants.QUERRY_TODAY_DATA_USAGE)) {
            loadStatsinBackground1 = LoadStatsinBackground(
                appContext,
                getSubscriberid(), networktype, startDate, endDate, 0, dataUsageInterface, querry
            )
            r1 = Runnable {
               if(loadStatsinBackground1?.status != AsyncTask.Status.RUNNING&&loadStatsinBackground1?.status != AsyncTask.Status.FINISHED)
                   loadStatsinBackground1?.execute()
                else
                    loadStatsinBackground1=null
            }
            if(loadStatsinBackground1?.status != AsyncTask.Status.RUNNING)
                h1.postDelayed(r1, 0)
        }
    }

    
    fun getTethringDataUsage(dataUsageInterface: DataUsageInterface) {
        //Tethering if network is mobile
        if (getNetworktype() == NetworkCapabilities.TRANSPORT_CELLULAR) {
            loadStatsinBackground2 = LoadStatsinBackground(
                appContext,
                getSubscriberid(), networktype, startDate, endDate,
                NetworkStats.Bucket.UID_TETHERING, dataUsageInterface,
                Constants.TOTAL_DATA_USAGE_ON_TETHERING
            );
            r2 = Runnable {
               if(loadStatsinBackground2?.status != AsyncTask.Status.RUNNING)
                   loadStatsinBackground2?.execute()
                else
                    loadStatsinBackground2=null
            }
            if(loadStatsinBackground2?.status != AsyncTask.Status.RUNNING)
                h2.postDelayed(r2, 0)
        }
    }

    fun getRemovedAppsDataUsage(dataUsageInterface: DataUsageInterface) {
        //Apps that have been removed
        loadStatsinBackground3 = LoadStatsinBackground(
            appContext,
            getSubscriberid(), networktype, startDate, endDate,
            NetworkStats.Bucket.UID_REMOVED, dataUsageInterface,
            Constants.TOTAL_DATA_USAGE_BY_UNINSTALLED_APPS
        );
        r3 = Runnable {
            if(loadStatsinBackground3?.status != AsyncTask.Status.RUNNING)
                loadStatsinBackground3?.execute()
            else
                loadStatsinBackground3=null
        }
        if(loadStatsinBackground3?.status != AsyncTask.Status.RUNNING)
            h3.postDelayed(r3, 0)
    }

    fun getDataUsedBySystemApps(dataUsageInterface: DataUsageInterface) {
        //Data used by the system
        loadStatsinBackground4 = LoadStatsinBackground(
            appContext,
            getSubscriberid(), networktype, startDate, endDate,
            Process.SYSTEM_UID, dataUsageInterface,
            Constants.TOTAL_DATA_USAGE_BY_SYSYTEM_PROCESSES
        )
        r4 = Runnable {
            if(loadStatsinBackground4?.status != AsyncTask.Status.RUNNING)
                loadStatsinBackground4?.execute()
            else
                loadStatsinBackground4=null
        }
        if(loadStatsinBackground4?.status != AsyncTask.Status.RUNNING)
            h4.postDelayed(r4, 0)
    }

    fun getDataUsedByAllTheApps(dataUsageInterface: DataUsageInterface) {
        //All Apps for the given network type
        var applicationsInfo = getAllAppsApplicationInfo()
        dataUsageInterface.totalNoOfPackages(applicationsInfo.size)
        for (info in applicationsInfo) {
            var loadStatsinBackground5:LoadStatsinBackground? = LoadStatsinBackground(
                appContext,
                getSubscriberid(),
                networktype,
                startDate,
                endDate,
                info.uid,
                dataUsageInterface,
                Constants.QUERY_SUMMERY_PER_APP,
                info,
                applicationsInfo.indexOf(info),
                applicationsInfo.size
            )
            r5 = Runnable {
                if(loadStatsinBackground5?.status != AsyncTask.Status.RUNNING){
                    loadStatsinBackground5?.execute()
                }else{
                    loadStatsinBackground5=null
                }
            }
            if(loadStatsinBackground5?.status != AsyncTask.Status.RUNNING)
                h5.postDelayed(r5, 2000)
        }
    }

    private fun getAllAppsApplicationInfo(): ArrayList<ApplicationInfo> {
        var uids = ArrayList<ApplicationInfo>();
        var installedPackages = appContext.getPackageManager().getInstalledPackages(
            PackageManager.GET_ACTIVITIES
        );
        var packageIterator = installedPackages.iterator();
        var manager = appContext.getPackageManager();
        var packageInfo: PackageInfo? = null;
        while (packageIterator.hasNext()) {
            packageInfo = packageIterator.next()
            var info: ApplicationInfo? = null;
            try {//packageInfo.packageName  give in below line as argument
                info = manager.getApplicationInfo(packageInfo.packageName!!, 0);
                uids.add(info);
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace();
            }
        }
        total_number_of_app_packages=uids.size
        return uids;
    }

    fun clearEveryThing() {
        loadStatsinBackground1?.cancel(true)
        loadStatsinBackground2?.cancel(true)
        loadStatsinBackground3?.cancel(true)
        loadStatsinBackground4?.cancel(true)
        loadStatsinBackground5?.cancel(true)
        h1.removeCallbacksAndMessages(r1)
        h2.removeCallbacksAndMessages(r2)
        h3.removeCallbacksAndMessages(r3)
        h4.removeCallbacksAndMessages(r4)
        h5.removeCallbacksAndMessages(r5)
        Log.d(TAG, "clearEveryThing: ")
    }

    private fun getSubscriberid(): String? {
        if (networktype == Constants.WIFII_NETWORK) {
            return ""
        } else {
            return getSubIdOf()
        }
    }

    //   @SuppressLint("MissingPermission")
//    private fun getsubID():String?{
//        if(getNetworktype()== NetworkCapabilities.TRANSPORT_CELLULAR){
//            subscriberid = if(Build.VERSION.SDK_INT>=29){
//                null
//            }else{
//                val manager =  appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//                manager.getSubscriberId()
//            }
//        }else
//        subscriberid=""
//        return subscriberid
//    }

    fun getNetworktype(): Int {
        return networktype;
    }

    fun setNetworktype(networkTyep: Int) {
        this.networktype = networkTyep;
    }

    fun getQuerry(): String? {
        return querry;
    }

    fun setQuerry(querry: String) {
        this.querry = querry
    }

    //    fun getSubIdOf(): String? {
//        subscriberid = if (getNetworktype() == NetworkCapabilities.TRANSPORT_CELLULAR) {
//            if (Build.VERSION.SDK_INT >= 29) {
//                null
//            } else {
//                var simSlot = MySharedPreferences(appContext).getSimSlot()
//                simSlot-=1
//                Log.d(TAG, "getSubIdOf: sim slot $simSlot")
//                val subscriptionManager: SubscriptionManager = appContext.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
//                Log.d(TAG, "getSubIdOf: info ${subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(simSlot)}")
//                subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(simSlot).subscriptionId.toString()
//            }
//        } else
//            ""
//        Log.d(TAG, "getSubIdOf: id $subscriberid")
//        return subscriberid
//    }
    fun getSubIdOf(): String? {
        //410010028636623
        //410010028636623
        var subscriptionId: Int
        if (Build.VERSION.SDK_INT >= 29) {
            return null
        } else {
            var simSlot = MySharedPreferences(appContext).getSimSlot()
            //Log.d(Constants.TAG, "getSubIdOf: sim slot $simSlot")
            val subscriptionManager: SubscriptionManager =
                appContext.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
            if (ActivityCompat.checkSelfPermission(
                    appContext,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return ""
            }
//            Log.d(
//                Constants.TAG,
//                "getSubIdOf: info ${
//                    subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(simSlot)
//                }"
//            )
            subscriptionId = if(subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(simSlot)!=null && subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(simSlot).subscriptionId==null)
                0
            else {
                if(subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(simSlot)!=null)
                    subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(simSlot)?.subscriptionId!!
                else
                    0
            }
        }
        try {
            var telephonyManager =
                appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (Build.VERSION.SDK_INT >= 24 && Build.VERSION.SDK_INT < 29) {
                subscriberid = telephonyManager.createForSubscriptionId(subscriptionId).subscriberId
             //   Log.d(TAG, "getSubIdOf: yes $subscriberid")
            } else {
                subscriberid = telephonyManager.getSubscriberId()
              //  Log.d(TAG, "getSubIdOf: No  $subscriberid")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return subscriberid
    }
//    fun getSubIdOf(): String? {
//        subscriberid = if (Build.VERSION.SDK_INT >= 29) {
//            null
//        } else {
//            var simSlot = MySharedPreferences(applicationContext).getSimSlot()
//            Log.d(Constants.TAG, "getSubIdOf: sim slot $simSlot")
//            simSlot -= 1
//            val subscriptionManager: SubscriptionManager = applicationContext.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
//            Log.d(Constants.TAG, "getSubIdOf: info ${subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(simSlot)}")
//            subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(simSlot).subscriptionId.toString()
//        }
//        try {
//            val c = Class.forName("android.telephony.TelephonyManager")
//            var telephonyManager=applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//            val m: Method = c.getMethod("getSubscriberId", *arrayOf<Class<*>?>(Int::class.javaPrimitiveType))
//            val o: Any = m.invoke(telephonyManager, arrayOf(subscriberid))
//            subscriberid = o as String
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return subscriberid
//    }
}