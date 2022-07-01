package com.mine.chat.dataandmodels.modelclasses

import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.Chat
import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.UserInfo

data class ChatAndUserInfo(
    var mChat: Chat,
    var mUserInfo: UserInfo
)
