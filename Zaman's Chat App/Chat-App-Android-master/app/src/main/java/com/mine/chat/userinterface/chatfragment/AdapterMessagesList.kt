package com.mine.chat.userinterface.chatfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.MyMessage
import com.mine.chat.databinding.ReceivedMessagesListBinding
import com.mine.chat.databinding.SentMessagesListBinding


class MessagesListAdapter internal constructor(private val viewModel: ChatViewModelDefault, private val userId: String) : ListAdapter<MyMessage, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    private val holderTypeMessageReceived = 1
    private val holderTypeMessageSent = 2

    class ReceivedViewHolder(private val binding: ReceivedMessagesListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ChatViewModelDefault, item: MyMessage) {
            binding.viewmodel = viewModel
            binding.message = item
            binding.executePendingBindings()
        }
    }

    class SentViewHolder(private val binding: SentMessagesListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ChatViewModelDefault, item: MyMessage) {
            binding.viewmodel = viewModel
            binding.message = item
            binding.executePendingBindings()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).senderID != userId) {
            holderTypeMessageReceived
        } else {
            holderTypeMessageSent
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            holderTypeMessageSent -> (holder as SentViewHolder).bind(
                viewModel,
                getItem(position)
            )
            holderTypeMessageReceived -> (holder as ReceivedViewHolder).bind(
                viewModel,
                getItem(position)
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            holderTypeMessageSent -> {
                val binding = SentMessagesListBinding.inflate(layoutInflater, parent, false)
                SentViewHolder(binding)
            }
            holderTypeMessageReceived -> {
                val binding = ReceivedMessagesListBinding.inflate(layoutInflater, parent, false)
                ReceivedViewHolder(binding)
            }
            else -> {
                throw Exception("Error reading holder type")
            }
        }
    }
}

class MessageDiffCallback : DiffUtil.ItemCallback<MyMessage>() {
    override fun areItemsTheSame(oldItem: MyMessage, newItem: MyMessage): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MyMessage, newItem: MyMessage): Boolean {
        return oldItem.epochTimeMs == newItem.epochTimeMs
    }
}
