package com.phoneclone.data.modelclasses

import com.phoneclone.data.R
import com.phoneclone.data.constants.MyConstants
import java.io.Serializable

class DetailsInfoToTransferClass :Serializable{

    var OPONENT_NAME=""
    var SELF_NAME=""


    var totalSizeInBytes: Long = 0
    var totalBytesTransferred:Long=0

    var totalNoOfFilesToShare: Int = 0

    var sizeInProperFormat = ""

    var noOfPhotosToShare = 0
    var noOfVideosToShare = 0
    var noOfAppsToShare = 0
    var noOfAudiosToShare=0
    var noOfDocsToShare=0
    var noOfContactsToShare = 0
    var noOfCalendarEventsToShare = 0

    var totalPhotosBytes:Double=0.0
    var totalCalendarBytes:Double=0.0
    var totalAppsBytes:Double=0.0
    var totalVideosBytes:Double=0.0
    var totalContactsBytes:Double=0.0
    var totalAudiosBytes:Double=0.0
    var totalDocBytes:Double=0.0

    var photosBytesTransferred:Double=0.0
    var videosBytesTransferred:Double=0.0
    var appsBytesTransferred:Double=0.0
    var contactsBytesTransferred:Double=0.0
    var audiosBytesTransferred:Double=0.0
    var docsBytesTransferred:Double=0.0
    var calendarEventsBytesTransferred:Double=0.0

    fun getTotalSizeToSend() {
        var totalBytesToSend: Long = 0

        noOfAppsToShare = 0
        noOfContactsToShare=0
        noOfPhotosToShare=0
        noOfVideosToShare=0
        noOfCalendarEventsToShare=0
        noOfAudiosToShare=0
        noOfDocsToShare=0

        MyConstants.FILES_TO_SHARE.forEach {
            totalBytesToSend += it.sizeInBytes
            when (it.fileType) {
                MyConstants.FILE_TYPE_CALENDAR -> {
                    noOfCalendarEventsToShare++
                    totalCalendarBytes+=it.sizeInBytes
                }
                MyConstants.FILE_TYPE_PICS -> {
                    noOfPhotosToShare++
                    totalPhotosBytes+=it.sizeInBytes
                }
                MyConstants.FILE_TYPE_VIDEOS -> {
                    noOfVideosToShare++
                    totalVideosBytes+=it.sizeInBytes
                }
                MyConstants.FILE_TYPE_APPS -> {
                    noOfAppsToShare++
                    totalAppsBytes+=it.sizeInBytes
                }
                MyConstants.FILE_TYPE_CONTACTS -> {
                    noOfContactsToShare++
                    totalContactsBytes+=it.sizeInBytes
                }
                MyConstants.FILE_TYPE_DOCS->{
                    noOfDocsToShare++
                    totalDocBytes+=it.sizeInBytes
                }
                MyConstants.FILE_TYPE_AUDIOS->{
                    noOfAudiosToShare++
                    totalAudiosBytes+=it.sizeInBytes
                }
            }
        }
        totalSizeInBytes=totalBytesToSend
        totalNoOfFilesToShare=MyConstants.FILES_TO_SHARE.size
    }

   fun convertToProperStorageType() {
       var data=totalSizeInBytes
        var d: Double = 0.0
        if (data >= (1000.0 * 1000.0 * 1000.0 * 1000.0)) {
            d = data / (1000.0 * 1000.0 * 1000.0 * 1000.0);
            sizeInProperFormat=String.format("%.2f", d) + " TB";
            return
        }
        if (data >= (1000.0 * 1000.0 * 1000.0)) {
            d = data / (1000.0 * 1000.0 * 1000.0);
            sizeInProperFormat=String.format("%.2f", d) + " GB";
            return
        }
        if (data >= (1000.0 * 1000.0)) {
            d = data / (1000.0 * 1000.0);
            sizeInProperFormat=String.format("%.2f", d) + " MB";
            return
        }
        if (data >= 1000.0) {
            d = data / 1000.0;
            sizeInProperFormat=String.format("%.2f", d) + " KB";
            return
        }
       sizeInProperFormat=String.format("%.2f", d) + " B";
    }

