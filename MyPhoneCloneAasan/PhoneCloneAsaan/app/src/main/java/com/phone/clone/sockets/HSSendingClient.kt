package com.phone.clone.sockets

import android.content.Context
import android.util.Log
import com.phone.clone.constants.HSMyConstants
import com.phone.clone.constants.HSMyConstants.get.TESTING_TAG
import com.phone.clone.interfaces.HSClientCallBacks
import com.phone.clone.modelclasses.HSDetailsInfoToTransferClass
import com.phone.clone.modelclasses.HSFileSharingModel

import java.io.*
import java.net.ConnectException
import java.net.Socket

class HSSendingClient(
    var context: Context,
    var transferHSDetailsClass: HSDetailsInfoToTransferClass
) {
    companion object{
        var socket: Socket?=null
    }

    private var sendConnectionRequestThread: Thread? = null
    private var sendConnectionRequestObj: ConnectionRequest? = null

    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null


    private var callBacksHS: HSClientCallBacks? = null

    init {
        callBacksHS = context as HSClientCallBacks
    }

    fun startSendingConnectionRequest() {
        sendConnectionRequestObj = ConnectionRequest()
        sendConnectionRequestThread = Thread(sendConnectionRequestObj)
        sendConnectionRequestThread?.start()
    }


    inner class ConnectionRequest() : Runnable {


        override fun run() {

            try {
               /* socket = Socket()
                socket?.reuseAddress = true
                socket?.connect(InetSocketAddress(IP_ADDRESS, HSMyConstants.PORT_NO))
                if (socket?.isConnected == true) {
                    Log.d(HSMyConstants.TESTING_TAG, "run: connected client $IP_ADDRESS")

                }*/
                socket?.let { it ->
                    Log.d(HSMyConstants.TESTING_TAG, "run: socket not empty")
                    inputStream = it.getInputStream()
                    outputStream = it.getOutputStream()
                    Log.d(TESTING_TAG, "run: InputStreams")
//                    var objectInputStream = ObjectInputStream(inputStream)
                    var objectOutputStream = ObjectOutputStream(outputStream)
                    Log.d(TESTING_TAG, "run: objectInputStreams")
                    var bufferedReader = BufferedReader(inputStream?.bufferedReader())
//                    var printWriter = PrintWriter(outputStream!!)
                    Log.d(TESTING_TAG, "run: buffered InputStreams")
                    objectOutputStream.writeObject(transferHSDetailsClass)
                    objectOutputStream.flush()
                    Log.d(TESTING_TAG, "run: sent details")
                    objectOutputStream.writeObject(HSMyConstants.FILES_TO_SHARE)
                    objectOutputStream.flush()
                    Log.d(TESTING_TAG, "run: sent files info")
                    for (file in HSMyConstants.FILES_TO_SHARE) {
                        sendFile(file)
                        Log.d(TESTING_TAG, "run: out of send")
                        if(HSMyConstants.FILES_TO_SHARE.indexOf(file)==HSMyConstants.FILES_TO_SHARE.size-1){
                            break
                        }
                        while (true) {
                            if (bufferedReader.readLine() == "ok") {
                                Log.d(TESTING_TAG, "run: file Sent ${file.fileName}")
                                break
                            }
                        }
                    }
                    while (true) {
                        if (bufferedReader.readLine() == "finish") {
                            callBacksHS?.transferFinished()
                            break
                        }
                    }
                }
            } catch (e: ConnectException) {
                e.printStackTrace()
                Log.e(HSMyConstants.TESTING_TAG, "run: ${e}")
                callBacksHS?.errorOccurred()
            } catch (e: IOException) {
                e.printStackTrace()
                callBacksHS?.errorOccurred()
                Log.e(HSMyConstants.TESTING_TAG, "run: ${e}")
            }
        }
    }

    private fun sendFile(HSFile: HSFileSharingModel) {
        val srcFile = File(HSFile.filePath)
        var len = -1
        var buffer = ByteArray(HSMyConstants.byteArraySize)
        val fileInputStream = srcFile.inputStream()
        var bytesTransferred = 0
        val bytesToTransfer = HSFile.sizeInBytes
        var count=0L
        while ((fileInputStream.read(buffer).also { len = it }) != -1) {
            outputStream?.write(buffer, 0, len)
            outputStream?.flush()
            bytesTransferred += len
            count++
            transferHSDetailsClass.updateProgress(len,HSFile.fileType)
//            Log.d(TESTING_TAG, "sendFile: $len  $bytesTransferred   $bytesToTransfer")
            if(count%30==0L){
                callBacksHS?.updateView(transferHSDetailsClass)
            }
        }
        fileInputStream.close()
        callBacksHS?.updateView(transferHSDetailsClass)
    }
}