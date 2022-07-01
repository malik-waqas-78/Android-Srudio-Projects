package com.phone.clone.modelclasses

import android.graphics.drawable.Drawable
import com.phone.clone.constants.HSMyConstants
import java.io.Serializable

class HSContactsModel (name : String, phoneNumber : String) :Serializable{
    var name=""

    var phoneNumber=""

    var fileType=HSMyConstants.FILE_TYPE_CONTACTS

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