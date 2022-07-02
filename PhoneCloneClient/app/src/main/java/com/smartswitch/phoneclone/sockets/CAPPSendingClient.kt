package com.smartswitch.phoneclone.sockets

import android.content.Context
import android.util.Log
import com.smartswitch.phoneclone.constants.CAPPMConstants
import com.smartswitch.phoneclone.constants.CAPPMConstants.get.TESTING_TAG
import com.smartswitch.phoneclone.interfaces.CAPPClientCallBacks
import com.smartswitch.phoneclone.modelclasses.CAPPDetailsInfoToTransferClass
import com.smartswitch.phoneclone.modelclasses.CAPPFileSharingModel

import java.io.*
import java.net.ConnectException
import java.net.Socket

class CAPPSendingClient(
    var context: Context,
    var transferHSDetailsClass: CAPPDetailsInfoToTransferClass
) {
    companion object{
        var socket: Socket?=null
    }

    private var sendConnectionRequestThread: Thread? = null
    private var sendConnectionRequestObj: ConnectionRequest? = null

    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null


    private var callBacksHS: CAPPClientCallBacks? = null

    init {
        callBacksHS = context as CAPPClientCallBacks
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
                    Log.d(CAPPMConstants.TESTING_TAG, "run: socket not empty")
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
                    objectOutputStream.writeObject(CAPPMConstants.FILES_TO_SHARE)
                    objectOutputStream.flush()
                    Log.d(TESTING_TAG, "run: sent files info")
                    for (file in CAPPMConstants.FILES_TO_SHARE) {
                        sendFile(file)
                        Log.d(TESTING_TAG, "run: out of send")
                        if(CAPPMConstants.FILES_TO_SHARE.indexOf(file)==CAPPMConstants.FILES_TO_SHARE.size-1){
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
                Log.e(CAPPMConstants.TESTING_TAG, "run: ${e}")
                callBacksHS?.errorOccurred()
            } catch (e: IOException) {
                e.printStackTrace()
                callBacksHS?.errorOccurred()
                Log.e(CAPPMConstants.TESTING_TAG, "run: ${e}")
            }
        }
    }

    private fun sendFile(HSFile: CAPPFileSharingModel) {
        val srcFile = File(HSFile.filePath)
        var len = -1
        var buffer = ByteArray(CAPPMConstants.byteArraySize)
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