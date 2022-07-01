package com.phoneclone.data.modelclasses

import android.graphics.drawable.Drawable
import com.phoneclone.data.constants.MyConstants
import java.io.Serializable

class ContactsModel (name : String, phoneNumber : String) :Serializable{
    var name=""

    var phoneNumber=""

    var fileType=MyConstants.FILE_TYPE_CONTACTS

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