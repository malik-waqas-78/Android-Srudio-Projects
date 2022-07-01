package com.mine.chat.userinterface.profilefragment

import androidx.lifecycle.*
import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.*
import com.mine.chat.dataandmodels.firebasedb.remotedb.FirebaseReferenceValueObserver
import com.mine.chat.dataandmodels.firebasedb.managerepository.DBRepository
import com.mine.chat.userinterface.ViewModelDefault
import com.mine.chat.dataandmodels.ModelResult
import com.mine.chat.utilandexts.convertTwoUserIDs


class ProfileViewModelFactory(private val myUserID: String, private val otherUserID: String) :
    ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModelDefault(myUserID, otherUserID) as T
    }
}

enum class LayoutState {
    IS_FRIEND, NOT_FRIEND, ACCEPT_DECLINE, REQUEST_SENT
}

class ProfileViewModelDefault(private val myUserID: String, private val userID: String) :
    ViewModelDefault() {

    private val repository: DBRepository = DBRepository()
    private val firebaseReferenceObserver = FirebaseReferenceValueObserver()
    private val _myUser: MutableLiveData<User> = MutableLiveData()
    private val _otherUser: MutableLiveData<User> = MutableLiveData()

    val otherUser: LiveData<User> = _otherUser
    val layoutState = MediatorLiveData<LayoutState>()

    init {
        layoutState.addSource(_myUser) { updateLayoutState(it, _otherUser.value) }
        setupProfile()
    }

    override fun onCleared() {
        super.onCleared()
        firebaseReferenceObserver.clear()
    }

    private fun updateLayoutState(myUser: User?, otherUser: User?) {
        if (myUser != null && otherUser != null) {
            layoutState.value = when {
                myUser.friends[otherUser.info.id] != null -> LayoutState.IS_FRIEND
                myUser.notifications[otherUser.info.id] != null -> LayoutState.ACCEPT_DECLINE
                myUser.sentRequests[otherUser.info.id] != null -> LayoutState.REQUEST_SENT
                else -> LayoutState.NOT_FRIEND
            }
        }
    }

    private fun setupProfile() {
        repository.loadUser(userID) { modelResult: ModelResult<User> ->
            onResult(_otherUser, modelResult)
            if (modelResult is ModelResult.Success) {
                repository.loadAndObserveUser(myUserID, firebaseReferenceObserver) { modelResult2: ModelResult<User> ->
                    onResult(_myUser, modelResult2)
                }
            }
        }
    }

    fun addFriendPressed() {
        repository.updateNewSentRequest(myUserID, UserRequest(_otherUser.value!!.info.id))
        repository.updateNewNotification(_otherUser.value!!.info.id, UserNotification(myUserID))
    }

    fun removeFriendPressed() {
        repository.removeFriend(myUserID, _otherUser.value!!.info.id)
        repository.removeChat(convertTwoUserIDs(myUserID, _otherUser.value!!.info.id))
        repository.removeMessages(convertTwoUserIDs(myUserID, _otherUser.value!!.info.id))
    }

    fun acceptFriendRequestPressed() {
        repository.updateNewFriend(UserFriend(myUserID), UserFriend(_otherUser.value!!.info.id))

        val newChat = Chat().apply {
            info.id = convertTwoUserIDs(myUserID, _otherUser.value!!.info.id)
            lastMyMessage = MyMessage(seen = true, text = "Say hello!")
        }

        repository.updateNewChat(newChat)
        repository.removeNotification(myUserID, _otherUser.value!!.info.id)
        repository.removeSentRequest(_otherUser.value!!.info.id, myUserID)
    }

    fun declineFriendRequestPressed() {
        repository.removeSentRequest(myUserID, _otherUser.value!!.info.id)
        repository.removeNotification(myUserID, _otherUser.value!!.info.id)
    }
}
