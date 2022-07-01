package com.mine.chat.utilandexts

fun isEmailValid(email: CharSequence): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isTextValid(minLength: Int, text: String?): Boolean {
    if (text.isNullOrBlank() || text.length < minLength) {
        return false
    }
    return true
}

