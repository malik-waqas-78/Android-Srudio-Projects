package com.smartswitch.phoneclone.modelclasses

import android.content.Context
import android.graphics.drawable.Drawable

import com.smartswitch.phoneclone.R

import com.smartswitch.phoneclone.constants.CAPPMConstants
import java.util.*

class CAPPFilesToShareModel(var context: Context, fileType: String, items: Int, totalBytes: Long?) {

    var isSelected = false

    var fileType = fileType
    var dataToShareInBytes: Long? = totalBytes
    var noOfItems: Int = items
    var dataToShareInFormat = ""
    var sizeDetails = ""
    var itemsCountDetails = ""
    var icon: Drawable? = null
    var innerList= ArrayList<Any>()
    var selectionDetails = ""
    var selectionDetailsTotal = ""

    init {
        dataToShareInFormat = convertToProperStorageType(dataToShareInBytes?.toDouble())
        when (fileType) {
            CAPPMConstants.FILE_TYPE_APPS -> {
                sizeDetails = """ $dataToShareInFormat"""
                itemsCountDetails = """Items : $noOfItems """
                icon = context.applicationContext.resources.getDrawable(R.drawable.apps, null)
            }
            CAPPMConstants.FILE_TYPE_VIDEOS -> {
                sizeDetails = """ $dataToShareInFormat"""
                itemsCountDetails = """Items : $noOfItems """
                icon = context.applicationContext.resources.getDrawable(R.drawable.videos, null)
            }
            CAPPMConstants.FILE_TYPE_CALENDAR -> {
                sizeDetails = """ --- """
                itemsCountDetails = """Items : $noOfItems """
                icon = context.applicationContext.resources.getDrawable(R.drawable.calender, null)
            }
            CAPPMConstants.FILE_TYPE_PICS -> {
                sizeDetails = """ $dataToShareInFormat"""
                itemsCountDetails = """Items : $noOfItems """
                icon = context.applicationContext.resources.getDrawable(R.drawable.gallery, null)
            }
            CAPPMConstants.FILE_TYPE_CONTACTS -> {
                sizeDetails = """ --- """
                itemsCountDetails = """Items : $noOfItems """
                icon = context.applicationContext.resources.getDrawable(R.drawable.msg, null)
            }
            CAPPMConstants.FILE_TYPE_DOCS -> {
                sizeDetails = """ $dataToShareInFormat"""
                itemsCountDetails = """Items : $noOfItems """
                icon = context.applicationContext.resources.getDrawable(R.drawable.docs, null)
            }
            CAPPMConstants.FILE_TYPE_AUDIOS -> {
                sizeDetails = """ $dataToShareInFormat"""
                itemsCountDetails = """Items : $noOfItems """
                icon = context.applicationContext.resources.getDrawable(R.drawable.audio, null)
            }
        }
    }

    private fun convertToProperStorageType(data: Double?): String {
        var d: Double = 0.0
        if (data == null) {
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