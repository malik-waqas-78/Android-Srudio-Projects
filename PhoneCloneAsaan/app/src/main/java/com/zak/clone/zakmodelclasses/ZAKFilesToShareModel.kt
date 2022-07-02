package com.zak.clone.zakmodelclasses

import android.content.Context
import android.graphics.drawable.Drawable

import com.zak.clone.R

import com.zak.clone.zakconstants.ZakMyConstants

class ZAKFilesToShareModel(var context: Context, fileType: String, items: Int, totalBytes: Long?) {

    var isSelected = false

    var fileType = fileType
    var dataToShareInBytes: Long? = totalBytes
    var noOfItems: Int = items
    var dataToShareInFormat = ""
    var sizeDetails = ""
    var itemsCountDetails = ""
    var icon: Drawable? = null
    var innerList=ArrayList<Any>()
    var selectionDetails = ""
    var selectionDetailsTotal = ""

    init {
        dataToShareInFormat = convertToProperStorageType(dataToShareInBytes?.toDouble())
        when (fileType) {
            ZakMyConstants.FILE_TYPE_APPS -> {
                sizeDetails = """Size : $dataToShareInFormat"""
                itemsCountDetails = """Items : $noOfItems """
                icon = context.applicationContext.resources.getDrawable(R.drawable.ic_apps, null)
            }
            ZakMyConstants.FILE_TYPE_VIDEOS -> {
                sizeDetails = """Size : $dataToShareInFormat"""
                itemsCountDetails = """Items : $noOfItems """
                icon = context.applicationContext.resources.getDrawable(R.drawable.ic_videos, null)
            }
            ZakMyConstants.FILE_TYPE_CALENDAR -> {
                sizeDetails = """size : --- """
                itemsCountDetails = """Items : $noOfItems """
                icon = context.applicationContext.resources.getDrawable(R.drawable.ic_contacts, null)
            }
            ZakMyConstants.FILE_TYPE_PICS -> {
                sizeDetails = """Size : $dataToShareInFormat"""
                itemsCountDetails = """Items : $noOfItems """
                icon = context.applicationContext.resources.getDrawable(R.drawable.ic_photos, null)
            }
            ZakMyConstants.FILE_TYPE_CONTACTS -> {
                sizeDetails = """size : --- """
                itemsCountDetails = """Items : $noOfItems """
                icon = context.applicationContext.resources.getDrawable(R.drawable.ic_contacts, null)
            }
            ZakMyConstants.FILE_TYPE_DOCS -> {
                sizeDetails = """Size : $dataToShareInFormat"""
                itemsCountDetails = """Items : $noOfItems """
                icon = context.applicationContext.resources.getDrawable(R.drawable.ic_doc, null)
            }
            ZakMyConstants.FILE_TYPE_AUDIOS -> {
                sizeDetails = """Size : $dataToShareInFormat"""
                itemsCountDetails = """Items : $noOfItems """
                icon = context.applicationContext.resources.getDrawable(R.drawable.ic_audio, null)
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