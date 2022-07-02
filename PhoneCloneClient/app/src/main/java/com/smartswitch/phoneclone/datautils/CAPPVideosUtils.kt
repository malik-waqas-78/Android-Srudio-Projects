package com.smartswitch.phoneclone.datautils

import android.content.ContentUris
import android.content.Context
import android.os.AsyncTask
import android.provider.MediaStore
import android.util.Log
import com.smartswitch.phoneclone.interfaces.CAPPUtilsCallBacks
import com.smartswitch.phoneclone.interfaces.CAPPMyInterFace
import com.smartswitch.phoneclone.modelclasses.CAPPMediaModelClass
import java.io.*

class CAPPVideosUtils(var context: Context, var HSMyInterFace: CAPPMyInterFace?) {

   companion object{
       var videosList = ArrayList<CAPPMediaModelClass>()
   }

    var handlersActionNotifier = context as CAPPUtilsCallBacks
    var totalSize = 0;
    var totalBytesToSendOrReceive=0L

    fun getListSize(): Int {
        return videosList.size
    }

    fun getJustSize():Long{
        var totalBytes = 0L
        for (i in videosList) {
            totalBytes += i.size!!.toLong()
        }
        return totalBytes
    }

    fun loadData() {
        LoadVideoss().execute()
    }
    inner class LoadVideoss: AsyncTask<Void, Void, Void?>(){
        override fun doInBackground(vararg params: Void?): Void? {
            if(videosList.isEmpty()){
                getVideos()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            handlersActionNotifier?.doneLoadingVideos()
        }

    }
    private fun getVideos() {


        videosList.clear()
        val contentResolver = context.contentResolver
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            "datetaken",
            "_data",
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.WIDTH,
            MediaStore.Video.Media.HEIGHT,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.SIZE
        )
        val cursor = contentResolver.query(uri, projection, null, null, null)
        var columnIndexId = cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        var clmIndex_dateAdded = cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)

        var clmIndex_dateTaken = cursor!!.getColumnIndexOrThrow("datetaken")
        var clmIndex_size = cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
        var clmindex_mimType = cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)
        var clmindex_width = cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH)
        var clmindex_height = cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)
        var clmindex_title = cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
        var clmindex_displayName =
            cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
        var clmindex_data = cursor!!.getColumnIndexOrThrow("_data")
        var count = 0
        if (cursor != null && cursor.moveToFirst()) {
            do {

                val videosModelClass = CAPPMediaModelClass()
                videosModelClass.contentUri =
                    ContentUris.withAppendedId(uri, cursor.getLong(columnIndexId)).toString()
                videosModelClass.dateAdded = cursor.getString(clmIndex_dateAdded)
                videosModelClass.path = cursor.getString(clmindex_data)
                videosModelClass.dateTaken = cursor.getString(clmIndex_dateTaken)

                videosModelClass.mimType = cursor.getString(clmindex_mimType)
                videosModelClass.width = cursor.getString(clmindex_width)
                videosModelClass.height = cursor.getString(clmindex_height)
                videosModelClass.title = cursor.getString(clmindex_title)
                videosModelClass.displayName = cursor.getString(clmindex_displayName)
                Log.d("92727", "VideoPath: ${videosModelClass.path}")
                //find error null pointer
                val file=File(videosModelClass.path)
                videosModelClass.size = file.length()
                videosModelClass.setSizeInProperFormat()
                totalBytesToSendOrReceive +=videosModelClass.size!!.toLong()
               if(videosModelClass.size!!.toLong()!=0L){
                   videosList.add(videosModelClass)
               }
                count++
            } while (cursor.moveToNext())
        }
        cursor.close()
        handlersActionNotifier.doneLoadingVideos()
    }
}