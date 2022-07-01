package com.mine.chat.dataandmodels.firebasedb.managerepository

import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.*
import com.mine.chat.dataandmodels.firebasedb.remotedb.FirebaseDataSource
import com.mine.chat.dataandmodels.firebasedb.remotedb.FirebaseReferenceChildObserver
import com.mine.chat.dataandmodels.firebasedb.remotedb.FirebaseReferenceValueObserver
import com.mine.chat.dataandmodels.ModelResult
import com.mine.chat.utilandexts.wrapSnapshotToArrayList
import com.mine.chat.utilandexts.wrapSnapshotToClass


class DBRepository {
    private val firebaseDatabaseService = FirebaseDataSource()

    //region Update
    fun updateUserStatus(userID: String, status: String) {
        firebaseDatabaseService.updateUserStatus(userID, status)
    }

    fun updateNewMessage(messagesID: String, myMessage: MyMessage) {
        firebaseDatabaseService.pushNewMessage(messagesID, myMessage)
    }

    fun updateNewUser(user: User) {
        firebaseDatabaseService.updateNewUser(user)
    }

    fun updateNewFriend(myUser: UserFriend, otherUser: UserFriend) {
        firebaseDatabaseService.updateNewFriend(myUser, otherUser)
    }

    fun updateNewSentRequest(userID: String, userRequest: UserRequest) {
        firebaseDatabaseService.updateNewSentRequest(userID, userRequest)
    }

    fun updateNewNotification(otherUserID: String, userNotification: UserNotification) {
        firebaseDatabaseService.updateNewNotification(otherUserID, userNotification)
    }

    fun updateChatLastMessage(chatID: String, myMessage: MyMessage) {
        firebaseDatabaseService.updateLastMessage(chatID, myMessage)
    }

    fun updateNewChat(chat: Chat){
        firebaseDatabaseService.updateNewChat(chat)
    }

    fun updateUserProfileImageUrl(userID: String, url: String){
        firebaseDatabaseService.updateUserProfileImageUrl(userID, url)
    }

    //endregion

    //region Remove
    fun removeNotification(userID: String, notificationID: String) {
        firebaseDatabaseService.removeNotification(userID, notificationID)
    }

    fun removeFriend(userID: String, friendID: String) {
        firebaseDatabaseService.removeFriend(userID, friendID)
    }

    fun removeSentRequest(otherUserID: String, myUserID: String) {
        firebaseDatabaseService.removeSentRequest(otherUserID, myUserID)
    }

    fun removeChat(chatID: String) {
        firebaseDatabaseService.removeChat(chatID)
    }

    fun removeMessages(messagesID: String){
        firebaseDatabaseService.removeMessages(messagesID)
    }

    //endregion

    //region Load Single

    fun loadUser(userID: String, b: ((ModelResult<User>) -> Unit)) {
        firebaseDatabaseService.loadUserTask(userID).addOnSuccessListener {
            b.invoke(ModelResult.Success(wrapSnapshotToClass(User::class.java, it)))
        }.addOnFailureListener { b.invoke(ModelResult.Error(it.message)) }
    }

    fun loadUserInfo(userID: String, b: ((ModelResult<UserInfo>) -> Unit)) {
        firebaseDatabaseService.loadUserInfoTask(userID).addOnSuccessListener {
            b.invoke(ModelResult.Success(wrapSnapshotToClass(UserInfo::class.java, it)))
        }.addOnFailureListener { b.invoke(ModelResult.Error(it.message)) }
    }

    fun loadChat(chatID: String, b: ((ModelResult<Chat>) -> Unit)) {
        firebaseDatabaseService.loadChatTask(chatID).addOnSuccessListener {
            b.invoke(ModelResult.Success(wrapSnapshotToClass(Chat::class.java, it)))
        }.addOnFailureListener { b.invoke(ModelResult.Error(it.message)) }
    }

    //endregion

    //region Load List

    fun loadUsers(b: ((ModelResult<MutableList<User>>) -> Unit)) {
        b.invoke(ModelResult.Loading)
        firebaseDatabaseService.loadUsersTask().addOnSuccessListener {
            val usersList = wrapSnapshotToArrayList(User::class.java, it)
            b.invoke(ModelResult.Success(usersList))
        }.addOnFailureListener { b.invoke(ModelResult.Error(it.message)) }
    }

    fun loadFriends(userID: String, b: ((ModelResult<List<UserFriend>>) -> Unit)) {
        b.invoke(ModelResult.Loading)
        firebaseDatabaseService.loadFriendsTask(userID).addOnSuccessListener {
            val friendsList = wrapSnapshotToArrayList(UserFriend::class.java, it)
            b.invoke(ModelResult.Success(friendsList))
        }.addOnFailureListener { b.invoke(ModelResult.Error(it.message)) }
    }

    fun loadNotifications(userID: String, b: ((ModelResult<MutableList<UserNotification>>) -> Unit)) {
        b.invoke(ModelResult.Loading)
        firebaseDatabaseService.loadNotificationsTask(userID).addOnSuccessListener {
            val notificationsList = wrapSnapshotToArrayList(UserNotification::class.java, it)
            b.invoke(ModelResult.Success(notificationsList))
        }.addOnFailureListener { b.invoke(ModelResult.Error(it.message)) }
    }

    //endregion

    //#region Load and Observe

    fun loadAndObserveUser(userID: String, observer: FirebaseReferenceValueObserver, b: ((ModelResult<User>) -> Unit)) {
        firebaseDatabaseService.attachUserObserver(User::class.java, userID, observer, b)
    }

    fun loadAndObserveUserInfo(userID: String, observer: FirebaseReferenceValueObserver, b: ((ModelResult<UserInfo>) -> Unit)) {
        firebaseDatabaseService.attachUserInfoObserver(UserInfo::class.java, userID, observer, b)
    }

    fun loadAndObserveUserNotifications(userID: String, observer: FirebaseReferenceValueObserver, b: ((ModelResult<MutableList<UserNotification>>) -> Unit)){
        firebaseDatabaseService.attachUserNotificationsObserver(UserNotification::class.java, userID, observer, b)
    }

    fun loadAndObserveMessagesAdded(messagesID: String, observer: FirebaseReferenceChildObserver, b: ((ModelResult<MyMessage>) -> Unit)) {
        firebaseDatabaseService.attachMessagesObserver(MyMessage::class.java, messagesID, observer, b)
    }

    fun loadAndObserveChat(chatID: String, observer: FirebaseReferenceValueObserver, b: ((ModelResult<Chat>) -> Unit)) {
        firebaseDatabaseService.attachChatObserver(Chat::class.java, chatID, observer, b)
    }

    //endregion
}

