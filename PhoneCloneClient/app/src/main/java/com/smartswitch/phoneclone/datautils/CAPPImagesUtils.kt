package com.smartswitch.phoneclone.datautils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.provider.MediaStore
import android.util.Log
import com.smartswitch.phoneclone.interfaces.CAPPUtilsCallBacks
import com.smartswitch.phoneclone.interfaces.CAPPMyInterFace
import com.smartswitch.phoneclone.modelclasses.CAPPMediaModelClass
import java.io.*
import java.util.*

class CAPPImagesUtils(var context: Context, var HSMyInterFace: CAPPMyInterFace?) {

   companion object{
       var imagesList = ArrayList<CAPPMediaModelClass>()
   }

    var handlersActionNotifier = context as CAPPUtilsCallBacks



    fun getListSize(): Int {
        return imagesList.size
    }


    fun getJustSize():Long{
        var totalBytes = 0L
        for (i in imagesList) {
            totalBytes += i.size!!.toLong()
        }
        return totalBytes
    }
    fun loadData() {
        LoadImages().execute()
    }

    inner class LoadImages:AsyncTask<Void,Void,Void?>(){
        override fun doInBackground(vararg params: Void?): Void? {
            if(imagesList.isEmpty()){
                getAllShownImagesPath()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            handlersActionNotifier?.doneLoadingPics()
        }

    }

    private fun getAllShownImagesPath() {
        imagesList.clear()
        val cursor: Cursor?
        //var absolutePathOfImage: String? = null
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            "datetaken",
            "_data",
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.SIZE
        )
        cursor = context.contentResolver.query(
            uri, projection, null,
            null, null
        )
        var columnIndexId = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        var clmIndex_dateAdded = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
        var clmIndex_dateTaken = cursor!!.getColumnIndexOrThrow("datetaken")
        var clmIndex_size = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
        var clmindex_mimType = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)
        var clmindex_width = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
        var clmindex_height = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)
        var clmindex_title = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE)
        var clmindex_displayName =
            cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        var clmindex_data = cursor!!.getColumnIndexOrThrow("_data")

        if (cursor != null && cursor.moveToFirst()) {
            do {

                val imagesModelClass = CAPPMediaModelClass()
                imagesModelClass.contentUri =
                    ContentUris.withAppendedId(uri, cursor.getLong(columnIndexId)).toString()
                imagesModelClass.path = cursor.getString(clmindex_data)
                imagesModelClass.dateAdded = cursor.getString(clmIndex_dateAdded)
                imagesModelClass.dateTaken = cursor.getString(clmIndex_dateTaken)

                imagesModelClass.mimType = cursor.getString(clmindex_mimType)
                imagesModelClass.width = cursor.getString(clmindex_width)
                imagesModelClass.height = cursor.getString(clmindex_height)
                imagesModelClass.title = cursor.getString(clmindex_title)
                imagesModelClass.displayName = cursor.getString(clmindex_displayName)
                Log.d("92727", "getAllShownImagesPath: ${imagesModelClass.path}")
                val file=File(imagesModelClass.path)
                imagesModelClass.size = file.length()
                imagesModelClass.setSizeInProperFormat()
                imagesList.add(imagesModelClass)
            } while (cursor.moveToNext())

        }
        cursor.close()
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

}


