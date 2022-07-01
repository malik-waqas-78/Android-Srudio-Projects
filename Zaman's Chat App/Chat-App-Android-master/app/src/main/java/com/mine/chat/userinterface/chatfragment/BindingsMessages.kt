package com.mine.chat.userinterface.chatfragment

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.MyMessage
import kotlin.math.abs

@BindingAdapter("bind_messages_list")
fun bindMessagesList(listView: RecyclerView, items: List<MyMessage>?) {
    items?.let {
        (listView.adapter as MessagesListAdapter).submitList(items)
        listView.scrollToPosition(items.size - 1)
    }
}

@BindingAdapter("bind_message", "bind_message_viewModel")
fun View.bindShouldMessageShowTimeText(myMessage: MyMessage, viewModel: ChatViewModelDefault) {
    val halfHourInMilli = 1800000
    val index = viewModel.messagesList.value!!.indexOf(myMessage)

    if (index == 0) {
        this.visibility = View.VISIBLE
    } else {
        val messageBefore = viewModel.messagesList.value!![index - 1]

        if (abs(messageBefore.epochTimeMs - myMessage.epochTimeMs) > halfHourInMilli) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
    }
}

