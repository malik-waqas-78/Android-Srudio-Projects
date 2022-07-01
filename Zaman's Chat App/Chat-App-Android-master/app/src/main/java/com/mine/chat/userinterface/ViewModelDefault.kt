package com.mine.chat.userinterface

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mine.chat.dataandmodels.Event
import com.mine.chat.dataandmodels.ModelResult

abstract class ViewModelDefault : ViewModel() {
    protected val mSnackBarText = MutableLiveData<Event<String>>()
    val snackBarText: LiveData<Event<String>> = mSnackBarText

    private val mDataLoading = MutableLiveData<Event<Boolean>>()
    val dataLoading: LiveData<Event<Boolean>> = mDataLoading

    protected fun <T> onResult(mutableLiveData: MutableLiveData<T>? = null, modelResult: ModelResult<T>) {
        when (modelResult) {
            is ModelResult.Loading -> mDataLoading.value = Event(true)

            is ModelResult.Error -> {
                mDataLoading.value = Event(false)
                modelResult.msg?.let { mSnackBarText.value = Event(it) }
            }

            is ModelResult.Success -> {
                mDataLoading.value = Event(false)
                modelResult.data?.let { mutableLiveData?.value = it }
                modelResult.msg?.let { mSnackBarText.value = Event(it) }
            }
        }
    }
}