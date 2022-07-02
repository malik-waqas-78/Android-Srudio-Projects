package com.photo.recovery.utils

import android.os.Build
import android.os.Environment
import android.os.FileObserver
import android.util.Log
import com.photo.recovery.constants.MyConstantsAAT
import com.photo.recovery.constants.MyConstantsAAT.Companion.OBS_TAG
import java.io.File
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ManageObserver {
    var directoriesBeingObserved = ArrayList<FileObserver>()
    var pathsBeingObserved: MutableList<String> = Collections.synchronizedList(ArrayList<String>())
   /* fun getRecycleBinPath(): String {
        return Environment.getExternalStorageDirectory().absolutePath + "/waqas"*//*"/Android/data/com.codesk.recyclebin/files/recycle Bin"*//*
    }

    fun getBackUPBinPath(): String {
        return Environment.getExternalStorageDirectory().absolutePath + "/waqas"*//*"/Android/data/com.codesk.recyclebin/files/backUp Bin"*//*
        //context.getExternalFilesDir(null)?.absolutePath+"/backUp Bin/"
    }
*/
    var backupPath:String?=null
    var recycleBinPath:String?=null

    companion object {
        private var instance: ManageObserver? = null

        val managerInstance: ManageObserver
            get() {
                if (instance == null) {
                    instance = ManageObserver()
                }
                return instance!!
            }
    }

    fun setPath(bPath:String?,rPath:String?){
        backupPath=bPath
        recycleBinPath=rPath
    }

    fun observe() {
        directoriesBeingObserved.clear()
        pathsBeingObserved.clear()
        val rootFile = File(Environment.getExternalStorageDirectory().absolutePath)
        getDirectoriesAndWatch(rootFile)
    }

    private fun getDirectoriesAndWatch(rootFile: File) {
        val listFiles = rootFile.listFiles()
        if (listFiles != null) {
            for (file in listFiles) {
                if (file.isDirectory && !file.absolutePath.contains("Android")&&!file.absolutePath.contains("Recycle Bin")) {
                    createFileObserver(file)
                    getDirectoriesAndWatch(file)
                }
            }
        }
    }

    fun createFileObserver(file: File) {
        if (!file.exists()) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //val mask:Int=FileObserver.DELETE or FileObserver.CREATE
            val fileObserver = object : FileObserver(file) {
                val mPath = file.absolutePath

                override fun startWatching() {
                    super.startWatching()
                    Log.d(OBS_TAG, "startWatching: ${file.path}")
                   try{
                       pathsBeingObserved.add(mPath)
                       directoriesBeingObserved.add(this)
                   }catch (e:Exception){
                       e.printStackTrace()
                   }
                }

                override fun onEvent(event: Int, path: String?) {
                    Log.d(OBS_TAG, "onEvent: $path")
                    if (path == null) {
                        return
                    }
                    if (File(mPath + "/" + path).isDirectory) {
                        if((event and FileObserver.CREATE)== CREATE){
                            createFileObserver(File(file.path+File.separatorChar+path))
                        }
                        return
                    }
                    Log.d(OBS_TAG, "onEvent: $path")
                    if (event == DELETE||event== MOVED_TO|| event==MOVED_FROM) {
                            if(event== MOVED_TO&&!path.contains(".trash")&&!path.contains(".pending")){
                                //take backup
                                val name = getNameFromPath(path)
                                if (name != ""&&!name.startsWith(".pending")&&!name.startsWith(".trash")) {
                                    val dir: String = name.getDir()
                                    backupThisFile(name, dir, mPath + File.separatorChar + path)
                                }
                            }else{
                                val name = getNameFromPath(path)
                                if (name != "") {
                                    val dir: String = name.getDir()
                                    recoverThisFile(name, dir)
                                }
                            }
                    }else {
                        if (event!=MODIFY&&event != FileObserver.ACCESS && event != FileObserver.MOVED_FROM && event != FileObserver.OPEN && event != 1073741840 && event != 1073741825
                            && event != CLOSE_NOWRITE || (event == CREATE || event == MOVED_TO)
                        ) {
                            val name = getNameFromPath(path)
                            if (name != ""&&!name.startsWith(".pending")&&!name.startsWith(".trash")) {
                                val dir: String = name.getDir()
                                backupThisFile(name, dir, mPath + File.separatorChar + path)
                            }
                        }
                    }
                }

                override fun stopWatching() {
                    super.stopWatching()
                    Log.d(OBS_TAG, "startWatching: ${file.path}")
                    pathsBeingObserved.remove(mPath)
                    directoriesBeingObserved.remove(this)
                    createFileObserver(file)
                }

                override fun finalize() {
                    super.finalize()
                    directoriesBeingObserved.remove(this)
                    createFileObserver(file)
                }

            }
            fileObserver.startWatching()
        } else {
            val fileObserver = object : FileObserver(file.path) {
                val mPath = file.absolutePath

                override fun startWatching() {
                    super.startWatching()
                    Log.d(OBS_TAG, "startWatching: ${file.path}")
                   try{
                       pathsBeingObserved.add(mPath)
                       directoriesBeingObserved.add(this)
                   }catch (e:Exception){

                   }
                }

                override fun onEvent(event: Int, path: String?) {

                    Log.d(OBS_TAG, "onEvent: $event $path")
                    if (path == null) {
                        return
                    }
                    if (File(mPath + "/" + path).isDirectory) {
                        if(event== CREATE){
                            createFileObserver(File(file.path))
                        }
                        return
                    }
                    if (event == DELETE||event== MOVED_TO|| event==MOVED_FROM) {
                        val name = getNameFromPath(path)
                        if (name != "") {
                            val dir: String = name.getDir()
                            recoverThisFile(name, dir)
                        }
                    } else {
                        if (event != FileObserver.ACCESS && event != FileObserver.MOVED_FROM && event != FileObserver.OPEN && event != 1073741840 && event != 1073741825
                            && event != CLOSE_NOWRITE || (event == CREATE || event == MOVED_TO)
                        ) {
                            val name = getNameFromPath(path)
                            if (name != "") {
                                val dir: String = name.getDir()
                                backupThisFile(name, dir, mPath + File.separatorChar + path)
                            }
                        }
                    }
                }

                override fun stopWatching() {
                    super.stopWatching()
                    Log.d(OBS_TAG, "startWatching: ${file.path}")
                    pathsBeingObserved.remove(mPath)
                    directoriesBeingObserved.remove(this)
                    createFileObserver(file)
                }

                override fun finalize() {
                    super.finalize()
                    directoriesBeingObserved.remove(this)
                    createFileObserver(file)
                }

            }
            fileObserver.startWatching()
        }

    }

    private fun backupThisFile(name: String, dir: String, srcPath: String?) {
        val folderPath = backupPath
        if (srcPath == null) {
            return
        }
        val rootFolderPath = folderPath + File.separatorChar + dir
        val rootFile = File(rootFolderPath)
        if (!rootFile.exists()) {
            rootFile.mkdirs()
        }
        val trgFile = File(rootFolderPath + File.separatorChar + name)
        val srcFile = File(srcPath)
        if (!srcFile.exists()) {
            return
        }
        if (!trgFile.exists()) {
            if (trgFile.createNewFile()) {
                val outputStream = trgFile.outputStream()
                val inputStream = srcFile.inputStream()
                try {
                    val buffer = ByteArray(1024)
                    while (inputStream.read(buffer) > 0) {
                        outputStream.write(buffer)
                    }
                } catch (e: Exception) {
                    Log.e(OBS_TAG, "backupThisFile: ${e.cause}")

                } finally {
                    outputStream.close()
                    inputStream.close()
                }
            }
        }
    }

    private fun recoverThisFile(name: String, dir: String) {
        val folderPath =backupPath
        val rootFolderPath = folderPath + File.separatorChar + dir
        val rootFile = File(rootFolderPath)
        if (rootFile.exists()) {

            val srcFile=File(rootFile.absolutePath+File.separator+name)
            if(srcFile.exists()){
                copyFileToRecycleBin(srcFile, dir)
            }
           /* val listFiles = rootFile.listFiles()
            if (listFiles != null) {
                for (f in listFiles) {
                    if (f.name.contains(name)) {
                        copyFileToRecycleBin(f, dir)
                        return
                    }
                }
            }*/
        }

    }

    private fun getNameFromPath(path: String?): String {
       /* path?.let {
            val index = it.lastIndexOf(File.separatorChar)
            return if (index < path.length) path.substring(it.length) else it.substring(
                index + 1
            )
        }*/
        return path.toString()
    }

    private fun copyFileToRecycleBin(srcFile: File?, dir: String) {
        if (srcFile == null) {
            return
        }
        val recycleBinPath = recycleBinPath
        val recycleFolder = File(recycleBinPath + File.separatorChar + dir)
        if (!recycleFolder.exists()) {
            recycleFolder.mkdirs()
        }
        val tarFile =
            File(recycleFolder.absolutePath + File.separatorChar + srcFile.name)
        if (tarFile.exists() || !srcFile.exists()) {
            return
        } else {
            if (tarFile.createNewFile()) {
                val outPutStream = tarFile.outputStream()
                val inputStream = srcFile.inputStream()
                try {
                    val buffer = ByteArray(1024)

                    while (inputStream.read(buffer) > 0) {
                        outPutStream.write(buffer)
                        outPutStream.flush()
                    }
                } catch (e: Exception) {
                    Log.e(OBS_TAG, "copyFileToRecovery: ${e.cause}")
                    e.printStackTrace()
                } finally {
                    outPutStream.close()
                    inputStream.close()
                    srcFile.delete()
                }

            }
        }
    }

}

private fun String.getDir(): String {
    val ext = this.substringAfterLast(".", "")
    if (MyConstantsAAT.Images_EXT.contains(ext)) {
        return MyConstantsAAT.MEDIA_FOLDER_NAME + File.separatorChar + MyConstantsAAT.IMAGE_FOLDER_NAME
    } else if (MyConstantsAAT.Videos_Ext.contains(ext)) {
        return MyConstantsAAT.MEDIA_FOLDER_NAME + File.separatorChar + MyConstantsAAT.VIDEOS_FOLDER_NAME
    } else if (MyConstantsAAT.Doc_EXT.contains(ext)) {
        return MyConstantsAAT.DOC_FOLDER_NAME
    } else if (MyConstantsAAT.Audios_EXT.contains(ext)) {
        return MyConstantsAAT.MEDIA_FOLDER_NAME + File.separatorChar + MyConstantsAAT.AUDIOS_FOLDER_NAME
    } else {
        return MyConstantsAAT.OTHERS_FOLDER_NAME
    }
}
