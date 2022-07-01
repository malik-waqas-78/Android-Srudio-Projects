package com.mine.chat.userinterface.notificationsfragment

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.*
import com.mine.chat.dataandmodels.firebasedb.managerepository.DBRepository
import com.mine.chat.userinterface.ViewModelDefault
import com.mine.chat.dataandmodels.ModelResult
import com.mine.chat.utilandexts.addNewItem
import com.mine.chat.utilandexts.removeItem
import com.mine.chat.utilandexts.convertTwoUserIDs

class NotificationsViewModelFactory(private val myUserID: String) :
    ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotificationsViewModelDefault(myUserID) as T
    }
}

class NotificationsViewModelDefault(private val myUserID: String) : ViewModelDefault() {

    private val dbRepository: DBRepository = DBRepository()
    private val updatedUserInfo = MutableLiveData<UserInfo>()
    private val userNotificationsList = MutableLiveData<MutableList<UserNotification>>()

    val usersInfoList = MediatorLiveData<MutableList<UserInfo>>()

    init {
        usersInfoList.addSource(updatedUserInfo) { usersInfoList.addNewItem(it) }
        loadNotifications()
    }

    private fun loadNotifications() {
        dbRepository.loadNotifications(myUserID) { modelResult: ModelResult<MutableList<UserNotification>> ->
            onResult(userNotificationsList, modelResult)
            if (modelResult is ModelResult.Success) modelResult.data?.forEach { loadUserInfo(it) }
        }
    }

    private fun loadUserInfo(userNotification: UserNotification) {
        dbRepository.loadUserInfo(userNotification.userID) { modelResult: ModelResult<UserInfo> ->
            onResult(updatedUserInfo, modelResult)
        }
    }

    private fun updateNotification(otherUserInfo: UserInfo, removeOnly: Boolean) {
        val userNotification = userNotificationsList.value?.find {
            it.userID == otherUserInfo.id
        }

        if (userNotification != null) {
            if (!removeOnly) {
                dbRepository.updateNewFriend(UserFriend(myUserID), UserFriend(otherUserInfo.id))
                val newChat = Chat().apply {
                    info.id = convertTwoUserIDs(myUserID, otherUserInfo.id)
                    lastMyMessage = MyMessage(seen = true, text = "Say hello!")
                }
                dbRepository.updateNewChat(newChat)
            }
            dbRepository.removeNotification(myUserID, otherUserInfo.id)
            dbRepository.removeSentRequest(otherUserInfo.id, myUserID)

            usersInfoList.removeItem(otherUserInfo)
            userNotificationsList.removeItem(userNotification)
        }
    }

    fun acceptNotificationPressed(userInfo: UserInfo) {
        updateNotification(userInfo, false)
    }

    fun declineNotificationPressed(userInfo: UserInfo) {
        updateNotification(userInfo, true)
    }
}