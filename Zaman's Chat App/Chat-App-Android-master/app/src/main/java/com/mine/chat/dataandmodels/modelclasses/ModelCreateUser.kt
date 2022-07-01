package com.mine.chat.dataandmodels.modelclasses

data class ModelCreateUser(
    var displayName: String = "",
    var email: String = "",
    var password: String = ""
)