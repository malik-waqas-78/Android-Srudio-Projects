package com.recovery.data.forwhatsapp.fileobserverspkg

import android.os.AsyncTask
import android.os.Environment
import java.io.File

class CheckObserverOKRA():AsyncTask<Void,Void,Void>() {

    override fun doInBackground(vararg params: Void?): Void? {
        val observer=ManageObserver.getInstance()

        if(observer?.directoriesBeingObserved?.isEmpty()==true){
            observer?.observe()
            return null
        }

        val pathsBeingObserved=ArrayList<String>(observer?.pathsBeingObserved!!)

        val pathToObserve=getPathToBeObserved(File(Environment.getExternalStorageDirectory().absolutePath+"/WhatsApp/Media"))

        for(path in pathToObserve){
            if(!pathsBeingObserved.contains(path)){
                observer.createFileObserver(File(path))
            }
        }
        return null
    }

    private fun getPathToBeObserved(file: File): ArrayList<String> {
        val arrayList=ArrayList<String>()
        val listFiles=file.listFiles()
        if(listFiles!=null){
            for(f in listFiles){
                if(f.isDirectory&&f.name!="Android"){
                    arrayList.add(f.absolutePath)
                    getPathToBeObserved(f)
                }
            }
        }
        return arrayList
    }

}