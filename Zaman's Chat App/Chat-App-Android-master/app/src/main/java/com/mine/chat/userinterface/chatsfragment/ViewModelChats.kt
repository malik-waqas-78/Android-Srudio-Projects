package com.mine.chat.userinterface.chatsfragment

import androidx.lifecycle.*
import com.mine.chat.dataandmodels.Event
import com.mine.chat.dataandmodels.ModelResult
import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.Chat
import com.mine.chat.dataandmodels.modelclasses.ChatAndUserInfo
import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.UserFriend
import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.UserInfo
import com.mine.chat.dataandmodels.firebasedb.remotedb.FirebaseReferenceValueObserver
import com.mine.chat.dataandmodels.firebasedb.managerepository.DBRepository
import com.mine.chat.userinterface.ViewModelDefault
import com.mine.chat.utilandexts.addNewItem
import com.mine.chat.utilandexts.convertTwoUserIDs
import com.mine.chat.utilandexts.updateItemAt


class ChatsViewModelFactory(private val myUserID: String) :
    ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatsViewModelDefault(myUserID) as T
    }
}

class ChatsViewModelDefault(val myUserID: String) : ViewModelDefault() {

    private val repository: DBRepository = DBRepository()
    private val firebaseReferenceObserverList = ArrayList<FirebaseReferenceValueObserver>()
    private val _updatedChatWithUserInfo = MutableLiveData<ChatAndUserInfo>()
    private val _selectedChat = MutableLiveData<Event<ChatAndUserInfo>>()

    var selectedChat: LiveData<Event<ChatAndUserInfo>> = _selectedChat
    val chatsList = MediatorLiveData<MutableList<ChatAndUserInfo>>()

    init {
        chatsList.addSource(_updatedChatWithUserInfo) { newChat ->
            val chat = chatsList.value?.find { it.mChat.info.id == newChat.mChat.info.id }
            if (chat == null) {
                chatsList.addNewItem(newChat)
            } else {
                chatsList.updateItemAt(newChat, chatsList.value!!.indexOf(chat))
            }
        }
        setupChats()
    }

    override fun onCleared() {
        super.onCleared()
        firebaseReferenceObserverList.forEach { it.clear() }
    }

    private fun setupChats() {
        loadFriends()
    }

    private fun loadFriends() {
        repository.loadFriends(myUserID) { modelResult: ModelResult<List<UserFriend>> ->
            onResult(null, modelResult)
            if (modelResult is ModelResult.Success) modelResult.data?.forEach { loadUserInfo(it) }
        }
    }

    private fun loadUserInfo(userFriend: UserFriend) {
        repository.loadUserInfo(userFriend.userID) { modelResult: ModelResult<UserInfo> ->
            onResult(null, modelResult)
            if (modelResult is ModelResult.Success) modelResult.data?.let { loadAndObserveChat(it) }
        }
    }

    private fun loadAndObserveChat(userInfo: UserInfo) {
        val observer = FirebaseReferenceValueObserver()
        firebaseReferenceObserverList.add(observer)
        repository.loadAndObserveChat(convertTwoUserIDs(myUserID, userInfo.id), observer) { modelResult: ModelResult<Chat> ->
            if (modelResult is ModelResult.Success) {
                _updatedChatWithUserInfo.value = modelResult.data?.let { ChatAndUserInfo(it, userInfo) }
            } else if (modelResult is ModelResult.Error) {
                chatsList.value?.let {
                    val newList = mutableListOf<ChatAndUserInfo>().apply { addAll(it) }
                    newList.removeIf { it2 -> modelResult.msg.toString().contains(it2.mUserInfo.id) }
                    chatsList.value = newList
                }
            }
        }
    }

    fun selectChatWithUserInfoPressed(chat: ChatAndUserInfo) {
        _selectedChat.value = Event(chat)
    }
}