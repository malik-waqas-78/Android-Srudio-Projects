package com.mine.chat

import android.app.Application
import com.mine.chat.utilandexts.UtilSharedPreferences


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this
    }

    companion object {
        lateinit var application: Application
            private set

        var myUserID: String = ""
            get() {
                field = UtilSharedPreferences.getUserID(application.applicationContext).orEmpty()
                return field
            }
            private set
    }
}
