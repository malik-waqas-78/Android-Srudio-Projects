package com.zak.clone.zakdatautils

import android.content.Context
import android.os.AsyncTask
import android.os.Environment
import com.zak.clone.zakconstants.ZakMyConstants
import com.zak.clone.zakinterfaces.ZakUtilsCallBacks
import com.zak.clone.zakmodelclasses.ZakFileSharingModel
import java.io.File

class ZakDocumentsUtils (var context: Context){

    companion object{
        var docsList = ArrayList<ZakFileSharingModel>()
    }

    var handlersActionNotifier = context as ZakUtilsCallBacks



    fun getListSize(): Int {
        return docsList.size
    }


    fun getJustSize():Long{
        var totalBytes = 0L
        for (i in docsList) {
            totalBytes += i.sizeInBytes
        }
        return totalBytes
    }
    fun loadData() {
        TaskLoadDocs().execute()
    }

    inner class TaskLoadDocs: AsyncTask<Void, Void, Void>(){
        override fun doInBackground(vararg p0: Void?): Void? {
            val rootFile= File(Environment.getExternalStorageDirectory().absolutePath+"/")
            if(docsList.isEmpty()){
                getDocsList(rootFile)
            }

            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            handlersActionNotifier.doneLoadingDocuments()
        }

    }

    private fun getDocsList(rootFile: File?) {

        if(rootFile!=null){
            val listFiles=rootFile.listFiles()
            if(listFiles!=null&&listFiles.isNotEmpty()){
                for(file in listFiles){
                    if(file.name.toLowerCase()!="android"&&file.isDirectory){
                        getDocsList(file)
                    }else if(file.name.toLowerCase()!="android"){
                        val ext=file.extension.toLowerCase()
                        if(ext.contains("pdf")||ext.contains("doc")||ext.contains("xls")
                            ||ext.contains("ppt")||ext.contains("rar")||ext.contains("zip")){
                            docsList.add(
                                ZakFileSharingModel(file.absolutePath,
                                    ZakMyConstants.FILE_TYPE_DOCS,getActualPath(file.absolutePath))
                            )
                        }
                    }
                }
            }
        }
    }
    fun getActualPath(path:String):String{
        var actualPath=path?.replace(Environment.getExternalStorageDirectory().absolutePath,"")
        return actualPath?:""
    }

}