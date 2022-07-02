package com.photo.recovery.utils

import android.os.AsyncTask
import android.os.Environment
import java.io.File

class CheckObserverAAT():AsyncTask<Void,Void,Void>() {

    override fun doInBackground(vararg params: Void?): Void? {
        val observer=ManageObserver.managerInstance

        if(observer.directoriesBeingObserved.isEmpty()){
            observer.observe()
            return null
        }

        val pathsBeingObserved=ArrayList<String>(observer.pathsBeingObserved)

        val pathToObserve=getPathToBeObserved(File(Environment.getExternalStorageDirectory().absolutePath))

        for(path in pathToObserve){
            if(!pathsBeingObserved.contains(path)){
                observer.observe();
                break;
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