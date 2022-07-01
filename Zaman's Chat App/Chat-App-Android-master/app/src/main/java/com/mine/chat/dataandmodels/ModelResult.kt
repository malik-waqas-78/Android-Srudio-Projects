package com.mine.chat.dataandmodels


sealed class ModelResult<out R> {
    data class Success<out T>(val data: T? = null, val msg: String? = null) : ModelResult<T>()
    class Error(val msg: String? = null) : ModelResult<Nothing>()
    object Loading : ModelResult<Nothing>()
}