    fun getContactsProgress(): Int {

        return ((contactsBytesTransferred*100.0)/totalContactsBytes).toInt()
    }
    fun getEventsProgress(): Int {

        return ((calendarEventsBytesTransferred*100.0)/totalCalendarBytes).toInt()
    }
    fun getAppsProgress(): Int {

        return ((appsBytesTransferred*100.0)/totalAppsBytes).toInt()
    }
    fun getPhotosProgress(): Int {

        return ((photosBytesTransferred*100.0)/totalPhotosBytes).toInt()
    }
    fun getVideosProgress(): Int {

        return ((videosBytesTransferred*100.0)/totalVideosBytes).toInt()
    }
    fun getDocsProgress(): Int {

        return ((docsBytesTransferred*100.0)/totalDocBytes).toInt()
    }
    fun getAudiosProgress(): Int {

        return ((audiosBytesTransferred*100.0)/totalAudiosBytes).toInt()
    }

    fun getProgressInHumanFormat():String{
        var data=totalBytesTransferred
        var d: Double = 0.0
        if (data >= (1000.0 * 1000.0 * 1000.0 * 1000.0)) {
            d = data / (1000.0 * 1000.0 * 1000.0 * 1000.0);
           return String.format("%.2f", d) + " TB";

        }
        if (data >= (1000.0 * 1000.0 * 1000.0)) {
            d = data / (1000.0 * 1000.0 * 1000.0);
           return  String.format("%.2f", d) + " GB";

        }
        if (data >= (1000.0 * 1000.0)) {
            d = data / (1000.0 * 1000.0);
            return String.format("%.2f", d) + " MB";

        }
        if (data >= 1000.0) {
            d = data / 1000.0;
            return  String.format("%.2f", d) + " KB";

        }
        return  String.format("%.2f", d) + " B";
    }
    fun getSizeInHumanFormat(mData:Double?):String{
        if(mData==null){
            return "0.00 B"
        }
        var data=mData
        var d: Double = 0.0
        if (data >= (1000.0 * 1000.0 * 1000.0 * 1000.0)) {
            d = data / (1000.0 * 1000.0 * 1000.0 * 1000.0);
            return String.format("%.2f", d) + " TB";

        }
        if (data >= (1000.0 * 1000.0 * 1000.0)) {
            d = data / (1000.0 * 1000.0 * 1000.0);
            return  String.format("%.2f", d) + " GB";

        }
        if (data >= (1000.0 * 1000.0)) {
            d = data / (1000.0 * 1000.0);
            return String.format("%.2f", d) + " MB";

        }
        if (data >= 1000.0) {
            d = data / 1000.0;
            return  String.format("%.2f", d) + " KB";

        }
        return  String.format("%.2f", d) + " B";
    }

    fun getTotalProgress():Int{
        return ((totalBytesTransferred*100.0)/totalSizeInBytes).toInt()
    }

    fun updateProgress(len: Int,fileType:String) {
        when(fileType){
            MyConstants.FILE_TYPE_CALENDAR -> {
                calendarEventsBytesTransferred+=len
            }
            MyConstants.FILE_TYPE_PICS -> {
              photosBytesTransferred+=len
            }
            MyConstants.FILE_TYPE_VIDEOS -> {
                videosBytesTransferred+=len
            }
            MyConstants.FILE_TYPE_APPS -> {
                appsBytesTransferred+=len
            }
            MyConstants.FILE_TYPE_CONTACTS -> {
                contactsBytesTransferred+=len
            }
            MyConstants.FILE_TYPE_AUDIOS->{
                audiosBytesTransferred+=len
            }
            MyConstants.FILE_TYPE_DOCS->{
                docsBytesTransferred+=len
            }
        }
        totalBytesTransferred+=len
    }

    companion object{
        val CONTACT_ICON:Int= R.drawable.ic_contact
        val IMAGES_ICON=R.drawable.ic_pics
        val Videos_ICON=R.drawable.ic_video
        val CALEDAR_ICON=R.drawable.ic_calendar
        val APPS_ICON=R.drawable.ic_apk
        val AUDIOS_ICON=R.drawable.ic_audio
        val DOCS_ICON=R.drawable.ic_docs
    }
}