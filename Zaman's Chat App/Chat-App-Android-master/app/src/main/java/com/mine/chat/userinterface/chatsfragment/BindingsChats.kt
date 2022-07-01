@file:Suppress("unused")

package com.mine.chat.userinterface.chatsfragment

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mine.chat.R
import com.mine.chat.dataandmodels.modelclasses.ChatAndUserInfo
import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.MyMessage

@BindingAdapter("bind_chats_list")
fun bindChatsList(listView: RecyclerView, items: List<ChatAndUserInfo>?) {
    items?.let { (listView.adapter as ChatsListAdapter).submitList(items) }
}

@BindingAdapter("bind_chat_message_text", "bind_chat_message_text_viewModel")
fun TextView.bindMessageYouToText(myMessage: MyMessage, viewModel: ChatsViewModelDefault) {
    this.text = if (myMessage.senderID == viewModel.myUserID) {
        "You: " + myMessage.text
    } else {
        myMessage.text
    }
}

@BindingAdapter("bind_message_view", "bind_message_textView", "bind_message", "bind_myUserID")
fun View.bindMessageSeen(view: View, textView: TextView, myMessage: MyMessage, myUserID: String) {
    if (myMessage.senderID != myUserID && !myMessage.seen) {
        view.visibility = View.VISIBLE
        textView.setTextAppearance(R.style.MessageNotSeen)
//        textView.alpha = 1f
    } else {
        view.visibility = View.INVISIBLE
        textView.setTextAppearance(R.style.MessageSeen)
//        textView.alpha = 1f
    }
}

