package com.mine.chat.userinterface.startfragments.loginUI

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mine.chat.dataandmodels.modelclasses.ModelLogin
import com.mine.chat.dataandmodels.firebasedb.managerepository.AuthenticationRepository
import com.mine.chat.userinterface.ViewModelDefault
import com.mine.chat.dataandmodels.Event
import com.mine.chat.dataandmodels.ModelResult
import com.mine.chat.utilandexts.isEmailValid
import com.mine.chat.utilandexts.isTextValid
import com.google.firebase.auth.FirebaseUser

class ViewModelDefaultLogin : ViewModelDefault() {

    private val authRepository = AuthenticationRepository()
    private val _isLoggedInEvent = MutableLiveData<Event<FirebaseUser>>()

    val isLoggedInEvent: LiveData<Event<FirebaseUser>> = _isLoggedInEvent
    val emailText = MutableLiveData<String>() // Two way
    val passwordText = MutableLiveData<String>() // Two way
    val isLoggingIn = MutableLiveData<Boolean>() // Two way

    private fun login() {
        isLoggingIn.value = true
        val login = ModelLogin(emailText.value!!, passwordText.value!!)

        authRepository.loginUser(login) { modelResult: ModelResult<FirebaseUser> ->
            onResult(null, modelResult)
            if (modelResult is ModelResult.Success) _isLoggedInEvent.value = Event(modelResult.data!!)
            if (modelResult is ModelResult.Success || modelResult is ModelResult.Error) isLoggingIn.value = false
        }
    }

    fun loginPressed() {
        if (!isEmailValid(emailText.value.toString())) {
            mSnackBarText.value = Event("Invalid email format")
            return
        }
        if (!isTextValid(6, passwordText.value)) {
            mSnackBarText.value = Event("Password is too short")
            return
        }

        login()
    }
}