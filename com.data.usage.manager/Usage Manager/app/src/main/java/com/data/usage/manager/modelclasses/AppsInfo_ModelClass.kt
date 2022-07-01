package com.data.usage.manager.modelclasses

import android.content.pm.ApplicationInfo

class AppsInfo_ModelClass(download: Long, upload: Long, appinfo: ApplicationInfo) {

    private var noof_bytes_downlaoded = 0.0
        get() = field
        set(value) {
            field = value
        }
    private var noof_bytes_uploaded = 0.0
        get() = field
        set(value) {
            field = value
        }
    private var appInfo: ApplicationInfo
    private var TotalDataUsed :Double? = null
    private var properDataUsageInFormating = ""


    init {
        noof_bytes_downlaoded = download.toDouble()
        noof_bytes_uploaded = upload.toDouble()
        this.appInfo = appinfo
    }

    fun getAppInfo(): ApplicationInfo {
        return appInfo
    }

    fun setAppInfo(appinfo: ApplicationInfo) {
        this.appInfo = appinfo
    }

    fun getTotalnoOfBytesUsed(): Double? {
        TotalDataUsed=noof_bytes_downlaoded+noof_bytes_uploaded
        return TotalDataUsed
    }
    fun getTotalDataUsed():Double?
    {
        return if (TotalDataUsed!=null) TotalDataUsed else getTotalnoOfBytesUsed()
    }

    fun setTotalDataUsed(TotalDataUsed:Double){
        this.TotalDataUsed=TotalDataUsed
    }
    fun getProperDataUsageInFormating():String{
        return convertToproperStorageType(getTotalDataUsed())
    }

    fun convertToproperStorageType(data :Double?):String {
        var d :Double=0.0
        if (data!! >= (1024.0 * 1000.0 * 1000.0 * 1000.0)) {
            d = data / (1024.0 * 1000.0 * 1000.0 * 1000.0);
            return String.format("%.2f", d) + " TB";
        }
        if (data!! >= (1024.0 * 1000.0 * 1000.0)) {
            d= data / (1024.0 * 1000.0 * 1000.0)
            return String.format("%.2f", d) + " GB";
        }
        if (data!! >= (1024.0 * 1000.0)) {
           d= data / (1024.0 * 1000.0);
            return String.format("%.2f", d) + " MB";
        }
        if (data!! >= 1024.0) {
            d =data / 1024.0;
            return String.format("%.2f", d) + " KB";
        }
        return String.format("%.2f", d) + " by";
    }
}