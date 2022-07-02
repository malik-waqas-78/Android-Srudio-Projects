package com.smartswitch.phoneclone.modelclasses

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import com.smartswitch.phoneclone.constants.CAPPMConstants
import java.io.File
import java.io.Serializable

class CAPPFileSharingModel(var filePath:String?, var fileType:String, var pathToSave:String, var fileName:String="") :Serializable{

    var sizeInBytes:Long=0
    var sizeInFormat:String=""

    var payloadID=""
    var icon=""
    var parentPath=""
    var isSelected=false

    init {
        if(fileName.isEmpty()){
            fileName=getFileNameFromPath()
        }
        sizeInBytes=File(filePath).length()
        sizeInFormat=convertToProperStorageType(sizeInBytes.toDouble())
    }

    fun populate(context: Context){
        val packageManager = context.packageManager as PackageManager

        var pathToApk = filePath
        var packageInfo = packageManager.getPackageArchiveInfo(pathToApk!!, 0);
        // those two lines do the magic:
        try {
            if (packageInfo != null) {
                fileName = packageManager.getApplicationLabel(packageInfo.applicationInfo) as String
            }
        } catch (exception: Exception) {
            //exception occurred
        }
    }
    private fun getFileNameFromPath(): String {
        val index=filePath?.lastIndexOf("/")
        val temp=filePath
        parentPath= getActualPath("")
        if (index != null) {
            return temp?.substring(index+1)!!
        }
        return ""
    }

    private fun convertToProperStorageType(data: Double): String {
        var d: Double = 0.0
        if (data >= (1000.0 * 1000.0 * 1000.0 * 1000.0)) {
            d = data / (1000.0 * 1000.0 * 1000.0 * 1000.0);
            return String.format("%.2f", d) + " TB";
        }
        if (data >= (1000.0 * 1000.0 * 1000.0)) {
            d = data / (1000.0 * 1000.0 * 1000.0);
            return String.format("%.2f", d) + " GB";
        }
        if (data >= (1000.0 * 1000.0)) {
            d = data / (1000.0 * 1000.0);
            return String.format("%.2f", d) + " MB";
        }
        if (data >= 1000.0) {
            d = data / 1000.0;
            return String.format("%.2f", d) + " KB";
        }
        return String.format("%.2f", d) + " B";
    }

    fun getThisFilesType():String{
        return CAPPMConstants.FILE_TYPE
    }


    fun getMyFileName():String {
        var name=fileName
        if(fileType==CAPPMConstants.FILE_TYPE_APPS){
            name+=".apk"
        }
        return name;
    }
    fun getParentName():String{
        val parent= File(filePath).parentFile
        return parent.name
    }
    fun getActualPath():String{
        var actualPath=filePath?.replace(Environment.getExternalStorageDirectory().absolutePath,"")
        return actualPath?:""
    }
    private fun getActualPath(p:String):String{
        val pathParam=if(p.isEmpty()){
            pathToSave
        }else{
            p
        }
        var actualPath= pathParam.replace(Environment.getExternalStorageDirectory().absolutePath,"")
        pathToSave=actualPath
        actualPath=actualPath.substring(0,actualPath.lastIndexOf("/")+1);
        return actualPath?:""
    }
}