package com.example.urdupoetry.utils

import android.content.Context
import android.os.AsyncTask
import com.example.urdupoetry.callbacks.PoetryCallBacks
import com.example.urdupoetry.modelclasses.Poetry
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader
import java.io.InputStreamReader

class PoetryFileReader(var context: Context, var callback: PoetryCallBacks,var fileName:String){

    inner class ReadFile : AsyncTask<Void,Void,Void>(){

        val readPoetry=ArrayList<Poetry>()

        override fun doInBackground(vararg params: Void?): Void? {
            if(!Constants.poetries.containsKey(fileName)) {
                val bufferedReader =
                    BufferedReader(InputStreamReader(context.assets.open("$fileName.csv")));
                val csvParser = CSVParser(bufferedReader, CSVFormat.DEFAULT);
                for(csvRecord in csvParser){
                    if(csvRecord.get(0) !="Name"){
                        readPoetry.add(Poetry(csvRecord.get(0)))
                    }
                }
                Constants.poetries.put(fileName, readPoetry)
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            callback.fileLoaded()
        }


    }
}