package com.photo.recovery.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ChatsModelClassAAT() : RealmObject() {
    @PrimaryKey
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