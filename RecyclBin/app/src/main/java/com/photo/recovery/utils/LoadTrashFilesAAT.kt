package com.photo.recovery.utils

import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import java.io.File

class LoadTrashFilesAAT(context: TrashCallBack) {

    var trashCallBack: TrashCallBack = context
    var list = ArrayList<File>()
    fun loadImageFiles(recovery:Boolean,type:String) {
        Log.d("trashFiles", "loadFiles: ")
        LoadFilesInTrashBin(0,recovery,type).execute()
    }

    fun loadVideosFiles(recovery:Boolean,type:String) {
        LoadFilesInTrashBin(1,recovery,type).execute()
    }

    fun loadAudiosFiles(recovery:Boolean,type:String) {
        LoadFilesInTrashBin(2,recovery,type).execute()
    }

    fun loadAllDocRecoverFiles(type:String){
        LoadFilesInTrashBin(3,false,type).execute()
    }



    inner class LoadFilesInTrashBin(var type: Int = 0,var recovery: Boolean,var fileType:String) : AsyncTask<Void, Void, ArrayList<File>?>() {
        override fun doInBackground(vararg params: Void?): ArrayList<File>? {
            Log.d("trashFiles", "doInBackground: ")
            if (type == 0) {
                if(!recovery) {
                    loadAllImages(File(Environment.getExternalStorageDirectory().absolutePath))
                }else{
                    loadAllRecoveryFiles(File(Environment.getExternalStorageDirectory().absolutePath +
                            File.separatorChar+"Recycle Bin"+File.separatorChar+fileType))
                }
            } else if (type == 1) {
                if(!recovery){
                    loadAllVideos(File(Environment.getExternalStorageDirectory().absolutePath))
                }else{
                    loadAllRecoveryFiles(File(Environment.getExternalStorageDirectory().absolutePath +
                            File.separatorChar+"Recycle Bin"+File.separatorChar+fileType))
                }

            } else if (type == 2) {
                if(!recovery){
                    loadAllAudios(File(Environment.getExternalStorageDirectory().absolutePath))
                }else{
                    loadAllRecoveryFiles(File(Environment.getExternalStorageDirectory().absolutePath +
                            File.separatorChar+"Recycle Bin"+File.separatorChar+fileType))
                }

            }else if(type==3){
                loadAllRecoveryFiles(File(Environment.getExternalStorageDirectory().absolutePath +
                        File.separatorChar+"Recycle Bin"+File.separatorChar+fileType))
            }
            return null
        }

        override fun onPostExecute(result: ArrayList<File>?) {
            super.onPostExecute(result)
            trashCallBack.trashLoaded(list)

        }
    }

    private fun loadAllRecoveryFiles(file: File) {
        file.listFiles()?.forEach {
           list.add(it)
        }
    }



    private fun loadAllImages(rootFile: File) {
        var filesList = rootFile.listFiles()
        if (filesList != null) {
            for (f in filesList) {
                if (f.absolutePath.contains("Android") || f.absolutePath.contains("DCIM") || f.absolutePath.contains("Pictures")) {
                    continue
                }
                if (f.isDirectory) {
                    //Log.d("trashFiles", "loadAllTrashFiles: ${f.absolutePath}")
                    loadAllImages(f)
                } else if (f.length() != 0L && f.name.length >= 5 && f.name[f.name.length - 4] == '.' && !f.name.contains(
                        ".chk.tmp"
                    ) && !f.name.contains(".enc.tmp") && (f.extension.contains(
                        "jpg"
                    ) ||
                            f.extension.contains("png") || f.extension.contains("gif") || f.extension.contains(
                        "jpe"
                    )
                            || f.name.contains("trash") || f.absolutePath.contains(
                        "thumbnail"
                    )
                            || f.extension.contains("tmp")) || f.name.contains("shared")
                ) {
                    addFile(f)
                    Log.d("trashFiles", "loadAllTrashFiles: ${f.absolutePath}")
                }
            }
        }
    }
    private fun loadAllVideos(rootFile: File) {
        var filesList = rootFile.listFiles()
        if (filesList != null) {
            for (f in filesList) {
                if (f.name.equals("Android") || f.name.equals("DCIM") || f.name.equals("Pictures") ||f.name.equals("Movies")) {
                    continue
                }
                if (f.isDirectory) {
                    //Log.d("trashFiles", "loadAllTrashFiles: ${f.absolutePath}")
                    loadAllVideos(f)
                } else if (f.length() != 0L && f.name.length >= 5 && f.name[f.name.length - 4] == '.' && !f.name.contains(
                        ".chk.tmp"
                    ) && !f.name.contains(".enc.tmp") && (f.extension.contains("mp4")|| f.extension.contains("mkv"))
                ) {
                    addFile(f)
                    Log.d("trashFiles", "loadAllTrashFiles: ${f.absolutePath}")
                }
            }
        }
    }
    private fun loadAllAudios(rootFile: File) {
        var filesList = rootFile.listFiles()
        if (filesList != null) {
            for (f in filesList) {
                if (f.name.equals("Android") || f.name.equals("DCIM") || f.name.equals("Pictures")||f.name.equals("WhatsApp")) {
                    continue
                }
                if (f.isDirectory) {
                    //Log.d("trashFiles", "loadAllTrashFiles: ${f.absolutePath}")
                    loadAllAudios(f)
                } else if (f.length() != 0L &&  ( f.extension.contains("mp3")||f.extension.contains("wav")||f.extension.contains("opus"))
                ) {
                    addFile(f)
                    Log.d("trashFiles", "loadAllTrashFiles: ${f.absolutePath}")
                }
            }
        }
    }

    private fun addFile(f: File) {
        for (file in list) {
            if (file.name == f.name) {
                return
            }
        }
        if (f.length() >= (50 * 1024 * 8) && (f.extension.contains("mp4") || f.extension.contains("mkv"))) {
            list.add(f)
        } else if (!f.extension.contains("mp4") && !f.extension.contains("mkv")) {
            list.add(f)
        }

    }

    interface TrashCallBack {
        fun trashLoaded(files: ArrayList<File>)
    }
}