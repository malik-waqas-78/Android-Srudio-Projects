package com.ppt.walkie.utils

import android.content.Context
import android.content.SharedPreferences
import com.ppt.walkie.R

class SharePrefHelperOKRA(var context: Context) {
    var sharedPreferences:SharedPreferences?=null
    var nameKey="NameKey"
    val IS_FIREST="ISFIRST"
    val AVATAR_KEY="AVATAR_KEY"
    val RECEIVE_CALLS_INBACKGROUND="RECEIVE_CALLS_IN_BACKGROUND"
    val IMAGE_PATH_KEY="imagepathkeytosaveimage"

    var avatar_index:Int=0

    var avatars= arrayOf(
        R.drawable.avatar1,

    )

    companion object{
        var name:String?=null
        var path:String?=null
    }
    init {
        sharedPreferences=context.getSharedPreferences("Walkie Talkie",0)
        name =getName()
    }

    fun getName():String?{
        if(name ==null){
            name =sharedPreferences?.getString(nameKey,"Enter Name")
        }
        return name
    }

    fun setName(newName:String){
        name =newName
        val edit=sharedPreferences?.edit()
        edit?.putString(nameKey,newName)
        edit?.commit()
    }


    fun getAvatar():Int{
        sharedPreferences?.getInt(AVATAR_KEY,0)?.let {
            avatar_index=it
            return avatars[avatar_index]
        }
       return 0
    }
    fun setAvatar(index:Int){
        var editor=sharedPreferences?.edit()
        editor?.putInt(AVATAR_KEY,index)
        avatar_index=index
        editor?.apply()
    }

    fun isFirstTime():Boolean{
        return sharedPreferences?.getBoolean(IS_FIREST,true)==true
    }

    fun setIsFirstTime(isFirst:Boolean){
        var editor=sharedPreferences?.edit()
        editor?.putBoolean(IS_FIREST,isFirst)
        editor?.apply()
    }

    fun canReceiveCallsInBackground():Boolean{
        return sharedPreferences?.getBoolean(RECEIVE_CALLS_INBACKGROUND,true)?:true
    }

    fun setCanReceiveInBackground(canReceive:Boolean){
        var editor=sharedPreferences?.edit()
        editor?.putBoolean(RECEIVE_CALLS_INBACKGROUND,canReceive)
        editor?.apply()
    }

    fun getImagePath():String?{
        if(path ==null){
            path =sharedPreferences?.getString(IMAGE_PATH_KEY,null)
        }
        return path
    }

    fun setImagePath(newpath:String){
        path =newpath
        val edit=sharedPreferences?.edit()
        edit?.putString(IMAGE_PATH_KEY,newpath)
        edit?.commit()
    }
}