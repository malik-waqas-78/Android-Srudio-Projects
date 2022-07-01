package com.mine.chat.dataandmodels.firebasedb.fbrelatedentity

import com.google.firebase.database.PropertyName


data class Chat(
    @get:PropertyName("lastMessage") @set:PropertyName("lastMessage") var lastMyMessage: MyMessage = MyMessage(),
    @get:PropertyName("info") @set:PropertyName("info") var info: ChatInfo = ChatInfo()
)

data class ChatInfo(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = ""
)