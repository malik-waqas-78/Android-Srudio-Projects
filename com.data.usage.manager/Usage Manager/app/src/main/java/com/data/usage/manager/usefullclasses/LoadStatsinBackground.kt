package com.data.usage.manager.usefullclasses

import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo

import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.RemoteException
import android.util.Log
import com.data.usage.manager.modelclasses.AppsInfo_ModelClass
import com.data.usage.manager.constants.Constants
import com.data.usage.manager.constants.Constants.get.TAG
import com.data.usage.manager.interfaces.DataUsageInterface
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class LoadStatsinBackground(var context:Context,subscriberid: String?, networktype: Int, startDate: Long, endDate: Long, uid: Int, interactWithUi: DataUsageInterface, querry: String?)
    : AsyncTask<String, Void, String>() {
    private var subscriberid:String?=null
    private var networktype :Int?=null
    private var startDate :Long?=null
    private var endDate:Long? =null
    private var uid:Int?=null
    private var dataUsageInterface: DataUsageInterface?=null
    private var networkStatsManager:NetworkStatsManager?=null
    private var retrivedData = HashMap<String, String>()
    private var querry:String?=null
    private var info:ApplicationInfo?=null
    private var index:Int?=null
    private var size:Int?=null
    private var appsInfo_modelClass: AppsInfo_ModelClass?=null
    var loading=false;
    init {
        this.subscriberid = subscriberid
        this.networktype = networktype
        this.startDate = startDate
        this.endDate = endDate
        this.uid = uid
        this.dataUsageInterface = interactWithUi
        this.querry = querry
        retrivedData = HashMap<String, String>()
        networkStatsManager = context.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
    }


   constructor(context:Context,subscriberid: String?, networktype: Int, startDate: Long, endDate: Long,
               uid: Int, dataUsageInterface: DataUsageInterface,
               querySummeryPerApp: String?, info: ApplicationInfo, index: Int, size: Int) :
           this(context,subscriberid,networktype,startDate,endDate,uid,dataUsageInterface,querySummeryPerApp) {

        this.index = index
        this.size = size
       this.info=info
    }

    override fun onCancelled() {
        Log.d(TAG, "onCancelled: ")
        super.onCancelled()
        Log.d(TAG, "onCancelled: ")
    }
    override fun onPreExecute() {
        super.onPreExecute();
        loading=true
        if (querry.equals(Constants.QUERY_SUMMERY_FOR_DEVICE)) {
            dataUsageInterface?.preDataConsumedByDevice()
        }else if(querry.equals(Constants.QUERRY_TODAY_DATA_USAGE)){
            dataUsageInterface?.preTodayDataUsage()
        } else if (querry.equals(Constants.QUERY_SUMMERY_PER_APP)) {
            dataUsageInterface?.preDataConsumedByApp()
        } else if (querry.equals(Constants.TOTAL_DATA_USAGE_ON_TETHERING)) {
            dataUsageInterface?.preTethringDataUsage()
        } else if (querry.equals(Constants.TOTAL_DATA_USAGE_BY_SYSYTEM_PROCESSES)) {
            dataUsageInterface?.preSystemAppsDataUsed()
        } else if (querry.equals(Constants.TOTAL_DATA_USAGE_BY_UNINSTALLED_APPS)) {
            dataUsageInterface?.preRemovedAppsDataUsage()
        }
    }

    override fun doInBackground(vararg params: String?): String? {
        retrivedData =HashMap<String, String>();
        if (querry.equals(Constants.QUERY_SUMMERY_FOR_DEVICE)) {
            getOverAllNetworkDataUsage()
        }else if(querry.equals(Constants.QUERRY_TODAY_DATA_USAGE)){
            getOverAllNetworkDataUsage()
        } else if (querry.equals(Constants.QUERY_SUMMERY_PER_APP)) {
            getPerAppNetworkDataUsage();
        } else if (querry.equals(Constants.TOTAL_DATA_USAGE_ON_TETHERING)) {
            getTethringDataUsed();
        } else if (querry.equals(Constants.TOTAL_DATA_USAGE_BY_SYSYTEM_PROCESSES)) {
            getSystemDataUsed();
        } else if (querry.equals(Constants.TOTAL_DATA_USAGE_BY_UNINSTALLED_APPS)) {
            getDataUsedByUninstalledApps();
        }
        return null;
    }

    override fun onPostExecute(s: String?) {
        super.onPostExecute(s);
        if (querry.equals(Constants.QUERY_SUMMERY_FOR_DEVICE)) {
            dataUsageInterface?.postDataConsumedByDevice(retrivedData)
        }else if(querry.equals(Constants.QUERRY_TODAY_DATA_USAGE)){
            dataUsageInterface?.postTodayDataUsage(retrivedData)
        } else if (querry.equals(Constants.QUERY_SUMMERY_PER_APP)) {
            dataUsageInterface?.postDataConsumedByApp(retrivedData, appsInfo_modelClass, index!!, size!!);
        } else if (querry.equals(Constants.TOTAL_DATA_USAGE_ON_TETHERING)) {
            dataUsageInterface?.postTethringDataUsage(retrivedData)
        } else if (querry.equals(Constants.TOTAL_DATA_USAGE_BY_SYSYTEM_PROCESSES)) {
            dataUsageInterface?.postSystemAppsDataUsed(retrivedData)
        } else if (querry.equals(Constants.TOTAL_DATA_USAGE_BY_UNINSTALLED_APPS)) {
            dataUsageInterface?.postRemovedAppsDataUsage(retrivedData)
        }
        loading=false
    }

    private fun getOverAllNetworkDataUsage() {

        try {
            var stats:NetworkStats.Bucket?  = networkStatsManager?.querySummaryForDevice(networktype!!, subscriberid, startDate!!, endDate!!)
            val upload = stats!!.txBytes
            val download = stats!!.rxBytes
            //put data
            retrivedData.put(Constants.QUERRY, Constants.QUERY_SUMMERY_FOR_DEVICE);
            if (networktype == NetworkCapabilities.TRANSPORT_WIFI)
                retrivedData.put(Constants.NETWORK_TYPE, Constants.WIFI_NETWORK);
            else
                retrivedData.put(Constants.NETWORK_TYPE, Constants.MOBILE_NETWORK);
            retrivedData.put(Constants.START_DATE, getDate(startDate));
            retrivedData.put(Constants.END_DATE, getDate(endDate));
            retrivedData.put(Constants.TOTAL_NOOF_BYTES_USED,(download+upload).toString())
            retrivedData.put(Constants.TOTAL_DATA_USAGE_PER_DEVICE, convertToproperStorageType(download.toDouble() + upload.toDouble()));
        } catch (e: RemoteException) {
            e.printStackTrace();
        }
    }

    private fun getPerAppNetworkDataUsage(){
        var stats = networkStatsManager!!.queryDetailsForUid(networktype!!, subscriberid, startDate!!, endDate!!, uid!!)
        var download = 0.0
        var upload =0.0
        while (stats.hasNextBucket()) {
            var bucket = NetworkStats.Bucket();
            stats.getNextBucket(bucket);
            download += bucket.getRxBytes();
            upload += bucket.getTxBytes();
        }
        stats.close();
        appsInfo_modelClass = AppsInfo_ModelClass(download.toLong(),upload.toLong(),info!!)
        //put data
        retrivedData.put(Constants.QUERRY, Constants.QUERY_SUMMERY_PER_APP);
        retrivedData.put(Constants.TOTAL_DATA_USAGE_PER_APP, convertToproperStorageType(download + upload));
        retrivedData.put(Constants.TOTAL_NOOF_BYTES_USED,(download+upload).toString())
    }

    private fun getTethringDataUsed() {
        var stats = networkStatsManager!!.queryDetailsForUid(networktype!!, subscriberid, startDate!!, endDate!!, uid!!)
        var download = 0.0
        var upload =0.0
        while (stats.hasNextBucket()) {
            var bucket = NetworkStats.Bucket();
            stats.getNextBucket(bucket);
            download += bucket.getRxBytes();
            upload += bucket.getTxBytes();
        }
        stats.close();
        //put data
        retrivedData.put(Constants.QUERRY, Constants.TOTAL_DATA_USAGE_ON_TETHERING);
        retrivedData.put(Constants.TOTAL_DATA_USAGE, convertToproperStorageType(download + upload));
        retrivedData.put(Constants.TOTAL_NOOF_BYTES_USED,(download+upload).toString())
    }

    private fun getSystemDataUsed() {
        var stats = networkStatsManager!!.queryDetailsForUid(networktype!!, subscriberid, startDate!!, endDate!!, uid!!)
        var download = 0.0
        var upload =0.0
        while (stats.hasNextBucket()) {
            var bucket = NetworkStats.Bucket();
            stats.getNextBucket(bucket);
            download += bucket.getRxBytes();
            upload += bucket.getTxBytes();
        }
        stats.close();
        //put data
        retrivedData.put(Constants.QUERRY, Constants.TOTAL_DATA_USAGE_BY_SYSYTEM_PROCESSES);
        retrivedData.put(Constants.TOTAL_DATA_USAGE, convertToproperStorageType(download + upload));
        retrivedData.put(Constants.TOTAL_NOOF_BYTES_USED,(download+upload).toString())
    }

    private fun getDataUsedByUninstalledApps() {
        var stats = networkStatsManager!!.queryDetailsForUid(networktype!!, subscriberid, startDate!!, endDate!!, uid!!)
        var download = 0.0
        var upload =0.0
        while (stats.hasNextBucket()) {
            var bucket = NetworkStats.Bucket();
            stats.getNextBucket(bucket);
            download += bucket.getRxBytes();
            upload += bucket.getTxBytes();
        }
        stats.close();
        //put data
        retrivedData.put(Constants.QUERRY, Constants.TOTAL_DATA_USAGE_BY_UNINSTALLED_APPS);
        retrivedData.put(Constants.TOTAL_DATA_USAGE, convertToproperStorageType(download + upload));
        retrivedData.put(Constants.TOTAL_NOOF_BYTES_USED,(download+upload).toString())
    }

    private fun convertToproperStorageType(data:Double) :String{
        var d:Double=0.0
        if (data >= (1024.0 * 1000.0 * 1000.0 * 1000.0)) {
            d = data /(1024.0 * 1000.0 * 1000.0 * 1000.0);
            return String.format("%.2f", d) + " TB";
        }
        if (data >= (1024.0 * 1000.0 * 1000.0)) {
            d = data / (1024.0 * 1000.0 * 1000.0);
            return String.format("%.2f", d) + " GB";
        }
        if (data >= (1024.0 * 1000.0)) {
            d = data /(1024.0 * 1000.0);
            return String.format("%.2f", d) + " MB";
        }
        if (data >= 1024.0) {
            d = data / 1024.0;
            return String.format("%.2f", d) + " KB";
        }
        return String.format("%.2f", d) + " B";
    }

    fun getDate(milliSeconds:Long?):String {
        var dateFormat = "dd/MM/yyyy HH:mm:ss";
        // Create a DateFormatter object for displaying date in specified format.
        var formatter = SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        var calendar = Calendar.getInstance();
        if (milliSeconds != null) {
            calendar.setTimeInMillis(milliSeconds)
        };
        return formatter.format(calendar.getTime());
    }

}