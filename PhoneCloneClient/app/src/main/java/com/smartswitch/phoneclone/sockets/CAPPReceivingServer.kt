package com.smartswitch.phoneclone.sockets

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.util.Log


import com.smartswitch.phoneclone.R
import com.smartswitch.phoneclone.constants.CAPPMConstants
import com.smartswitch.phoneclone.constants.CAPPMConstants.get.TAG
import com.smartswitch.phoneclone.constants.CAPPMConstants.get.TESTING_TAG
import com.smartswitch.phoneclone.interfaces.CAPPServerCallBacks
import com.smartswitch.phoneclone.modelclasses.CAPPDetailsInfoToTransferClass
import com.smartswitch.phoneclone.modelclasses.CAPPFileSharingModel
import java.io.*
import java.net.*

class CAPPReceivingServer(var context: Context) {


    var socket: Socket?=null
    private var aceptConnectionsThread: Thread? = null
    private var acceptConnectionObj: AcceptConnections? = null

    private var callBacksHS: CAPPServerCallBacks? = null

    var transferClassHS: CAPPDetailsInfoToTransferClass? = null

    var inputStream: InputStream? = null
    var outputStream: OutputStream? = null

    init {
        callBacksHS = context as CAPPServerCallBacks
        try {
            /*server = ServerSocket()
            server?.reuseAddress = true
            server?.bind(InetSocketAddress(HSMyConstants.PORT_NO))*/

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
                    transferClassHS = objectInputStream.readObject() as CAPPDetailsInfoToTransferClass
                    callBacksHS?.receivedTransferInfo(transferClassHS!!)

                    CAPPMConstants.FILES_TO_SHARE.addAll(objectInputStream.readObject() as ArrayList<CAPPFileSharingModel>)

                    for (file in CAPPMConstants.FILES_TO_SHARE) {
                        receiveFile(file)
                        Log.d(TESTING_TAG, "listenForIncomingConnections: out of received")
                        printWriter.println("ok")
                        printWriter.flush()
                        Log.d(TESTING_TAG, "listenForIncomingConnections: sent ok")
                    }
                    printWriter.println("finish")
                    printWriter.flush()
                    callBacksHS?.transferFinished()
                }


            } catch (e: ConnectException) {
                e.printStackTrace()
                callBacksHS?.errorOccurred()
                Log.e(TESTING_TAG, "run: error ${e}")
            } catch (e: IOException) {
                e.printStackTrace()
                callBacksHS?.errorOccurred()
                Log.e(TESTING_TAG, "run: ${e}")
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
                callBacksHS?.errorOccurred()
                Log.d(TESTING_TAG, "listenForIncomingConnections: $e")
            }

        }
    }

    private fun receiveFile(HSFileClass: CAPPFileSharingModel) {
        val trgFile = getTargetFile(HSFileClass)
        val fileOutputStream = trgFile.outputStream()
        var len = -1
        val buffer = ByteArray(CAPPMConstants.byteArraySize)
        var bytesTransferred: Long = 0
        val bytesToTransfer = HSFileClass.sizeInBytes
        var count = 0L
        while (bytesTransferred < bytesToTransfer && ((inputStream?.read(buffer)
                ?.also { len = it }) != -1)
        ) {
            fileOutputStream.write(buffer, 0, len)
            fileOutputStream.flush()
            bytesTransferred += len
            transferClassHS?.updateProgress(len, HSFileClass.fileType)
            //  Log.d(TAG, "receiveFile: $len  $bytesTransferred $bytesToTransfer")
            if (count % 10 == 0L) {
                callBacksHS?.updateView(transferClassHS)
            }
        }
        fileOutputStream.close()
        callBacksHS?.updateView(transferClassHS)
        if (HSFileClass.fileType == CAPPMConstants.FILE_TYPE_PICS) {
            MediaScannerConnection.scanFile(
                context.applicationContext, arrayOf("""${HSFileClass.filePath}"""), arrayOf("image/*")
            ) { path: String, uri: Uri ->
            }
        } else if (HSFileClass.fileType == CAPPMConstants.FILE_TYPE_VIDEOS) {
            MediaScannerConnection.scanFile(
                context.applicationContext, arrayOf("""${HSFileClass.filePath}"""), arrayOf("video/*")
            ) { path: String, uri: Uri ->
            }
        }
        Log.d(TAG, "receiveFile: HSFileClass received ${HSFileClass.fileName}")
    }

    private fun getTargetFile(fInfo: CAPPFileSharingModel): File {
        val fileInfo = fInfo

        var rootPath: String = ""
        if (fInfo.fileType == CAPPMConstants.FILE_TYPE_APPS) {
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