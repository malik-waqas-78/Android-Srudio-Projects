package com.mine.chat.userinterface.notificationsfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.UserInfo
import com.mine.chat.databinding.NotificationsListBinding


class NotificationsListAdapter internal constructor(private val viewModel: NotificationsViewModelDefault) :
    ListAdapter<UserInfo, NotificationsListAdapter.ViewHolder>(UserInfoDiffCallback()) {

    class ViewHolder(private val binding: NotificationsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: NotificationsViewModelDefault, item: UserInfo) {
            binding.viewmodel = viewModel
            binding.userinfo = item
            binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = NotificationsListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }
}

class UserInfoDiffCallback : DiffUtil.ItemCallback<UserInfo>() {
    override fun areItemsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
        return oldItem.id == newItem.id
    }
}