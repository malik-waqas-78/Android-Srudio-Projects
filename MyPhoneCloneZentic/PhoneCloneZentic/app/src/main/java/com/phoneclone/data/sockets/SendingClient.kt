package com.phoneclone.data.sockets

import android.content.Context
import android.util.Log
import com.phoneclone.data.constants.MyConstants
import com.phoneclone.data.constants.MyConstants.get.TESTING_TAG
import com.phoneclone.data.interfaces.ClientCallBacks
import com.phoneclone.data.modelclasses.DetailsInfoToTransferClass
import com.phoneclone.data.modelclasses.FileSharingModel

import java.io.*
import java.net.ConnectException
import java.net.Socket

class SendingClient(
    var context: Context,
    var transferDetailsClass: DetailsInfoToTransferClass
) {
   companion object{
       var socket: Socket? = null
   }
    private var sendConnectionRequestThread: Thread? = null
    private var sendConnectionRequestObj: ConnectionRequest? = null

    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null


    private var callBacks: ClientCallBacks? = null

    init {
        callBacks = context as ClientCallBacks
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
                socket?.connect(InetSocketAddress(IP_ADDRESS, MyConstants.PORT_NO))
                if (socket?.isConnected == true) {
                    Log.d(MyConstants.TESTING_TAG, "run: connected client $IP_ADDRESS")

                }*/
                socket?.let { it ->
                    Log.d(MyConstants.TESTING_TAG, "run: socket not empty")
                    inputStream = it.getInputStream()
                    outputStream = it.getOutputStream()
                    Log.d(TESTING_TAG, "run: InputStreams")
//                    var objectInputStream = ObjectInputStream(inputStream)
                    var objectOutputStream = ObjectOutputStream(outputStream)
                    Log.d(TESTING_TAG, "run: objectInputStreams")
                    var bufferedReader = BufferedReader(inputStream?.bufferedReader())
//                    var printWriter = PrintWriter(outputStream!!)
                    Log.d(TESTING_TAG, "run: buffered InputStreams")
                    objectOutputStream.writeObject(transferDetailsClass)
                    objectOutputStream.flush()
                    Log.d(TESTING_TAG, "run: sent details")
                    objectOutputStream.writeObject(MyConstants.FILES_TO_SHARE)
                    objectOutputStream.flush()
                    Log.d(TESTING_TAG, "run: sent files info")
                    for (file in MyConstants.FILES_TO_SHARE) {
                        sendFile(file)
                        Log.d(TESTING_TAG, "run: out of send")
                        if(MyConstants.FILES_TO_SHARE.indexOf(file)==MyConstants.FILES_TO_SHARE.size-1){
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
                            callBacks?.transferFinished()
                            break
                        }
                    }
                }
            } catch (e: ConnectException) {
                e.printStackTrace()
                Log.e(MyConstants.TESTING_TAG, "run: ${e}")
                callBacks?.errorOccurred()
            } catch (e: IOException) {
                e.printStackTrace()
                callBacks?.errorOccurred()
                Log.e(MyConstants.TESTING_TAG, "run: ${e}")
            }
        }
    }

    private fun sendFile(file: FileSharingModel) {
        val srcFile = File(file.filePath)
        var len = -1
        var buffer = ByteArray(MyConstants.byteArraySize)
        val fileInputStream = srcFile.inputStream()
        var bytesTransferred = 0
        val bytesToTransfer = file.sizeInBytes
        var count=0L
        while ((fileInputStream.read(buffer).also { len = it }) != -1) {
            outputStream?.write(buffer, 0, len)
            outputStream?.flush()
            bytesTransferred += len
            count++
            transferDetailsClass.updateProgress(len,file.fileType)
//            Log.d(TESTING_TAG, "sendFile: $len  $bytesTransferred   $bytesToTransfer")
            if(count%30==0L){
                callBacks?.updateView(transferDetailsClass)
            }
        }
        fileInputStream.close()
        callBacks?.updateView(transferDetailsClass)
    }
}