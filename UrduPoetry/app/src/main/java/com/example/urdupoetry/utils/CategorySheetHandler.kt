package com.example.urdupoetry.utils

import android.os.AsyncTask
import android.util.JsonReader
import android.util.Log
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class CategorySheetHandler {

    inner class GetCategoryData(): AsyncTask<Void,Void,Void>(){
        override fun doInBackground(vararg params: Void?): Void? {


        return null

            /*val url= URL("https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=1FiEp7k_7Aepv9iUd0VSPsXgRkn-sfGqCzPfM-JM40f0&sheet=Sheet1")
            val connection :HttpURLConnection=url.openConnection() as HttpURLConnection
            connection.requestMethod="GET"
            val inputStream : InputStream =BufferedInputStream(connection.inputStream)
            val bufferedReader : BufferedReader = BufferedReader(InputStreamReader(inputStream));

            val stringBuilder:StringBuilder = StringBuilder()
            var line :String
            try{
                while(bufferedReader.readLine().also { line=it }!=null){
                    stringBuilder.append(line)
                }
            }catch(e:Exception){

            }finally {
                try {
                    bufferedReader.close()
                    inputStream.close()
                }catch(e: IOException){

                }
            }

            val jsonObj=JSONObject(stringBuilder.toString())

            Log.d("TAG", "doInBackground: ")
            return null*/
        }

    }
}