package com.recovery.data.forwhatsapp.fileobserverspkg

import android.os.Build
import android.os.FileObserver
import android.util.Log
import com.recovery.data.forwhatsapp.constants.MyConstantsOKRA
import java.io.File
import java.lang.Exception

class ManageObserver {
    val OBS_TAG = "OBS_TAG"
    public var directoriesBeingObserved = ArrayList<FileObserver>()
    var pathsBeingObserved = ArrayList<String>()

    var backupPath: String? = null
    var recycleBinPath: String? = null
    var rootPathToWatch = ArrayList<String>()

    companion object {

        private var instance: ManageObserver? = null

        fun getInstance(): ManageObserver? {
            if (instance == null) {
                instance = ManageObserver()
            }
            return instance
        }
    }

    fun setPath(bPath: String?, rPath: String?, rootPathToWatch: ArrayList<String>) {
        backupPath = bPath
        recycleBinPath = rPath
        this.rootPathToWatch = rootPathToWatch
    }

    fun observe() {
        for (rootPath in rootPathToWatch) {
            try {
                val rootFile = File(rootPath)
                if (rootFile.exists()) {
                    getDirectoriesAndWatch(rootFile)
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun getDirectoriesAndWatch(rootFile: File) {
        val listFiles = rootFile.listFiles()
        if (listFiles != null) {
            for (file in listFiles) {
                if (file.isDirectory && !file.isHidden && (file.absolutePath.contains(MyConstantsOKRA.IMAGES_PATH) ||
                            file.absolutePath.contains(MyConstantsOKRA.VIDEOS_PATH) || file.absolutePath.contains(
                        MyConstantsOKRA.ANIMATIONS_PATH
                    ) ||
                            file.absolutePath.contains(MyConstantsOKRA.STICKERS_PATH) || file.absolutePath.contains(
                        MyConstantsOKRA.VOICE_NOTES
                    ) ||
                            file.absolutePath.contains(MyConstantsOKRA.DOCUMENTS_PATH) || file.absolutePath.contains(
                        MyConstantsOKRA.AUDIO_PATH
                    ))
                ) {
                    createFileObserver(file)
                    if(file.absolutePath.contains(MyConstantsOKRA.VOICE_NOTES)){
                        getDirectoriesAndWatch(file)
                    }
                }
            }
        }
    }

    fun createFileObserver(file: File) {
        if (!file.exists()) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val mask: Int = FileObserver.DELETE or FileObserver.CREATE
            val fileObserver = object : FileObserver(file) {
                val mPath = file.absolutePath

                override fun startWatching() {
                    super.startWatching()
                    Log.d(OBS_TAG, "startWatching: ${file.path}")
                    pathsBeingObserved.add(mPath)
                    directoriesBeingObserved.add(this)
                }

                override fun onEvent(event: Int, path: String?) {
                    Log.d(OBS_TAG, "onEvent: $path")
                    if (path == null) {
                        return
                    }
                    if (File(mPath + "/" + path).isDirectory) {
                        if ((event and FileObserver.CREATE) == CREATE) {
                            createFileObserver(File(file.path + File.separatorChar + path))
                        }
                        return
                    }
                    Log.d(OBS_TAG, "onEvent: $path")
                    if ((event and DELETE) == DELETE) {
                        val name = path.toString()
                        if (name != "") {
                            val dir: String = mPath.getDir()
                            if (dir.isNotEmpty())
                                recoverThisFile(name, dir)
                        }
                    } else if(event != FileObserver.ACCESS && event != FileObserver.MOVED_FROM && event != FileObserver.OPEN && event != 1073741840 && event != 1073741825
                            && event != CLOSE_NOWRITE || (event == CREATE || event == MOVED_TO)
                            ) {
                        val name = path.toString()
                        if (name != "") {
                            val dir: String = mPath.getDir()
                            if (dir.isNotEmpty())
                                backupThisFile(name, dir, mPath + File.separatorChar + path)
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
                    Log.d(OBS_TAG, "finalize: ${file.path}")
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
                    pathsBeingObserved.add(mPath)
                    directoriesBeingObserved.add(this)
                }

                override fun onEvent(event: Int, path: String?) {

                    Log.d(OBS_TAG, "onEvent: $event $path")
                    if (path == null) {
                        return
                    }
                    if (File(mPath + "/" + path).isDirectory) {
                        if (event == CREATE) {
                            createFileObserver(File(file.path))
                        }
                        return
                    }
                    if (event == DELETE) {
                        val name = path
                        if (name != "") {
                            val dir: String = mPath.getDir()
                            recoverThisFile(name, dir)
                        }
                    } else if (Build.VERSION.SDK_INT >= 29) {
                        if (event == CREATE) {
                            val name = path.toString()
                            if (name != "") {
                                val dir: String = mPath.getDir()
                                backupThisFile(name, dir, mPath + File.separatorChar + path)
                            }
                        }
                    } else {
                        if (event != FileObserver.ACCESS && event != FileObserver.MOVED_FROM && event != FileObserver.OPEN && event != 1073741840 && event != 1073741825
                            && event != CLOSE_NOWRITE || (event == CREATE || event == MOVED_TO)
                        ) {
                            val name = path.toString()
                            if (name != "") {
                                val dir: String = mPath.getDir()
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
        val rootFolderPath = folderPath + dir
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
        val folderPath = backupPath
        val rootFolderPath = folderPath + dir
        val rootFile = File(rootFolderPath)
        if (rootFile.exists()) {

            val srcFile = File(rootFile.absolutePath + File.separator + name)
            if (srcFile.exists()) {
                copyFileToRecycleBin(srcFile, dir)
            }
        }

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

    when {
        this.contains(MyConstantsOKRA.IMAGES_PATH) -> {
            return "/${MyConstantsOKRA.IMAGES_PATH}"
        }
        this.contains(MyConstantsOKRA.VIDEOS_PATH) -> {
            return "/${MyConstantsOKRA.VIDEOS_PATH}"
        }
        this.contains(MyConstantsOKRA.DOCUMENTS_PATH) -> {
            return "/${MyConstantsOKRA.DOCUMENTS_PATH}"
        }
        this.contains(MyConstantsOKRA.AUDIO_PATH) -> {
            return "/${MyConstantsOKRA.AUDIO_PATH}"
        }
        this.contains(MyConstantsOKRA.STICKERS_PATH) -> {
            return "/${MyConstantsOKRA.STICKERS_PATH}"
        }
        this.contains(MyConstantsOKRA.ANIMATIONS_PATH) -> {
            return "/${MyConstantsOKRA.ANIMATIONS_PATH}"
        }
        this.contains(MyConstantsOKRA.VOICE_NOTES) -> {
            return "/${MyConstantsOKRA.VOICE_NOTES}"
        }
        else -> {
            return ""
        }
    }
}
