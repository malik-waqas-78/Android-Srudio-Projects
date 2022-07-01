package com.data.usage.manager.interfaces

import com.data.usage.manager.modelclasses.AppsInfo_ModelClass

interface DataUsageInterface {
    fun preDataConsumedByDevice()
    fun postDataConsumedByDevice(retrivedData : HashMap<String, String>)
    fun preTodayDataUsage()
    fun postTodayDataUsage(retrivedData : HashMap<String, String>)
    fun preDataConsumedByApp()
    fun postDataConsumedByApp(retrievedData: java.util.HashMap<String, String>, appsInfo_modelClass: AppsInfo_ModelClass?, index: Int, size: Int)
    fun preTethringDataUsage()
    fun postTethringDataUsage(retrivedData : HashMap<String, String>)
    fun preRemovedAppsDataUsage()
    fun postRemovedAppsDataUsage(retrivedData : HashMap<String, String>)
    fun preSystemAppsDataUsed()
    fun postSystemAppsDataUsed(retrivedData : HashMap<String, String>)
    fun totalNoOfPackages(count:Int)
}