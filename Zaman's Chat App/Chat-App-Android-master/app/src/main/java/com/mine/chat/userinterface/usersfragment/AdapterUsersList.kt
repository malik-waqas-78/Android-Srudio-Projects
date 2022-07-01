package com.mine.chat.userinterface.usersfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.User
import com.mine.chat.databinding.UsersListBinding


class UsersListAdapter internal constructor(private val viewModel: UsersViewModelDefault) :
    ListAdapter<User, UsersListAdapter.ViewHolder>(UserDiffCallback()) {

    class ViewHolder(private val binding: UsersListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: UsersViewModelDefault, item: User) {
            binding.viewmodel = viewModel
            binding.user = item
            binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = UsersListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.info.id == newItem.info.id
    }
}