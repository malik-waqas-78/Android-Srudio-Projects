package com.mine.chat.userinterface.chatfragment

import androidx.lifecycle.*
import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.Chat
import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.MyMessage
import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.UserInfo
import com.mine.chat.dataandmodels.firebasedb.remotedb.FirebaseReferenceChildObserver
import com.mine.chat.dataandmodels.firebasedb.remotedb.FirebaseReferenceValueObserver
import com.mine.chat.dataandmodels.firebasedb.managerepository.DBRepository
import com.mine.chat.userinterface.ViewModelDefault
import com.mine.chat.dataandmodels.ModelResult
import com.mine.chat.utilandexts.addNewItem

class ChatViewModelFactory(private val myUserID: String, private val otherUserID: String, private val chatID: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModelDefault(myUserID, otherUserID, chatID) as T
    }
}

class ChatViewModelDefault(private val myUserID: String, private val otherUserID: String, private val chatID: String) : ViewModelDefault() {

    private val dbRepository: DBRepository = DBRepository()

    private val _otherUser: MutableLiveData<UserInfo> = MutableLiveData()
    private val _addedMessage = MutableLiveData<MyMessage>()

    private val fbRefMessagesChildObserver = FirebaseReferenceChildObserver()
    private val fbRefUserInfoObserver = FirebaseReferenceValueObserver()

    val messagesList = MediatorLiveData<MutableList<MyMessage>>()
    val newMessageText = MutableLiveData<String>()
    val otherUser: LiveData<UserInfo> = _otherUser

    init {
        setupChat()
        checkAndUpdateLastMessageSeen()
    }

    override fun onCleared() {
        super.onCleared()
        fbRefMessagesChildObserver.clear()
        fbRefUserInfoObserver.clear()
    }

    private fun checkAndUpdateLastMessageSeen() {
        dbRepository.loadChat(chatID) { modelResult: ModelResult<Chat> ->
            if (modelResult is ModelResult.Success && modelResult.data != null) {
                modelResult.data.lastMyMessage.let {
                    if (!it.seen && it.senderID != myUserID) {
                        it.seen = true
                        dbRepository.updateChatLastMessage(chatID, it)
                    }
                }
            }
        }
    }

    private fun setupChat() {
        dbRepository.loadAndObserveUserInfo(otherUserID, fbRefUserInfoObserver) { modelResult: ModelResult<UserInfo> ->
            onResult(_otherUser, modelResult)
            if (modelResult is ModelResult.Success && !fbRefMessagesChildObserver.isObserving()) {
                loadAndObserveNewMessages()
            }
        }
    }

    private fun loadAndObserveNewMessages() {
        messagesList.addSource(_addedMessage) { messagesList.addNewItem(it) }

        dbRepository.loadAndObserveMessagesAdded(
            chatID,
            fbRefMessagesChildObserver
        ) { modelResult: ModelResult<MyMessage> ->
            onResult(_addedMessage, modelResult)
        }
    }

    fun sendMessagePressed() {
        if (!newMessageText.value.isNullOrBlank()) {
            val newMsg = MyMessage(myUserID, newMessageText.value!!)
            dbRepository.updateNewMessage(chatID, newMsg)
            dbRepository.updateChatLastMessage(chatID, newMsg)
            newMessageText.value = null
        }
    }
}