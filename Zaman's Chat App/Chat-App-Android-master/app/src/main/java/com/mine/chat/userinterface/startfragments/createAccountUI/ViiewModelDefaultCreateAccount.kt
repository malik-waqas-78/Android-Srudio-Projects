package com.mine.chat.userinterface.startfragments.createAccountUI

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mine.chat.dataandmodels.Event
import com.mine.chat.dataandmodels.ModelResult
import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.User
import com.mine.chat.dataandmodels.firebasedb.managerepository.AuthenticationRepository
import com.mine.chat.dataandmodels.firebasedb.managerepository.DBRepository
import com.mine.chat.dataandmodels.modelclasses.ModelCreateUser
import com.mine.chat.userinterface.ViewModelDefault
import com.mine.chat.utilandexts.isEmailValid
import com.mine.chat.utilandexts.isTextValid
import com.google.firebase.auth.FirebaseUser

class ViiewModelDefaultCreateAccount : ViewModelDefault() {

    private val dbRepository = DBRepository()
    private val authRepository = AuthenticationRepository()
    private val mIsCreatedEvent = MutableLiveData<Event<FirebaseUser>>()

    val isCreatedEvent: LiveData<Event<FirebaseUser>> = mIsCreatedEvent
    val displayNameText = MutableLiveData<String>() // Two way
    val emailText = MutableLiveData<String>() // Two way
    val passwordText = MutableLiveData<String>() // Two way
    val isCreatingAccount = MutableLiveData<Boolean>()

    private fun createAccount() {
        isCreatingAccount.value = true
        val createUser =
            ModelCreateUser(displayNameText.value!!, emailText.value!!, passwordText.value!!)

        authRepository.createUser(createUser) { modelResult: ModelResult<FirebaseUser> ->
            onResult(null, modelResult)
            if (modelResult is ModelResult.Success) {
                mIsCreatedEvent.value = Event(modelResult.data!!)
                dbRepository.updateNewUser(User().apply {
                    info.id = modelResult.data.uid
                    info.displayName = createUser.displayName
                })
            }
            if (modelResult is ModelResult.Success || modelResult is ModelResult.Error) isCreatingAccount.value = false
        }
    }

    fun createAccountPressed() {
        if (!isTextValid(2, displayNameText.value)) {
            mSnackBarText.value = Event("Display name is too short")
            return
        }

        if (!isEmailValid(emailText.value.toString())) {
            mSnackBarText.value = Event("Invalid email format")
            return
        }
        if (!isTextValid(6, passwordText.value)) {
            mSnackBarText.value = Event("Password is too short")
            return
        }

        createAccount()
    }
}