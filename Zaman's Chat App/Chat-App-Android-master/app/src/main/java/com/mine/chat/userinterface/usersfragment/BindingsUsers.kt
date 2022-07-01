package com.mine.chat.userinterface.usersfragment

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.User

@BindingAdapter("bind_users_list")
fun bindUsersList(listView: RecyclerView, items: List<User>?) {
    items?.let { (listView.adapter as UsersListAdapter).submitList(items) }
}

