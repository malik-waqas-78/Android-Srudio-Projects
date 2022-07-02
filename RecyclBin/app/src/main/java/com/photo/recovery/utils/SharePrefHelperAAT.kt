package com.photo.recovery.utils

import android.content.Context
import android.content.SharedPreferences

class SharePrefHelperAAT(var context: Context) {
    val ISFIREST="ISFIRST"
    var sPreferences:SharedPreferences?=null
    init {
        if(sPreferences==null){
            sPreferences=context.getSharedPreferences("RecycleBin", 0)
        }
    }

    fun isFirstTime():Boolean{
        return sPreferences?.getBoolean(ISFIREST,true)==true
    }

    fun setIsFirstTime(isFirst:Boolean){
        var editor=sPreferences?.edit()
        editor?.putBoolean(ISFIREST,isFirst)
        editor?.apply()
    }

}