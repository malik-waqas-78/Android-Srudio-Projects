package com.photo.recovery.database

import com.photo.recovery.models.ChatProfileModelClassAAT
import com.photo.recovery.models.ChatsModelClassAAT
import io.realm.Realm
import io.realm.RealmConfiguration

class ManageChatsAAT() {

    lateinit var profileTable:Realm
    lateinit var chatTable:Realm

    lateinit var senderName: String
    lateinit var lastMsg: String
    lateinit var lastMsgDate: String
    lateinit var lastMsgTime:String
    lateinit var icon: ByteArray
    lateinit var messengerType:String
    constructor(name:String,msg:String,date:String,time:String,icon:ByteArray,type:String):this(){
        this.senderName=name
        this.lastMsg=msg
        this.lastMsgDate=date
        this.lastMsgTime=time
        this.icon=icon
        this.messengerType=type
        initRealm()
    }

    fun insertData(){
        val profile=ChatProfileModelClassAAT(senderName,lastMsg,lastMsgDate,lastMsgTime.toLong(),icon,messengerType)
        val chat=ChatsModelClassAAT(lastMsgTime,senderName,lastMsg,icon,lastMsgDate,messengerType)

        profileTable.beginTransaction()
        profileTable.copyToRealmOrUpdate(profile)
        profileTable.commitTransaction()

        chatTable.beginTransaction()
        chatTable.copyToRealmOrUpdate(chat)
        chatTable.commitTransaction()

    }

    private fun initRealm() {

        val realmProfile = RealmConfiguration.Builder()
            .name("profiles")
            .build()
        profileTable = Realm.getInstance(realmProfile)
        val realmChat = RealmConfiguration.Builder()
            .name("chats")
            .build()
        chatTable = Realm.getInstance(realmChat)
    }
}