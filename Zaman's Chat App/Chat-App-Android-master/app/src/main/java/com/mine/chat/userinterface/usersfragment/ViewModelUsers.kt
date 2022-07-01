package com.mine.chat.userinterface.usersfragment

import androidx.lifecycle.*
import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.User
import com.mine.chat.dataandmodels.firebasedb.managerepository.DBRepository
import com.mine.chat.userinterface.ViewModelDefault
import com.mine.chat.dataandmodels.Event
import com.mine.chat.dataandmodels.ModelResult


class UsersViewModelFactory(private val myUserID: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UsersViewModelDefault(myUserID) as T
    }
}

class UsersViewModelDefault(private val myUserID: String) : ViewModelDefault() {
    private val repository: DBRepository = DBRepository()

    private val _selectedUser = MutableLiveData<Event<User>>()
    var selectedUser: LiveData<Event<User>> = _selectedUser
    private val updatedUsersList = MutableLiveData<MutableList<User>>()
    val usersList = MediatorLiveData<List<User>>()

    init {
        usersList.addSource(updatedUsersList) { mutableList ->
            usersList.value = updatedUsersList.value?.filter { it.info.id != myUserID }
        }
        loadUsers()
    }

    private fun loadUsers() {
        repository.loadUsers { modelResult: ModelResult<MutableList<User>> ->
            onResult(updatedUsersList, modelResult)
        }
    }

    fun selectUser(user: User) {
        _selectedUser.value = Event(user)
    }
}