package com.mine.chat.userinterface.settingsfragment

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.UserInfo
import com.mine.chat.dataandmodels.firebasedb.remotedb.FirebaseReferenceValueObserver
import com.mine.chat.dataandmodels.firebasedb.managerepository.AuthenticationRepository
import com.mine.chat.dataandmodels.firebasedb.managerepository.DBRepository
import com.mine.chat.dataandmodels.firebasedb.managerepository.MyStorageRepository
import com.mine.chat.userinterface.ViewModelDefault
import com.mine.chat.dataandmodels.Event
import com.mine.chat.dataandmodels.ModelResult

class SettingsViewModelFactory(private val userID: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModelDefault(userID) as T
    }
}

class SettingsViewModelDefault(private val userID: String) : ViewModelDefault() {

    private val dbRepository: DBRepository = DBRepository()
    private val storageRepository = MyStorageRepository()
    private val authRepository = AuthenticationRepository()

    private val _userInfo: MutableLiveData<UserInfo> = MutableLiveData()
    val userInfo: LiveData<UserInfo> = _userInfo

    private val _editStatusEvent = MutableLiveData<Event<Unit>>()
    val editStatusEvent: LiveData<Event<Unit>> = _editStatusEvent

    private val _editImageEvent = MutableLiveData<Event<Unit>>()
    val editImageEvent: LiveData<Event<Unit>> = _editImageEvent

    private val _logoutEvent = MutableLiveData<Event<Unit>>()
    val logoutEvent: LiveData<Event<Unit>> = _logoutEvent

    private val firebaseReferenceObserver = FirebaseReferenceValueObserver()

    init {
        loadAndObserveUserInfo()
    }

    override fun onCleared() {
        super.onCleared()
        firebaseReferenceObserver.clear()
    }

    private fun loadAndObserveUserInfo() {
        dbRepository.loadAndObserveUserInfo(userID, firebaseReferenceObserver)
        { modelResult: ModelResult<UserInfo> -> onResult(_userInfo, modelResult) }
    }

    fun changeUserStatus(status: String) {
        dbRepository.updateUserStatus(userID, status)
    }

    fun changeUserImage(byteArray: ByteArray) {
        storageRepository.updateUserProfileImage(userID, byteArray) { modelResult: ModelResult<Uri> ->
            onResult(null, modelResult)
            if (modelResult is ModelResult.Success) {
                dbRepository.updateUserProfileImageUrl(userID, modelResult.data.toString())
            }
        }
    }

    fun changeUserImagePressed() {
        _editImageEvent.value = Event(Unit)
    }

    fun changeUserStatusPressed() {
        _editStatusEvent.value = Event(Unit)
    }

    fun logoutUserPressed() {
        authRepository.logoutUser()
        _logoutEvent.value = Event(Unit)
    }
}

