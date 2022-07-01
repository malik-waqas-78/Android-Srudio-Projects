package com.mine.chat.userinterface.mainfragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mine.chat.App
import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.UserNotification
import com.mine.chat.dataandmodels.firebasedb.remotedb.FirebaseAuthStateObserver
import com.mine.chat.dataandmodels.firebasedb.remotedb.FirebaseReferenceConnectedObserver
import com.mine.chat.dataandmodels.firebasedb.remotedb.FirebaseReferenceValueObserver
import com.mine.chat.dataandmodels.firebasedb.managerepository.AuthenticationRepository
import com.mine.chat.dataandmodels.firebasedb.managerepository.DBRepository
import com.mine.chat.dataandmodels.ModelResult
import com.google.firebase.auth.FirebaseUser


class ViewModelMain : ViewModel() {

    private val dbRepository = DBRepository()
    private val authRepository = AuthenticationRepository()

    private val _userNotificationsList = MutableLiveData<MutableList<UserNotification>>()

    private val fbRefNotificationsObserver = FirebaseReferenceValueObserver()
    private val fbAuthStateObserver = FirebaseAuthStateObserver()
    private val fbRefConnectedObserver = FirebaseReferenceConnectedObserver()
    private var userID = App.myUserID

    var userNotificationsList: LiveData<MutableList<UserNotification>> = _userNotificationsList

    init {
       setupAuthObserver()
    }

    override fun onCleared() {
        super.onCleared()
        fbRefNotificationsObserver.clear()
        fbRefConnectedObserver.clear()
        fbAuthStateObserver.clear()
    }

    private fun setupAuthObserver(){
        authRepository.observeAuthState(fbAuthStateObserver) { modelResult: ModelResult<FirebaseUser> ->
            if (modelResult is ModelResult.Success) {
                userID = modelResult.data!!.uid
                startObservingNotifications()
                fbRefConnectedObserver.start(userID)
            } else {
                fbRefConnectedObserver.clear()
                stopObservingNotifications()
            }
        }
    }

    private fun startObservingNotifications() {
        dbRepository.loadAndObserveUserNotifications(userID, fbRefNotificationsObserver) { modelResult: ModelResult<MutableList<UserNotification>> ->
            if (modelResult is ModelResult.Success) {
                _userNotificationsList.value = modelResult.data
            }
        }
    }

    private fun stopObservingNotifications() {
        fbRefNotificationsObserver.clear()
    }
}
