package com.phoneclone.data.sockets

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.os.Looper
import android.util.Log


import com.phoneclone.data.R
import com.phoneclone.data.constants.MyConstants
import com.phoneclone.data.constants.MyConstants.get.TAG
import com.phoneclone.data.constants.MyConstants.get.TESTING_TAG
import com.phoneclone.data.interfaces.ServerCallBacks
import com.phoneclone.data.modelclasses.DetailsInfoToTransferClass
import com.phoneclone.data.modelclasses.FileSharingModel
import java.io.*
import java.net.*

class ReceivingServer(var context: Context) {
    companion object {
        var socket: Socket? = null
    }

    private var aceptConnectionsThread: Thread? = null
    private var acceptConnectionObj: AcceptConnections? = null

    private var callBacks: ServerCallBacks? = null

    var transferClass: DetailsInfoToTransferClass? = null

    var inputStream: InputStream? = null
    var outputStream: OutputStream? = null

    init {
        callBacks = context as ServerCallBacks
        try {
            /*server = ServerSocket()
            server?.reuseAddress = true
            server?.bind(InetSocketAddress(MyConstants.PORT_NO))*/

        } catch (e: IOException) {
            Log.e(TESTING_TAG, "error : ${e}")
        }
    }

    fun startAcceptingConnections() {
        acceptConnectionObj = AcceptConnections()
        aceptConnectionsThread = Thread(acceptConnectionObj)
        aceptConnectionsThread?.start()
    }

    inner class AcceptConnections() : Runnable {

        override fun run() {
            listenForIncomingConnections()
        }

        private fun listenForIncomingConnections() {
            try {
                /* Log.d(TESTING_TAG, "ip: ${server?.inetAddress?.address}")
                 socket = server?.accept()*/
                Log.d(TESTING_TAG, "run: connected")
                socket?.let { socket1 ->
                    Log.d(TESTING_TAG, "run: socket not empty")
                    inputStream = socket1.getInputStream()
                    outputStream = socket1.getOutputStream()

                    var objectInputStream = ObjectInputStream(inputStream)

                    var printWriter: PrintWriter = PrintWriter(outputStream!!)
                    transferClass = objectInputStream.readObject() as DetailsInfoToTransferClass
                    callBacks?.receivedTransferInfo(transferClass!!)

                    MyConstants.FILES_TO_SHARE.addAll(objectInputStream.readObject() as ArrayList<FileSharingModel>)

                    for (file in MyConstants.FILES_TO_SHARE) {
                        receiveFile(file)
                        Log.d(TESTING_TAG, "listenForIncomingConnections: out of received")
                        printWriter.println("ok")
                        printWriter.flush()
                        Log.d(TESTING_TAG, "listenForIncomingConnections: sent ok")
                    }
                    printWriter.println("finish")
                    printWriter.flush()
                    callBacks?.transferFinished()
                }


            } catch (e: ConnectException) {
                e.printStackTrace()
                callBacks?.errorOccurred()
                Log.e(TESTING_TAG, "run: error ${e}")
            } catch (e: IOException) {
                e.printStackTrace()
                callBacks?.errorOccurred()
                Log.e(TESTING_TAG, "run: ${e}")
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
                callBacks?.errorOccurred()
                Log.d(TESTING_TAG, "listenForIncomingConnections: $e")
            }

        }
    }

    private fun receiveFile(fileClass: FileSharingModel) {
        val trgFile = getTargetFile(fileClass)
        val fileOutputStream = trgFile.outputStream()
        var len = -1
        val buffer = ByteArray(MyConstants.byteArraySize)
        var bytesTransferred: Long = 0
        val bytesToTransfer = fileClass.sizeInBytes
        var count = 0L
        while (bytesTransferred < bytesToTransfer && ((inputStream?.read(buffer)
                ?.also { len = it }) != -1)
        ) {
            fileOutputStream.write(buffer, 0, len)
            fileOutputStream.flush()
            bytesTransferred += len
            transferClass?.updateProgress(len, fileClass.fileType)
            //  Log.d(TAG, "receiveFile: $len  $bytesTransferred $bytesToTransfer")
            if (count % 10 == 0L) {
                callBacks?.updateView(transferClass)
            }
        }
        fileOutputStream.close()
        callBacks?.updateView(transferClass)
        if (fileClass.fileType == MyConstants.FILE_TYPE_PICS) {
            MediaScannerConnection.scanFile(
                context.applicationContext, arrayOf("""${fileClass.filePath}"""), arrayOf("image/*")
            ) { path: String, uri: Uri ->
            }
        } else if (fileClass.fileType == MyConstants.FILE_TYPE_VIDEOS) {
            MediaScannerConnection.scanFile(
                context.applicationContext, arrayOf("""${fileClass.filePath}"""), arrayOf("video/*")
            ) { path: String, uri: Uri ->
            }
        }
        Log.d(TAG, "receiveFile: fileClass received ${fileClass.fileName}")
    }

    private fun getTargetFile(fInfo: FileSharingModel): File {
        val fileInfo = fInfo

        var rootPath: String = ""
        if (fInfo.fileType == MyConstants.FILE_TYPE_APPS) {
            rootPath = Environment.getExternalStorageDirectory().absolutePath + "/${
                context.applicationContext.resources.getString(R.string.app_name)
            }/"

            val rootFile = File(rootPath)
            if (!rootFile.exists()) {
                rootFile.mkdirs()
            }

            fileInfo.filePath = rootPath + fileInfo.getMyFileName()
            Log.d(TAG, "getTargetFile: ${fileInfo.filePath}")
        } else {
            rootPath = Environment.getExternalStorageDirectory().absolutePath + fileInfo.parentPath

            val rootFile = File(rootPath)
            if (!rootFile.exists()) {
                rootFile.mkdirs()
            }

            fileInfo.filePath =
                Environment.getExternalStorageDirectory().absolutePath + fileInfo.pathToSave

        }


        Log.e(TAG, "getTargetFile: creating file ${fileInfo.filePath}")
        val file = File(fileInfo.filePath)
        if (!file.exists()) {
            file.createNewFile()
        }
        Log.d(TAG, "getTargetFile: ${file.absolutePath}")
        Log.d(TAG, "getTargetFile: ${file.name}")
        return file
    }


}