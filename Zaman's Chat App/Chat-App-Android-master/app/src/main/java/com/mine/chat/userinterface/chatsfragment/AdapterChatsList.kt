package com.mine.chat.userinterface.chatsfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mine.chat.dataandmodels.modelclasses.ChatAndUserInfo
import com.mine.chat.databinding.ChatListItemBinding


class ChatsListAdapter internal constructor(private val viewModel: ChatsViewModelDefault) :
    ListAdapter<(ChatAndUserInfo), ChatsListAdapter.ViewHolder>(ChatDiffCallback()) {

    class ViewHolder(private val binding: ChatListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ChatsViewModelDefault, item: ChatAndUserInfo) {
            binding.viewmodel = viewModel
            binding.chatwithuserinfo = item
            binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ChatListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }
}

class ChatDiffCallback : DiffUtil.ItemCallback<ChatAndUserInfo>() {
    override fun areItemsTheSame(oldItem: ChatAndUserInfo, itemAndUserInfo: ChatAndUserInfo): Boolean {
        return oldItem == itemAndUserInfo
    }

    override fun areContentsTheSame(oldItem: ChatAndUserInfo, itemAndUserInfo: ChatAndUserInfo): Boolean {
        return oldItem.mChat.info.id == itemAndUserInfo.mChat.info.id
    }
}