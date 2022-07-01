package com.zak.clone.zakmodelclasses

import android.graphics.drawable.Drawable
import com.zak.clone.zakconstants.ZakMyConstants
import java.io.Serializable

class ZakContactsModel (name : String, phoneNumber : String) :Serializable{
    var name=""

    var phoneNumber=""

    var fileType=ZakMyConstants.FILE_TYPE_CONTACTS

    var isSelected=false
    companion object{
        var icon: Drawable?=null
        val NAME_INDEX=0
        val PHONE_NUMBER_INDEX=1
    }


    init {
        this.name=name
        this.phoneNumber=phoneNumber
    }


}