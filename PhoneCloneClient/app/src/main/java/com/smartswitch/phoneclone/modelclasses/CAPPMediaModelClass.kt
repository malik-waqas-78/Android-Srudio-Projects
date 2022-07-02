package com.smartswitch.phoneclone.modelclasses

import android.os.Environment
import java.io.File
import java.io.Serializable


// Thi
// s class can be use for both images and videos
class CAPPMediaModelClass : Serializable{
    var isSelected=false

    var contentUri:String?=null//path

    var displayName:String?=null//name+ext ceApp_1602212155188.jpg

    var size:Long?=null//size in bytes 3049870

    var mimType:String?=null //type/ext image/jpeg

    var title:String?=null //FaceApp_1602212155188

    var dateAdded:String?=null   // 1602212155

    var dateTaken:String?=null   //1602212155

    var width:String?=null //width

    var height:String?=null //height

    var path:String?=null //original path

    var fileType=""

    var sizeInFormat=""

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
    fun getParentName():String{
        val parent= File(path).parentFile
        return parent.name
    }
    fun getActualPath():String{
        var actualPath=path?.replace(Environment.getExternalStorageDirectory().absolutePath,"")
        return actualPath?:""
    }
}