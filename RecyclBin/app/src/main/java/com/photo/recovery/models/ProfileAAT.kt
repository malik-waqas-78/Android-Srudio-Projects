package com.photo.recovery.models

class ProfileAAT() {

    lateinit var senderName: String
    lateinit var lastMsg: String
    lateinit var lastMsgDate: String
    lateinit var icon: ByteArray
    lateinit var messengerType:String
    var timeInMillis:Long=0

    constructor(name:String,msg:String,date:String,time:Long,icon:ByteArray,type:String):this(){
        this.senderName=name
        this.lastMsg=msg
        this.lastMsgDate=date
        this.icon=icon
        this.messengerType=type
        this.timeInMillis=time
    }
}