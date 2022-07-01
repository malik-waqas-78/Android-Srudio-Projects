package com.example.simplechat.modelclasses

import java.io.Serializable

data class ContactsModelClass(val name: String?, var contactNo:String): Serializable {

    init {
        if(contactNo.length>6){
            contactNo = contactNo.substring(contactNo.length - 6)
        }

    }
}