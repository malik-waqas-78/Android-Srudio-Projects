package com.smartswitch.phoneclone.datautils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.util.Log
import com.smartswitch.phoneclone.R
import com.smartswitch.phoneclone.constants.CAPPMConstants.get.TAG
import com.smartswitch.phoneclone.interfaces.CAPPUtilsCallBacks
import com.smartswitch.phoneclone.interfaces.CAPPMyInterFace
import com.smartswitch.phoneclone.modelclasses.CAPPAppsModel
import java.io.*


class CAPPAppsUtils(var context: Context, var HSMyInterFace: CAPPMyInterFace?) {


    var handlersActionNotifier = context as CAPPUtilsCallBacks

    var packageManager: PackageManager = context.packageManager
    var totalBytesToSendOrReceive: Long=0L
   companion object{
       var apksList = ArrayList<CAPPAppsModel>()
   }

    fun getJustSzie(): Long {
        var totalBytes = 0L
        for (i in apksList) {
            var f = File(i!!.srcDir)
            totalBytes += f.length()!!.toLong()
        }
        return totalBytes
    }



    fun loadData() {
        // Thread { getCalendarEvents() }.start()
        AsyncTaskToLoadData().execute()
    }

    inner class AsyncTaskToLoadData : AsyncTask<String, Int, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String {
            if(apksList.isEmpty()){
                apksList = loadApps()
            }
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            handlersActionNotifier.doneLoadingApks()
        }
    }

    fun loadApps(): ArrayList<CAPPAppsModel> {
        return checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA))
    }

    private fun checkForLaunchIntent(list: List<ApplicationInfo>): ArrayList<CAPPAppsModel> {
        Log.d(TAG, "checkForLaunchIntent: list size ${list.size}")
        val appList = ArrayList<CAPPAppsModel>()
        var count = 0
        for (info in list) {
            Log.d(TAG, "checkForLaunchIntent: src dir ${info.sourceDir}")
            Log.d(TAG, "checkForLaunchIntent: name  ${info.name}")
            Log.d(TAG, "checkForLaunchIntent: package name ${info.packageName}")
            try {
                if (packageManager.getLaunchIntentForPackage(info.packageName) != null) {
                    if (!isSystemPackage(
                            packageManager.getPackageInfo(
                                info.packageName,
                                PackageManager.GET_META_DATA
                            )
                        ) && context.resources.getString(R.string.app_name) != info.loadLabel(packageManager)
                    ) {
                        var app = CAPPAppsModel().apply {
                            apkName = info.loadLabel(packageManager).toString()
                            icon=info.loadIcon(packageManager)
                            srcDir = info.sourceDir
                            var f = File(info.sourceDir)
                            size = f.length()
                            setSizeInProperFormat()
                            totalBytesToSendOrReceive += size
                            Log.d(TAG, "checkForLaunchIntent: added ${info.packageName}")
                        }
                        appList.add(app)
                        count++
                        /*  if (count >= 3) {
                              Log.d(TAG, "checkForLaunchIntent: ${info.packageName}")
                              break
                          }*/

                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG, "checkForLaunchIntent: exception inserting app ${e.message}")
            }
        }
        Log.d(TAG, "checkForLaunchIntent: size of app ${appList.size}")
        return appList
    }

    private fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        Log.d(TAG, "isSystemPackage: system package ${pkgInfo.packageName}")
    }

}
