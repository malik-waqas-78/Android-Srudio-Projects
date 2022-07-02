package com.photo.recovery.utils

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import com.photo.recovery.constants.MyConstantsAAT
import com.photo.recovery.models.AllFilesModelClassAAT
import java.io.File
import java.lang.Exception

class RecoverFilesAAT(var context: Context, var files:ArrayList<AllFilesModelClassAAT>, var callBack: RecoverFilesCallBack) {

    fun recoverFiles(){
        FileRecoveryTask().execute()
    }
    inner class FileRecoveryTask: AsyncTask<Void, Void, Void?>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            files.forEach {
                try{

                    recoverThisFile(File(it.filePath),it.subType)
                }catch (e:Exception){

                }
            }

            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            callBack.filesRecoveryDone()
        }

    }
    fun recoverThisFile(srcFile: File,type:String){

        if(!srcFile.exists()){
            return
        }

        try{
            val rootFile=File(Environment.getExternalStorageDirectory().absolutePath+File.separatorChar+"Recycle Bin"+File.separatorChar+type)
            if(!rootFile.exists()){
                rootFile.mkdirs()
            }
            val trgFile=File(rootFile.path,srcFile.name)
            if(!trgFile.exists()){
                if(trgFile.createNewFile()){
                    val outputStream=trgFile.outputStream()
                    val inputStream=srcFile.inputStream()
                    val byteArray=ByteArray(1024)
                    while(inputStream.read(byteArray)!=-1){
                        outputStream.write(byteArray)
                        outputStream.flush()
                    }
                    outputStream.close()
                    inputStream.close()
                    if(type==MyConstantsAAT.IMAGE_FILE_TYPE){
                        MediaScannerConnection.scanFile(
                            context.applicationContext, arrayOf("""${srcFile.absolutePath}"""), arrayOf("image/*")){ path: String, uri: Uri ->
                        }
                    }else if(type==MyConstantsAAT.VIDEO_FILE_TYPE){
                        MediaScannerConnection.scanFile(
                            context.applicationContext, arrayOf("""${srcFile.absolutePath}"""), arrayOf("video/*")){ path: String, uri: Uri ->
                        }
                    }
                    srcFile.delete()
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
            Log.d("TAG", "recoverThisFile: ${e.message}")
        }
    }

    interface RecoverFilesCallBack{
        fun filesRecoveryDone()
    }

}