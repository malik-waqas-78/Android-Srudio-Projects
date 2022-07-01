package com.phone.clone.datautils

import android.content.Context
import android.os.AsyncTask
import android.os.Environment
import com.phone.clone.constants.HSMyConstants
import com.phone.clone.interfaces.HSUtilsCallBacks
import com.phone.clone.modelclasses.HSFileSharingModel
import java.io.File

class HSAudiosUtils(var context: Context) {


    companion object {
        var audiosList = ArrayList<HSFileSharingModel>()
    }

    var handlersActionNotifier = context as HSUtilsCallBacks


    fun getListSize(): Int {
        return audiosList.size
    }


    fun getJustSize(): Long {
        var totalBytes = 0L
        for (i in audiosList) {
            totalBytes += i.sizeInBytes
        }
        return totalBytes
    }

    fun loadData() {
        TaskLoadAudios().execute()
    }

    inner class TaskLoadAudios : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            val rootFile = File(Environment.getExternalStorageDirectory().absolutePath + "/")
            if (audiosList.isEmpty()) {
                getAudiosList(rootFile)
            }

            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            handlersActionNotifier.doneLoadingAudios()
        }

    }

    private fun getAudiosList(rootFile: File?) {

        if (rootFile != null) {
            val listFiles = rootFile.listFiles()
            if (listFiles != null && listFiles.isNotEmpty()) {
                for (file in listFiles) {
                    if (file.name.toLowerCase() != "android" && file.isDirectory) {
                        getAudiosList(file)
                    } else if (file.name.toLowerCase() != "android") {
                        val ext = file.extension.toLowerCase()
                        if (ext.contains("mp3") || ext.contains("opus") || ext.contains("pcm") || ext.contains(
                                "wave"
                            )
                        ) {
                            audiosList.add(
                                HSFileSharingModel(
                                    file.absolutePath,
                                    HSMyConstants.FILE_TYPE_AUDIOS,
                                    getActualPath(file.absolutePath)
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    fun getActualPath(path: String): String {
        var actualPath =
            path?.replace(Environment.getExternalStorageDirectory().absolutePath, "")
        return actualPath ?: ""
    }

}