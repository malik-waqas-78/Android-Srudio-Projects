package com.smartswitch.phoneclone.modelclasses

import android.graphics.drawable.Drawable
import com.smartswitch.phoneclone.constants.CAPPMConstants
import java.io.Serializable

class CAPPAppsModel :Serializable {


    var apkName= "" ///
    var srcDir = "" ///
    var size:Long=0L ///
    var installed=false
    var isSelected=false
    var icon:Drawable?=null
    var sizeInFormat=""
    var fileType=CAPPMConstants.FILE_TYPE_APPS
    fun setSizeInProperFormat(){
        sizeInFormat=convertToProperStorageType(size?.toDouble())
    }
    private fun convertToProperStorageType(data: Double?): String {
        var d: Double = 0.0
        if(data==null){
            return "0.00 B"
        }
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
}