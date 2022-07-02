package com.photo.recovery.models

class ChatsAAT() {
    lateinit var id: String
    lateinit var senderName: String
    lateinit var textMsg: String
    lateinit var icon: ByteArray
    lateinit var date: String
    lateinit var messengerType: String
    lateinit var timeInMillis: String

    constructor(
        timeInMillis: String,
        senderName: String,
        textMsg: String,
        icon: ByteArray,
        date: String,
        messengerType: String
    ) : this() {
        this.timeInMillis=timeInMillis
        this.senderName = senderName
        this.textMsg = textMsg
        this.icon = icon
        this.date = date
        this.messengerType = messengerType
        id = date + timeInMillis
    }
}